package com.example.dantas.calendallpro.model;

public class Usuario {

    private String user;
    private String pass;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Usuario(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
}
