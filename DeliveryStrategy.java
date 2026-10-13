public interface DeliveryStrategy {
    double calculateDeliveryFee(double baseFee);
    String getServiceName();
}
