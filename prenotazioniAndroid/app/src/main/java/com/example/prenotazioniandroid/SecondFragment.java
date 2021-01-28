/**
 * @ Author Andrea Mattone
 * CONTROLLERS FRAGMENT
 * */

package com.example.prenotazioniandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class SecondFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvLoggedIn = view.findViewById(R.id.id_loggedInAs);
        tvLoggedIn.setText("Logged in as " + GlobalVariables.getUserName() + " with role " + GlobalVariables.getRuolo());


        /*Handler bottone Visualizza mie prenotazioni Attive*/
        view.findViewById(R.id.id_BtnVisualizzaMiePrenotazioniAttive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                NavHostFragment.findNavController(SecondFragment.this)
                                        .navigate(R.id.action_SecondFragment_to_MiePrenotazioniAttiveFragment);
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

            }
        });

        /*Handler bottone Visualizza mie prenotazioni Effettuate*/
        view.findViewById(R.id.id_BtnVisualizzaMiePrenotazioniEffettuate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                NavHostFragment.findNavController(SecondFragment.this)
                                        .navigate(R.id.action_SecondFragment_to_MiePrenotazioniEffettuateFragment);
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

            }
        });

        /*Handler bottone Visualizza mie prenotazioni Disdette*/
        view.findViewById(R.id.id_BtnVisualizzaMiePrenotazioniDisdette).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                NavHostFragment.findNavController(SecondFragment.this)
                                        .navigate(R.id.action_SecondFragment_to_MiePrenotazioniDisdetteFragment);
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

            }
        });



        /*Handler bottone Prenotazioni disponibili*/
        view.findViewById(R.id.id_BtnPrenotazioniDisponibili).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                NavHostFragment.findNavController(SecondFragment.this)
                                        .navigate(R.id.action_SecondFragment_to_PrenotazioniDisponibiliFragment);
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

            }
        });



        /*Handler bottone di logout*/
        view.findViewById(R.id.id_btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Resetto i valori delle variabili globali*/
                GlobalVariables.setUserName(null);
                GlobalVariables.setRuolo(null);


                AsyncHttpClient client = GlobalVariables.getClient();
                RequestParams params = new RequestParams();
                params.put("action", "android_Logout");
                params.put("sessionID", GlobalVariables.getSessionID());

                /*Effettuo una richiesta http per la verifica dell'utente*/
                client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        //DO NOTHING
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("failure", String.valueOf(throwable));
                    }

                });

                GlobalVariables.setSessionID("null");
                /*Torno alla pagina di login*/
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

            }
        });



    }
}