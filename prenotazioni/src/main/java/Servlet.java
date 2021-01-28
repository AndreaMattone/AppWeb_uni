import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name="Servlet",urlPatterns={"/Servlet"})
public class Servlet extends HttpServlet {

    /*E' utilizzato per inizializzare le variabili della servlet, invocato dopo il caricamento nel web container della servlet*/
    public void init(ServletConfig conf) throws ServletException {
        DAO.registerDriver();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch(action) {
            case "postLogin":
                processRequestLogin(request,response);
                break;
            case "postLogout":
                processRequestLogout(request,response);
                break;
            case "postCorso":
                processRequestPostCorso(request,response);
                break;
            case "postDocente":
                processRequestPostDocente(request,response);
                break;
            case "postACD":
                processRequestPostACD(request,response);
                break;
            case "postRmvCorso":
                processRequestRmvCorso(request,response);
                break;
            case "postRmvDocente":
                processRequestRmvDocente(request,response);
                break;
            case "postRmvACD":
                processRequestRmvACD(request,response);
                break;
            case "postPrenota":
                processRequestEffettuaPrenotazione(request,response);
                break;
            case "postPrenEffettuata":
                processRequesSegnaPrenComeEffettuata(request,response);
                break;
            case "postDisdiciPren":
                processRequestDisdiciPrenotazione(request,response);
                break;
            /*In questo caso passo nel metodo post per evitare la limitazione di invio caratteri (256) gelle get*/
            case "getPrenotazioniUtenteAttive":
                processRequestGetPrenotazioniUtenteAttive(request,response);
                break;
            case "getPrenotazioniUtenteEffettuate":
                processRequestGetPrenotazioniUtenteEffettuate(request,response);
                break;
            case "getPrenotazioniUtenteDisdette":
                processRequestGetPrenotazioniUtenteDisdette(request,response);
                break;
            case "getPrenotazioniTuttiAttive":
                processRequestGetPrenotazioniTuttiAttive(request,response);
                break;
            case "getPrenotazioniTuttiEffettuate":
                processRequestGetPrenotazioniTuttiEffettuate(request,response);
                break;
            case "getPrenotazioniTuttiDisdette":
                processRequestGetPrenotazioniTuttiDisdette(request,response);
                break;
            /**Chiamate android #########################################################
             * */
            case "android_Login":
                processRequestAndroidLogin(request,response);
                break;
            case "android_Logout":
                processRequestAndroidLogout(request,response);
                break;
            case "android_getPrenotazioniAttive":
                processRequestAndroidGetPrenotazioniAttive(request,response);
                break;
            case "android_getPrenotazioniEffettuate":
                processRequestAndroidGetPrenotazioniEffettuate(request,response);
                break;
            case "android_getPrenotazioniDisdette":
                processRequestAndroidGetPrenotazioniDisdette(request,response);
                break;
            case "android_segnaComeEffettuata":
                processRequestAndroidSegnaComeEffettuata(request,response);
                break;
            case "android_segnaComeDisdetta":
                processRequestAndroidSegnaComeDisdetta(request,response);
                break;
            case "android_getACD":
                processRequestAndroidGetACD(request,response);
                break;
            case "android_getPrenotazioniChoosedACD":
                processRequestAndroidGetPrenotazioniChoosedACD(request,response);
                break;
            case "android_putPrenotazione":
                processRequestAndroidPutPrenotazione(request,response);
                break;
            case "android_checkSession":
                processRequestAndroidCheckSession(request,response);
                break;
            case "android_getACDguest":
                processRequestAndroidGetACDGuest(request,response);
                break;
            case "android_getPrenotazioniChoosedACDguest":
                processRequestAndroidGetPrenotazioniChoosedACDGuest(request,response);
                break;

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch(action){
            case "getSessionIDs":
                processRequestGetSessionIDa(request,response);
                break;
            case "getDocenti":
                processRequestGetDocenti(request,response);
                break;
            case "getCorsi":
                processRequestGetCorsi(request,response);
                break;
            case "getACD":
                processRequestGetACD(request,response);
                break;
            case "getPrenotazioni": /*Per valorizzare le caselle della zona "effettua prenotazioni"*/
                processRequestGetPrenotazioni(request,response);
                break;
            case "checkSession":
                processRequestCheckSession(request,response);
                break;
            case "getACDGuest":
                processRequestGetACDGuest(request,response);
                break;
        }
    }





/**###################################################################################################################*/


    /**[WEB] Funzione utilizzata per restituire lo stato della sessione*/
    protected void processRequestCheckSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");
        PrintWriter out = response.getWriter();

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            response.setContentType("application/text");
            String ret = "ok";
            out.print(ret);
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }
    }


    /**[WEB] Funzioni che permettono di segnare le prenotazioni come effettuate o come disdette*/
    protected void processRequesSegnaPrenComeEffettuata(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && (s.getAttribute("ruolo").equals("admin") || s.getAttribute("ruolo").equals("user")) ) {

            int idPren = Integer.parseInt(request.getParameter("idPren"));
            if(idPren!=-1) {
                /*Recupero la prenotazione interessata*/
                ArrayList<Prenotazione> prenotazioni = DAO.recuperaPrenotazione();
                Prenotazione myPrenotazione = null;
                for (Prenotazione p : prenotazioni) {
                    if (p.getIdPrenotazione() == idPren) {
                        myPrenotazione = p;
                    }
                }

                if (myPrenotazione != null) {
                    String utente = "'" + myPrenotazione.getUtenteP() + "'";
                    String docente = "'" + myPrenotazione.getDocenteP() + "'";
                    String corso = "'" + myPrenotazione.getCorsoP() + "'";
                    String giorno = "'" + myPrenotazione.getGiornoRipetizione() + "'";
                    int slot = myPrenotazione.getSlotRipetizione();
                    int stato = 2;
                    DAO.rmvPrenotazione(utente, docente, corso, giorno, slot, 1);
                    int check = DAO.addPrenotazione(utente, docente, corso, giorno, slot, stato);
                    if (check == 1) {
                        out.print("Prenotazione segnata come effettuata.");
                    } else {
                        out.print("Errore");
                    }
                } else {
                    System.out.println("Errore in processRequesSegnaPrenComeEffettuata");
                }
            }else{
                System.out.println("Errore in processRequesSegnaPrenComeEffettuata");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }


    }

    protected void processRequestDisdiciPrenotazione(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && (s.getAttribute("ruolo").equals("admin") || s.getAttribute("ruolo").equals("user")) ) {
            int idPren = Integer.parseInt(request.getParameter("idPren"));
            if(idPren!=-1) {
                /*Recupero la prenotazione interessata*/
                ArrayList<Prenotazione> prenotazioni = DAO.recuperaPrenotazione();
                Prenotazione myPrenotazione = null;
                for (Prenotazione p : prenotazioni) {
                    if (p.getIdPrenotazione() == idPren) {
                        myPrenotazione = p;
                    }
                }
                if (myPrenotazione != null) {
                    String utente = "'" + myPrenotazione.getUtenteP() + "'";
                    String docente = "'" + myPrenotazione.getDocenteP() + "'";
                    String corso = "'" + myPrenotazione.getCorsoP() + "'";
                    String giorno = "'" + myPrenotazione.getGiornoRipetizione() + "'";
                    int slot = myPrenotazione.getSlotRipetizione();
                    int stato = 3;
                    DAO.rmvPrenotazione(utente, docente, corso, giorno, slot, 1);
                    int check = DAO.addPrenotazione(utente, docente, corso, giorno, slot, stato);
                    if (check == 1) {
                        out.print("Prenotazione segnata come disdetta.");
                    } else {
                        out.print("Errore");
                    }
                } else {
                    System.out.println("Errore in processRequestDisdiciPrenotazione");
                }
            }else{
                System.out.println("Errore in processRequestDisdiciPrenotazione");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }

    }



    /**[WEB] Funzioni che restituiscono gli elenchi delle prenotazioni attive,effettuate e disdette di TUTTI gli utenti*/
    /*STATO 1*/
    protected void processRequestGetPrenotazioniTuttiAttive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Devo restituire solo quelle attive, ovvero con stato 1*/
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            response.setContentType("application/json");
            /*Recupero tutta la lista delle prenotazioni*/
            ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();
            /*Filtro nuovamente le ripetizioni tenendo soltanto quelle attive*/
            ArrayList<Prenotazione> toSend = new ArrayList<>();
            for (Prenotazione p: prenotazione) {
                /*Solo se la prenotazione è attiva allora la prendo, se è gia avvenuta o è stata cancellata non mi interessa*/
                if(p.getStatoRipetizione()==1){
                    toSend.add(p);
                }
            }
            /*in toSend ho l'elenco delle prenotazioni attualmente attive*/
            String json = new Gson().toJson(toSend);
            out.print(json);
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }



    }
    /*STATO 2*/
    protected void processRequestGetPrenotazioniTuttiEffettuate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Devo restituire solo quelle attive, ovvero con stato 1*/
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            response.setContentType("application/json");

            /*Recupero tutta la lista delle prenotazioni*/
            ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();
            /*Filtro nuovamente le ripetizioni tenendo soltanto quelle effettuate*/
            ArrayList<Prenotazione> toSend = new ArrayList<>();
            for (Prenotazione p: prenotazione) {
                /*Solo se la prenotazione è effettuata*/

                if(p.getStatoRipetizione()==2){

                    toSend.add(p);
                }
            }

            /*in toSend ho l'elenco delle prenotazioni attualmente attive*/
            String json = new Gson().toJson(toSend);
            out.print(json);
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }
    }
    /*STATO 3*/
    protected void processRequestGetPrenotazioniTuttiDisdette(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Devo restituire solo quelle attive, ovvero con stato 1*/
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            response.setContentType("application/json");
            /*Recupero tutta la lista delle prenotazioni*/
            ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();

            /*Filtro nuovamente le ripetizioni tenendo soltanto quelle disdette*/
            ArrayList<Prenotazione> toSend = new ArrayList<>();
            for (Prenotazione p: prenotazione) {
                /*Solo se la prenotazione è disdetta*/
                if(p.getStatoRipetizione()==3){
                    toSend.add(p);
                }
            }

            /*in toSend ho l'elenco delle prenotazioni attualmente attive*/
            String json = new Gson().toJson(toSend);
            out.print(json);
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }



    }



    /**[WEB] Funzioni che restituiscono gli elenchi delle prenotazioni attive,effettuate e disdette di UN determinato utente*/
    /*STATO 1*/
    protected void processRequestGetPrenotazioniUtenteAttive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Devo restituire solo quelle attive, ovvero con stato 1*/
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && (s.getAttribute("ruolo").equals("admin") || s.getAttribute("ruolo").equals("user")) ){

            response.setContentType("application/json");
            /*Recupero tutta la lista delle prenotazioni*/
            ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();
            /*Filtro la lista solo per l'utente che mi interessa*/
            String nomeUtente = (String)s.getAttribute("userName");
            if(nomeUtente!=null && !nomeUtente.isEmpty() && !nomeUtente.isBlank()){
                ArrayList<Prenotazione> prenotazioneFiltrata = new ArrayList<>();
                for (Prenotazione p: prenotazione) {
                    if (p.getUtenteP().equals(nomeUtente)) {
                        prenotazioneFiltrata.add(p);
                    }
                }

                /*Filtro nuovamente le ripetizioni tenendo soltanto quelle attive*/
                ArrayList<Prenotazione> toSend = new ArrayList<>();
                for (Prenotazione p: prenotazioneFiltrata) {
                    /*Solo se la prenotazione è attiva allora la prendo, se è gia avvenuta o è stata cancellata non mi interessa*/
                    if(p.getStatoRipetizione()==1){
                        toSend.add(p);
                    }
                }

                /*in toSend ho l'elenco delle prenotazioni attualmente attive*/
                String json = new Gson().toJson(toSend);
                out.print(json);
            }else{
                System.out.println("Errore in processRequestGetPrenotazioniUtenteAttive");
            }

        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }


    }
    /*STATO 2*/
    protected void processRequestGetPrenotazioniUtenteEffettuate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Devo restituire solo quelle attive, ovvero con stato 1*/
        PrintWriter out = response.getWriter();



        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && (s.getAttribute("ruolo").equals("admin") || s.getAttribute("ruolo").equals("user")) ) {


            response.setContentType("application/json");
            /*Recupero tutta la lista delle prenotazioni*/
            ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();

            /*Filtro la lista solo per l'utente che mi interessa*/

            String nomeUtente = (String) s.getAttribute("userName");
            if (nomeUtente != null && !nomeUtente.isEmpty() && !nomeUtente.isBlank()) {
                ArrayList<Prenotazione> prenotazioneFiltrata = new ArrayList<>();
                for (Prenotazione p : prenotazione) {
                    if (p.getUtenteP().equals(nomeUtente)) {
                        prenotazioneFiltrata.add(p);
                    }
                }

                /*Filtro nuovamente le ripetizioni tenendo soltanto quelle effettuate*/
                ArrayList<Prenotazione> toSend = new ArrayList<>();
                for (Prenotazione p : prenotazioneFiltrata) {
                    /*Solo se la prenotazione è effettuata*/
                    if (p.getStatoRipetizione() == 2) {
                        toSend.add(p);
                    }
                }

                /*in toSend ho l'elenco delle prenotazioni attualmente attive*/
                String json = new Gson().toJson(toSend);
                out.print(json);
            } else {
                System.out.println("Errore in processRequestGetPrenotazioniUtenteEffettuate");
            }

        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }


    }
    /*STATO 3*/
    protected void processRequestGetPrenotazioniUtenteDisdette(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Devo restituire solo quelle attive, ovvero con stato 1*/
        PrintWriter out = response.getWriter();


        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && (s.getAttribute("ruolo").equals("admin") || s.getAttribute("ruolo").equals("user")) ) {



            response.setContentType("application/json");
            /*Recupero tutta la lista delle prenotazioni*/
            ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();

            /*Filtro la lista solo per l'utente che mi interessa*/
            String nomeUtente = (String) s.getAttribute("userName");
            if (nomeUtente != null && !nomeUtente.isEmpty() && !nomeUtente.isBlank()) {
                ArrayList<Prenotazione> prenotazioneFiltrata = new ArrayList<>();
                for (Prenotazione p : prenotazione) {
                    if (p.getUtenteP().equals(nomeUtente)) {
                        prenotazioneFiltrata.add(p);
                    }
                }

                /*Filtro nuovamente le ripetizioni tenendo soltanto quelle disdette*/
                ArrayList<Prenotazione> toSend = new ArrayList<>();
                for (Prenotazione p : prenotazioneFiltrata) {
                    /*Solo se la prenotazione è disdetta*/
                    if (p.getStatoRipetizione() == 3) {
                        toSend.add(p);
                    }
                }

                /*in toSend ho l'elenco delle prenotazioni attualmente attive*/
                String json = new Gson().toJson(toSend);
                out.print(json);
            } else {
                System.out.println("Errore in processRequestGetPrenotazioniUtenteDisdette");
            }

        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }




    }



    /**[WEB] Funzioni per l'effettuazione di prenotazioni, in getPrenotazioni non c'è la verifica della sessione o del ruolo
     * in quanto i guest possono vedere l'elenco delle prenotazioni disponibili, mentre in effettua prenotazioni è presente il controllo in
     * quanto solo chi ha effettuato l'accesso puo effettuare la prenotazione*/
    /*Valorizza la zona delle prenotazioni da poter scegliere, restituendo l'elenco delle prenotazioni attive, che da pagina web userò per filtrare il "calendario" */
    protected void processRequestGetPrenotazioni(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        String ACDperPrenotazione = request.getParameter("ACDperPrenotazione");

        if(!ACDperPrenotazione.equals("null")){
            response.setContentType("application/json");
            String[] parts = request.getParameter("ACDperPrenotazione").split(" - ");
            String nomeCorso = parts[0];
            String nomeDocente = parts[1];


            if(nomeCorso!=null && nomeDocente!=null && !nomeCorso.isEmpty() && !nomeDocente.isEmpty() && !nomeCorso.isBlank() && !nomeDocente.isBlank()){
                int check = 0;
                /*Devo verificare che esista una associazione corso docente con questo docente e corso*/
                ArrayList<AssociazioneCorsoDocente> verificaACD = new ArrayList<>();
                verificaACD = DAO.recuperaAssociazioneCorsoDocente();
                for(AssociazioneCorsoDocente verAcd : verificaACD){
                    if(verAcd.getCorsoACD().equals(nomeCorso) && verAcd.getDocenteACD().equals(nomeDocente)){
                        /*Se esiste una ACD con questa associazione allora posso committare i dati*/
                        check=1;
                    }
                }

                if(check==1){
                    /*Recupero tutta la lista delle prenotazioni*/
                    ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();

                    /*Filtro la lista solo per il corso e il docente che mi interessa*/
                    ArrayList<Prenotazione> prenotazioneFiltrata = new ArrayList<>();
                    for (Prenotazione p: prenotazione) {
                        if (p.getDocenteP().equals(nomeDocente) && p.getCorsoP().equals(nomeCorso)) {
                            prenotazioneFiltrata.add(p);
                        }
                    }

                    /*Filtro nuovamente le ripetizioni tenendo soltanto quelle attive*/
                    ArrayList<Prenotazione> toSend = new ArrayList<>();
                    for (Prenotazione p: prenotazioneFiltrata) {
                        /*Solo se la prenotazione è attiva allora la prendo, se è gia avvenuta o è stata cancellata non mi interessa*/
                        if(p.getStatoRipetizione()==1){
                            toSend.add(p);
                        }
                    }

                    /*in toSend ho l'elenco delle prenotazioni attualmente attive*/
                    String json = new Gson().toJson(toSend);
                    out.print(json);
                    System.out.println("processRequestGetPrenotazioni OK");
                }else{
                    System.out.println("Errore in processRequestGetPrenotazioni");
                }
            }else{
                System.out.println("Errore in processRequestGetPrenotazioni");
            }
        }else{
            response.setContentType("application/text");
            out.print("error");
        }


    }
    /*TODO controllare se esiste una ACD corrispondente a quella ricevuta per essere sicuro di fare una prenotazione a una ACD esistente*/
    protected void processRequestEffettuaPrenotazione(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && (s.getAttribute("ruolo").equals("admin") || s.getAttribute("ruolo").equals("user")) ) {

            String[] prenotazioniRichieste = request.getParameter("prenotazioniRichieste").split(";");
            int numOccorrenze = Integer.parseInt(request.getParameter("occorrenze"));


            String[] parts = request.getParameter("ACDperPrenotazione").split(" - ");
            String nomeCorso = parts[0];
            String nomeDocente = parts[1];

            String utente = (String)s.getAttribute("userName");


            if(utente!=null && nomeCorso!=null && nomeDocente!=null && !utente.isBlank() && !nomeCorso.isBlank() && !nomeDocente.isBlank()
                    && !utente.isEmpty() && !nomeCorso.isEmpty() && !nomeDocente.isEmpty() && numOccorrenze!=0){

                utente = "'" + utente + "'";
                nomeCorso = "'" + nomeCorso + "'";
                nomeDocente = "'" + nomeDocente + "'";

                int index=0;
                String giorno=null;
                int mainCheck = 1;

                ArrayList<Prenotazione> verifyPrenArray = DAO.recuperaPrenotazione();
                while(index<numOccorrenze){


                    String[] prenot = prenotazioniRichieste[index].split("-");
                    String giornoT = "'" + prenot[0] + "'";
                    int slotT = Integer.parseInt(prenot[1]);

                    /**NB VERIFICO PRIMA CHE LA PRENOTAZIOEN NON SIA GIA ESISTENTE*/
                    boolean verifyPren = true;

                    for(Prenotazione p: verifyPrenArray){
                        if(("'"+p.getDocenteP()+"'").equals(nomeDocente) && ("'"+p.getCorsoP()+"'").equals(nomeCorso)
                                && p.getGiornoRipetizione().equals(prenot[0]) &&
                                (p.getSlotRipetizione() == Integer.parseInt(prenot[1])) ){
                            System.out.println("STAI TENTANDO DI EFEFTTUARE UNA PRENOTAZIONE GIA PRESENTE");
                            verifyPren=false;
                        }else{
                        }
                    }


                    if(verifyPren==true){
                        int check = DAO.addPrenotazione(utente,nomeDocente,nomeCorso,giornoT,slotT,1);
                        if(check!=1){
                            mainCheck=0;
                        }
                    }


                    index++;
                }

                if(mainCheck==1){
                    out.print("Prenotazioni effettuate con successo.");
                    System.out.println("Prenotazioni effettuate con successo");
                }else{
                    out.print("Errore nell'aggiunta delle prenotazioni'.");
                    System.out.println("Errore nell'aggiunta delle prenotazioni");
                }
            }else{
                System.out.println("Errore nell'aggiunta delle prenotazioni");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }



        out.close();
    }


    /**[WEB] Funzioni per la gestione delle richieste di rimozione di un utente/docente/ACD*/
    protected void processRequestRmvACD(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println(request.getParameter("nomeACD"));
        PrintWriter out = response.getWriter();


        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            String[] parts = request.getParameter("nomeACD").split(" - ");
            String nomeDocente1 = parts[0];
            String nomeCorso1 = parts[1];
            String nomeDocente=nomeDocente1;
            String nomeCorso=nomeCorso1;

            if(nomeDocente1!=null && !nomeDocente1.isEmpty() && !nomeDocente1.isBlank() && nomeCorso1!=null && !nomeCorso1.isEmpty() && !nomeCorso1.isBlank()) {
                nomeDocente = "'" + nomeDocente + "'";
                nomeCorso = "'" + nomeCorso + "'";
                int check = DAO.rmvAssociazioneCorsoDocente(nomeDocente,nomeCorso);
                if(check==1){

                    /*Se ho rimosso correttamente una Associazione corso docente devo verificare se ci sono prenotazioni
                     * ATTIVE con quella ACD che ho eliminato e rimuoverle, quelle disdette o gia effettuate NON
                     * le cancello perchè deve rimanere lo storico*/
                    ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();
                    for(Prenotazione p: prenotazione){
                        if(p.getCorsoP().equals(nomeCorso1) && p.getDocenteP().equals(nomeDocente1)&& p.getStatoRipetizione()==1){
                            DAO.rmvPrenotazione("'"+p.getUtenteP()+"'","'"+p.getDocenteP()+"'","'"+p.getCorsoP()+"'","'"+p.getGiornoRipetizione()+"'",p.getSlotRipetizione(),p.getStatoRipetizione());
                        }
                    }

                    out.print("Rimozione dell'associazione: '" + nomeDocente + "' - '" + nomeCorso +"' andata a buon fine.");
                    System.out.println("Rimozione dell'associazione corso docente andata a buon fine");
                }else{
                    out.print("Errore nella rimozione dell'associazione corso-docente.");
                    System.out.println("Errore nella rimozione dell'associazione corso-docente.");
                }
            }else{
                out.print("Errore nella rimozione dell'associazione corso-docente.");
                System.out.println("Errore nella rimozione dell'associazione corso-docente.");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }







        out.close();



    }

    protected void processRequestRmvDocente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            String nomeDocente1 = request.getParameter("nomeDocente");
            String nomeDocente=nomeDocente1;
            if(nomeDocente1!=null && !nomeDocente1.isEmpty() && !nomeDocente1.isBlank()){
                nomeDocente = "'" + nomeDocente + "'";
                int check = DAO.rmvDocente(nomeDocente);
                if(check==1){

                /*Se il docente viene correttamente rimosso dovrò rimuovere TUTTE le prenotazioni ATTIVE
                 relative a quel docente, ovvero che hanno quel professorName, non quelle gia effettuate o disdette perchè deve rimanere lo storico
                 */
                    ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();
                    for(Prenotazione p: prenotazione){
                        if(p.getDocenteP().equals(nomeDocente1) && p.getStatoRipetizione()==1){
                            DAO.rmvPrenotazione("'"+p.getUtenteP()+"'","'"+p.getDocenteP()+"'","'"+p.getCorsoP()+"'","'"+p.getGiornoRipetizione()+"'",p.getSlotRipetizione(),p.getStatoRipetizione());
                        }
                    }


                    out.print("Docente: '" + nomeDocente + "' rimosso correttamente.");
                    System.out.println("Docente rimosso correttamente");
                }else{
                    out.print("Errore nella rimozione del docente.");
                    System.out.println("Errore nella rimozione del docente.");
                }
            }else{
                out.print("Errore nella rimozione del docente.");
                System.out.println("Errore nella rimozione del docente.");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }



        out.close();

    }

    protected void processRequestRmvCorso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            String nomeCorso1 = request.getParameter("nomeCorso");
            String nomeCorso=nomeCorso1;
            if(nomeCorso1!=null && !nomeCorso1.isEmpty() && !nomeCorso1.isBlank()){
                nomeCorso = "'" + nomeCorso + "'";
                int check = DAO.rmvCorso(nomeCorso);
                if(check==1){

                /*Se il corso viene correttamente rimosso dovrò rimuovere TUTTE le prenotazioni ATTIVE
                 relative a quel corso, ovvero che hanno quel courseName, non quelle gia effettuate o disdette perchè deve rimanere lo storico
                 */
                    ArrayList<Prenotazione> prenotazione = DAO.recuperaPrenotazione();
                    for(Prenotazione p: prenotazione){
                        if(p.getCorsoP().equals(nomeCorso1) && p.getStatoRipetizione()==1){
                            DAO.rmvPrenotazione("'"+p.getUtenteP()+"'","'"+p.getDocenteP()+"'","'"+p.getCorsoP()+"'","'"+p.getGiornoRipetizione()+"'",p.getSlotRipetizione(),p.getStatoRipetizione());
                        }
                    }

                    out.print("Corso: '" + nomeCorso + "' rimosso correttamente.");
                    System.out.println("Corso rimosso correttamente");
                }else{
                    out.print("Errore nella rimozione del corso.");
                    System.out.println("Errore nella rimozione del corso.");
                }
            }else{
                out.print("Errore nella rimozione del corso.");
                System.out.println("Errore nella rimozione del corso.");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }

        out.close();

    }



    /**[WEB] Funzioni per la gestione delle richieste di elenchi di ACD/Corso/Docenti/ACDguest*/
    /*La ACD guest è utilizzata in caso della visualizzazione delle prenotazioni disponibili in quanto non è necessario il controllo
    * sulla sessione utente e sul ruolo*/
    protected void processRequestGetACDGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        ArrayList<AssociazioneCorsoDocente> acd = DAO.recuperaAssociazioneCorsoDocente();
        String json = new Gson().toJson(acd);
        out.print(json);

    }

    protected void processRequestGetACD(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            response.setContentType("application/json");
            ArrayList<AssociazioneCorsoDocente> acd = DAO.recuperaAssociazioneCorsoDocente();
            String json = new Gson().toJson(acd);
            out.print(json);
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }




    }

    protected void processRequestGetCorsi(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            response.setContentType("application/json");
            ArrayList<Corso> corsi = DAO.recuperaCorsi();
            String json = new Gson().toJson(corsi);
            out.print(json);
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }



    }

    protected void processRequestGetDocenti(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            response.setContentType("application/json");
            ArrayList<Docente> docenti = DAO.recuperaDocenti();
            String json = new Gson().toJson(docenti);
            out.print(json);
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }



    }


    /**[WEB] Funzioni per la gestione delle richieste di inserimento docenti/corsi/ACD*/
    protected void processRequestPostACD(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            String docenteACD = request.getParameter("nomeDocente");
            String corsoACD = request.getParameter("nomeCorso");
            if(docenteACD!=null && corsoACD!=null && !docenteACD.isEmpty() && !corsoACD.isEmpty() && !docenteACD.isBlank() && !corsoACD.isBlank()){
                docenteACD = "'" + docenteACD + "'";
                corsoACD = "'" + corsoACD + "'";

                int check = DAO.addAssociazioneCorsoDocente(docenteACD,corsoACD);
                if(check==1){
                    out.print("Associazione tra '" + docenteACD + "' e il corso '" + corsoACD + "' andata a buon fine");
                    System.out.println("Aggiunta dell'associazione corso-docente andata a buon fine");
                }else{
                    out.print("Errore nell'aggiunta dell'associazione corso-docente.");
                    System.out.println("Errore nell'aggiunta dell'associazione corso-docente");
                }
            }else{
                out.print("Errore nell'aggiunta dell'associazione corso-docente.");
                System.out.println("Errore nell'aggiunta dell'associazione corso-docente");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }

        out.close();
    }

    protected void processRequestPostDocente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            String nomeDocente = request.getParameter("nomeDocente");
            if(nomeDocente!=null && !nomeDocente.isEmpty() && !nomeDocente.isBlank()){
                nomeDocente = "'" + nomeDocente + "'";
                int check = DAO.addDocente(nomeDocente);
                if(check==1){
                    out.print("Docente: '" + nomeDocente + "' aggiunto correttamente.");
                    System.out.println("Docente aggiunto correttamente");
                }else{
                    out.print("Errore nell'aggiunta del docente.");
                    System.out.println("Errore nell'aggiunta del docente");
                }
            }else{
                out.print("Errore nell'aggiunta del docente.");
                System.out.println("Errore nell'aggiunta del docente");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }


        out.close();
    }

    protected void processRequestPostCorso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");

        if(sessionIdActual.equals(sessionIdReceived) && s.getAttribute("ruolo").equals("admin") ) {
            String titoloCorso = request.getParameter("titoloCorso");
            if(titoloCorso!=null && !titoloCorso.isEmpty() && !titoloCorso.isBlank()){

                titoloCorso = "'" + titoloCorso + "'";
                int check = DAO.addCorso(titoloCorso);
                if(check==1){
                    out.print("Corso: '" + titoloCorso + "' aggiunto correttamente.");
                    System.out.println("Corso aggiunto correttamente");
                }else{
                    out.print("Errore nell'aggiunta del corso.");
                    System.out.println("Errore nell'aggiunta del corso");
                }
            }else{
                out.print("Errore nell'aggiunta del corso.");
                System.out.println("Errore nell'aggiunta del corso");
            }
        }else{
            response.setContentType("application/text");
            String ret = "sessionFail";
            out.print(ret);
        }





        out.close();
    }



    /**[WEB]Funzioni di gestione del login e del logout*/
    protected void processRequestLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        /*Se la sessione è sempre la stessa, la invalido, se è una nuova non mi interessa invalidarla
        * */
        String actualSessionId = request.getParameter("sessionID");
        System.out.println("LOGOUT: "+ s.getAttribute("userName") + " cleared session with session id: "+ actualSessionId);
        if(s.getId().equals(actualSessionId)){
            s.invalidate();
        }

    }

    /*0=user, 1=admin, error=guest*/
    protected void processRequestLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String ret = null;
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        /*Per la sessione utente, mi salvo l'id della sessione attuale*/
        HttpSession s = request.getSession();
        String actualSessionId = s.getId();

        if(userName==null || password==null || userName.isBlank()  || password.isBlank() || userName.isEmpty() || password.isEmpty()){
            ret = "guest";
            out.print(ret);
        }else{
            int ruolo = DAO.recuperaRuolo(userName,password);
            if(ruolo!=-1){
                if(ruolo==1){
                    /*Salvo in sessione utente userName e ruolo, oltre che a ritornare il ruolo all'html*/
                    s.setAttribute("userName",userName);
                    s.setAttribute("ruolo", "admin");
                    ret = "admin";
                }else{
                    /*Salvo in sessione utente userName e ruolo, oltre che a ritornare il ruolo all'html*/
                    s.setAttribute("userName",userName);
                    s.setAttribute("ruolo", "user");
                    ret = "user";
                }
                out.print(ret);
                System.out.println("LOGIN: "+ userName + " with sessionID: "+ actualSessionId);
            }else{
                System.out.println("ERRORE: L'username o la password sono errati o inesistenti");
                ret = "guest";
                out.print(ret);
            }
        }
        out.close();
    }

    protected void processRequestGetSessionIDa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        String actualSessionId = s.getId();
        PrintWriter out = response.getWriter();
        out.print(actualSessionId);
    }








/**###################################################################################################################*/


    /**[Android] Restutuisce il ruolo e la sessione dell'utente che richiede il login*/
    protected void processRequestAndroidLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();

        PrintWriter out = response.getWriter();
        String ret = "";

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        /*Se gli input sono accettabili recupero il ruolo dell'utente e carico in sessione il ruolo e l'username*/
        if(userName!=null && password!=null && !userName.isEmpty() && !password.isEmpty()){
            int ruolo = DAO.recuperaRuolo(userName,password);
            if(ruolo!=-1){
                if(ruolo==1){
                    System.out.println("[Android] Logged admin with session " + s.getId());
                    s.setAttribute("userName", request.getParameter("userName"));
                    s.setAttribute("ruolo","admin");
                    ret = "admin";
                }else{
                    System.out.println("[Android] Logged user with session " + s.getId());
                    s.setAttribute("userName", request.getParameter("userName"));
                    s.setAttribute("ruolo","user");
                    ret = "user";
                }

            }else{
                System.out.println("[Android ] ERRORE: L'username o la password sono errati o inesistenti");
                s.setAttribute("ruolo","guest");
                ret = "guest";
            }

            /*Ritorno al client android il ruolo e il sessionID*/
            response.setContentType("application/json");
            Gson gson = new Gson();
            ret = ret+"-"+s.getId();
            out.print("[" + gson.toJson(ret)+ "]");

        }

    }

    /**[Android] Gestisce il logout pulendo la sessione utente*/
    protected void processRequestAndroidLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        /*Se la sessione è sempre la stessa, la invalido, se è una nuova non mi interessa invalidarla
         * */
        String actualSessionId = request.getParameter("sessionID");
        System.out.println("[Android] LOGOUT: cleared session with session id: "+ actualSessionId);
        if(s.getId().equals(actualSessionId)){
            s.invalidate();
        }
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        Gson gson = new Gson();
        out.print("[ok]");

    }

    /**[Android] Restituisce l'elenco delle prenotazioni attive*/
    protected void processRequestAndroidGetPrenotazioniAttive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        HttpSession s = request.getSession();

        /*Verifico che la sessione sia attiva e che l'utente sia un admin o un user*/
        if( (s.getId().equals(request.getParameter("sessionID"))) && ((s.getAttribute("ruolo")=="admin")
        || (s.getAttribute("ruolo")=="user"))){
            ArrayList<Prenotazione> myPrenotazioni = DAO.recuperaPrenotazione();

            /*Filtro in base solo alle prenotazioni attive*/
            ArrayList<Prenotazione> prenAtt = new ArrayList<>();
            for(Prenotazione p: myPrenotazioni){
                if(p.getStatoRipetizione()==1){
                    prenAtt.add(p);
                }
            }
            /*Filtro in base solo all'utente desiderato*/
            ArrayList<Prenotazione> toSendArray = new ArrayList<>();
            for(Prenotazione p: prenAtt){
                if(p.getUtenteP().equals(request.getParameter("userName"))){
                    toSendArray.add(p);
                }
            }

            Gson gson = new Gson();
            out.print(gson.toJson(toSendArray));
        }else{
            System.out.println("Error in processRequestAndroidGetPrenotazioniAttive");
        }


    }

    /**[Android] Restituisce l'elenco delle prenotazioni effettuate*/
    protected void processRequestAndroidGetPrenotazioniEffettuate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        HttpSession s = request.getSession();

        /*Verifico che la sessione sia attiva e che l'utente sia un admin o un user*/
        if( (s.getId().equals(request.getParameter("sessionID"))) && ((s.getAttribute("ruolo")=="admin")
                || (s.getAttribute("ruolo")=="user"))){
            ArrayList<Prenotazione> myPrenotazioni = DAO.recuperaPrenotazione();

            /*Filtro in base solo alle prenotazioni effettuate*/
            ArrayList<Prenotazione> prenEff = new ArrayList<>();
            for(Prenotazione p: myPrenotazioni){
                if(p.getStatoRipetizione()==2){
                    prenEff.add(p);
                }
            }

            /*Filtro in base solo all'utente desiderato*/
            ArrayList<Prenotazione> toSendArray = new ArrayList<>();
            for(Prenotazione p: prenEff){
                if(p.getUtenteP().equals(request.getParameter("userName"))){
                    toSendArray.add(p);
                }
            }

            Gson gson = new Gson();
            out.print(gson.toJson(toSendArray));
        }else{
            System.out.println("Error in processRequestAndroidGetPrenotazioniEffettuate");
        }



    }

    /**[Android] Restituisce l'elenco delle prenotazioni disdette*/
    protected void processRequestAndroidGetPrenotazioniDisdette(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        HttpSession s = request.getSession();

        /*Verifico che la sessione sia attiva e che l'utente sia un admin o un user*/
        if( (s.getId().equals(request.getParameter("sessionID"))) && ((s.getAttribute("ruolo")=="admin")
                || (s.getAttribute("ruolo")=="user"))){
            ArrayList<Prenotazione> myPrenotazioni = DAO.recuperaPrenotazione();

            /*Filtro in base solo alle prenotazioni attive*/
            ArrayList<Prenotazione> prenDis = new ArrayList<>();
            for(Prenotazione p: myPrenotazioni){
                if(p.getStatoRipetizione()==3){
                    prenDis.add(p);
                }
            }

            /*Filtro in base solo all'utente desiderato*/
            ArrayList<Prenotazione> toSendArray = new ArrayList<>();
            for(Prenotazione p: prenDis){
                if(p.getUtenteP().equals(request.getParameter("userName"))){
                    toSendArray.add(p);
                }
            }


            Gson gson = new Gson();
            out.print(gson.toJson(toSendArray));
        }else{
            System.out.println("Error in processRequestAndroidGetPrenotazioniDisdette");
        }


    }

    /**[Android] Segna una prenotazione attiva come effettuata*/
    protected void processRequestAndroidSegnaComeEffettuata(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        HttpSession s = request.getSession();
        String idPren = request.getParameter("idPren");
        String userName = request.getParameter("userName");
        int ok=0;
        String ret = "ko";
        try{
            int id = Integer.parseInt(idPren);

            /*Verifico inoltre il ruolo dell'utente che la richiede*/
            if(userName!=null && !userName.isEmpty() && ((s.getAttribute("ruolo")=="admin")
                    || (s.getAttribute("ruolo")=="user")) ){
                ArrayList<Prenotazione> myP = DAO.recuperaPrenotazione();
                for(Prenotazione p : myP){
                    if(p.getIdPrenotazione()==id && p.getUtenteP().equals(userName) && p.getStatoRipetizione()==1){
                        String utente = "'"+p.getUtenteP()+"'";
                        String docente = "'"+p.getDocenteP()+"'";
                        String corso = "'"+p.getCorsoP()+"'";
                        String giorno = "'"+p.getGiornoRipetizione()+"'";
                        int slotRipetizione = p.getSlotRipetizione();

                        DAO.rmvPrenotazione(utente,docente,corso,giorno,slotRipetizione,1);
                        int check = DAO.addPrenotazione(utente,docente,corso,giorno,slotRipetizione,2);
                        if(check==1){
                            ret = "ok";
                        }else{
                            ret = "ko4";
                        }

                    }
                }

            }else{
                ret = "ko2";
            }
        }catch (NumberFormatException n){
            ret = "ko1";
        }

        Gson gson = new Gson();
        System.out.println(gson.toJson(ret));
        out.print("["+gson.toJson(ret)+"]");

    }

    /**[Android] Segna una prenotazione attiva come disdetta*/
    protected void processRequestAndroidSegnaComeDisdetta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        String idPren = request.getParameter("idPren");
        HttpSession s = request.getSession();
        String userName = request.getParameter("userName");
        int ok=0;
        String ret = "ko";

        try{
            int id = Integer.parseInt(idPren);
            /*Verifico inoltre il ruolo dell'utete che la richiede*/
            if(userName!=null && !userName.isEmpty()  && ((s.getAttribute("ruolo")=="admin")
                    || (s.getAttribute("ruolo")=="user"))){
                ArrayList<Prenotazione> myP = DAO.recuperaPrenotazione();
                for(Prenotazione p : myP){
                    if(p.getIdPrenotazione()==id && p.getUtenteP().equals(userName) && p.getStatoRipetizione()==1){
                        String utente = "'"+p.getUtenteP()+"'";
                        String docente = "'"+p.getDocenteP()+"'";
                        String corso = "'"+p.getCorsoP()+"'";
                        String giorno = "'"+p.getGiornoRipetizione()+"'";
                        int slotRipetizione = p.getSlotRipetizione();

                        DAO.rmvPrenotazione(utente,docente,corso,giorno,slotRipetizione,1);
                        int check = DAO.addPrenotazione(utente,docente,corso,giorno,slotRipetizione,3);
                        if(check==1){
                            ret = "ok";
                        }else{
                            ret = "ko4";
                        }

                    }
                }

            }else{
                ret = "ko2";
            }
        }catch (NumberFormatException n){
            ret = "ko1";
        }


        Gson gson = new Gson();
        out.print("["+gson.toJson(ret)+"]");

    }

    /**[Android] Ritorno l'elenco delle Associazioni corso-docente*/
    protected void processRequestAndroidGetACD(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        /*Verifico la sessione e il ruolo dell'utente che richiede l'elenco delle ACD*/
        if( (s.getId().equals(request.getParameter("sessionID"))) && ((s.getAttribute("ruolo")=="admin")
                || (s.getAttribute("ruolo")=="user"))){
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            ArrayList<AssociazioneCorsoDocente> myACD = DAO.recuperaAssociazioneCorsoDocente();
            Gson gson = new Gson();
            out.print(gson.toJson(myACD));
        }else{
            System.out.println("Error in processRequestAndroidGetACD");
        }



    }

    /**[Android] Ritorno l'elenco delle prenotazioni attualmente Attive relative a una determinata Associazione
     * corso docente*/
    protected void processRequestAndroidGetPrenotazioniChoosedACD(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        HttpSession s = request.getSession();

        /*Verifico che la sessione sia attiva e il ruolo dell'utente*/
        if( (s.getId().equals(request.getParameter("sessionID"))) && ((s.getAttribute("ruolo")=="admin")
                || (s.getAttribute("ruolo")=="user"))){

            String corso = request.getParameter("corso");
            String docente = request.getParameter("docente");
            /*Verifico l'esistenza di questa ACD*/
            int ok=0;
            ArrayList<AssociazioneCorsoDocente> acd = DAO.recuperaAssociazioneCorsoDocente();
            for(AssociazioneCorsoDocente a : acd){
                if(a.getDocenteACD().equals(docente) && a.getCorsoACD().equals(corso)){
                    ok=1;
                }
            }

            if(ok==1){ /*La ACD esiste, recupero quindi le prenotazioni relative a questa ACD*/
                ArrayList<Prenotazione> prenotazione1 = DAO.recuperaPrenotazione();
                /*Filtro le prenotazioni ATTIVE  relative alla ACD selezionata*/
                ArrayList<Prenotazione> prenotazioniACDfilteredAttive = new ArrayList<>();
                for(Prenotazione p: prenotazione1){
                    if(p.getCorsoP().equals(corso) && p.getDocenteP().equals(docente) && p.getStatoRipetizione()==1){
                        prenotazioniACDfilteredAttive.add(p);
                    }
                }

                Gson gson = new Gson();
                out.print(gson.toJson(prenotazioniACDfilteredAttive));
            }else{
                System.out.println("Errore in processRequestAndroidGetPrenotazioniChoosedACD");
            }
        }else{
            System.out.println("Error in processRequestAndroidGetPrenotazioniChoosedACD");
        }



    }

    /**[Android] Effettuo l'inserimento di una prenotazione*/
    protected void processRequestAndroidPutPrenotazione(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        HttpSession s = request.getSession();
        /*Verifico la sessione e il ruolo dell'utente che richiede l'insrimento*/
        if( (s.getId().equals(request.getParameter("sessionID"))) && ((s.getAttribute("ruolo")=="admin")
                || (s.getAttribute("ruolo")=="user"))){
            String userName = request.getParameter("userName");
            String docente = request.getParameter("docente");
            String corso = request.getParameter("corso");
            String GiornoSlot = request.getParameter("GiornoSlot");
            String parts[] = GiornoSlot.split("-");
            String giorno = parts[0];
            String orario = parts[1];

            System.out.println("[Android] Richiesta prenotazione: "+userName+" con docente "+docente+
                    " del corso " + corso+ " il giorno "+giorno+" nell'orario "+orario);


            /*Devo verificare l'esistenza della ACD*/
            ArrayList<AssociazioneCorsoDocente> acd = DAO.recuperaAssociazioneCorsoDocente();
            int ok=0;
            for(AssociazioneCorsoDocente a:acd){
                if(a.getCorsoACD().equals(corso) && a.getDocenteACD().equals(docente)){
                    ok=1;
                }
            }

            if(ok==1){ /*Se entro qua vuol dire che docente e corso sono accettabili, oltre che esistere la ACD*/
                userName = "'"+userName+"'";
                docente = "'"+docente+"'";
                corso = "'"+corso+"'";
                giorno = "'"+giorno+"'";

                /*Rendo l'orario uno slot*/
                int slot = -1;
                if(orario.equals("15/16")){slot=1;}
                if(orario.equals("16/17")){slot=2;}
                if(orario.equals("17/18")){slot=3;}
                if(orario.equals("18/19")){slot=4;}

                if(slot!=-1){

                    int check = DAO.addPrenotazione(userName,docente,corso,giorno,slot,1);
                    if(check==1){
                        Gson gson = new Gson();
                        out.print("["+gson.toJson("ok")+"]");
                    }else{
                        Gson gson = new Gson();
                        out.print("["+gson.toJson("ko")+"]");
                    }

                }else{
                    Gson gson = new Gson();
                    out.print("["+gson.toJson("ko")+"]");
                }
            }else{
                Gson gson = new Gson();
                out.print("["+gson.toJson("ko")+"]");
            }
        }else{
            System.out.println("Error in processRequestAndroidPutPrenotazione");
        }














    }

    /**[Android] Verifica se la sessione utente è attiva*/
    protected void processRequestAndroidCheckSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession s = request.getSession();
        String sessionIdActual = s.getId();
        String sessionIdReceived = request.getParameter("sessionID");
        PrintWriter out = response.getWriter();

        if(sessionIdActual.equals(sessionIdReceived)) {
            response.setContentType("application/json");
            Gson gson = new Gson();
            String ret = "sessionOk";
            out.print("[" + gson.toJson(ret)+ "]");
        }else{
            response.setContentType("application/json");
            Gson gson = new Gson();
            String ret = "sessionFail";
            out.print("[" + gson.toJson(ret)+ "]");
        }
    }

    /**[Android] Ritorno l'elenco delle prenotazioni attualmente Attive relative a una determinata Associazione
     * corso docente
     *
     * GUEST = NO CONTROLLI SU SESSIONE o RUOLI UTENTE*/
    protected void processRequestAndroidGetPrenotazioniChoosedACDGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        String corso = request.getParameter("corso");
        String docente = request.getParameter("docente");
        /*Verifico l'esistenza di questa ACD*/
        int ok=0;
        ArrayList<AssociazioneCorsoDocente> acd = DAO.recuperaAssociazioneCorsoDocente();
        for(AssociazioneCorsoDocente a : acd){
            if(a.getDocenteACD().equals(docente) && a.getCorsoACD().equals(corso)){
                ok=1;
            }
        }

        if(ok==1){ /*La ACD esiste, recupero quindi le prenotazioni relative a questa ACD*/
            ArrayList<Prenotazione> prenotazione1 = DAO.recuperaPrenotazione();
            /*Filtro le prenotazioni ATTIVE  relative alla ACD selezionata*/
            ArrayList<Prenotazione> prenotazioniACDfilteredAttive = new ArrayList<>();
            for(Prenotazione p: prenotazione1){
                if(p.getCorsoP().equals(corso) && p.getDocenteP().equals(docente) && p.getStatoRipetizione()==1){
                    prenotazioniACDfilteredAttive.add(p);
                }
            }

            Gson gson = new Gson();
            out.print(gson.toJson(prenotazioniACDfilteredAttive));
        }else{
            System.out.println("Errore in processRequestAndroidGetPrenotazioniChoosedACD");
        }

    }

    /**[Android] Ritorno l'elenco delle Associazioni corso-docente
     *
     * GUEST=no controllo su ruolo o sessione*/
    protected void processRequestAndroidGetACDGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        ArrayList<AssociazioneCorsoDocente> myACD = DAO.recuperaAssociazioneCorsoDocente();
        Gson gson = new Gson();
        out.print(gson.toJson(myACD));

    }


}
