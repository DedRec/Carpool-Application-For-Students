package com.example.drivercarpool;

public class Helper {
    String name, email, username, password, userid;

    public Helper() {
    }

    public Helper(String name, String email, String username, String password, String userid) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
