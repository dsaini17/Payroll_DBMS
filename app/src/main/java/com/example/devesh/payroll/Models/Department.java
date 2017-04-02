package com.example.devesh.payroll.Models;

import java.util.ArrayList;

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


    public static ArrayList<String> getAllDepartments(){
        ArrayList<String> departmentList = new ArrayList<>();

//        CharSequence[] charSequences = new CharSequence[]{
//          "Legal","Public Realtions","Marketing","Finance","Human Resource","Board","Logistics"
//        };

        departmentList.add("Legal");
        departmentList.add("Public Relations");
        departmentList.add("Marketing");
        departmentList.add("Finance");
        departmentList.add("Human Resource");
        departmentList.add("Board");
        departmentList.add("Logistics");

//        return charSequences;
        return  departmentList;
    }

    public static ArrayList<Department> getAllDepartmentInfo(){
        ArrayList<Department> deptList = new ArrayList<>();
        deptList.add(new Department(1,"Legal"));
        deptList.add(new Department(2,"Public Relations"));
        deptList.add(new Department(3,"Marketing"));
        deptList.add(new Department(4,"Finance"));
        deptList.add(new Department(5,"Human Resource"));
        deptList.add(new Department(6,"Board"));
        deptList.add(new Department(7,"Logistics"));

        return deptList;
    }
}
