package ch.hslu.mobpro.persistenz;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends Activity {

    private int resumeCount;
    private SharedPreferences sharedPreferences;
    private TextView txtCounterText;
    private TextView txtTeaPrefsSummary;
    private Button btnTeaPrefEdit;
    private Button btnSetDefaultPreferences;
    private CheckBox cbUseExternalStorage;
    private Button btnSave;
    private Button btnLoad;
    private TextView lblTextFileContent;
    private TextView lblExtStorage;
    private final String filename = "InternalFile";
    private EditText editedTxtFileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCounterText = (TextView) findViewById(R.id.txtResumeText);
        txtTeaPrefsSummary = (TextView) findViewById(R.id.txtTeaPrefSummary);
        btnTeaPrefEdit = (Button) findViewById(R.id.btnEditPreferences);
        btnSetDefaultPreferences = (Button) findViewById(R.id.btnSetPreferences);
        cbUseExternalStorage = (CheckBox) findViewById(R.id.cbExternerSpeicher);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        lblTextFileContent = (TextView) findViewById(R.id.lblTextFileContent);
        editedTxtFileText = (EditText) findViewById(R.id.editedTxtFileText);
        lblExtStorage = (TextView) findViewById(R.id.lblExtStorage);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        resumeCount = sharedPreferences.getInt("CounterAppPref",0);

        btnTeaPrefEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prefIntent = new Intent(MainActivity.this, TeaPreferenceActivity.class);
                startActivity(prefIntent);
            }
        });

        btnSetDefaultPreferences.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                defaultPreferences();
            }
        });

        if (isExternalStorageWriteable())
            lblExtStorage.setText("External storage is mounted (writable)");
        else if (isExternalStorageReadable())
            lblExtStorage.setText("External storage is mounted (readable)");
        else
            lblExtStorage.setText("External storage is not mounted");
        

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveFile();
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                readFile();
            }
        });

    }

    private void defaultPreferences(){
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor teaPrefEditor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();

        String teaPref = "Ginger from se root";
        String sweetenerPref = "natural";

        teaPrefEditor.putString("teaPreferred", teaPref);
        teaPrefEditor.putString("teaSweetener", sweetenerPref);
        teaPrefEditor.putBoolean("teaWithSugar", true);
        teaPrefEditor.apply();

        String sweetenerPrefText = translateSweetenerValuesIntoText(sweetenerPref);

        String textSummaryPt = String.format(getResources().getString(R.string.teaPrefsSummary), teaPref);
        textSummaryPt =  textSummaryPt + String.format(getResources().getString(R.string.teaPrefsWithSweetenerSummary), sweetenerPrefText);
        txtTeaPrefsSummary.setText(textSummaryPt);
    }
    private void saveFile(){
        if (cbUseExternalStorage.isChecked() && isExternalStorageWriteable()) {
            MainActivityPermissionsDispatcher.writeFileToExternalStorageWithCheck(MainActivity.this, editedTxtFileText.getText().toString());
        } else {
            writeFileToInternalStorage(editedTxtFileText.getText().toString());
        }
        editedTxtFileText.setText("");
    }

    private void readFile() {
        if (cbUseExternalStorage.isChecked()) {
           MainActivityPermissionsDispatcher.readFileToExternalStorageWithCheck(this);
        } else {
            lblTextFileContent.setText(readInternalFile());
        }
    }

    private String readInternalFile() {
        FileInputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput(filename);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void writeFileToExternalStorage(String content) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Persistenz");
        dir.mkdirs();
        File file = new File(dir, filename);

        try {
            FileOutputStream f = new FileOutputStream(file);
            f.write(content.getBytes());
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFileToInternalStorage(String text) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String translateSweetenerValuesIntoText(String value){

        String[] sweetenerValues = getResources().getStringArray(R.array.teaSweetenerValues);
        String[] sweetenerText = getResources().getStringArray(R.array.teaSweetener);

        if(sweetenerValues.length != sweetenerText.length){
            return value;
        }

        for(int i = 0; i < sweetenerValues.length; i++){
            if(sweetenerValues[i].equals(value)){
                return sweetenerText[i];
            }
        }

        return value;

    }

    @Override
    protected void onResume() {
        super.onResume();

        addCounterText();
        addTeaPrefsText();
    }

    private void addCounterText(){

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CounterAppPref", resumeCount + 1);
        editor.apply();

        String text = String.format(getResources().getString(R.string.appPref_counterText), "" +resumeCount);
        txtCounterText.setText(text);

    }

    private void addTeaPrefsText(){

        final SharedPreferences tiiPref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean teaWithSugar = tiiPref.getBoolean("teaWithSugar", true);
        String suesstoff = tiiPref.getString("teaSweetener","Honig");
        String lieblingstee = tiiPref.getString("teaPreferred","Ginger from se root");

        String textSummaryPt = String.format(getResources().getString(R.string.teaPrefsSummary), lieblingstee);
        if(teaWithSugar){

            String suessstoffText = translateSweetenerValuesIntoText(suesstoff);
            textSummaryPt =  textSummaryPt + String.format(getResources().getString(R.string.teaPrefsWithSweetenerSummary), suessstoffText);
        }

        txtTeaPrefsSummary.setText(textSummaryPt);
    }



    @OnShowRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationalforExternalStorage(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Die App benötigt zugriff auf den Externen Speicher, bitte säg jo!")
                .setPositiveButton("Erlauben", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }


    @OnPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForCamera() {
        Toast.makeText(this, "wiso seisch nei????", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForCamera() {
        Toast.makeText(this, "dont show camera plis", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    void readFileToExternalStorage() {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Persistenz");
        dir.mkdirs();
        File file = new File(dir, filename);

        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lblTextFileContent.setText(myData);
    }

    @OnShowRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationalforReadExternalStorage(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Die App benötigt zugriff auf den Externen Speicher, bitte säg jo!")
                .setPositiveButton("Erlauben", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }


    @OnPermissionDenied(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForReadExtStorage() {
        Toast.makeText(this, "nei nei, wetti ned!", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForReadExtStorage() {
        Toast.makeText(this, "wiso? besch secher? neiiiii", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
