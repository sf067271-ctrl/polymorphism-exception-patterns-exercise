public abstract class Parcel {
    private final String trackingNumber;
    private final String receiverName;
    private final double actualWeight;
    private DeliveryStrategy deliveryService;
    private ParcelStatus status = ParcelStatus.PENDING;

    protected Parcel(String trackingNumber, String receiverName, double actualWeight,
            DeliveryStrategy deliveryService) {
        if (trackingNumber == null || !trackingNumber.matches("^ABC\\d{4}$")) {
            throw new IllegalArgumentException("Tracking number must be ABC followed by exactly four digits.");
        }
        if (receiverName == null || receiverName.isBlank()) {
            throw new IllegalArgumentException("Receiver name must not be empty.");
        }
        if (!Double.isFinite(actualWeight) || actualWeight <= 0) {
            throw new IllegalArgumentException("Actual weight must be a finite number greater than zero.");
        }
        if (deliveryService == null) {
            throw new IllegalArgumentException("Select a valid delivery service.");
        }
        this.trackingNumber = trackingNumber;
        this.receiverName = receiverName.strip();
        this.actualWeight = actualWeight;
        this.deliveryService = deliveryService;
    }

    public abstract double calculateChargeableWeight();
    public abstract double calculateBaseFee();
    public abstract String getParcelType();

    public double calculateDeliveryFee() {
        return deliveryService.calculateDeliveryFee(calculateBaseFee());
    }

    public void changeDeliveryService(DeliveryStrategy service) {
        if (status == ParcelStatus.DELIVERED) {
            throw new IllegalArgumentException("Parcel already delivered!");
        }
        validateDeliveryService(service);
        deliveryService = service;
    }

    // Subclass dimensions must be initialized before checking chargeable weight.
    protected void validateDeliveryService(DeliveryStrategy service) {
        if (service == null) {
            throw new IllegalArgumentException("Select a valid delivery service.");
        }
        if (service instanceof SameDayDelivery && calculateChargeableWeight() > 10) {
            throw new IllegalArgumentException("Same Day delivery is only for parcels up to 10 kg!");
        }
    }

    public void markAsDelivered() {
        if (status == ParcelStatus.DELIVERED) {
            throw new IllegalArgumentException("Parcel was already delivered.");
        }
        status = ParcelStatus.DELIVERED;
    }

    public String getTrackingNumber() { return trackingNumber; }
    public String getReceiverName() { return receiverName; }
    public double getActualWeight() { return actualWeight; }
    public DeliveryStrategy getDeliveryService() { return deliveryService; }
    public ParcelStatus getStatus() { return status; }
}
