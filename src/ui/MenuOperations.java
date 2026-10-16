package ui;

import common.Messages;
import common.ParcelValidator;
import common.TrackingNumberValidator;
import delivery.DeliveryStrategy;
import delivery.ExpressDelivery;
import delivery.SameDayDelivery;
import delivery.StandardDelivery;
import model.Box;
import model.Document;
import model.Fragile;
import model.Parcel;
import model.ParcelStatus;
import operations.ParcelOperations;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuOperations {
    private final Scanner scanner;
    private final ParcelOperations manager = ParcelOperations.getInstance();

    public MenuOperations(Scanner scanner) {
        this.scanner = scanner;
    }

    public void addParcel() {
        Messages.printParcelTypeMenu();

        String type = "";
        boolean validType = false;

        while (!validType) {
            System.out.print(Messages.CHOICE_PROMPT);
            type = scanner.nextLine().trim();

            if (type.equals("1") || type.equals("2") || type.equals("3")) {
                validType = true;
            } else if (type.matches("0[0-9]+")) {
                System.out.println(Messages.INVALID_CHOICE);
            } else {
                System.out.println(Messages.INVALID_CHOICE);
            }
        }

        System.out.println(Messages.SEPARATOR);

        if (type.equals("1")) {
            System.out.println("Parcel Type Selected: Document");
        } else if (type.equals("2")) {
            System.out.println("Parcel Type Selected: Box");
        } else {
            System.out.println("Parcel Type Selected: Fragile");
        }

        System.out.println(Messages.SEPARATOR);

        String trackingNumber = "";
        boolean validTrackingNumber = false;

        while (!validTrackingNumber) {
            System.out.print(Messages.TRACKING_NUMBER_PROMPT);
            trackingNumber = scanner.nextLine().toUpperCase().trim();

            try {
                if (!TrackingNumberValidator.isValid(trackingNumber)) {
                    throw new IllegalArgumentException(Messages.INVALID_TRACKING_NUMBER);
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
            System.out.print(Messages.RECEIVER_NAME_PROMPT);
            receiverName = scanner.nextLine().trim();
            if (!ParcelValidator.hasRequiredText(receiverName)) {
                System.out.println(Messages.EMPTY_FIELD);
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

            System.out.print(Messages.WEIGHT_PROMPT + "(>0-" + maxWeight + " kg): ");
            try {
                weight = Double.parseDouble(scanner.nextLine().trim());
                if (!ParcelValidator.isValidWeight(weight, maxWeight)) {
                    System.out
                            .println(Messages.weightLimit(maxWeight));
                } else {
                    validWeight = true;
                }
            } catch (NumberFormatException e) {
                System.out.println(Messages.INVALID_NUMBER);
            }
        }
        Parcel parcel;

        DeliveryStrategy standard = new StandardDelivery();
        switch (type) {
            case "1" -> {
                int pages = 0;
                boolean validPages = false;

                while (!validPages) {
                    System.out.print(Messages.PAGES_PROMPT);
                    try {
                        pages = Integer.parseInt(scanner.nextLine().trim());
                        if (!ParcelValidator.isValidPageCount(pages)) {
                            System.out.println(Messages.INVALID_PAGES);
                        } else {
                            validPages = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(Messages.INVALID_NUMBER);
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
                        System.out.print(Messages.dimensionPrompt(dimensionNames[i]));
                        try {
                            dimensions[i] = Integer.parseInt(scanner.nextLine().trim());
                            if (!ParcelValidator.isValidDimension(dimensions[i])) {
                                System.out.println(Messages.INVALID_DIMENSIONS);
                            } else {
                                validDimension = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Messages.INVALID_NUMBER);
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
                            System.out.print(Messages.HANDLING_NOTE_PROMPT);
                            note = scanner.nextLine().trim();
                            if (!ParcelValidator.hasRequiredText(note)) {
                                System.out.println(Messages.EMPTY_FIELD);
                            } else {
                                validNote = true;
                            }
                        }
                        parcel = new Fragile(trackingNumber, receiverName, weight, length, width, height, note,
                                standard);
                    }
                    default -> throw new IllegalArgumentException(Messages.INVALID_CHOICE);
                }
            }
            default -> throw new IllegalArgumentException(Messages.INVALID_CHOICE);
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
        System.out.printf(Messages.PARCEL_ADDED, parcel.calculateDeliveryFee());
    }

    public void viewAllParcels() {
        ArrayList<Parcel> parcels = manager.getAllParcels();

        if (parcels.isEmpty()) {
            System.out.println(Messages.NO_PARCELS);
            return;
        }

        int trackingWidth = "TRACKING".length();
        int typeWidth = "TYPE".length();
        int receiverWidth = "RECEIVER".length();
        int weightWidth = "CHARGEABLE WEIGHT".length();
        int serviceWidth = "SERVICE".length();
        int feeWidth = "FEE (Php)".length();
        int statusWidth = "STATUS".length();

        for (Parcel parcel : parcels) {
            String status = parcel.getStatus() == ParcelStatus.PENDING ? "Pending" : "Delivered";

            trackingWidth = Math.max(trackingWidth, parcel.getTrackingNumber().length());
            typeWidth = Math.max(typeWidth, parcel.getParcelType().length());
            receiverWidth = Math.max(receiverWidth, parcel.getReceiverName().length());
            weightWidth = Math.max(weightWidth, (parcel.calculateChargeableWeight() + "kg").length());
            serviceWidth = Math.max(serviceWidth, parcel.getDeliveryService().getServiceName().length());
            feeWidth = Math.max(feeWidth, String.format("%.2f", parcel.calculateDeliveryFee()).length());
            statusWidth = Math.max(statusWidth, status.length());
        }

        String format = "%-" + trackingWidth + "s  %-" + typeWidth + "s  %-" + receiverWidth
                + "s  %-" + weightWidth + "s  %-" + serviceWidth + "s  %-" + feeWidth
                + "s  %-" + statusWidth + "s%n";

        System.out.println();
        System.out.printf(format, "TRACKING", "TYPE", "RECEIVER", "CHARGEABLE WEIGHT", "SERVICE", "FEE (Php)",
                "STATUS");
        for (Parcel parcel : parcels) {
            String status = parcel.getStatus() == ParcelStatus.PENDING ? "Pending" : "Delivered";
            System.out.printf(format, parcel.getTrackingNumber(), parcel.getParcelType(),
                    parcel.getReceiverName(), parcel.calculateChargeableWeight() + "kg",
                    parcel.getDeliveryService().getServiceName(),
                    String.format("P%,.2f", parcel.calculateDeliveryFee()), status);
        }
    }

    public void changeDeliveryService() {
        String trackingNumber = "";
        boolean validTrackingNumber = false;

        while (!validTrackingNumber) {
            System.out.print(Messages.TRACKING_NUMBER_PROMPT);
            trackingNumber = scanner.nextLine().trim();

            if (!ParcelValidator.hasRequiredText(trackingNumber)) {
                System.out.println(Messages.EMPTY_FIELD);
            } else {
                validTrackingNumber = true;
            }
        }

        Parcel parcel = manager.findParcel(trackingNumber);

        if (parcel == null) {
            throw new IllegalArgumentException(Messages.PARCEL_NOT_FOUND);
        }

        if (parcel.getStatus() == ParcelStatus.DELIVERED) {
            throw new IllegalArgumentException(Messages.PARCEL_ALREADY_DELIVERED);
        }

        manager.changeDeliveryService(trackingNumber, selectDeliveryService());
        System.out.printf(Messages.DELIVERY_SERVICE_CHANGED, parcel.calculateDeliveryFee());
    }

    public void markAsDelivered() {
        String trackingNumber = "";
        boolean validTrackingNumber = false;
        while (!validTrackingNumber) {
            System.out.print(Messages.TRACKING_NUMBER_PROMPT);
            trackingNumber = scanner.nextLine().trim();
            if (!ParcelValidator.hasRequiredText(trackingNumber)) {
                System.out.println(Messages.EMPTY_FIELD);
            } else {
                validTrackingNumber = true;
            }
        }
        manager.markAsDelivered(trackingNumber);
        System.out.println(Messages.PARCEL_DELIVERED);
    }

    private DeliveryStrategy selectDeliveryService() {

        Messages.printDeliveryServiceMenu();

        String choice = "";
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print(Messages.CHOICE_PROMPT);

            choice = scanner.nextLine().trim();
            if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                validChoice = true;
            } else if (choice.matches("0[0-9]+")) {
                System.out.println(Messages.INVALID_CHOICE);
            } else {
                System.out.println(Messages.INVALID_CHOICE);
            }
        }

        return switch (choice) {
            case "1" -> new StandardDelivery();
            case "2" -> new ExpressDelivery();
            case "3" -> new SameDayDelivery();
            default -> throw new IllegalArgumentException(Messages.INVALID_CHOICE);
        };
    }
}
