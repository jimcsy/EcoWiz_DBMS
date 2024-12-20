package commerce;

public class Customer extends CustomerCredentials{
    private String username, password;

    public int displayOptions(){
        int choice;
        while (true){
            clearScreen();
            header("", 0);
            
            System.out.println("\n[1] Login \n[2] Sign-up \n[3] Exit");
            choice = validInput(2, "Enter choice: \t");

            if(choice == -1){
                back(2);
                return -1;
            }

            if(choice < 1 || choice > 3){
                System.out.print("\n**Select a number from the available options!**");
                delay(1500);
                continue;
            }

            switch(choice){
                case 1:
                    if(login()){
                        return 1;
                    }
                    continue;
                case 2:
                    if(signup()){
                        return 1;
                    }
                    continue;
                case 3:
                    System.out.println("Logout");
                    return -1;
            }
            break;
        }
        return 0;
    }

    public boolean login(){
        boolean isSucccessful = true;
        System.out.print("\nWait for a moment...");

        while (isSucccessful){
            delay(1500);
            clearScreen();
            lines('+', 70);
            System.out.println("\n" +  String.format("%38s", "L O G I N"));
            lines('-', 70);

            System.out.println();
            int customerId = validInput(2, "Enter customer iD: \t");

            if(customerId == -1){
                back(2);
                isSucccessful = false;
                break;
            }else if (customerId < 10000 || customerId > 99999){
                System.out.println("Invalid customer ID. It must be a 5-digit number. Please try again.");
                isSucccessful = false;
                break;
            }else{
                System.out.print("Enter password: \t");
                password = scan.nextLine();

                isSucccessful = super.customerLogin(customerId, password);
                break;
            }
            
        }
        return isSucccessful;
    }

    public boolean signup () {
        boolean isSuccessful = true;
        System.out.print("\nWait for a moment...");
        clearScreen();
        System.out.print("Note: \tUsername and password must be at least 8 characters long.\n\tNo whitespace in the username. \n\nPress [enter]...");
        scan.nextLine();

        while (isSuccessful) {
            clearScreen();

            lines('+', 70);
            System.out.println("\n" +  String.format("%41s", "S I G N - U P"));
            lines('-', 70);

            System.out.print("\nEnter username: \t");
            username = scan.nextLine().trim();
    
            if (username.isEmpty()) {
                back(2);
                isSuccessful = false;
                break;              
            }else if (username.contains(" ")) {
                System.out.print("Username must not contain whitespaces.");
                delay(1500);
            }else if (username.length() < 8) {
                System.out.print("Username must be at least 8 characters!");
                delay(1500);
            } else {
                isSuccessful = true;
                break;
            }
        }
    
        while(isSuccessful){
            System.out.print("Enter password: \t");
            password = scan.nextLine();
    
            if (password.isEmpty()) {
                back(2);
                isSuccessful = false;
                break;              
            }else if (password.length() < 8) {
                System.out.println("\nPassword must be at least 8 characters!");
                delay(1500);
            }else{
                isSuccessful = true;
                super.saveCredentials(username, password);
                break;
            }
        }
        return isSuccessful;
    }
}
