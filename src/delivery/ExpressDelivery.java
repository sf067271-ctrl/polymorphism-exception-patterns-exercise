package delivery;

public class ExpressDelivery implements DeliveryStrategy {
    private static final double MULTIPLIER = 1.5;

    @Override
    public double calculateDeliveryFee(double baseFee) { return baseFee * MULTIPLIER; }

    @Override
    public String getServiceName() { return "Express Delivery"; }
}
