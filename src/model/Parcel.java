package model;

import common.Messages;
import delivery.DeliveryStrategy;
import delivery.SameDayDelivery;

public abstract class Parcel {
    private final String trackingNumber;
    private final String receiverName;
    private final double actualWeight;
    private DeliveryStrategy deliveryService;
    private ParcelStatus status = ParcelStatus.PENDING;

    protected Parcel(String trackingNumber, String receiverName, double actualWeight,
            DeliveryStrategy deliveryService) {
        if (trackingNumber == null || !trackingNumber.matches("^ABC\\d{4}$")) {
            throw new IllegalArgumentException(Messages.INVALID_TRACKING_NUMBER);
        }
        if (receiverName == null || receiverName.isBlank()) {
            throw new IllegalArgumentException(Messages.EMPTY_FIELD);
        }
        if (!Double.isFinite(actualWeight) || actualWeight <= 0) {
            throw new IllegalArgumentException(Messages.INVALID_WEIGHT);
        }
        if (deliveryService == null) {
            throw new IllegalArgumentException(Messages.INVALID_DELIVERY_SERVICE);
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
            throw new IllegalArgumentException(Messages.PARCEL_ALREADY_DELIVERED);
        }
        validateDeliveryService(service);
        deliveryService = service;
    }

    // Subclass dimensions must be initialized before checking chargeable weight.
    protected void validateDeliveryService(DeliveryStrategy service) {
        if (service == null) {
            throw new IllegalArgumentException(Messages.INVALID_DELIVERY_SERVICE);
        }
        if (service instanceof SameDayDelivery && calculateChargeableWeight() > 10) {
            throw new IllegalArgumentException(Messages.SAME_DAY_WEIGHT_LIMIT);
        }
    }

    public void markAsDelivered() {
        if (status == ParcelStatus.DELIVERED) {
            throw new IllegalArgumentException(Messages.PARCEL_ALREADY_DELIVERED);
        }
        status = ParcelStatus.DELIVERED;
    }

    public String getTrackingNumber() { return trackingNumber; }
    public String getReceiverName() { return receiverName; }
    public double getActualWeight() { return actualWeight; }
    public DeliveryStrategy getDeliveryService() { return deliveryService; }
    public ParcelStatus getStatus() { return status; }
    
}
