package com.example.prenotazioniandroid.entity;

public class Prenotazione {
    private int idPrenotazione;
    private String utenteP;
    private String docenteP;
    private String corsoP;
    private String giornoRipetizione;
    private int slotRipetizione;
    private int statoRipetizione;

    public Prenotazione(int idPrenotazione, String utenteP, String docenteP, String corsoP, String giornoRipetizione, int slotRipetizione, int statoRipetizione){
        this.idPrenotazione=idPrenotazione;
        this.utenteP=utenteP;
        this.docenteP=docenteP;
        this.corsoP=corsoP;
        this.giornoRipetizione=giornoRipetizione;
        this.slotRipetizione=slotRipetizione;
        this.statoRipetizione=statoRipetizione;
    }

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public String getUtenteP() {
        return utenteP;
    }

    public String getDocenteP() {
        return docenteP;
    }

    public String getCorsoP() {
        return corsoP;
    }

    public String getGiornoRipetizione() {
        return giornoRipetizione;
    }

    public int getSlotRipetizione() {
        return slotRipetizione;
    }

    public int getStatoRipetizione() {
        return statoRipetizione;
    }

    @Override
    public String toString() {
        return idPrenotazione + " " + utenteP + " " + docenteP + " " + corsoP + " " + giornoRipetizione + " " + slotRipetizione + " " + statoRipetizione;
    }

}
