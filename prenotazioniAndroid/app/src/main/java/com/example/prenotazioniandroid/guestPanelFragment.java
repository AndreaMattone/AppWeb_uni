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
import android.widget.Button;
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

import cz.msebera.android.httpclient.Header;

public class guestPanelFragment extends Fragment {

    Spinner mySpinner;
    TextView id_textViewStaiPrenotandoPer;
    Spinner id_spinnerPrenotazione;
    String corsoActual=null;
    String docenteActual=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_panel, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mySpinner = view.findViewById(R.id.id_mySpinner);
        id_textViewStaiPrenotandoPer = view.findViewById(R.id.id_textViewStaiPrenotandoPer);
        id_spinnerPrenotazione = view.findViewById(R.id.id_spinnerPrenotazione);


        AsyncHttpClient client = GlobalVariables.getClient();
        RequestParams params = new RequestParams();
        params.put("action", "android_getACDguest");
        params.put("userName", GlobalVariables.getUserName());

        /**Qua non al gestisco la sessione*/
        client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    /*Recupero gli oggetti JSON relativi alla risposta con l'elenco delle ACD
                     * */
                    int responseLength = response.length();
                    ArrayList<JSONObject> myACD = new ArrayList<>();
                    for(int c=0;c<responseLength;c++){
                        myACD.add(response.getJSONObject(c));
                    }
                    //System.out.println(myACD.toString());


                    /*Devo ora valorizzare lo spinner*/
                    /*Valorizzo la listView
                     * */
                    ArrayList<String> ACD = new ArrayList<>();
                    for(int c=0;c<responseLength;c++){
                        JSONObject tmpObj = myACD.get(c);

                        String docente = tmpObj.get("docenteACD").toString();
                        String corso = tmpObj.get("corsoACD").toString();

                        ACD.add(corso + "-" + docente);
                    }

                    ArrayAdapter<String> adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),
                            android.R.layout.simple_list_item_1, ACD);
                    mySpinner.setAdapter(adapter);


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





        /**
         * GESTISCO LA SCELTA DELL'associazione corso docente con cui si vuole fare la ripetizione
         * valorizzo lo spinner delle prenotazioni"
         * */
        view.findViewById(R.id.id_btnChoosedACD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String acdChoosed = mySpinner.getSelectedItem().toString();
                System.out.println(acdChoosed);
                String[] parts = acdChoosed.split("-");
                String corso = parts[0];
                String docente = parts[1];
                corsoActual=corso;
                docenteActual=docente;
                id_textViewStaiPrenotandoPer.setText("Stai prenotando per il corso "+corsoActual+" con il docente "+docenteActual);
                AsyncHttpClient client = GlobalVariables.getClient();
                RequestParams params = new RequestParams();
                params.put("action", "android_getPrenotazioniChoosedACDguest");
                params.put("userName", GlobalVariables.getUserName());
                params.put("docente", docenteActual);
                params.put("corso", corsoActual);

                client.post(GlobalVariables.getLink() + "Servlet", params, new JsonHttpResponseHandler() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {

                            /*Recupero gli oggetti JSON relativi alla risposta con l'elenco delle ACD
                             * */
                            int responseLength = response.length();
                            ArrayList<JSONObject> prenotazioniAttiveACDfiltered = new ArrayList<>();
                            for(int c=0;c<responseLength;c++){
                                prenotazioniAttiveACDfiltered.add(response.getJSONObject(c));
                            }
                            //System.out.println(myACD.toString());

                            /*Devo ora valorizzare lo spinner*/
                            /*Valorizzo la listView
                             * */

                            ArrayList<String> DaySlotOccuped = new ArrayList<>();
                            for(JSONObject p: prenotazioniAttiveACDfiltered){
                                DaySlotOccuped.add(p.get("giornoRipetizione")+"-"+p.get("slotRipetizione"));
                            }

                            ArrayList<String> DaySlotFree = new ArrayList<>();

                            if(!DaySlotOccuped.contains("LUN-1")){DaySlotFree.add("LUN-15/16");}
                            if(!DaySlotOccuped.contains("LUN-2")){DaySlotFree.add("LUN-16/17");}
                            if(!DaySlotOccuped.contains("LUN-3")){DaySlotFree.add("LUN-17/18");}
                            if(!DaySlotOccuped.contains("LUN-4")){DaySlotFree.add("LUN-18/19");}
                            if(!DaySlotOccuped.contains("MAR-1")){DaySlotFree.add("MAR-15/16");}
                            if(!DaySlotOccuped.contains("MAR-2")){DaySlotFree.add("MAR-16/17");}
                            if(!DaySlotOccuped.contains("MAR-3")){DaySlotFree.add("MAR-17/18");}
                            if(!DaySlotOccuped.contains("MAR-4")){DaySlotFree.add("MAR-18/19");}
                            if(!DaySlotOccuped.contains("MER-1")){DaySlotFree.add("MER-15/16");}
                            if(!DaySlotOccuped.contains("MER-2")){DaySlotFree.add("MER-16/17");}
                            if(!DaySlotOccuped.contains("MER-3")){DaySlotFree.add("MER-17/18");}
                            if(!DaySlotOccuped.contains("MER-4")){DaySlotFree.add("MER-18/19");}
                            if(!DaySlotOccuped.contains("GIO-1")){DaySlotFree.add("GIO-15/16");}
                            if(!DaySlotOccuped.contains("GIO-2")){DaySlotFree.add("GIO-16/17");}
                            if(!DaySlotOccuped.contains("GIO-3")){DaySlotFree.add("GIO-17/18");}
                            if(!DaySlotOccuped.contains("GIO-4")){DaySlotFree.add("GIO-18/19");}
                            if(!DaySlotOccuped.contains("VEN-1")){DaySlotFree.add("VEN-15/16");}
                            if(!DaySlotOccuped.contains("VEN-2")){DaySlotFree.add("VEN-16/17");}
                            if(!DaySlotOccuped.contains("VEN-3")){DaySlotFree.add("VEN-17/18");}
                            if(!DaySlotOccuped.contains("VEN-4")){DaySlotFree.add("VEN-18/19");}

                            System.out.println(DaySlotFree);

                            ArrayAdapter<String> adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),
                                    android.R.layout.simple_list_item_1, DaySlotFree);
                            id_spinnerPrenotazione.setAdapter(adapter);

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








    }

}