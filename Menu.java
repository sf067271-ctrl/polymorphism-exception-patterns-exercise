import java.util.Scanner;

public class Menu {

    public void displayMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            MenuOperations operations = new MenuOperations(scanner);
            boolean running = true;
            while (running) {
                System.out.println("\n=========================================");
                System.out.println("          PARCEL DELIVERY SYSTEM");
                System.out.println("=========================================");
                System.out.println("[1] Add Parcel");
                System.out.println("[2] View All Parcels");
                System.out.println("[3] Change Delivery Service");
                System.out.println("[4] Mark as Delivered");
                System.out.println("[5] Exit");
                System.out.println("=========================================");
                System.out.print("Enter your choice: ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                try {
                    String input = scanner.nextLine().strip();
                    if (input.matches("0[0-9]+")) {
                        throw new IllegalArgumentException("Enter your choice without leading zeros.");
                    }
                    int choice = Integer.parseInt(input);
                    if (!input.matches("[1-5]")) {
                        throw new IllegalArgumentException("Invalid choice. Please select 1 to 5.");
                    }
                    switch (choice) {
                        case 1 -> operations.addParcel();
                        case 2 -> operations.viewAllParcels();
                        case 3 -> operations.changeDeliveryService();
                        case 4 -> operations.markAsDelivered();
                        case 5 -> running = false;
                        default -> throw new IllegalArgumentException("Invalid choice. Please select 1 to 5.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (java.util.NoSuchElementException e) {
                    System.out.println("Input ended.");
                    running = false;
                }
            }
            System.out.println("Thank you for using the Parcel Delivery System!");
        }
    }
}
