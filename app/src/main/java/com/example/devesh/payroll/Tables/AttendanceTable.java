package com.example.devesh.payroll.Tables;

import com.example.devesh.payroll.BasicTable;

/**
 * Created by devesh on 2/4/17.
 */

public class AttendanceTable extends BasicTable {

    public static final String TABLE_NAME = "ATTENDANCE_TABLE";

    public static final String TABLE_CREATE_CMD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + LBR
            + Columns.EMPLOYEE_ID + TYPE_INT + UNIQUE + COMMA
            + Columns.PRESENT + TYPE_INT + DEFAULT + " 0 " + COMMA
            + Columns.WORKING_DAYS + TYPE_INT + DEFAULT + " 0 " + COMMA
            + P_KEY + LBR + Columns.EMPLOYEE_ID + RBR
            + RBR + ";";

    public interface Columns {
        String EMPLOYEE_ID = "Employee_ID";
        String PRESENT = "Days_Worked";
        String WORKING_DAYS = "Working_Days";
    }
}
