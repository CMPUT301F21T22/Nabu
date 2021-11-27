package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Objects;

import ca.cmput301f21t22.nabu.data.LatLngPoint;

public class LatLngPointTest {

    //Tests to make sure the object's creation works
    @Test
    public void testCreate() {
        LatLngPoint latLngPoint = new LatLngPoint(11, 55);
    }

    //Tests if the equals function for each delta works
    @Test
    public void testEquals() {
        double lat = 56;
        double lng = 77;
        LatLngPoint latLngPoint1 = new LatLngPoint(lat, lng);
        LatLngPoint latLngPoint2 = new LatLngPoint(lat, lng);
        LatLngPoint notLatLngPoint = new LatLngPoint(11, 12);

        assertTrue(latLngPoint1.equals(latLngPoint2));
        assertTrue(latLngPoint2.equals(latLngPoint1));
        assertTrue(latLngPoint1.equals(latLngPoint1));
        assertFalse(latLngPoint1.equals(notLatLngPoint));
        assertFalse(notLatLngPoint.equals(latLngPoint2));
    }

    //Tests if the hashcode returned on calling the hash code method is correct
    @Test
    public void testHashCode() {
        double lat = 88;
        double lng = 7;
        LatLngPoint latLngPoint = new LatLngPoint(lat, lng);
        assertEquals(Objects.hash(lat, lng), latLngPoint.hashCode());
    }

    //Tests if the latitude is correctly stored and returned
    @Test
    public void testGetLatitude() {
        double lat = 99;
        LatLngPoint latLngPoint = new LatLngPoint(lat, 76);
        assertEquals(lat, latLngPoint.getLatitude(), 0);
    }

    //Tests if the longitude is correctly stored and returned
    @Test
    public void testGetLongitude() {
        double lng = 99;
        LatLngPoint latLngPoint = new LatLngPoint(54, lng);
        assertEquals(lng, latLngPoint.getLongitude(), 0);
    }


}
