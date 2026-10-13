public class Fragile extends Box {
    private static final double MAX_WEIGHT = 30;
    private final double HANDLING_FEE = 100;
    private final String handlingNote;

    public Fragile(String trackingNumber, String receiverName, double actualWeight,
            int length, int width, int height, String handlingNote,
            DeliveryStrategy service) {
        super(trackingNumber, receiverName, actualWeight, length, width, height, service);
        if (actualWeight > MAX_WEIGHT) {
            throw new IllegalArgumentException("Fragile weight must not exceed 30 kg.");
        }
        if (handlingNote == null || handlingNote.isBlank()) {
            throw new IllegalArgumentException("Handling note must not be empty.");
        }
        this.handlingNote = handlingNote.strip();
    }

    public static double getMaxWeight() { return MAX_WEIGHT; }

    @Override
    public double calculateBaseFee() { return super.calculateBaseFee() + HANDLING_FEE; }

    @Override
    public String getParcelType() { return "Fragile"; }

    public String getHandlingNote() { return handlingNote; }
}
