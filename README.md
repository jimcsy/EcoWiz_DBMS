<h1 align ="center">
  <br>
  <img src="Ecowiz/assets/logo.png" alt="icon" height="120" /img> 
  <br>
  <strong>
  🛒 ECO-FRIENDLY POS SYSTEM
  </strong>
  <br>
</h1>

Table of Contents: 
---

1. [Project Overview](#i-project-overview)

2. [SQL Queries](#ii-sql-queries)
3. [CRUD Operation Integration](#iii-crud-operation-integration)
4. [Schema](#iv-schema)
5. [Program Ex](#v-how-to-run-the-program)
6. [About the Developer ](#about-the-developer)

---

## I. PROJECT OVERVIEW&nbsp;🔍
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This Java-based console system was designed to support the United Nations' 12th Sustainable Development Goal (SDG) – Responsible Consumption and Production. The system simulates a point-of-sale (POS) transaction platform focused on promoting eco-friendly products. It calculates environmental impact scores and offers incentives for sustainable purchases.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Developed using Java, the system integrates **JDBC (Java Database Connectivity)** to facilitate seamless communication with a MySQL database. JDBC is used to perform operations like storing and retrieving product data, transaction details, and user information efficiently, ensuring smooth and reliable interactions with the database.
Discover more: [Features](#features) • [Goal](#goal) • [Limitation](#limitation)

###  &nbsp;&nbsp;🔑&nbsp;&nbsp;**FEATURES**
- **Eco-Friendly Product Focus**: The system promotes sustainable products, contributing to responsible consumption and production.
- **Incentive and Reward System**: Users earn points for purchasing eco-friendly products, which can be redeemed for discounts, encouraging further sustainable purchasing behavio
- **Database Integration**: MySQL is used to store product details, user profiles, transaction history, and user activity.
- **User Interface**: The console-based interface allows users to browse products, make purchases, check environmental impact scores, and track reward points.

###   &nbsp;&nbsp;🥅&nbsp;&nbsp;**GOAL**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This project aims to promote eco-friendly products in our supermarket by assigning eco points (1-10) based on environmental impact scores. It offers consumers discounts based on the eco points they have earned. The core focus is on incentivizing sustainable consumer behavior and responsible consumption within the retail environment.

###   &nbsp;&nbsp;🚧&nbsp;&nbsp;**LIMITATION**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This project does not cover administrative features such as checking product availability, updating product prices, or managing product quantities. Although these features would add value to the system, they were excluded due to time constraints and the creators' involvement in other ongoing projects. 

## II. SQL QUERIES 📊
1. **Customer Table** - Stores customer details such as   `customer_id`, `user_name`, `user_password`, and `eco_points`. The customer_id is auto-incremented, starting from 10000.
```SQL
CREATE TABLE customer (
  customer_id int NOT NULL AUTO_INCREMENT,
  user_name varchar(50),
  user_password varchar(50),
  eco_points int,
  PRIMARY KEY (customer_id)
);
```
- The customer_id is auto-incremented, starting from 10000.
```sql
ALTER TABLE customer AUTO_INCREMENT = 10000;
```

2. **Product Table** - The Products table contains information about the items available in the store. It includes `product_id` as the primary key, `product_name` for the item's name, `price` to store its cost, quantity to track stock availability, `eco_score` for the product's environmental impact ranging from 1-10, and `product_description` to provide additional details about each product.
```SQL
CREATE TABLE product (
  product_id int NOT NULL,
  product_name varchar(50),
  price decimal(10,2),
  quantity int,
  eco_score int,
  product_description text,
  PRIMARY KEY (product_id)
);
```
- The `init.file` already contains values in these columns, ensuring that the store will function correctly.
```SQL
INSERT INTO product (product_id, product_name, price, quantity, eco_score, product_description) 
VALUES 
  (101, 'Single-Use Plastic Cutlery Set', 280.00, 198, 1, 'A disposable cutlery set made from plastic, convenient but environmentally harmful.'),
  (202, 'Reusable Water Bottle', 840.00, 200, 7, 'Stainless steel water bottle to reduce plastic waste. Keeps liquids hot or cold.'),
  (303, 'Organic Cotton T-Shirt', 1400.00, 200, 8, 'Eco-friendly t-shirt made from organic cotton, reducing pesticide use.'),
  (404, 'Recycled Paper Notebook', 308.00, 200, 10, 'Notebook made from 100% recycled paper with eco-friendly ink.'),
  (505, 'Biodegradable Trash Bag', 503.44, 200, 6, 'Trash bag made from biodegradable materials to reduce landfill impact.'),
  (606, 'Solar-Powered Charger', 1679.44, 190, 10, 'A charger that uses solar energy to power device, reducing electricity use.'),
  (707, 'Compostable Food Container', 700.00, 200, 8, 'Food container made from compostable materials, ideal for reducing plastic waste.'),
  (808, 'Eco-Friendly Laundry Detergent', 560.00, 200, 7, 'Plant-based detergent with no harsh chemicals, safe for the environment.'),
  (909, 'Plastic Shower Curtain', 727.44, 199, 2, 'A standard plastic shower curtain that is water-resistant but not eco-friendly.');
);
```

3. **Transaction Table** - This table records customer transactions. It has `transaction_id` as the primary key, `customer_id` as a foreign key linking to the Customer table, `total_amount` for the total cost of the transaction, `eco_score` for the environmental score associated with the purchase, and `transaction_date` to keep a record of when the transaction occurred.
```SQL
CREATE TABLE user_transaction (
  transaction_id int NOT NULL AUTO_INCREMENT,
  customer_id int,
  total_amount decimal(10,2),
  eco_score int,
  transaction_date date,
  PRIMARY KEY (transaction_id),
  KEY customer_id (customer_id),
  CONSTRAINT fk_customer_transaction FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE
);
```

4. **Transaction Product Table** - This table links products to transactions and allows a single transaction to involve multiple products. It includes `transaction_id` and `product_id` as foreign keys referencing the Transactions and Products tables, respectively, and `quantity` to record how many units of a product were purchased in a specific transaction.
```sql
CREATE TABLE transaction_product (
  transaction_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (transaction_id, product_id),
  FOREIGN KEY (transaction_id) REFERENCES user_transaction(transaction_id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);
```

## III. CRUD OPERATION INTEGRATION 🔄

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To build the connection between MySQL Workbench and the Java program, JDBC (Java Database Connectivity) was utilized. Below is the integration of the Create, Read, Update, and Delete operations.

1. **Create Operation**: The insertion of new data, such as login credentials and transactions, uses the create operation. For example, here's a snippet of code for saving a new customer.
```java
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
```
2. **Read Operation**: The retrieval of product information from Ecowiz uses the read operation.
```java
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
```
3. **Update Operation**: The modification of the eco_score of a customer whenever transactions are made uses the update operation.
```java
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
```
4. **Delete Operation**: The deletion of a user's account uses the delete operation.
```java
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
```

## IV. SCHEMA 🗂️
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Here’s the schema created using `dbdiagram.io`. Below is the explanation of the existing entity relationships in the database.
![Schema](Ecowiz/src/db/schema.png)
#### 1. **Customer and User_Transaction**
- **Relationship**: One-to-Many (One customer can have many transactions).
- Each transaction is associated with a single customer, but a customer can have multiple transactions over time.

#### 2. **User_Transaction and Transaction_Product**
- **Relationship**: One-to-One (Each user_transaction is tied to a single transaction_product record).
- Each record in the `user_transaction` table corresponds directly to one record in the `transaction_product` table. This means that the details of the transaction are closely linked to the products involved in that transaction.

#### 3. **Product and Transaction_Product**
- **Relationship**: One-to-Many (One product can appear in many transactions).
- A product can be part of multiple transactions (many different transactions can contain the same product), but each record in the `transaction_product` table refers to one specific product.


## V. HOW TO RUN THE PROGRAM❓

### 1. **Prerequisite**&nbsp;🧩
  + Java Development Kit
  + Database Management System – `MySql`
  + Java Database Connectivity (JDBC) driver
  + IDE - `Visual Studio Code (VsCode)`


### 2. **Run the Program**&nbsp;▶️ 
- **Download** and **extract** the folder from this repository, `ecowiz.zip`.
- **Open** the extracted folder in a **new window** within `VSCode`.
- **Check the File Structure**
  ```bash
  lib/
  ├── mysql-connector-j-9.1.0.jar
  src/
  ├── commerce/
  │   ├── Customer.java
  │   ├── CustomerCredentials.java
  │   ├── CustomerHomePage.java
  │   ├── Guest.java
  │   └── Products.java
  ├── core/
  │   ├── DatabaseConnection.java
  │   └── General.java
  ├── main/
  │   └── Ecowiz.java
  ├── transaction/
  │   ├── Buy.java
  │   ├── Redeem.java
  │   └── Transaction.java
  ecowizDatabase.sql
  README.md
  ```

- **Change Credentials** <br>
Locate the `DatabaseConnection.java` file in your `VsCode` and update the values with your *`MySql credentials`*.

  ```java
  // Class file name: DBConnection.java
  private static String dbUrl = "jdbc:mysql://localhost:3306/";
  private static String dbUser = "root";
  private static String dbPassword = "genesis";  
  private static String sqlFilePath = "C:\\Users\\Genesis Jim\\Desktop\\OOP_Finals - Copy (2)\\ecowizDatabase.sql"; 
  ```
#### 2.1 **Using Visual Studio Code (VSCode):**
  - After changing the credential values, locate the `Java Projects` in the Explorer tab and add the `mysql-connector-java.jar` file under the `Referenced Libraries `section.
  - **Run** the `Ecowiz.java` file.
  - If errors occur, please verify the file structure and ensure that your credentials are correctly entered.

#### 2.2 Using Command Prompt (CMD):
- **Navigate** to the project directory by running the following command:
  - ```java
    cd "path\to\your\project\src"
    #cd "C:\Users\Genesis Jim\Desktop\Finals\EcoWiz\src"
    ```
- **Compile** the program using the following command:
  - ```java
    javac -d out -cp ".;path\to\your\mysql-connector-java.jar" main\*.java transaction\*.java commerce\*.java core\*.java

    ```
  - This step will generate a folder named `out` within your src directory. Inside the `out` folder, you will find *four subfolders*, each named after the corresponding package (`main`, `transaction`, `commerce`, `core`). These subfolders will contain the compiled `.class files`, which are the converted versions of your `.java files`.
-  **Run** the compiled program using the following command:
    - ```java
      java -cp "out; path\to\your\mysql-connector-java.jar" main.Ecowiz
      ```

About the Developer 🧑‍💻
---
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Good day! I am **Genesis**, the developer of **Ecowiz**, an eco-friendly Point of Sale (POS) system. This project was developed as part of the requirements for my **Database Management System** course.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The journey of creating Ecowiz has been a fulfilling experience, combining my passion for sustainable practices with technical skills in `Java` and `MySQL`. Through this project, I aim to promote eco-conscious shopping by incorporating features like environmental impact calculations and incentives for sustainable choices.

**Thank you for taking the time to learn about Ecowiz!**

---
[Back to Top](#i-project-overview)