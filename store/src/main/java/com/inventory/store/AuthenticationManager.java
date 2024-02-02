package com.inventory.store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AuthenticationManager {
    private final String COMPANY_LOGIN_FILE = "companyLoginInfo.txt";

    boolean authenticateUser(String uniqueID, String password) throws IOException {
        try (BufferedReader loginReader = new BufferedReader(new FileReader(COMPANY_LOGIN_FILE))) {
            String line;
            while ((line = loginReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(uniqueID) && parts[2].equals(password)) {
                    return true;
                }
            }
        }

        return false;
    }

    void changePassword(String uniqueID, String currentPassword, String newPassword) throws IOException {
        try (BufferedReader loginReader = new BufferedReader(new FileReader(COMPANY_LOGIN_FILE));
             BufferedWriter loginWriter = new BufferedWriter(new FileWriter(COMPANY_LOGIN_FILE + ".tmp"))) {

            String line;
            boolean foundID = false;

            while ((line = loginReader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 3 && parts[1].equals(uniqueID) && parts[2].equals(currentPassword)) {
                    line = parts[0] + ", " + parts[1] + ", " + newPassword;
                    foundID = true;
                }
                loginWriter.write(line + "\n");
            }

            if (foundID) {
                System.out.println("Password changed successfully.");
                File oldFile = new File(COMPANY_LOGIN_FILE);
                File newFile = new File(COMPANY_LOGIN_FILE + ".tmp");
                oldFile.delete();
                newFile.renameTo(oldFile);
            } else {
                System.out.println("Invalid unique ID or password. Password not changed.");
            }
        }
    }
}
