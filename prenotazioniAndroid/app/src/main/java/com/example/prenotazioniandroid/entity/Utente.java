package com.example.prenotazioniandroid.entity;

public class Utente {
    private String userName;
    private String passwordUtente;
    private int ruoloUtente;

    public Utente(String userName,String passwordUtente,int ruoloUtente){
        this.userName=userName;
        this.passwordUtente=passwordUtente;
        this.ruoloUtente=ruoloUtente;
    }


    public int getRuoloUtente() {
        return ruoloUtente;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordUtente() {
        return passwordUtente;
    }

    @Override
    public String toString() {
        return userName + " " + passwordUtente + " " + ruoloUtente;
    }

}
