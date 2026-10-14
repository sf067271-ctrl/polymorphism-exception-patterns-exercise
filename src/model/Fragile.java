package model;

import common.Messages;
import delivery.DeliveryStrategy;

public class Fragile extends Box {
    private static final double MAX_WEIGHT = 30;
    private final double HANDLING_FEE = 100;
    private final String handlingNote;

    public Fragile(String trackingNumber, String receiverName, double actualWeight,
            int length, int width, int height, String handlingNote,
            DeliveryStrategy service) {
        super(trackingNumber, receiverName, actualWeight, length, width, height, service);
        if (actualWeight > MAX_WEIGHT) {
            throw new IllegalArgumentException(Messages.weightLimit(MAX_WEIGHT));
        }
        if (handlingNote == null || handlingNote.isBlank()) {
            throw new IllegalArgumentException(Messages.EMPTY_FIELD);
        }
        this.handlingNote = handlingNote.trim();
    }

    public static double getMaxWeight() { return MAX_WEIGHT; }

    @Override
    public double calculateBaseFee() { return super.calculateBaseFee() + HANDLING_FEE; }

    @Override
    public String getParcelType() { return "Fragile"; }
}
