package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.ExportClass.ExportDatabase;
import com.example.devesh.payroll.Models.Department;
import com.example.devesh.payroll.Tables.DepartmentTable;
import com.example.devesh.payroll.Tables.SalaryTable;

import java.io.IOException;
import java.util.ArrayList;

public class BonusActivity extends AppCompatActivity {

    EditText singleEmployeeID , singleEmployeeValue , percentageValue , legalValue , prValue , hrValue ,
            logiValue , boardValue , marketingValue , financeValue ;

    Button singleUpdate , percentageUpdate , departmentUpdate ;
    SQLiteDatabase database;
    ArrayList<String> queryList,departList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);

        init();

        listeners();

        departList = Department.getAllDepartments();

    }

    public void listeners(){

        singleUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSingleUpdate();
            }
        });

        percentageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPercentageUpdate();
            }
        });

        departmentUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDepartmentUpdate();
            }
        });
    }

    public void init(){

        departList = new ArrayList<>();
        queryList = new ArrayList<>();

        singleEmployeeID = (EditText) findViewById(R.id.singleBonusEmployee);
        singleEmployeeValue = (EditText) findViewById(R.id.singleBonusValue);

        percentageValue = (EditText) findViewById(R.id.percentageBonusValue);

        legalValue = (EditText) findViewById(R.id.legalBonus);
        prValue = (EditText) findViewById(R.id.prBonus);
        hrValue = (EditText) findViewById(R.id.hrBonus);
        logiValue = (EditText) findViewById(R.id.logisticsBonus);
        boardValue = (EditText) findViewById(R.id.boardBonus);
        marketingValue = (EditText) findViewById(R.id.marketingBonus);
        financeValue = (EditText) findViewById(R.id.financeBonus);

        singleUpdate = (Button) findViewById(R.id.singleBonusUpdate);
        percentageUpdate = (Button) findViewById(R.id.percentageBonusUpdate);
        departmentUpdate = (Button) findViewById(R.id.departmentUpdateButton);
    }

    public void performSingleUpdate(){
        Integer employee = Integer.valueOf(singleEmployeeID.getText().toString().trim());
        Integer bonus = Integer.valueOf(singleEmployeeValue.getText().toString().trim());

        queryList.clear();

        database = MyDatabase.getWritable(getApplicationContext());

        String updateBonus = "UPDATE "+ SalaryTable.TABLE_NAME+" SET "+SalaryTable.Columns.BONUS+" = "+SalaryTable.Columns.BONUS +"+"+ bonus
                + " WHERE "+SalaryTable.Columns.EMPLOYEE_ID+" = "+employee+";";

        queryList.add(updateBonus);
        database.execSQL(updateBonus);

        try {
            askToCreateFile(queryList);
            ExportDatabase.Export();
            Toast.makeText(getApplicationContext(),"Bonus Given",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        singleEmployeeID.setText("");
        singleEmployeeValue.setText("");

    }

    public void performPercentageUpdate() {
        Integer percentage = Integer.valueOf(percentageValue.getText().toString().trim());

        if(percentage>10){
            Toast.makeText(getApplicationContext(),"Company Policy , Can't be greater than 10",Toast.LENGTH_LONG).show();
        }
        else {
            database = MyDatabase.getWritable(getApplicationContext());

            queryList.clear();
            String updateBonus = "UPDATE " + SalaryTable.TABLE_NAME + " SET " + SalaryTable.Columns.BONUS + " = " + SalaryTable.Columns.BONUS + " +( " +
                    SalaryTable.Columns.SALARY + "/100 )*" + percentage + " ; ";
            Log.d("percentage bonus", updateBonus);
            queryList.add(updateBonus);
            database.execSQL(updateBonus);

            try {
                askToCreateFile(queryList);
                ExportDatabase.Export();
                Toast.makeText(getApplicationContext(), "Bonus Given", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        percentageValue.setText("");

    }

    public void performDepartmentUpdate(){

        database = MyDatabase.getWritable(getApplicationContext());
        queryList.clear();

        Integer legalBonus = 0,hrBonus =0, prBonus = 0,marketingBonus =0, logiBonus = 0,boardBonus = 0,
                financeBonus = 0;

        if(!legalValue.getText().toString().isEmpty())
            legalBonus = Integer.valueOf(legalValue.getText().toString().trim());
        if(!hrValue.getText().toString().isEmpty())
            hrBonus = Integer.valueOf(hrValue.getText().toString().trim());
        if(!prValue.getText().toString().isEmpty())
            prBonus = Integer.valueOf(prValue.getText().toString().trim());
        if(!marketingValue.getText().toString().isEmpty())
            marketingBonus = Integer.valueOf(marketingValue.getText().toString().trim());
        if(!logiValue.getText().toString().isEmpty())
            logiBonus = Integer.valueOf(logiValue.getText().toString().trim());
        if(!boardValue.getText().toString().isEmpty())
            boardBonus = Integer.valueOf(boardValue.getText().toString().trim());
        if(!financeValue.getText().toString().isEmpty())
            financeBonus = Integer.valueOf(financeValue.getText().toString().trim());



            String legalQuery = "UPDATE " + SalaryTable.TABLE_NAME
                    +" JOIN " + DepartmentTable.TABLE_NAME + " ON "
                    + SalaryTable.TABLE_NAME+"."+SalaryTable.Columns.EMPLOYEE_ID + " = "
                    + DepartmentTable.TABLE_NAME+"."+DepartmentTable.Columns.EMPLOYEE_ID
                    +" SET " + SalaryTable.TABLE_NAME+"."+SalaryTable.Columns.BONUS + " = "
                    + SalaryTable.TABLE_NAME+"."+SalaryTable.Columns.BONUS + "+" + legalBonus
                    + " WHERE "
                    + DepartmentTable.TABLE_NAME + "." + DepartmentTable.Columns.NAME + " = " + departList.get(0) + ";";


            Log.d("legalQuery", legalQuery);

            queryList.add(legalQuery);
            database.execSQL(legalQuery);



            try {
                askToCreateFile(queryList);
                ExportDatabase.Export();
                Toast.makeText(getApplicationContext(), "Bonus Given", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        legalValue.setText("");
        hrValue.setText("");
        prValue.setText("");
        logiValue.setText("");
        boardValue.setText("");
        marketingValue.setText("");
        financeValue.setText("");

    }

    public void askToCreateFile(ArrayList<String> sendData) throws IOException {
        Integer queryNumber = getPrefs();
        String fileName = "Query"+String.valueOf(queryNumber)+".txt";
        ExportDatabase.createFile(sendData,fileName);
    }

    public int getPrefs(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Integer oldValue = sharedPreferences.getInt("query",0);
        editor.remove("query");
        editor.putInt("query",oldValue+1);
        // Log.d("Prefs", " old = "+oldValue + "new = "+sharedPreferences.getInt("query",-1));
        editor.apply();

        return oldValue;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BonusActivity.this,MainActivity.class));
        finish();
    }
}
