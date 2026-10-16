package common;

import java.util.regex.Pattern;

public final class TrackingNumberValidator {
    private static final Pattern TRACKING_NUMBER_PATTERN =
            Pattern.compile("^ABC\\d{4}$", Pattern.CASE_INSENSITIVE);

    private TrackingNumberValidator() {}

    public static boolean isValid(String trackingNumber) {
        return trackingNumber != null && TRACKING_NUMBER_PATTERN.matcher(trackingNumber).matches();
    }
}
