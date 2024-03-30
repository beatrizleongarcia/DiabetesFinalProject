/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diabetes.project;

/**
 *
 * @author loredana
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DiabetesInterface {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    public static Set<Patient> patients = new HashSet<>();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void menu() {
        KieContext kieContext = null; // Initialize outside the loop to persist the context
        //DBMannager.createTables();
        while (true) {
            System.out.println("\nDiabetes Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. Evaluate Patients.");
            System.out.println("3. Exit.");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addNewPatient(scanner);
                    break;
                case 2:
                    if (kieContext == null) {
                        kieContext = evaluatePatients(); // Only initialize if not already done
                    }
                    performEvaluation(kieContext); // This will handle the evaluation logic
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }



    private static Set<Patient> addNewPatient(Scanner scanner) {
        System.out.println("Adding a new patient...");

        System.out.println("Enter patient name:");
        String name = scanner.nextLine();

        int typeOfDiabetes;
        do {
            System.out.println("Enter type of diabetes (1 for Type 1, 2 for Type 2):");
            typeOfDiabetes = scanner.nextInt();
            if (typeOfDiabetes < 1 || typeOfDiabetes > 2) {
                System.out.println("Invalid input. Please enter 1 for Type 1 or 2 for Type 2 diabetes.");
            }
        } while (typeOfDiabetes < 1 || typeOfDiabetes > 2);

        System.out.println("Enter BMI:");
        double bmi = scanner.nextDouble();

        System.out.println("Enter age:");
        int age = scanner.nextInt();

        int insulinProd;
        do {
            System.out.println("Enter insulin production (1=NO PROD, 2=HYPO, 3=NORMAL, 4=HYPER):");
            insulinProd = scanner.nextInt();
            if (insulinProd < 1 || insulinProd > 4) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
            }
        } while (insulinProd < 1 || insulinProd > 4);

        scanner.nextLine(); // consume newline after reading number

        boolean insulinRes = promptYesNo("Is insulin resistant? (y/n):");
        boolean hypotension = promptYesNo("Has hypotension? (y/n):");
        boolean dyslipidemia = promptYesNo("Has dyslipidemia? (y/n):");
        boolean pad = promptYesNo("Has peripheral artery disease (PAD)? (y/n):");
        boolean nafld = promptYesNo("Has non-alcoholic fatty liver disease (NAFLD)? (y/n):");
        boolean osteoporosis = promptYesNo("Has osteoporosis? (y/n):");

        // Creating the patient object
        Patient patient = new Patient(name, typeOfDiabetes, bmi, age, insulinProd, insulinRes, hypotension, dyslipidemia, pad, nafld, osteoporosis);

        patients.add(patient);
        
        int doctorId = logIn(); // Llamar al método logIn() y capturar el id del doctor

        if (doctorId != -1) { // Verificar si el inicio de sesión fue exitoso
            Doctor.setCurrentDoctorId(doctorId); // Pasar el id del doctor al método setCurrentDoctorId()
            DBMannager.insertPatient(patient.getName(), patient.getTypeOfDiabetes(), patient.getBmi(), patient.getAge(), patient.getInsulinProd(), patient.isInsulinRes(), patient.isHypotension(), patient.isDyslipidemia(), patient.isPad(), patient.isNafld(), patient.isOsteoporosis());
            System.out.println("Patient added successfully.");
        } else {
            System.out.println("Failed to log in. Cannot set current doctor ID.");
        }
        System.out.println("Patient added successfully.");
        return patients;   
    }

    private static boolean promptYesNo(String message) {
        String input;
        do {
            System.out.print(message);
            input = scanner.nextLine().trim().toLowerCase();
            if (!input.equals("y") && !input.equals("n")) {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        } while (!input.equals("y") && !input.equals("n"));
        return input.equals("y");
    }


    private static void addTreatmentToPatient(Scanner scanner) {
        System.out.print("Enter patient's name to add treatment: ");
        String name = scanner.nextLine();
        for (Patient patient : patients) {
            if (patient.getName().equalsIgnoreCase(name)) {
                System.out.print("Enter treatment name: ");
                String treatmentName = scanner.nextLine();
                System.out.print("Should the treatment be applied? (true/false): ");
                boolean shouldBeApplied = scanner.nextBoolean();
                System.out.print("Enter treatment priority: ");
                double priority = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                Treatment treatment = new Treatment(treatmentName, shouldBeApplied, priority);
                patient.addTreatment(treatment);
                System.out.println("Treatment added successfully to " + name);
                return;
            }
        }
        System.out.println("Patient not found.");
    }

    private static void listPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients in the system.");
            return;
        }
        for (Patient patient : patients) {
            System.out.println("\nPatient: " + patient.getName());
            if (patient.treatments.isEmpty()) {
                System.out.println("No treatments assigned.");
                continue;
            }
            for ( Treatment treatment : patient.treatments) {
                System.out.println(treatment);
            }
        }
    }
    

    // DOCTOR METHODS 
    public static Doctor promptForDoctorCredentials(){
        
        System.out.println("Input your username:\n");
        String username = scanner.nextLine();
        System.out.println("Input yor password:\n");
        String password = scanner.nextLine();
        
        return new Doctor(username, password);
    }
    // TODO Integrate this in the main
    public static int firstPrompt(){
        System.out.println("Welcome to our DSS!\n");
        System.out.println("1.Sing up\n");
        System.out.println("2.Login \n");
        int option = scanner.nextInt();
        return option;
    }
    
    /*public static boolean logIn(Doctor doctor) {
        try {
            
            String inputPasswordHash = doctor.password;

            if (doctor.getUsername().equals(doctor.username) && Utils.hashPassword("a").equals(inputPasswordHash)) {
                System.out.println(ANSI_GREEN + "Login successful" + ANSI_RESET);
                return true;
            } else {
                System.out.println(ANSI_RED + "Login failed" + ANSI_RESET);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }*/
    public static int logIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input your username:");
        String username = scanner.nextLine();
        System.out.println("Input your password:");
        String password = scanner.nextLine();

        try {
            String storedPasswordHash = DBMannager.getPasswordHash(username);

            if (storedPasswordHash != null && storedPasswordHash.equals(Utils.hashPassword(password))) {
                int doctorId = DBMannager.getDoctorIdByUsername(username); // Asumiendo que tienes un método para obtener el id del doctor por nombre de usuario
                System.out.println(ANSI_GREEN + "Login successful" + ANSI_RESET);
                return doctorId; 
            } else {
                System.out.println(ANSI_RED + "Login failed" + ANSI_RESET);
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return -1;
        } finally {
            scanner.close();
        }
    }

    
    public static KieContext evaluatePatients() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        KieContext kieContext = new KieContext(ks,kc);
        return kieContext;
    }
   
    public static void performEvaluation(KieContext kieContext) {
        KieSession ksession = kieContext.getKieContainer().newKieSession("diabetesSession");

        // Insert patients into the session. This example assumes patients are accessible
        for(Patient patient : patients) {
            ksession.insert(patient);
        }

        ksession.fireAllRules();
        ksession.dispose();
        System.out.println(patients);

        // Optionally, print patients or results after evaluation
        System.out.println("Evaluation completed.");
    }
 
     
}

