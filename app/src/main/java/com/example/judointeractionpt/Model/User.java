package com.example.judointeractionpt.Model;

public class User {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String phonenumber;
    private String judoclub;
    private String countrycode;
    private String phonenumber_withoutCountryCode;


    public User() {
    }

    public User(String firstname, String lastname, String email, String username, String phoneNumber, String judoClub, String countryCode, String phoneNumber_withoutCountryCode) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.phonenumber = phoneNumber;
        this.judoclub = judoClub;
        this.countrycode = countryCode;
        this.phonenumber_withoutCountryCode = phoneNumber_withoutCountryCode;
    }

    public void setPhonenumber_withoutCountryCode(String phoneNumber_withoutCountryCode)
    {
        this.phonenumber_withoutCountryCode = phoneNumber_withoutCountryCode;
    }

    public String getPhonenumber_withoutCountryCode()
    {
        return phonenumber_withoutCountryCode;
    }

    public void setCountrycode(String countryCode)
    {
        this.countrycode = countryCode;
    }

    public String getCountrycode()
    {
        return  countrycode;
    }

    public void setPhonenumber(String phoneNumber)
    {
        this.phonenumber = phoneNumber;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setJudoclub(String judoClub)
    {
        this.judoclub = judoClub;
    }

    public String getJudoclub()
    {
        return judoclub;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}