package com.example.devesh.payroll;

import android.content.Intent;
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
    Button addEmployeeButton;


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
    }

    public void setClickListeners(){

        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddUserActivity.class));
                finish();
            }
        });

    }

    public void Export() throws IOException {
        File inFile = new File(Environment.getDataDirectory()+"/data/com.example.devesh.payroll/databases/"+ MyDatabase.DB_NAME);
        Log.d(TAG_EXPORT,Environment.getDataDirectory()+"/data/com.example.devesh.payroll/databases/"+ MyDatabase.DB_NAME);
        FileInputStream fileInputStream = new FileInputStream(inFile);

        File outFile = new File(Environment.getExternalStorageDirectory()+"/"+MyDatabase.DB_NAME);
        Log.d(TAG_EXPORT,Environment.getExternalStorageDirectory()+"/"+MyDatabase.DB_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int length;

        while ((length=fileInputStream.read(buffer))>0){
            fileOutputStream.write(buffer,0,length);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        fileInputStream.close();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,PasswordActivity.class));
        finish();
    }
}
