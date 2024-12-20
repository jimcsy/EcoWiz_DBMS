package commerce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import core.DatabaseConnection;
import core.General; 

public class CustomerCredentials extends General{
    private String username, password;
    private int customerId, ecoPoints;

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public int getCustomerId(){
        return customerId;
    }
    
    public int getEcoPoints(){
        return ecoPoints;
    }

    public boolean saveCredentials(String newUsername, String newPassword) {
        boolean isSuccessful = false;
        String sql = "INSERT INTO customer (user_Name, user_Password, eco_points) VALUES (?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, 0);
    
            this.username = newUsername;
            this.password = newPassword;
            this.ecoPoints = 0;

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                    if (rs.next()) {
                        this.customerId = rs.getInt(1);
                        System.out.println("\nCustomer credentials saved successfully!");
                        System.out.println("Customer ID:  " + this.customerId);
                        System.out.print("\nPress [enter]...");
                        scan.nextLine();
    
                        isSuccessful = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("\nPress [enter]...");
            scan.nextLine();
        }
        return isSuccessful;
    }
    
    public boolean customerLogin(int checkId, String checkPassword) {
        boolean isSuccessful = false;
        String checkSql = "SELECT * FROM customer WHERE customer_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
    
            pstmt.setInt(1, checkId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.password = rs.getString("user_password");
        
                    if (checkPassword.equals(password)) {
                        System.out.print("\nLogin successful!");
                        this.customerId = checkId;
                        this.username = rs.getString("user_name");
                        this.ecoPoints = rs.getInt("eco_points");
                        isSuccessful = true;
                    } else {
                        System.out.print("\nPassword doesn't match!");
                        delay(1500);
                    }
                } else {
                    System.out.print("\nNo customer found with this ID.");
                    delay(1500);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("\nPress [enter]...");
            scan.nextLine();
        }
        return isSuccessful;
    }

    public boolean updateEcoScore(int updateEcoScore) {
        boolean isSuccessful = false;
        String checkSql = "UPDATE customer SET eco_points = ? WHERE customer_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            
            this.ecoPoints = ecoPoints + updateEcoScore;
            pstmt.setInt(1, ecoPoints);
            pstmt.setInt(2, customerId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("\nPress [enter]...");
            scan.nextLine();
        }
        return isSuccessful;
    }

    public boolean deleteAccount() {
        boolean isSuccessful = false;
        System.out.println("\nAre you sure you want to delete your account?");
        int choice = validInput(1, "Type [1] if yes: ");

        if (choice != 1){
            System.out.print("Account deletion has been cancelled.");
            return isSuccessful;
        }

        String deleteSql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
    
            pstmt.setInt(1, customerId); 
    
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                String text = "\n       Your account has been successfully deleted. You can come back at any time by signing up again. We hope to see you!";
                designSentence(text, 70, 2);
                back(1);
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("\nPress [enter]...");
            scan.nextLine();
        }
        return isSuccessful;
    }

}