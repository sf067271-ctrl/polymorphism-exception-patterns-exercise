public class Document extends Parcel {
    private static final double MAX_WEIGHT = 0.5;
    private final double BASE_FEE = 50;
    private final int numberOfPages;

    public Document(String trackingNumber, String receiverName, double actualWeight,
            int numberOfPages, DeliveryStrategy service) {
        super(trackingNumber, receiverName, actualWeight, service);
        if (actualWeight > MAX_WEIGHT) {
            throw new IllegalArgumentException("Document weight must not exceed 0.5 kg.");
        }
        if (numberOfPages < 1 || numberOfPages > 100) {
            throw new IllegalArgumentException("Number of pages must be between 1 and 100.");
        }
        this.numberOfPages = numberOfPages;
        validateDeliveryService(service);
    }

    public static double getMaxWeight() { return MAX_WEIGHT; }

    @Override
    public double calculateChargeableWeight() { return getActualWeight(); }

    @Override
    public double calculateBaseFee() { return BASE_FEE; }

    @Override
    public String getParcelType() { return "Document"; }

    public int getNumberOfPages() { return numberOfPages; }
}
