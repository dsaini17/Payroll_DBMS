package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 4/4/17.
 */

public class User {

    Integer Employee_Id;

    String Employee_Name;

    public User(Integer employee_Id, String employee_Name) {
        Employee_Id = employee_Id;
        Employee_Name = employee_Name;
    }

    public Integer getEmployee_Id() {
        return Employee_Id;
    }

    public void setEmployee_Id(Integer employee_Id) {
        Employee_Id = employee_Id;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }
}
