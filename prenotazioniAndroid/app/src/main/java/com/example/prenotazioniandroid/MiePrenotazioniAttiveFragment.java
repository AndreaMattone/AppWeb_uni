package com.example.prenotazioniandroid;
import com.example.prenotazioniandroid.entity.Prenotazione;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MiePrenotazioniAttiveFragment extends Fragment {

    Spinner id_prenAttEffettuaSpinner;
    Spinner id_prenAttDisdiciSpinner;
    int codiceSelezionatoEffettuata;
    int codiceSelezionatoDisdetta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mie_prenotazioni_attive, container, false);
    }




    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        id_prenAttEffettuaSpinner = view.findViewById(R.id.id_prenAttEffettuaSpinner);
        id_prenAttDisdiciSpinner = view.findViewById(R.id.id_prenAttDisdiciSpinner);



        /**
         *  INIZIO VALORIZZANDO LA LIST VIEW CON LE PRENOTAZIONI ATTIVE, in questa chiamata Http non verifico la sessione lato client
         *  perchè è eseguita all'inizio della on viewCreated, perciò significa che la sessione è attiva in questo momento, dato
         *  che solo se la sessione è attiva si sarebbe potuto chiamare questo fragment
         * */
        AsyncHttpClient client = GlobalVariables.getClient();
        RequestParams params = new RequestParams();
        params.put("action", "android_getPrenotazioniAttive");
        params.put("userName", GlobalVariables.getUserName());
        params.put("sessionID", GlobalVariables.getSessionID());
        client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    /*Recupero gli oggetti JSON relativi alla risposta con l'elenco delle prenotazioni attive
                    * */
                    int responseLength = response.length();
                    ArrayList<JSONObject> myAttive = new ArrayList<>();
                    for(int c=0;c<responseLength;c++){
                        myAttive.add(response.getJSONObject(c));
                    }
                    //System.out.println(myAttive.toString());

                    /*Valorizzo la listView
                    * */
                    ArrayList<String> prenAtt = new ArrayList<>();
                    ArrayList<String> codici = new ArrayList<>();

                    for(int c=0;c<responseLength;c++){
                        JSONObject tmpObj = myAttive.get(c);
                        int idPrenotazione = (int)tmpObj.get("idPrenotazione");
                        String utenteP = tmpObj.get("utenteP").toString();
                        String docenteP = tmpObj.get("docenteP").toString();
                        String corsoP = tmpObj.get("corsoP").toString();
                        String giornoRipetizione = tmpObj.get("giornoRipetizione").toString();
                        int slotRipetizione = (int)tmpObj.get("slotRipetizione");
                        int statoRipetizione = (int)tmpObj.get("statoRipetizione");
                        prenAtt.add("[COD: "+idPrenotazione+"] docente:"+docenteP+" corso: "+corsoP+" del giorno: "+giornoRipetizione+" nel seguente slot: "+slotRipetizione);
                        codici.add(idPrenotazione+"");
                    }
                    ListView activeListView =  (ListView) getView().findViewById(R.id.activeListViewAtt);
                    ArrayAdapter<String> adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),
                            android.R.layout.simple_list_item_1, prenAtt);
                    activeListView.setAdapter(adapter);

                    /*Valorizzo gli spinner*/
                    ArrayAdapter<String> adapter1  = new ArrayAdapter<String> (getContext().getApplicationContext(),
                            android.R.layout.simple_list_item_1, codici);
                    id_prenAttDisdiciSpinner.setAdapter(adapter1);
                    id_prenAttEffettuaSpinner.setAdapter(adapter1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("ERRORE in MiePrenotazioniAttive");
                Log.d("failure", String.valueOf(throwable));
            }

        });






        /**
         * GESTISCO "SEGNA UNA PRENOTAZIONE COME EFFETTUATA" e in più verifico la sessione
         * */
        view.findViewById(R.id.id_btnCommit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked btnCommit 1");
                String cod = id_prenAttEffettuaSpinner.getSelectedItem().toString();
                int ok = 0;
                codiceSelezionatoEffettuata=-1;
                try {
                    codiceSelezionatoEffettuata = Integer.parseInt(cod);
                    ok=1;
                } catch (NumberFormatException n) {
                    ok=0;
                }
                /*Se il codice inserito è un integer*/
                if(ok==1){
                    System.out.println(codiceSelezionatoEffettuata);

                    /** Verifico che la sessione utente sia attiva
                     * */
                    AsyncHttpClient client = GlobalVariables.getClient();
                    RequestParams params = new RequestParams();
                    params.put("action", "android_checkSession");
                    params.put("sessionID", GlobalVariables.getSessionID());
                    client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                String responseValue = response.getString(0);
                                if(responseValue.equals("sessionOk")){

                                    /**
                                     * Se la sessione è attiva allora posso segnare come effettuata la prenotazione*/
                                    AsyncHttpClient client = GlobalVariables.getClient();
                                    RequestParams params = new RequestParams();
                                    params.put("action", "android_segnaComeEffettuata");
                                    params.put("userName", GlobalVariables.getUserName());
                                    params.put("idPren", codiceSelezionatoEffettuata);
                                    params.put("sessionID", GlobalVariables.getSessionID());
                                    client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
                                        @SuppressLint("ShowToast")
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                            super.onSuccess(statusCode, headers, response);

                                            try {
                                                String ret = response.getString(0);
                                                System.out.println(ret);
                                                if(ret.equals("ok")){
                                                    Toast.makeText(getContext(),"OPERAZIONE ANDATA A BUON FINE",Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(getContext(),"ERRORE",Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            Log.d("failure", String.valueOf(throwable));
                                        }

                                    });



                                }else{
                                    Toast.makeText(getContext(),"Sessione utente scaduta, effettua nuovamente il Login",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Log.d("failure", String.valueOf(throwable));
                        }

                    });




                }else{
                    Toast.makeText(getContext(),"Invalid number",Toast.LENGTH_SHORT).show();
                    System.out.println("Invalid number");
                }

            }
        });



        /**
         * GESTISCO "SEGNA UNA PRENOTAZIONE COME DISDETTA"
         * */
        view.findViewById(R.id.id_btnCommit2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked btnCommit 2");
                String cod = id_prenAttDisdiciSpinner.getSelectedItem().toString();
                int ok = 0;
                codiceSelezionatoDisdetta=-1;
                try {
                    codiceSelezionatoDisdetta = Integer.parseInt(cod);
                    ok=1;
                } catch (NumberFormatException n) {
                    ok=0;
                }

                if(ok==1){
                    System.out.println(codiceSelezionatoDisdetta);

                    AsyncHttpClient client = GlobalVariables.getClient();
                    RequestParams params = new RequestParams();
                    params.put("action", "android_checkSession");
                    params.put("sessionID", GlobalVariables.getSessionID());

                    client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                String responseValue = response.getString(0);
                                if(responseValue.equals("sessionOk")){
                                    AsyncHttpClient client = GlobalVariables.getClient();
                                    RequestParams params = new RequestParams();
                                    params.put("action", "android_segnaComeDisdetta");
                                    params.put("userName", GlobalVariables.getUserName());
                                    params.put("idPren", codiceSelezionatoDisdetta);
                                    params.put("sessionID", GlobalVariables.getSessionID());
                                    client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
                                        @SuppressLint("ShowToast")
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                            super.onSuccess(statusCode, headers, response);

                                            try {
                                                String ret = response.getString(0);
                                                System.out.println(ret);
                                                if(ret.equals("ok")){
                                                    Toast.makeText(getContext(),"OPERAZIONE ANDATA A BUON FINE",Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(getContext(),"ERRORE",Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            Log.d("failure", String.valueOf(throwable));
                                        }

                                    });
                                }else{
                                    Toast.makeText(getContext(),"Sessione utente scaduta, effettua nuovamente il Login",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Log.d("failure", String.valueOf(throwable));
                        }

                    });



                }else{
                    Toast.makeText(getContext(),"Invalid number",Toast.LENGTH_SHORT).show();
                    System.out.println("Invalid number");
                }


            }
        });



    }
}