package commerce;

public class Guest extends Products {
    public String username;

    public boolean login(){
        header("", 0);
        System.out.print("\nWhat do you want us to call you? \nEnter desired name: ");
        username = scan.nextLine().trim();

        if(username.isEmpty() || username == "exit" ){
            back(2);
            return false; 
        }
        return true;
    }

    public void displayOptions(){
        clearScreen();
        if (login()){
            clearScreen();
            username = username.substring(0, 1).toUpperCase() + username.substring(1);
            String direction = String.format("       Hello %s, thank you for your interest in contributing to the 12th SDG, Responsible Consumption and Production. EcoWiz is a platform dedicated to managing and promoting eco-friendly products, calculating environmental impact scores, and offering incentives for sustainable purchases.", username);
            String direction2 = ("\n       Buy products, earn eco points, and redeem them for discounts on your next purchase. This guest feature lets you view our products and their details. To start earning, please register first. Enjoy!");
            
            designSentence(direction, 70, 2);
            designSentence(direction2, 70, 2);
            System.out.println("\nCONVERSION SYSTEM: \n\t100pts = 10% discount \n\t200pts = 20% discount \n\t300pts = 30% discount" + 
                                        " \n\t400pts = 40% discount \n\t500pts = 50% discount");
            back(3);
            
            while(true){
                if(chooseProduct(username, 0, 0) == -1){
                    delay(1500);
                    break;
                }
            }
        }
        return;
    }
}
