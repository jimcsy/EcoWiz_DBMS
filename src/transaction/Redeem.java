package transaction;

public class Redeem extends Transaction{
    int totalEcoScore = 0;
    double totalAmount = 0;
    double discTotal, discPercentage;

    public int buyProduct(String username, int customerId, int ecoScore){
        clearScreen();
        header(username, customerId);
        System.out.println("\nREDEEM ECO POINTS");
        System.out.print("Note: Purchases made with eco points cannot earn additional eco points. \n");
        back(3);

        discPercentage = redeemPoints(username, customerId, ecoScore);
        if (discPercentage != -1){
            super.buyProduct(username, customerId, ecoScore);
            return totalEcoScore;
        }
        return -1;
    }

    public int totalAmount(String username, int orderQuantity, int customerId){
        totalAmount = orderQuantity * getProdPrice();
        discTotal = totalAmount * (1 - (discPercentage / 100)); 
        
        lines('=', 70);
        System.out.println( "\n" + orderQuantity + "pc/s. of '" + getProdName() + "' cost " + String.format("%.2f", totalAmount) + " pesos.");
        lines('-', 70);
        System.out.println("\nDiscount: \t\t" + discPercentage + "%");
        System.out.println("Discounted Price: \t" + String.format("%.2f", discTotal) + " pesos");

        int choice = 0;
        do{
            System.out.println();
            choice = validInput(1, "Are you sure want to make this purchase? [1] yes [2] no: ");
        }while(!(choice == 1 || choice == 2));

        if(choice == 1){
            int hundredths = (int) (discPercentage * 10);
            totalEcoScore = -1 * hundredths;
            saveTransaction(customerId, totalAmount, 0, orderQuantity);
            updateQty(orderQuantity);
            printReceipt(username, customerId, orderQuantity, discTotal, totalEcoScore);

        }else if(choice == 2 || choice == -1){
            System.out.print("\nThe purchase has been cancelled...");
            return -1;
        }
        return totalEcoScore;
    } 

    public int redeemPoints(String username, int customerId, int ecoPoints){
        clearScreen();
        header(username, customerId);
        
        System.out.println("\n1st Step: Convert your eco score! \n\nYour eco score: " + ecoPoints);
        int choice;
        int n = 0;

        if (ecoPoints < 100){
            System.out.print("\nNo voucher can be redeemed as of now. \n");
            back(3);
            System.out.print("\nReturning...");
            return -1;
        }else{
            int firstDig = Integer.toString(ecoPoints).charAt(0) - '0';
            int numDigits = (int) (Math.log10(ecoPoints) + 1) - 2 ; //1
            if (numDigits > 1) firstDig = firstDig * 5;

            for (int x = 1; x <= firstDig; x++){
                System.out.println("[" + x +"] " + x * 100 + "pts = " + x * 10 + "% discount");
                n++;
                if (x == 5){
                    break;
                }
            }
        }

        while(true){
            System.out.println();
            choice = validInput(2, "Enter your choice: ");

            if (choice == -1){
                System.out.print("\nReturning...");
                return -1;
            } else if(choice <= 0 || choice > n){
                System.out.println("Choose only from the list!");
                continue;
            }else{
                System.out.println(choice * 10);
                return choice * 10;
            }
        }
    }
}
