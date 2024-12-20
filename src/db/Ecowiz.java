package db;

import commerce.Customer;
import commerce.CustomerHomePage;
import commerce.Guest;
import core.DatabaseConnection;

public class Ecowiz{
    public static void main(String[] args) {
        Customer user = new Customer();
        CustomerHomePage menu = new CustomerHomePage();
        Guest guest = new Guest();

        if (!DatabaseConnection.createDatabase("init")) {
            System.out.println("Failed to connect to the database. Exiting...");
            System.exit(1);
        }

        boolean isRunning = true;
        user.instructionBack();
        while (isRunning) {
            user.clearScreen();
            user.header("", 0);

            System.out.println("\n[1] Customer \n[2] Guest \n[3] Exit ");
            int choice = user.validInput(1, "Enter your selection: \t");

            if (choice < 1 || choice > 3) {
                System.out.print("\n**Select a number from the available options!**");
                user.delay(1500);
                continue;
            }

            switch (choice) {
                case 1:
                    handleUserRole(user, menu);
                    break;
                case 2:
                    handleGuestRole(guest);
                    break;
                case 3:
                    exitApplication();
                    isRunning = false;
                    break;
            }
        }
    }

    private static void handleUserRole(Customer user, CustomerHomePage menu) {
        if (user.displayOptions() != -1) {
            menu.userRole(user);
        }
    }

    private static void handleGuestRole(Guest guest) {
        guest.displayOptions();
    }

    private static void exitApplication() {
        System.out.println("Exiting the application...");
    }
}
