package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 2/4/17.
 */

public class Employee {

    Integer Employee_ID;
    String Name;
    String Address;
    Integer Contact;
    String Email;

    public Employee(Integer employee_ID, String name, String address, Integer contact, String email) {
        Employee_ID = employee_ID;
        Name = name;
        Address = address;
        Contact = contact;
        Email = email;
    }

    public Integer getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(Integer employee_ID) {
        Employee_ID = employee_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Integer getContact() {
        return Contact;
    }

    public void setContact(Integer contact) {
        Contact = contact;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
