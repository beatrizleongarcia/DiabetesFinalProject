/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diabetes.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author carlo
 */
public class DBMannager {
    
    
     public static void createTables(){
            try {
                        
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:./db/treatments.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");
			
			// Create tables: begin
                        Statement stmt3 = c.createStatement();
			String sql3 = "CREATE TABLE  Doctor (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "passwordHash TEXT NOT NULL" +
                        ")";
  
			stmt3.executeUpdate(sql3);
			stmt3.close();
			Statement stmt1 = c.createStatement();
			String sql1 = "CREATE TABLE Patient (" +
                        "    id       INTEGER  PRIMARY KEY AUTOINCREMENT," +
                        "    name TEXT NOT NULL," +
                        "    typeOfDiabetes INTEGER," +
                        "    bmi DOUBLE," +
                        "    age INT," +
                        "    insulinProd INTEGER," +
                        "    insulinRes BOOLEAN," +
                        "    hypotension BOOLEAN," +
                        "    dyslipidemia BOOLEAN," +
                        "    pad BOOLEAN," +
                        "    nafld BOOLEAN," +
                        "    osteoporosis BOOLEAN," +
                        "    doctor_id INTEGER," +
                        "    FOREIGN KEY (doctor_id) REFERENCES Doctor(id)" +
                                
                            ")";
			stmt1.executeUpdate(sql1);
			stmt1.close();
                        
			Statement stmt2 = c.createStatement();
			String sql2 =  "CREATE TABLE Treatment (" +
                        "    id       INTEGER  PRIMARY KEY AUTOINCREMENT," +
                        "    patient_id INTEGER," +
                        "    name TEXT NOT NULL," +
                        "    shouldBeApplied BOOLEAN," +
                        "    priority DOUBLE," +
                        "    FOREIGN KEY (patient_id) REFERENCES Patient(id)" +
                        ")";
  
			stmt2.executeUpdate(sql2);
			stmt2.close();			
			System.out.println("Tables created.");

			// Close database connection
			c.close();
			System.out.println("Database connection closed.");
		} catch (Exception e) {
			e.printStackTrace();
                        System.out.println("inside catch");
		}
        }
 public static Set<Patient> getPatientsFromDatabase() {
        Set<Patient> patients = new HashSet<>();
        String url = "jdbc:sqlite:./db/treatments.db";
        String sql = "SELECT * FROM Patient";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                int typeOfDiabetes = rs.getInt("typeOfDiabetes");
                double bmi = rs.getDouble("bmi");
                int age = rs.getInt("age");
                int insulinProd = rs.getInt("insulinProd");
                boolean insulinRes = rs.getBoolean("insulinRes");
                boolean hypotension = rs.getBoolean("hypotension");
                boolean dyslipidemia = rs.getBoolean("dyslipidemia");
                boolean pad = rs.getBoolean("pad");
                boolean nafld = rs.getBoolean("nafld");
                boolean osteoporosis = rs.getBoolean("osteoporosis");

                Patient patient = new Patient(name, typeOfDiabetes, bmi, age, insulinProd, insulinRes,
                        hypotension, dyslipidemia, pad, nafld, osteoporosis);
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

public static Patient searchPatient(String nombre){
        String nameToFind = nombre;

        // Sentencia SQL de búsqueda
        String sql = "SELECT * FROM Patient WHERE name = ?";

        // Conexión a la base de datos
        String url = "jdbc:sqlite:./db/treatments.db";
        Patient pat = null; 

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Establecer el parámetro de la sentencia SQL
            pstmt.setString(1, nameToFind);

            // Ejecutar la consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                // Verificar si se encontraron resultados
                if (rs.next()) {
                    // Obtener los datos del paciente
                    String name = rs.getString("name");
                    int typeOfDiabetes = rs.getInt("typeOfDiabetes");
                    double bmi = rs.getDouble("bmi");
                    int age = rs.getInt("age");
                    int insulinProd = rs.getInt("insulinProd");
                    boolean insulinRes = rs.getBoolean("insulinRes");
                    boolean hypotension = rs.getBoolean("hypotension");
                    boolean dyslipidemia = rs.getBoolean("dyslipidemia");
                    boolean pad = rs.getBoolean("pad");
                    boolean nafld = rs.getBoolean("nafld");
                    boolean osteoporosis = rs.getBoolean("osteoporosis");

                    // Mostrar los datos del paciente
                    System.out.println("Paciente encontrado:");
                    System.out.println("Nombre: " + name);
                    System.out.println("Tipo de diabetes: " + typeOfDiabetes);
                    System.out.println("BMI: " + bmi);
                    System.out.println("Edad: " + age);
                    System.out.println("Producción de insulina: " + insulinProd);
                    System.out.println("Resistencia a la insulina: " + insulinRes);
                    System.out.println("Hipotensión: " + hypotension);
                    System.out.println("Dislipidemia: " + dyslipidemia);
                    System.out.println("PAD: " + pad);
                    System.out.println("NAFLD: " + nafld);
                    System.out.println("Osteoporosis: " + osteoporosis);
                    pat = new Patient(name,typeOfDiabetes,bmi,age,insulinProd,insulinRes,hypotension,dyslipidemia,pad,nafld,osteoporosis);
                } else {
                    System.out.println("No se encontró ningún paciente con el nombre '" + nameToFind + "'.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
                            return pat;

}

    
    public static void insertPatient(String name, int typeOfDiabetes, double bmi, int age, int insulinProd, boolean insulinRes, boolean hypotension, boolean dyslipidemia, boolean pad, boolean nafld, boolean osteoporosis){
         String sql = "INSERT INTO Patient (name, typeOfDiabetes, bmi, age, insulinProd, insulinRes, hypotension, dyslipidemia, pad, nafld, osteoporosis, doctor_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Conexión a la base de datos
        String url = "jdbc:sqlite:./db/treatments.db";


        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Establecer los parámetros de la sentencia SQL
            pstmt.setString(1, name);
            pstmt.setInt(2, typeOfDiabetes);
            pstmt.setDouble(3, bmi);
            pstmt.setInt(4, age);
            pstmt.setInt(5, insulinProd);
            pstmt.setBoolean(6, insulinRes);
            pstmt.setBoolean(7, hypotension);
            pstmt.setBoolean(8, dyslipidemia);
            pstmt.setBoolean(9, pad);
            pstmt.setBoolean(10, nafld);
            pstmt.setBoolean(11, osteoporosis);
            pstmt.setInt(12,  Doctor.currentDoctorId);

            // Ejecutar la sentencia SQL de inserción
            int filasInsertadas = pstmt.executeUpdate();
            if (filasInsertadas > 0) {
                //System.out.println("La inserción se realizó correctamente.");
            } else {
                //System.out.println("La inserción no se pudo realizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int getPatientIdByName(String name){
        String sql = "SELECT id FROM patient WHERE name = ?";
        String url = "jdbc:sqlite:./db/treatments.db";
        int id = 0;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Establecer el parámetro de la sentencia SQL
            pstmt.setString(1, name);

            // Ejecutar la consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                // Verificar si se encontraron resultados
                if (rs.next()) {
                    // Obtener los datos del paciente
                     id = rs.getInt("id");
                  
                } else {
                    System.out.println("No se encontró ningún paciente con el nombre '" + name + "'.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
       return id ;
    }
    
  

    // Omitiendo el método createDoctorTable aquí por brevedad.

    public static void insertDoctor(String username, String passwordHash) {
        String url = "jdbc:sqlite:./db/treatments.db";
        String sql = "INSERT INTO Doctor (username, passwordHash) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            pstmt.executeUpdate();
            System.out.println("Doctor inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static void insertTreatment(int patid, String treatment, Boolean shouldBeApplied, Double priority){
        int patientId = patid ; // ID del paciente al que se asociará el tratamiento
        

        // Sentencia SQL de inserción para agregar el nuevo tratamiento a la base de datos
        String sql = "INSERT INTO Treatment (patient_id, name, shouldBeApplied, priority) " +
                     "VALUES (?, ?, ?, ?)";

        // Conexión a la base de datos
        String url = "jdbc:sqlite:./db/treatments.db";


        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Establecer los parámetros de la sentencia SQL
            pstmt.setInt(1, patientId);
            pstmt.setString(2, treatment);
            pstmt.setBoolean(3, shouldBeApplied);
            pstmt.setDouble(4, priority);

            // Ejecutar la sentencia SQL de inserción
            int filasInsertadas = pstmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Nuevo tratamiento creado correctamente.");
            } else {
                System.out.println("No se pudo crear el nuevo tratamiento.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean doesDoctorExist(String username) {
        String url = "jdbc:sqlite:./db/treatments.db";
        String sql = "SELECT COUNT(*) AS count FROM Doctor WHERE username = ?";
        boolean exists = false;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                exists = count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    public static boolean isPasswordCorrect(String username, String password) {
        String url = "jdbc:sqlite:./db/treatments.db";
        String sql = "SELECT id, passwordHash FROM Doctor WHERE username = ?";
        boolean correct = false;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String passwordHashFromDB = rs.getString("passwordHash");
                String inputPasswordHash = password;//le he quitado el metedo hash 
                correct = passwordHashFromDB.equals(inputPasswordHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return correct;
    }

    public static String getPasswordHash(String username) {
        String url = "jdbc:sqlite:./db/treatments.db";
        String sql = "SELECT passwordHash FROM Doctor WHERE username = ?";
        String passwordHash = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                passwordHash = rs.getString("passwordHash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return passwordHash;
    }   


    public static int getDoctorIdByUsername(String username) {
        int doctorId = 0; // Valor predeterminado si no se encuentra el doctor

        String url = "jdbc:sqlite:./db/treatments.db";
        String sql = "SELECT id FROM Doctor WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                doctorId = rs.getInt("id");
            } else {
                System.out.println("Doctor with username '" + username + "' not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorId;
    }

    
}
