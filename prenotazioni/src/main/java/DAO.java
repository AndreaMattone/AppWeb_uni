import java.sql.*;
import java.util.ArrayList;

public class DAO {

    /*Informazioni preliminari per la connessione al DB*/
    private static final String url1 = "jdbc:mysql://localhost:3306/testtweb3";
    private static final String user = "root";
    private static final String password = "";

    /*Registrazione del Driver*/
    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }





    /*Query che recupera l'elenco dei docenti*/
    public static ArrayList<Docente> recuperaDocenti() {
        /*Crea la connessione*/
        Connection conn1 = null;

        ArrayList<Docente> out = new ArrayList<>();
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();

            /*Indico il tipo di statement da eseguire*/
            ResultSet rs = st.executeQuery("SELECT * FROM DOCENTE");

            /*Recupero il result set dello statement eseguito*/
            while (rs.next()) {
                Docente c = new Docente(rs.getString("professorName"));
                out.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        /*Restituisco il result set*/
        return out;
    }

    /*Query che recupera l'elenco dei corsi*/
    public static ArrayList<Corso> recuperaCorsi() {
        /*Crea la connessione*/
        Connection conn1 = null;

        ArrayList<Corso> out = new ArrayList<>();
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();

            /*Indico il tipo di statement da eseguire*/
            ResultSet rs = st.executeQuery("SELECT * FROM CORSO");

            /*Recupero il result set dello statement eseguito*/
            while (rs.next()) {
                Corso c = new Corso(rs.getString("courseName"));
                out.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        /*Restituisco il result set*/
        return out;
    }

    /*Query che recupera l'elenco delle associazioni tra corsi e docenti*/
    public static ArrayList<AssociazioneCorsoDocente> recuperaAssociazioneCorsoDocente(){
        /*Crea la connessione*/
        Connection conn1 = null;

        ArrayList<AssociazioneCorsoDocente> out = new ArrayList<>();
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();

            /*Indico il tipo di statement da eseguire*/
            ResultSet rs = st.executeQuery("SELECT * FROM AssociazioneCorsoDocente");

            /*Recupero il result set dello statement eseguito*/
            while (rs.next()) {
                AssociazioneCorsoDocente a = new AssociazioneCorsoDocente(rs.getString("docenteACD"), rs.getString("corsoACD"));
                out.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        /*Restituisco il result set*/
        return out;
    }

    /*Query che recupera l'elenco degli utenti*/
    public static ArrayList<Utente> recuperaUtenti(){
        /*Crea la connessione*/
        Connection conn1 = null;

        ArrayList<Utente> out = new ArrayList<>();
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();

            /*Indico il tipo di statement da eseguire*/
            ResultSet rs = st.executeQuery("SELECT * FROM Utente");

            /*Recupero il result set dello statement eseguito*/
            while (rs.next()) {
                Utente u = new Utente(rs.getString("userName"), rs.getString("passwordUtente"),rs.getInt("ruoloUtente"));
                out.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        /*Restituisco il result set*/
        return out;
    }

    public static ArrayList<Prenotazione> recuperaPrenotazione(){
        /*Crea la connessione*/
        Connection conn1 = null;

        ArrayList<Prenotazione> out = new ArrayList<>();
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();

            /*Indico il tipo di statement da eseguire*/
            ResultSet rs = st.executeQuery("SELECT * FROM Prenotazione");

            /*Recupero il result set dello statement eseguito*/
            while (rs.next()) {
                Prenotazione p = new Prenotazione(rs.getInt("idPrenotazione"),rs.getString("utenteP"),rs.getString("docenteP"),rs.getString("corsoP"),rs.getString("giornoRipetizione"),rs.getInt("slotRipetizione"),rs.getInt("statoRipetizione"));
                out.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        /*Restituisco il result set*/
        return out;
    }


    public static void addUtente(String userName, String passwordUtente, int ruoloUtente){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      INSERT INTO Utente (userName, passwordUtente, ruoloUtente) VALUES ("+userName+","+passwordUtente+","+ruoloUtente+")     ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void rmvUtente(String userName){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      DELETE FROM Utente WHERE userName="+userName+"     ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static int addDocente(String professorName) {
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      INSERT INTO Docente (professorName) VALUES ("+professorName+")     ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static int rmvDocente(String professorName) {
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      DELETE FROM Docente WHERE professorName="+professorName+"     ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static int addCorso(String courseName){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      INSERT INTO Corso(courseName) VALUES ("+courseName+")     ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static int rmvCorso(String courseName){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      DELETE FROM Corso WHERE courseName="+courseName+"   ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static int addPrenotazione(String utenteP,String docenteP,String corsoP, String giornoRipetizione, int slotRipetizione,int statoRipetizione){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      INSERT INTO Prenotazione(utenteP,docenteP,corsoP,giornoRipetizione,slotRipetizione,statoRipetizione) VALUES ("+utenteP+","+docenteP+","+corsoP+","+giornoRipetizione+","+slotRipetizione+","+statoRipetizione+" )     ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void rmvPrenotazione(String utenteP,String docenteP,String corsoP, String giornoRipetizione, int slotRipetizione, int statoRipetizione){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      DELETE FROM Prenotazione WHERE utenteP="+utenteP+" AND docenteP="+docenteP+" AND corsoP="+corsoP+" AND giornoRipetizione="+giornoRipetizione+" AND slotRipetizione="+slotRipetizione+"  AND statoRipetizione="+statoRipetizione+"   ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static int addAssociazioneCorsoDocente(String docenteACD, String corsoACD){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      INSERT INTO AssociazioneCorsoDocente (docenteACD, corsoACD) VALUES ("+docenteACD+","+corsoACD+")     ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static int rmvAssociazioneCorsoDocente(String docenteACD, String corsoACD){
        /*Crea la connessione*/
        Connection conn1 = null;
        try {
            /*Mi connetto al DB*/
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                //System.out.println("Connected to the database");
            }else{
                System.out.println("Failed to connect to the database");
            }
            /*Creo uno statement*/
            Statement st = conn1.createStatement();
            String query ="      DELETE FROM AssociazioneCorsoDocente WHERE docenteACD="+docenteACD+" AND corsoACD = "+corsoACD+"    ";
            st.executeUpdate(query);
            System.out.println("Executed: " + query);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }


    /*
     * 1 amministratore
     * 0 utente base
     * -1 errore
     * */
    public static int recuperaRuolo(String userName, String passwordUtente){
        int ruolo=-1;

        ArrayList<Utente> utenti = DAO.recuperaUtenti();

        for (Utente u: utenti){
            if(u.getUserName().equals(userName) && u.getPasswordUtente().equals(passwordUtente)){
                ruolo = u.getRuoloUtente();
            }
        }

        return ruolo;
    }

}
