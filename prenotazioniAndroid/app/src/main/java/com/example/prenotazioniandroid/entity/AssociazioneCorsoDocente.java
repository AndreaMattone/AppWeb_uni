package com.example.prenotazioniandroid.entity;

public class AssociazioneCorsoDocente {
    private String docenteACD;
    private String corsoACD;

    public AssociazioneCorsoDocente(String docenteACD, String corsoACD){
        this.docenteACD=docenteACD;
        this.corsoACD=corsoACD;
    }

    public String getCorsoACD() {
        return corsoACD;
    }

    public String getDocenteACD() {
        return docenteACD;
    }

    @Override
    public String toString() {
        return docenteACD + " " + corsoACD;
    }

}
