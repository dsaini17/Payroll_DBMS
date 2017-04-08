package com.example.devesh.payroll;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devesh.payroll.Dialogs.developerDialog;
import com.example.devesh.payroll.Dialogs.passwordDialog;

import java.util.Map;

import is.arontibo.library.ElasticDownloadView;

public class PasswordActivity extends AppCompatActivity implements passwordDialog.DialogListener{

    public static int requestCodePermission ;
    String[] requestPermissions;

    EditText passwordEditText;
    Button loginButton;
    TextView passwordTextView,developerTextView;
    ElasticDownloadView magicView;

    String enteredPassword,originalPassword;

    public static final int ANIMATION_DURATION = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // Initialize
        init();

        // Ask Permissions
        verifyStoragePermission();

        // Shared Preferences
        verifyPreferences();

        // Listeners
        setListeners();

        magicView.startIntro();
        magicView.setProgress(0);
    }

    public void init(){

        requestPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        requestCodePermission = 404;

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        developerTextView = (TextView) findViewById(R.id.developerTextView);
        loginButton = (Button) findViewById(R.id.loginButton);
        magicView = (ElasticDownloadView) findViewById(R.id.magicView);
    }

    public void verifyPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Map<String, ?> hashMap = sharedPreferences.getAll();

        int length = hashMap.size();

        if(length==0){
            editor.putString("password","payroll");
            editor.putInt("query",1);
            Log.d("Prefs_Verify", String.valueOf(sharedPreferences.getInt("query",0)));
            editor.apply();
        }
    }

    public void setListeners(){

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originalPassword = getPassword();
                enteredPassword = passwordEditText.getText().toString().trim();
                if(enteredPassword.isEmpty())
                    Toast.makeText(PasswordActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                else{
                    if(enteredPassword.matches(originalPassword)){
                        animation_success();
                    }
                    else {
                        animation_fail();
                        Toast.makeText(PasswordActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        passwordEditText.setText("");
                    }
                }
            }
        });

        passwordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Dialog
                createDialog();
            }
        });

        developerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeveloperDialog();
            }
        });
    }

    public void createDialog(){
        passwordDialog dialog = new passwordDialog();
        dialog.show(getSupportFragmentManager(),"Password Dialog");
    }

    public void showDeveloperDialog(){
        developerDialog dialog = new developerDialog();
        dialog.show(getSupportFragmentManager(),"Developer Dialog");
    }

    @Override
    public void Positive_Click(Bundle args) {
        String old_password = args.getString("oldPassword");
        String new_password = args.getString("newPassword");

        originalPassword = getPassword();

        if(!old_password.matches(originalPassword))
            Toast.makeText(getApplicationContext(),"Incorrect Old Pasword",Toast.LENGTH_SHORT).show();
        else{
            if(new_password.isEmpty())
                Toast.makeText(getApplicationContext(),"Empty New Password",Toast.LENGTH_SHORT).show();
            else{
                updateSharedPreferences(new_password);
                Toast.makeText(getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPassword(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String retValue = sharedPreferences.getString("password","payroll");
        return retValue;
    }

    public void updateSharedPreferences(String setValue){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("password");
        editor.putString("password",setValue);
        editor.apply();

        return;
    }

    public void verifyStoragePermission(){
        if(!hasPermission())
            getPermission();
    }

    public boolean hasPermission(){
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED);
    }

    public void getPermission(){
        ActivityCompat.requestPermissions(this,requestPermissions,requestCodePermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==requestCodePermission){
            if(grantResults.length>0){
                if(!(grantResults[1]==PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(PasswordActivity.this,"Please Grant the Permissions to Continue",Toast.LENGTH_SHORT).show();
                    getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void animation_success(){

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                magicView.startIntro();
                //magicView.setProgress(0);
            }
        });
        magicView.setProgress(100);
        magicView.success();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                finish();
            }
        },8*ANIMATION_DURATION);


    }

    public void animation_fail(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                magicView.startIntro();
            }
        });

        magicView.setProgress(45);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                magicView.fail();
            }
        },5*ANIMATION_DURATION);

    }

}
