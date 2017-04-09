package com.example.devesh.payroll.Tables;

import com.example.devesh.payroll.BasicTable;

/**
 * Created by devesh on 2/4/17.
 */

public class SalaryTable extends BasicTable {

    public static final String TABLE_NAME = "SALARY_TABLE";

    public static final String TABLE_CREATE_CMD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + LBR
            + Columns.EMPLOYEE_ID + TYPE_INT + UNIQUE + COMMA
            + Columns.SALARY + TYPE_INT + NOT_NULL + COMMA
            + Columns.BONUS + TYPE_INT + COMMA
            + P_KEY + LBR + Columns.EMPLOYEE_ID + RBR
            + RBR + ";";

    public interface Columns {
        String EMPLOYEE_ID = "Employee_ID";
        String SALARY = "Basic_Salary";
        String BONUS = "Bonus";
    }
}
