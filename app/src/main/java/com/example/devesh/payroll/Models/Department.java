package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 2/4/17.
 */

public class Department {

    Integer Department_ID;
    String Name;

    public Department(Integer department_ID, String name) {
        Department_ID = department_ID;
        Name = name;
    }

    public Integer getDepartment_ID() {
        return Department_ID;
    }

    public void setDepartment_ID(Integer department_ID) {
        Department_ID = department_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
