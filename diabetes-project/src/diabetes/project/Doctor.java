/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diabetes.project;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author loredana
 */

public class Doctor {
    public String username;
    public String password;
    public static int currentDoctorId;
    
    public Doctor(String username, String password) {
        this.username = username;
        setPassword(password);
    }
    
    public static void setCurrentDoctorId(int id){
        currentDoctorId =id;
    }
    
    private void setPassword(String password) {
        this.password = Utils.hashPassword(password);
    }
    
  
    
    // Getter for username
    public String getUsername() {
        return username;
    }

    // This is a demonstration purpose method.
    // In a real-world scenario, you would not have a method to get the password hash directly.
    public String getPassword() {
        return password; 
    }

   }

