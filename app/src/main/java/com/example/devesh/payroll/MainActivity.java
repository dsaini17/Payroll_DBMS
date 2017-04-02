package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.devesh.payroll.Database.MyDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_EXPORT = "ExportFunction";
    SQLiteDatabase currDatabase;
    Button addEmployeeButton,allEmployeeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setClickListeners();



    }

    public void init(){
        currDatabase = MyDatabase.getReadable(getApplicationContext());

        addEmployeeButton = (Button) findViewById(R.id.addEmployeeButton);
        allEmployeeButton = (Button) findViewById(R.id.allEmployeeButton);
    }

    public void setClickListeners(){

        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddUserActivity.class));
                finish();
            }
        });

        allEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AllUserActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,PasswordActivity.class));
        finish();
    }
}
