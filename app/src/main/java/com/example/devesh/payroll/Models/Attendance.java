package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 2/4/17.
 */

public class Attendance {

    String Employee_Name;
    Integer Employee_ID;
    Integer WorkingDays;
    Integer Present;

    public Attendance(String employee_Name, Integer employee_ID, Integer workingDays, Integer present) {
        Employee_Name = employee_Name;
        Employee_ID = employee_ID;
        WorkingDays = workingDays;
        Present = present;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }

    public Integer getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(Integer employee_ID) {
        Employee_ID = employee_ID;
    }

    public Integer getWorkingDays() {
        return WorkingDays;
    }

    public void setWorkingDays(Integer workingDays) {
        WorkingDays = workingDays;
    }

    public Integer getPresent() {
        return Present;
    }

    public void setPresent(Integer present) {
        Present = present;
    }
}
