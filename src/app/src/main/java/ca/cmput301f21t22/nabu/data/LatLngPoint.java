package ca.cmput301f21t22.nabu.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * A data object, storing a latitude and a longitude.
 */
public class LatLngPoint implements Serializable {
    private final double latitude;
    private final double longitude;

    public LatLngPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LatLngPoint that = (LatLngPoint) o;
        return Double.compare(that.latitude, this.latitude) == 0 && Double.compare(that.longitude, this.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude);
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}
