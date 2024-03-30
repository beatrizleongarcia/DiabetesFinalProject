/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package diabetes.project;

import java.util.Scanner;
import java.util.Set;
import static org.apache.tools.ant.dispatch.DispatchUtils.execute;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;



public class DiabetesLogic {
    /**
     * @param args the command line arguments
     */
   
     private static Scanner scanner = new Scanner(System.in);
     public static void main(String[] args) {
         //DBMannager.createTables();
         System.out.println("1. Log In");
         System.out.println("2. Add Doctor");
     int choice = scanner.nextInt();
     switch(choice){
         
         case 1:    
            boolean authorized =isUserCorrect(scanner);
            if (authorized){
            DiabetesInterface.menu(); // This calls your custom execute method }
            }
            case 2:  
            addDoctor(scanner);
     
     
     
  }
     
     }
     
    public static boolean isUserCorrect(Scanner scanner){
       
        boolean doctorExist = false; 
        
        String username = null;
        String password = null; 
        while(!doctorExist){
        System.out.println("please type the username");
        username = scanner.next();
        doctorExist = DBMannager.doesDoctorExist(username);
        
        }
        doctorExist = false; 
        while(!doctorExist){
            System.out.println("please type password");
       password = scanner.next(); 
       Doctor doc = new Doctor(username, password);
       doctorExist = DBMannager.isPasswordCorrect(username, doc.getPassword());
       
       }
         return doctorExist;
    }
    public static void addDoctor(Scanner scanner){
        System.out.println("please type the user");
       String user = scanner.next();
        System.out.println("please type the password");
       String password = scanner.next();
       Doctor doc = new Doctor(user, password);
       DBMannager.insertDoctor(doc.getUsername(), doc.getPassword());
               
    }
}

    