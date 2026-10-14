package common;

public final class Messages {
    private Messages() {}

    public static final String CHOICE_PROMPT = "Enter your choice: ";
    public static final String TRACKING_NUMBER_PROMPT = "Tracking Number (ABC0000): ";
    public static final String RECEIVER_NAME_PROMPT = "Receiver Name: ";
    public static final String WEIGHT_PROMPT = "Actual Weight ";
    public static final String PAGES_PROMPT = "Number of Pages (1-100): ";
    public static final String HANDLING_NOTE_PROMPT = "Handling Note: ";

    public static final String INVALID_CHOICE = "Invalid choice. Please select one of the available options.";
    public static final String EMPTY_FIELD = "Required value must not be empty.";
    public static final String INVALID_NUMBER = "Please enter a valid number.";
    public static final String INVALID_WEIGHT = "Weight must be a finite number greater than zero.";
    public static final String INVALID_TRACKING_NUMBER = "Tracking number must be ABC followed by exactly four digits.";
    public static final String DUPLICATE_TRACKING_NUMBER = "Tracking number already exists!";
    public static final String INVALID_PAGES = "Number of pages must be between 1 and 100.";
    public static final String INVALID_DIMENSIONS = "Dimensions must be greater than zero.";
    public static final String INVALID_DELIVERY_SERVICE = "Select a valid delivery service.";
    public static final String SAME_DAY_WEIGHT_LIMIT = "Same Day delivery is only for parcels up to 10 kg!";
    public static final String PARCEL_NOT_FOUND = "Parcel not found!";
    public static final String PARCEL_ALREADY_DELIVERED = "Parcel already delivered!";
    public static final String PARCEL_WAS_ALREADY_DELIVERED = "Parcel was already delivered.";

    public static final String NO_PARCELS = "No parcels available.";
    public static final String PARCEL_ADDED = "Parcel added successfully! Delivery fee: Php %.2f%n";
    public static final String DELIVERY_SERVICE_CHANGED = "Delivery service changed! New fee: Php %.2f%n";
    public static final String PARCEL_DELIVERED = "Parcel delivered successfully!";
    public static final String INPUT_ENDED = "Input ended.";
    public static final String GOODBYE = "Thank you for using the Parcel Delivery System!";

    public static final String SEPARATOR = "-".repeat(40);

    public static String weightLimit(double maxWeight) {
        return "Weight must be greater than 0 and at most " + maxWeight + " kg.";
    }

    public static String dimensionPrompt(String dimensionName) {
        return dimensionName + " (cm): ";
    }

    public static void printMainMenu() {
        System.out.println("\n=========================================");
        System.out.println("          PARCEL DELIVERY SYSTEM");
        System.out.println("=========================================");
        System.out.println("1. Add Parcel");
        System.out.println("2. View All Parcels");
        System.out.println("3. Change Delivery Service");
        System.out.println("4. Mark as Delivered");
        System.out.println("5. Exit");
        System.out.println("=========================================");
        System.out.print(CHOICE_PROMPT);
    }

    public static void printParcelTypeMenu() {
        System.out.println("Select Parcel Type: ");
        System.out.println("1. Document");
        System.out.println("2. Box");
        System.out.println("3. Fragile");
    }

    public static void printDeliveryServiceMenu() {
        System.out.println(SEPARATOR);
        System.out.println("Select Delivery Service: ");
        System.out.println("1. Standard Delivery");
        System.out.println("2. Express Delivery");
        System.out.println("3. Same Day Delivery");
        System.out.println(SEPARATOR);
    }
}
