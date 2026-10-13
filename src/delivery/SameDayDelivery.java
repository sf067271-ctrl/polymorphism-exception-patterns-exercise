package delivery;

public class SameDayDelivery implements DeliveryStrategy {
    private static final double MULTIPLIER = 2;
    private static final double EXTRA_FEE = 100;

    @Override
    public double calculateDeliveryFee(double baseFee) { return baseFee * MULTIPLIER + EXTRA_FEE; }

    @Override
    public String getServiceName() { return "Same Day"; }
}
