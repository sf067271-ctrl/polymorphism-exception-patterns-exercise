import java.util.ArrayList;

public class ParcelOperations {
    private static final ParcelOperations instance = new ParcelOperations();
    private final ArrayList<Parcel> parcels = new ArrayList<>();

    private ParcelOperations() { }

    public static ParcelOperations getInstance() { return instance; }

    public void checkDuplicateTracking(String trackingNumber) {
        if (findParcel(trackingNumber) != null) {
            throw new IllegalArgumentException("Tracking number already exists!");
        }
    }

    public void addParcel(Parcel parcel) {
        if (parcel == null) {
            throw new IllegalArgumentException("Parcel must not be null.");
        }
        checkDuplicateTracking(parcel.getTrackingNumber());
        parcels.add(parcel);
    }

    public Parcel findParcel(String trackingNumber) {
        for (Parcel parcel : parcels) {
            if (parcel.getTrackingNumber().equals(trackingNumber)) {
                return parcel;
            }
        }
        return null;
    }

    public ArrayList<Parcel> getAllParcels() {
        return new ArrayList<>(parcels);
    }

    public void changeDeliveryService(String trackingNumber, DeliveryStrategy service){
        requireParcel(trackingNumber).changeDeliveryService(service);
    }

    public void markAsDelivered(String trackingNumber) {
        requireParcel(trackingNumber).markAsDelivered();
    }

    private Parcel requireParcel(String trackingNumber) {
        Parcel parcel = findParcel(trackingNumber);
        if (parcel == null) {
            throw new IllegalArgumentException("Parcel not found!");
        }
        return parcel;
    }
}
