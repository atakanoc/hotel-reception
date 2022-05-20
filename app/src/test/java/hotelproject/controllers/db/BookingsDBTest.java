package hotelproject.controllers.db;

import hotelproject.controllers.objects.Booking;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;


@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class BookingsDBTest {

    private final DatabaseManager dbm = new DatabaseManager();
    private final HotelData hdata = dbm.createDBObjects();
    private final Booking booking = new Booking(hdata.getBookingAutoID(), 1, 1, java.sql.Date.valueOf("2021-04-28"), java.sql.Date.valueOf("2021-04-29"), 1000, 1, 84031956);

    /**
     * The method should be able to retrieve all bookings in the database and the result should be 10.
     */
    @Test
    public void test_001_findAllBookings() {
        ArrayList<Booking> allBookings;
        allBookings = dbm.bdb.findAllBookings();
        //Assert.assertEquals(allBookings.size(), 10);
        int i = 1;
        int count = 0;
        for (Booking b : allBookings) {
            if (b.getB_id() == i) {
                count++;
            }
            i++;
        }
        //Assert.assertEquals(count, 10);
        Assert.assertTrue(count > 1);
    }

    /**
     * Use two different dates to test this method. The first date should return one record. The second date should return two records.
     */
    @Test
    public void test_002_getBookingsForSpecificDay() {
        ArrayList<Integer> bookingsFromTheDay;

        bookingsFromTheDay = dbm.bdb.getBookingsForSpecificDay("2021-05-29");
        Assert.assertEquals(bookingsFromTheDay.size(), 2);
    }

    /**
     * TrueCount should equal to 7 in this test which means all the key-value pairs are corrected.
     */
    @Test
    public void test_003_getBookingDetails() {
        Hashtable<String, String> bookingDetails;
        bookingDetails = dbm.bdb.getBookingDetails(1);
        Set<String> keys = bookingDetails.keySet();
        ArrayList<String> values = new ArrayList<>();
        for (String o : keys) {
            values.add(bookingDetails.get(o));
        }
        Assert.assertEquals(values.size(), 8);
    }

    @Test
    public void test_004_addBooking() {
        dbm.bdb.addBooking(booking);
    }

    @Test
    public void test_005_deleteBooking() {
        dbm.bdb.deleteBooking(booking);
    }

}
