package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 2/4/17.
 */

public class Employee {

    Integer Employee_ID;
    String Name;
    String Address;
    Integer Contact;
    String Department;
    String Email;
    Integer Salary;
    Float Tax_Amount;

    public Employee(Integer employee_ID, String name, String address, Integer contact, String department, String email, Integer salary, Float tax_Amount) {
        Employee_ID = employee_ID;
        Name = name;
        Address = address;
        Contact = contact;
        Department = department;
        Email = email;
        Salary = salary;
        Tax_Amount = tax_Amount;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public Integer getSalary() {
        return Salary;
    }

    public void setSalary(Integer salary) {
        Salary = salary;
    }

    public Float getTax_Amount() {
        return Tax_Amount;
    }

    public void setTax_Amount(Float tax_Amount) {
        Tax_Amount = tax_Amount;
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
