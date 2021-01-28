/**
 * @ Author Andrea Mattone
 * LOGIN FRAGMENT
 * */

package com.example.prenotazioniandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FirstFragment extends Fragment {

    EditText userNameEt;
    EditText passwordEt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginButton = view.findViewById(R.id.button_first);
        Button guestButton = view.findViewById(R.id.id_btnGuestAccess);
        userNameEt = view.findViewById(R.id.id_userNameLoginEt);
        passwordEt = view.findViewById(R.id.id_pswEt);

        /*Campo di testo che indica l'avvenuto login o l'errore*/
        final TextView textViewErrorLogin = view.findViewById(R.id.id_textViewErrorLogin);
        textViewErrorLogin.setText("");




        /*Listener del bottone del "login", effettua nel DB un controllo sulla correttezza ed esistenza
        * dell'utente che tenta di registrarsi e rimanda ai controlli di conseguenza
        * */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                /*Recupero i valori di username e psw inseriti nei form*/
                final String userName = userNameEt.getText().toString();
                String password = passwordEt.getText().toString();
                AsyncHttpClient client = GlobalVariables.getClient();
                RequestParams params = new RequestParams();
                params.put("action", "android_Login");
                params.put("userName", userName);
                params.put("password", password);

                /*Effettuo una richiesta http per la verifica dell'utente*/
                client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String temp = response.getString(0);
                            String parts[] = temp.split("-");

                            String ruolo = parts[0];
                            GlobalVariables.setSessionID(parts[1]);

                            System.out.println("Ruolo: "+ruolo + " sessionID: "+GlobalVariables.getSessionID());
                            /*Se l'utente è un admin o un user allora gli permetto di visualizzare i controller*/
                            if(ruolo.equals("admin")){
                                GlobalVariables.setUserName(userName);
                                GlobalVariables.setRuolo("admin");
                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                            }else if(ruolo.equals("user")){
                                GlobalVariables.setUserName(userName);
                                GlobalVariables.setRuolo("user");
                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                            }else{
                                GlobalVariables.setRuolo("guest");
                                textViewErrorLogin.setText("Error during login...");
                                //TODO if guest
                                //if not registered DO NOTHING
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        System.out.println("ERRORE in Login");
                        Log.d("failure", String.valueOf(throwable));
                    }

                });


            }

        });

        guestButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_guestFragment);
            }
        });



    }






}