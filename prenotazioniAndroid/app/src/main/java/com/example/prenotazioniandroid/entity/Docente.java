package com.example.prenotazioniandroid.entity;

public class Docente {
    private String professorName;

    public Docente(String professorName) {
        this.professorName=professorName;
    }

    public String getProfessorName() {
        return professorName;
    }

    @Override
    public String toString() {
        return professorName;
    }

}
