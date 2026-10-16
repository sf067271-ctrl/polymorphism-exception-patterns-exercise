package common;

public final class ParcelValidator {
    private ParcelValidator() {}

    public static boolean hasRequiredText(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean isValidWeight(double weight) {
        return Double.isFinite(weight) && weight > 0;
    }

    public static boolean isValidWeight(double weight, double maxWeight) {
        return isValidWeight(weight) && weight <= maxWeight;
    }

    public static boolean isValidPageCount(int pages) {
        return pages >= 1 && pages <= 100;
    }

    public static boolean isValidDimension(int dimension) {
        return dimension > 0;
    }
}
