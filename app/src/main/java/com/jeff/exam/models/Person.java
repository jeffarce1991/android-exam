package com.jeff.exam.models;

import android.util.Log;

import com.jeff.exam.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Person{
    private int id;
    private String firstName;
    private String lastName;
    private String birthday;


    private String age;
    private String email;
    private String mobileNumber;
    private String address;
    private String contactPerson;
    private String contactPersonMobileNumber;
    //private List<Contact> contacts;

    public Person() {
    }

    public Person(int id, String firstName, String lastName, String birthday, String age, String email, String mobileNumber, String address, String contactPerson, String contactPersonMobileNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.age = age;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.contactPerson = contactPerson;
        this.contactPersonMobileNumber = contactPersonMobileNumber;
    }

    public Person(String firstName, String lastName, String birthday, String age, String email, String mobileNumber, String address, String contactPerson, String contactPersonMobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.age = age;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.contactPerson = contactPerson;
        this.contactPersonMobileNumber = contactPersonMobileNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getAge(){
        return String.valueOf(Util.calculateAge(getBirthday(), "m/d/Y"));
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonMobileNumber() {
        return contactPersonMobileNumber;
    }

    public void setContactPersonMobileNumber(String contactPersonMobileNumber) {
        this.contactPersonMobileNumber = contactPersonMobileNumber;
    }
}
