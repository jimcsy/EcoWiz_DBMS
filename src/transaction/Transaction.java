package transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import commerce.Products;
import core.DatabaseConnection;

public abstract class Transaction extends Products{
    private int transactionId;

    LocalDate getDate = LocalDate.now();
    DateTimeFormatter  formatDate = DateTimeFormatter.ofPattern("E, MMM dd yyyy");

    abstract int totalAmount(String username, int orderQuantity, int customerId);

    public int buyProduct(String username, int customerId, int ecoScore){
        int orderId = chooseProduct(username, customerId, ecoScore); 
        int totalEcoScore = 0;

        while(orderId != -1){
            System.out.println("\nHow many '" + getProdName() + "' you want: ");
            int orderQuantity = validInput(2, "Enter quantity: ");

            if(orderQuantity == -1){
                System.out.print("\nReturning...");
                return -1;
            }

            if(orderQuantity > getProductQty()){
                System.out.print("\nThe desired quantity exceeds our available stock. Please try again.");
                break;
            }
            
            if(orderQuantity <= 0){
                System.out.print("\nQuantity cannot be 0 or less.");
                continue;
            }else{
                totalEcoScore = totalAmount(username, orderQuantity, customerId);
                if (totalEcoScore != -1){
                    System.out.print("\nThank you...");
                    delay(500); 
                    return totalEcoScore;
                }else{
                    return -1;
                }
            }
        }
        return totalEcoScore;
    }

    public void saveTransaction(int customerId, double totalAmount, int totalEcoScore, int orderQuantity) {
        String sql = "INSERT INTO user_transaction (customer_id, total_amount, eco_score, transaction_date) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            // Set the parameters for the insert statement
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, totalAmount);
            pstmt.setInt(3, totalEcoScore);
            pstmt.setString(4, getDate.toString());

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Now retrieve the last inserted ID using SELECT LAST_INSERT_ID()
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                    if (rs.next()) {
                        this.transactionId = rs.getInt(1);  // Get the last inserted ID
                        System.out.print("\nTransaction saved successfully!");
                        productTransaction(transactionId, orderQuantity);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            back(3);
        }
    }

    //foreign key DBMS
    public void productTransaction(int transactionId, int quantity){
        String sql = "INSERT INTO transaction_product (transaction_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transactionId);
            pstmt.setInt(2, getProdId());
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            back(3);
        }
    }

    public void printReceipt(String username, int customer_id, int quantity, double totalAmount, int totalEcoScore){
        delay(1500);
        clearScreen();
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        lines('\\', 53);
        System.out.println("\n||--------------->> E C O W I Z <<------------------||");
        System.out.println("||--------------<< Receipt #" + transactionId + " >>---------------||");
        lines('-', 53);

        System.out.println("\nDate: " + getDate.format(formatDate).toString());
        System.out.println("Customer: " + username + String.format("%33s", "ID: " + customer_id));
        lines('-', 53);
        
        System.out.println( "\n" + String.format("%-20s %-25s", "Product:", getProdName()));
        System.out.println(String.format("%-20s %-25s", "Product ID:", getProdId()));
        System.out.println(String.format("%-20s %.2f %s", "Price:", getProdPrice(), "pesos" ));
        System.out.println(String.format("%-20s %-25s", "Quantity:", quantity));

        lines('=', 53);
        
        if(totalEcoScore < 0){
            System.out.print("\n" + String.format("%-20s %.2f %s", "Total price:", (getProdPrice() * quantity), "pesos" ));
            System.out.print("\n" + String.format("%-20s %.2f %s", "Discount:", (getProdPrice() * quantity) - totalAmount, "pesos\n" ));
            lines('-', 53);
        }
        System.out.println("\n" + String.format("%-20s %.2f %s", "Total amount:", totalAmount, "pesos" ));
        System.out.println(String.format("%-20s %-25s", "Eco score:", totalEcoScore));
        lines('-', 53);
        
        System.out.println();
        lines('/', 53);

        back(3);
    }

    
}