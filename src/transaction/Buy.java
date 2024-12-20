package transaction;

public class Buy extends Transaction{
    private int totalEcoScore = 0, choice = 0;
    private double totalAmount = 0;

    public int totalAmount(String username, int orderQuantity, int customerId){
        lines('=', 70);
        totalAmount = orderQuantity * getProdPrice();
        System.out.println("\n"+ orderQuantity + "pc/s. of '" + getProdName() + "' cost " + String.format("%.2f", totalAmount) + " pesos.");

        do{
            System.out.println();
            choice = validInput(1, "Are you sure want to make this purchase? [1] yes [2] no: ");
        }while(!(choice == 1 || choice == 2));

        if(choice == 1){
            totalEcoScore = getEcoScore() * orderQuantity;
            System.out.println("You scored " + totalEcoScore + " eco points with this purchase!");
            delay(1500);
            saveTransaction(customerId, totalAmount, totalEcoScore, orderQuantity);
            updateQty(orderQuantity);
            printReceipt(username, customerId, orderQuantity, totalAmount, totalEcoScore);

        }else if(choice == 2 || choice == -1){
            System.out.print("The purchase has been cancelled...");
            return -1;
        }
        return totalEcoScore;
    }
}