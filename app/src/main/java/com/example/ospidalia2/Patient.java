package com.example.ospidalia2;

/**
 * Created by Mostafa POP on 8/15/2017.
 */

public class Patient {
    String mail;
    String password;
    String name;
    String gender;

    public Patient() {
    }

    public Patient(String mail, String password, String name, String gender) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
