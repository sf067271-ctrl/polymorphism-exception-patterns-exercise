package delivery;

public class StandardDelivery implements DeliveryStrategy {
    @Override
    public double calculateDeliveryFee(double baseFee) { return baseFee; }

    @Override
    public String getServiceName() { return "Standard"; }
}
