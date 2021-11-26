package ca.cmput301f21t22.nabu.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * A data object, storing a latitude and a longitude.
 */
public class LatLngPoint implements Serializable {
    private final double latitude;
    private final double longitude;

    /**
     * Create an instance of LatLngPoint.
     *
     * @param latitude  The latitude of the point.
     * @param longitude The longitude of the point.
     */
    public LatLngPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return Whether two objects are structurally equivalent.
     * @see Object#equals(Object)
     */
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

    /**
     * @return The hash code of the object's fields.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude);
    }

    /**
     * @return The latitude of the point.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * @return The longitude of the point.
     */
    public double getLongitude() {
        return this.longitude;
    }
}
