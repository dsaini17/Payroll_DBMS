package com.example.devesh.payroll;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devesh.payroll.Dialogs.passwordDialog;

import java.util.Map;

public class PasswordActivity extends AppCompatActivity implements passwordDialog.DialogListener{

    public static int requestCodePermission ;
    String[] requestPermissions;

    EditText passwordEditText;
    Button loginButton;
    TextView passwordTextView;

    String enteredPassword,originalPassword;

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

    }

    public void init(){

        requestPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        requestCodePermission = 404;

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        loginButton = (Button) findViewById(R.id.loginButton);
    }

    public void verifyPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Map<String, ?> hashMap = sharedPreferences.getAll();

        int length = hashMap.size();

        if(length==0){
            editor.putString("password","payroll");
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
                        startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                        finish();
                    }
                    else {
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
    }

    public void createDialog(){
        passwordDialog dialog = new passwordDialog();
        dialog.show(getSupportFragmentManager(),"Password Dialog");
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

}
