package ui;

import common.Messages;
import java.util.Scanner;

public class Menu {

    public void displayMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            MenuOperations operations = new MenuOperations(scanner);
            boolean running = true;
            while (running) {
                Messages.printMainMenu();

                if (!scanner.hasNextLine()) {
                    break;
                }

                try {
                    String input = scanner.nextLine().trim();
                    
                    if (input.matches("0[0-9]+")) {
                        throw new IllegalArgumentException(Messages.INVALID_CHOICE);
                    }
                    int choice = Integer.parseInt(input);
                    if (!input.matches("[1-5]")) {
                        throw new IllegalArgumentException(Messages.INVALID_CHOICE);
                    }

                    switch (choice) {
                        case 1 -> operations.addParcel();
                        case 2 -> operations.viewAllParcels();
                        case 3 -> operations.changeDeliveryService();
                        case 4 -> operations.markAsDelivered();
                        case 5 -> running = false;
                        default -> throw new IllegalArgumentException(Messages.INVALID_CHOICE);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(Messages.INVALID_NUMBER);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (java.util.NoSuchElementException e) {
                    System.out.println(Messages.INPUT_ENDED);
                    running = false;
                }
            }
            System.out.println(Messages.GOODBYE);
        }
    }
}
