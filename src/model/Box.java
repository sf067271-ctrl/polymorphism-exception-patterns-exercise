package model;

import common.Messages;
import delivery.DeliveryStrategy;

public class Box extends Parcel {
    private static final double MAX_WEIGHT = 50;
    private final double RATE_PER_KG = 40;
    private final double VOLUMETRIC_DIVISOR = 3500;
    private final int length;
    private final int width;
    private final int height;

    public Box(String trackingNumber, String receiverName, double actualWeight,
            int length, int width, int height, DeliveryStrategy service) {
        super(trackingNumber, receiverName, actualWeight, service);
        if (actualWeight > MAX_WEIGHT) {
            throw new IllegalArgumentException(Messages.weightLimit(MAX_WEIGHT));
        }
        if (length <= 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException(Messages.INVALID_DIMENSIONS);
        }
        this.length = length;
        this.width = width;
        this.height = height;
        validateDeliveryService(service);
    }

    public static double getMaxWeight() { return MAX_WEIGHT; }

    @Override
    public double calculateChargeableWeight() {
        double volumetricWeight = ((double) length * width * height) / VOLUMETRIC_DIVISOR;
        return Math.ceil(Math.max(getActualWeight(), volumetricWeight));
    }

    @Override
    public double calculateBaseFee() { return RATE_PER_KG * calculateChargeableWeight(); }

    @Override
    public String getParcelType() { return "Box"; }

    public int getLength() { return length; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
