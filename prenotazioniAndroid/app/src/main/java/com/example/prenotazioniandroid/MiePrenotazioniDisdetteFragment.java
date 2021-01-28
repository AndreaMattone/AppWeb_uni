package com.example.prenotazioniandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MiePrenotazioniDisdetteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mie_prenotazioni_disdette, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /***
         * //TODO
         */
        AsyncHttpClient client = GlobalVariables.getClient();
        RequestParams params = new RequestParams();
        params.put("action", "android_getPrenotazioniDisdette");
        params.put("userName", GlobalVariables.getUserName());
        params.put("sessionID", GlobalVariables.getSessionID());
        client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    /*Recupero gli oggetti JSON relativi alla risposta con l'elenco delle prenotazioni disdette
                     * */
                    int responseLength = response.length();
                    ArrayList<JSONObject> myDisdette = new ArrayList<>();
                    for(int c=0;c<responseLength;c++){
                        myDisdette.add(response.getJSONObject(c));
                    }
                    //System.out.println(myEffettuate.toString());

                    /*Valorizzo la listView
                     * */
                    ArrayList<String> prenDis = new ArrayList<>();
                    for(int c=0;c<responseLength;c++){
                        JSONObject tmpObj = myDisdette.get(c);
                        int idPrenotazione = (int)tmpObj.get("idPrenotazione");
                        String utenteP = tmpObj.get("utenteP").toString();
                        String docenteP = tmpObj.get("docenteP").toString();
                        String corsoP = tmpObj.get("corsoP").toString();
                        String giornoRipetizione = tmpObj.get("giornoRipetizione").toString();
                        int slotRipetizione = (int)tmpObj.get("slotRipetizione");
                        int statoRipetizione = (int)tmpObj.get("statoRipetizione");
                        prenDis.add("Docente:"+docenteP+" corso: "+corsoP+" del giorno: "+giornoRipetizione+" nel seguente slot: "+slotRipetizione);
                    }
                    ListView activeListViewEff =  (ListView) getView().findViewById(R.id.activeListViewDis);
                    ArrayAdapter<String> adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),
                            android.R.layout.simple_list_item_1, prenDis);
                    activeListViewEff.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("ERRORE in MiePrenotazioniEffettuate");
                Log.d("failure", String.valueOf(throwable));
            }

        });





    }




}