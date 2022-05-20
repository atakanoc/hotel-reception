package hotelproject.controllers.db;

import hotelproject.controllers.objects.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class is used to manipulate all data from the hotel database.
 */
public class HotelData {
    public final DatabaseManager dbm;
    private final ArrayList<Room> rooms;
    private final ArrayList<RoomType> roomTypes;
    private final ArrayList<Booking> bookings;
    private final ArrayList<User> users;
    private final ArrayList<Customer> customers;

    /**
     * This constructor is used for initializing all encapsulated data.
     */
    public HotelData() {
        dbm = new DatabaseManager();
        rooms = dbm.rdb.findAllRooms();
        roomTypes = dbm.rdb.findAllRoomTypes();
        bookings = dbm.bdb.findAllBookings();
        users = dbm.udb.getAllUsers();
        customers = dbm.cdb.findAllCustomers();
    }

    /**
     * This constructor is used for initializing all encapsulated data.
     *
     * @param dbm       build connection with database.
     * @param rooms     all room objects
     * @param roomTypes all room type objects
     * @param bookings  all booking objects
     * @param users     all user objects
     * @param customers all customer objects
     */
    public HotelData(DatabaseManager dbm, ArrayList<Room> rooms, ArrayList<RoomType> roomTypes, ArrayList<Booking> bookings,
                     ArrayList<User> users, ArrayList<Customer> customers) {
        this.dbm = dbm;
        this.rooms = rooms;
        this.roomTypes = roomTypes;
        this.bookings = bookings;
        this.users = users;
        this.customers = customers;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Add a new room.
     * First add the room object in the list then update the room in the database.
     *
     * @param room the room object
     */
    public void addRoom(Room room) {
        rooms.add(room);
        dbm.rdb.addRoom(room);
    }

    /**
     * Delete a room.
     * First delete the room object in the list then delete the room in the database.
     *
     * @param room the room object
     */
    public void deleteRoom(Room room) {
        rooms.remove(room);
        dbm.rdb.deleteRoom(room);
    }

    /**
     * Update a room information.
     * First find the focal room's index in the list, then call RoomsDB's update method to update the room object.
     *
     * @param room    the encapsulated new room object.
     * @param oldRNum the old room number for identify the room.
     */
    public void updateRoom(Room room, int oldRNum) {
        for (Room r : rooms) {
            if (oldRNum == r.getR_num()) {
                int index = rooms.indexOf(r);
                rooms.set(index, room);
                dbm.rdb.updateRoom(room, oldRNum);
                break;
            }
        }
    }

    /**
     * Find all available rooms during the time period.
     *
     * @param checkIn  the check-in date
     * @param checkOut the check-out date
     * @return all available rooms saved in ArrayList.
     */
    public ArrayList<Integer> availableRooms(Date checkIn, Date checkOut) {

        ArrayList<Integer> allRooms = dbm.rdb.findAllRoomNums();
        ArrayList<Integer> allBookedRooms = dbm.rdb.findAllBookedRooms(checkIn, checkOut);

        ArrayList<Integer> union = new ArrayList<>(allRooms);
        union.addAll(allBookedRooms);

        ArrayList<Integer> intersection = new ArrayList<>(allRooms);

        intersection.retainAll(allBookedRooms);
        union.removeAll(intersection);

        // return the union after intersection
        return union;
    }

    /**
     * View a room's detail.
     *
     * @param room the room object for viewing
     * @return the room's information saved in Hashtable.
     */
    public Hashtable<String, String> viewDetails(Room room) {
        return dbm.rdb.viewRoomDetails(room);
    }

    /**
     * Add a new room type.
     * First add in ArrayList then update the database.
     *
     * @param newRoomType new room type object
     */
    public void addRoomType(RoomType newRoomType) {
        roomTypes.add(newRoomType);
        dbm.rdb.addRoomType(newRoomType);
    }

    /**
     * Add a new customer.
     * First add in ArrayList then update the database.
     *
     * @param customer new customer object.
     */
    public void addCustomer(Customer customer) {
        customers.add(customer);
        dbm.cdb.addCustomer(customer);
    }

    /**
     * Update a customer's information according to its social security number.
     * First update ArrayList for customers. Then update the database.
     *
     * @param customer  the new customer object
     * @param oldCSSNum the customer's social security number
     */
    public void updateCustomer(Customer customer, int oldCSSNum) {
        for (Customer c : customers) {
            if (oldCSSNum == c.getC_ss_number()) {
                int index = customers.indexOf(c);
                customers.set(index, customer);
                dbm.cdb.updateCustomer(customer, oldCSSNum);
                break;
            }
        }
    }

    public void deleteCustomer(Customer customer) {
        dbm.cdb.deleteCustomer(customer);
        customers.remove(customer);
    }

    /**
     * Add a new user.
     *
     * @param user user object for adding
     */
    public void addUser(User user) {
        users.add(user);
        dbm.udb.addUser(user);
    }

    /**
     * Update a user's information.
     *
     * @param user     the new user object
     * @param oldUName the user's name
     * @throws SQLException this method need to handle SQLException when call
     */
    public void updateUserInformation(User user, String oldUName) throws SQLException {
        for (User u : users) {
            if (oldUName.equals(u.getU_name())) {
                int index = users.indexOf(u);
                users.set(index, user);
                dbm.udb.updateUserInformation(user, oldUName);
                break;
            }
        }
    }

    /**
     * Get booking's id.
     *
     * @return booking id is an integer number
     */
    public int getBookingAutoID() {
        Booking lastBID = bookings.get(bookings.size() - 1);
        return (lastBID.getB_id() + 1);
    }

    /**
     * Add a new booking in the system.
     * First add the new booking to the ArrayList.
     * Then update the database.
     *
     * @param newBooking new booking object
     */
    public void addBooking(Booking newBooking) {
        bookings.add(newBooking);
        dbm.bdb.addBooking(newBooking);
    }

    /**
     * Update a booking's information.
     * First add the new booking to the ArrayList.
     * Then update the database.
     *
     * @param updateBooking new booking object
     */
    public void updateBooking(Booking updateBooking) {
        for (Booking b : bookings) {
            if (b.getB_id() == updateBooking.getB_id()) {
                int index = bookings.indexOf(b);
                bookings.set(index, updateBooking);
                dbm.bdb.updateBooking(updateBooking);
                break;
            }
        }
    }

    public void deleteBooking(Booking booking) {
        dbm.bdb.deleteBooking(booking);
        bookings.remove(booking);
    }
}
