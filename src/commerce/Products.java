package commerce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import core.DatabaseConnection;
import core.General;

public class Products extends General{
    private String productName, productDescription;
    private int productId, quantity, ecoPoints, order;
    private double price;

    StringBuilder combinedIds = new StringBuilder();

    public String getProdName(){
        return productName;
    }

    public int getOrderId(){
        return order;
    }

    public int getProductQty(){
        return quantity;
    }

    public int getEcoScore(){
        return ecoPoints;
    }

    public int getProdId(){
        return productId;
    }

    public double getProdPrice(){
        return price;
    }

    public int chooseProduct(String username, int customerId, int ecoScore){
        String [] available;
        int choice = 0;
        int order;

        while(true){
            printAvailableProd(username, customerId, ecoScore);
            available = (combinedIds.toString().split("\\|"));
            
            if (available.length <= 1){//empty
                System.out.print("\nPress [enter]...");
                return -1;
            }

            while(true){
                choice = validInput(2, "Choose from the list: ");
                if (choice == -1){
                    System.out.print("\nReturning...");
                    return -1;
                } else if (choice >= 1 && choice < available.length){
                    order = Integer.parseInt(available[choice]);
                    seeInformation(order, customerId);
                    break;
                }else{
                    System.out.println("Choose only from the list!");
                    continue;
                }}
            combinedIds.setLength(0);
            Arrays.fill(available, null);
            return order;
        }
    }

    public void printAvailableProd(String username, int customerId, int ecoScore){
        clearScreen();
        header(username, customerId);
        String sql = "SELECT * FROM product;";
        
        int idx = 1;
        System.out.print(String.format("\n%-10s %-36s %-12s %-10s%n", "No.", "Product ID", "Price", "Quantity"));

        boolean productsAvailable = false;

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
    
            while(rs.next()){
                productName = rs.getString("product_name");
                productId = rs.getInt("product_id");
                quantity = rs.getInt("quantity");
                price = rs.getDouble("price");

            if(quantity != 0){
                System.out.print(String.format("%-10d| %-35s | %-10.2f | %-10d%n",
                idx++, productName, price, quantity));
                
                lines('=', 70);
                System.out.println();

                combinedIds.append("|").append(productId);
                productsAvailable = true;
            }
            }

            if (!productsAvailable) {
                System.out.println("\nNo products are available at the moment. Please try checking the database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("\nPress [enter]...");
            scan.nextLine();
        }
    }

    public void seeInformation(int order, int customer_id) {
        String checkSql = "SELECT * FROM product WHERE product_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            
            pstmt.setInt(1, order);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.productId = order;
                    this.productName = rs.getString("product_name");
                    this.ecoPoints = rs.getInt("eco_score");
                    this.quantity = rs.getInt("quantity");
                    this.price = rs.getDouble("price");
                    this.productDescription = rs.getString("product_description");
    
                    clearScreen();
                    lines('_', 70);
                    System.out.print("\nPRODUCT ID: \t" + order + "\tPRODUCT NAME: \t" + productName + 
                                    String.format("\nPRICE: \t\t%.2f", price) + "\tECO POINTS: \t" + ecoPoints 
                                    + "\n\nDESCRIPTION: \t" );
                    designSentence(productDescription, 50, 1);

                    if(customer_id == 0){
                        System.out.print("\nPress [enter]...");
                        scan.nextLine();
                    }
                } else {
                    System.out.println("No product found with ID: " + order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateQty(int negativeQty) {
        boolean isSuccessful = false;
        String checkSql = "UPDATE product SET quantity = ? WHERE product_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            
            this.quantity = quantity - negativeQty;
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccessful;
    }
}