import java.util.ArrayList;
import java.util.Scanner;

public class MenuOperations {
    private final Scanner scanner;
    private final ParcelOperations manager = ParcelOperations.getInstance();

    public MenuOperations(Scanner scanner) {
        this.scanner = scanner;
    }

    public void addParcel() {
        System.out.println("\nSelect Parcel Type: ");
        System.out.println("[1] Document");
        System.out.println("[2] Box");
        System.out.println("[3] Fragile");

        String type = "";
        boolean validType = false;

        while (!validType) {
            System.out.print("Enter choice: ");
            type = scanner.nextLine().strip();

            if (type.equals("1") || type.equals("2") || type.equals("3")) {
                validType = true;
            } else if (type.matches("0[0-9]+")) {
                System.out.println("Enter your choice without leading zeros.");
            } else {
                System.out.println("Invalid choice. Please select 1 to 3.");
            }
        }

        String trackingNumber = "";
        boolean validTrackingNumber = false;

        while (!validTrackingNumber) {
            System.out.print("Tracking Number: ");
            trackingNumber = scanner.nextLine().strip();

            try {
                if (!trackingNumber.matches("^ABC\\d{4}$")) {
                    throw new IllegalArgumentException("Tracking number must be ABC followed by exactly four digits.");
                }
                manager.checkDuplicateTracking(trackingNumber);
                validTrackingNumber = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        String receiverName = "";

        boolean validReceiver = false;

        while (!validReceiver) {
            System.out.print("Receiver Name: ");
            receiverName = scanner.nextLine().strip();
            if (receiverName.isBlank()) {
                System.out.println("Receiver name must not be empty.");
            } else {
                validReceiver = true;
            }
        }

        double maxWeight = 0.0;

        switch (type) {
            case "1" -> maxWeight = Document.getMaxWeight();
            case "2" -> maxWeight = Box.getMaxWeight();
            default -> maxWeight = Fragile.getMaxWeight();
        }

        double weight = 0;
        boolean validWeight = false;
        while (!validWeight) {
            System.out.print("Actual Weight (kg): ");
            try {
                weight = Double.parseDouble(scanner.nextLine().strip());
                if (!Double.isFinite(weight) || weight <= 0 || weight > maxWeight) {
                    System.out
                            .println("Weight must be a finite number greater than 0 and at most " + maxWeight + " kg.");
                } else {
                    validWeight = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
        Parcel parcel;

        // Validate details with Standard first; store only after the user's service is
        // valid.
        DeliveryStrategy standard = new StandardDelivery();
        switch (type) {
            case "1" -> {
                int pages = 0;
                boolean validPages = false;
                while (!validPages) {
                    System.out.print("Number of Pages (1-100): ");
                    try {
                        pages = Integer.parseInt(scanner.nextLine().strip());
                        if (pages < 1 || pages > 100) {
                            System.out.println("Number of pages must be between 1 and 100.");
                        } else {
                            validPages = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid whole number.");
                    }
                }
                parcel = new Document(trackingNumber, receiverName, weight, pages, standard);
            }
            case "2", "3" -> {

                String[] dimensionNames = { "Length", "Width", "Height" };
                int[] dimensions = new int[3];

                for (int i = 0; i < dimensions.length; i++) {
                    boolean validDimension = false;
                    while (!validDimension) {
                        System.out.print(dimensionNames[i] + " (cm): ");
                        try {
                            dimensions[i] = Integer.parseInt(scanner.nextLine().strip());
                            if (dimensions[i] <= 0) {
                                System.out.println("Dimensions must be greater than zero.");
                            } else {
                                validDimension = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid whole number.");
                        }
                    }
                }
                int length = dimensions[0];
                int width = dimensions[1];
                int height = dimensions[2];
                switch (type) {
                    case "2" -> parcel = new Box(trackingNumber, receiverName, weight, length, width, height, standard);
                    case "3" -> {
                        String note = "";
                        boolean validNote = false;
                        while (!validNote) {
                            System.out.print("Handling Note: ");
                            note = scanner.nextLine().strip();
                            if (note.isBlank()) {
                                System.out.println("Handling note must not be empty.");
                            } else {
                                validNote = true;
                            }
                        }
                        parcel = new Fragile(trackingNumber, receiverName, weight, length, width, height, note,
                                standard);
                    }
                    default -> throw new IllegalArgumentException("Invalid parcel type.");
                }
            }
            default -> throw new IllegalArgumentException("Invalid parcel type.");
        }
        boolean validDeliveryService = false;
        while (!validDeliveryService) {
            DeliveryStrategy service = selectDeliveryService();
            try {
                parcel.changeDeliveryService(service);
                validDeliveryService = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        manager.addParcel(parcel);
        System.out.printf("Parcel added successfully! Delivery fee: Php %.2f%n", parcel.calculateDeliveryFee());
    }

    public void viewAllParcels() {
        ArrayList<Parcel> parcels = manager.getAllParcels();

        if (parcels.isEmpty()) {
            System.out.println("No parcels available.");
            return;
        }

        int trackingWidth = "TRACKING".length();
        int typeWidth = "TYPE".length();
        int receiverWidth = "RECEIVER".length();
        int weightWidth = "WEIGHT".length();
        int serviceWidth = "SERVICE".length();
        int feeWidth = "FEE (Php)".length();
        int statusWidth = "STATUS".length();

        for (Parcel parcel : parcels) {
            String status = parcel.getStatus() == ParcelStatus.PENDING ? "Pending" : "Delivered";

            trackingWidth = Math.max(trackingWidth, parcel.getTrackingNumber().length());
            typeWidth = Math.max(typeWidth, parcel.getParcelType().length());
            receiverWidth = Math.max(receiverWidth, parcel.getReceiverName().length());
            weightWidth = Math.max(weightWidth, String.format("%.2f", parcel.calculateChargeableWeight()).length());
            serviceWidth = Math.max(serviceWidth, parcel.getDeliveryService().getServiceName().length());
            feeWidth = Math.max(feeWidth, String.format("%.2f", parcel.calculateDeliveryFee()).length());
            statusWidth = Math.max(statusWidth, status.length());
        }

        String format = "%-" + trackingWidth + "s  %-" + typeWidth + "s  %-" + receiverWidth
                + "s  %-" + weightWidth + "s  %-" + serviceWidth + "s  %-" + feeWidth
                + "s  %-" + statusWidth + "s%n";

        System.out.println();
        System.out.printf(format, "TRACKING", "TYPE", "RECEIVER", "WEIGHT", "SERVICE", "FEE (Php)", "STATUS");
        for (Parcel parcel : parcels) {
            String status = parcel.getStatus() == ParcelStatus.PENDING ? "Pending" : "Delivered";
            System.out.printf(format, parcel.getTrackingNumber(), parcel.getParcelType(),
                    parcel.getReceiverName(), String.format("%.2f", parcel.calculateChargeableWeight()),
                    parcel.getDeliveryService().getServiceName(),
                    String.format("%.2f", parcel.calculateDeliveryFee()), status);
        }
    }

    public void changeDeliveryService() {

        if (manager.getAllParcels().isEmpty()) {
            System.out.println("No parcels available.");
            return;
        }

        String trackingNumber = "";
        boolean validTrackingNumber = false;

        while (!validTrackingNumber) {
            System.out.print("Tracking Number: ");
            trackingNumber = scanner.nextLine().strip();

            if (trackingNumber.isBlank()) {
                System.out.println("Tracking number must not be empty.");
            } else {
                validTrackingNumber = true;
            }
        }

        Parcel parcel = manager.findParcel(trackingNumber);

        if (parcel == null) {
            throw new IllegalArgumentException("Parcel not found!");
        }

        if (parcel.getStatus() == ParcelStatus.DELIVERED) {
            throw new IllegalArgumentException("Parcel already delivered!");
        }
        
        manager.changeDeliveryService(trackingNumber, selectDeliveryService());
        System.out.printf("Delivery service changed! New fee: Php %.2f%n", parcel.calculateDeliveryFee());
    }

    public void markAsDelivered() {
        if (manager.getAllParcels().isEmpty()) {
            System.out.println("No parcels available.");
            return;
        }

        String trackingNumber = "";
        boolean validTrackingNumber = false;
        while (!validTrackingNumber) {
            System.out.print("Tracking Number: ");
            trackingNumber = scanner.nextLine().strip();
            if (trackingNumber.isBlank()) {
                System.out.println("Tracking number must not be empty.");
            } else {
                validTrackingNumber = true;
            }
        }
        manager.markAsDelivered(trackingNumber);
        System.out.println("Parcel delivered successfully!");
    }

    private DeliveryStrategy selectDeliveryService() {
        System.out.println("\nSelect Delivery Service: ");
        System.out.println("[1] Standard Delivery");
        System.out.println("[2] Express Delivery");
        System.out.println("[3] Same Day Delivery");

        String choice = "";
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("Enter choice: ");
            choice = scanner.nextLine().strip();
            if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                validChoice = true;
            } else if (choice.matches("0[0-9]+")) {
                System.out.println("Enter your choice without leading zeros.");
            } else {
                System.out.println("Invalid choice. Please select 1 to 3.");
            }
        }

        return switch (choice) {
            case "1" -> new StandardDelivery();
            case "2" -> new ExpressDelivery();
            case "3" -> new SameDayDelivery();
            default -> throw new IllegalArgumentException("Invalid choice.");
        };
    }

}
