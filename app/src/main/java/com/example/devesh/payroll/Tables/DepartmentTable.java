package com.example.devesh.payroll.Tables;

import com.example.devesh.payroll.BasicTable;

/**
 * Created by devesh on 2/4/17.
 */

public class DepartmentTable extends BasicTable {

    public static final String TABLE_NAME = "DEPARTMENT_TABLE";

    public static final String TABLE_CREATE_CMD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + LBR
            + Columns.EMPLOYEE_ID + TYPE_INT + COMMA
            + Columns.NAME + TYPE_TEXT + NOT_NULL + COMMA
            + Columns.DEPT_ID + TYPE_INT + NOT_NULL + COMMA
            + P_KEY + LBR + Columns.EMPLOYEE_ID + RBR
            + RBR + ";";

    public interface Columns {
        String DEPT_ID = "Department_ID";
        String EMPLOYEE_ID = "Employee_ID";
        String NAME = "Department_Name";
    }
}
