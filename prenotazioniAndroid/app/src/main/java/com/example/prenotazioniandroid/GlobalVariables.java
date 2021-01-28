package com.example.prenotazioniandroid;
import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;

/**All'interno di questa casse sono presenti le variabili globali in sharing tra le activities,
* NB: ho messo nel manifest android_name:".GlobalVariables".
* */
public class GlobalVariables extends Application {

    private static String userName;
    private static String ruolo;
    private static String link = "http://192.168.56.1:8080/prenotazioni/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String sessionID = null;

    public static AsyncHttpClient getClient(){return client;}

    public static String getSessionID() {
        return sessionID;
    }
    public static void setSessionID(String sessionID) {
        GlobalVariables.sessionID = sessionID;
    }
    public static String getLink() {
        return link;
    }
    public static String getRuolo() {
        return ruolo;
    }
    public static String getUserName() {
        return userName;
    }
    public static void setLink(String link) {
        GlobalVariables.link = link;
    }
    public static void setRuolo(String ruolo) {
        GlobalVariables.ruolo = ruolo;
    }
    public static void setUserName(String userName) {
        GlobalVariables.userName = userName;
    }
}
