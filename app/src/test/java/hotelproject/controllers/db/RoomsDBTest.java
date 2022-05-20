package hotelproject.controllers.db;

import hotelproject.controllers.objects.Room;
import hotelproject.controllers.objects.RoomType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class RoomsDBTest {


    private final DatabaseManager dbm = new DatabaseManager();
    private final int r_num = 666;
    private final Room testRoom1 = new Room(r_num, 6, "Hexagonal");
    private final String t_name1 = "Hexagonal"; // generateRandomString();
    private final RoomType testRoomType1 = new RoomType(t_name1, 1, 1, 1, 1, 1, 1, 1, 1);
    private final String t_name2 = "Heptagonal"; // generateRandomString();
    private final RoomType testRoomType2 = new RoomType(t_name2, 1, 1, 1, 1, 1, 1, 1, 1);

    /**
     * Setting up variables prior to testing
     *
     * @see DatabaseManager
     * @see RoomsDB
     */
    @Before
    public void setUp() {
        // Because of the constraint on adding a new room that the room type of the room must exist in the room_type table, we must prior to adding a room ensure that the room type is in the room_type table.

        dbm.rdb.deleteRoomType(testRoomType1);
        dbm.rdb.deleteRoomType(testRoomType2);
        dbm.rdb.addRoomType(testRoomType1);
        dbm.rdb.addRoomType(testRoomType2);
    }

    /**
     * Ensure there are no remnants after testing in the SQL database
     *
     * @see RoomsDB
     */
    @After
    public void tearDown() {
        dbm.rdb.deleteRoom(testRoom1); // Because of constraints, and dependencies on room_type, rooms have to be deleted prior to deletion of room_type
        dbm.rdb.deleteRoomType(testRoomType1);
        dbm.rdb.deleteRoomType(testRoomType2);
    }

    /**
     * @return Object of String type, a random string, thought to be used as name of room type
     */
    public String generateRandomString() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    /**
     * Tests 'addRoomType()' method, by use of 'findAllRoomTypes()' method
     *
     * @see RoomsDB
     */
    @Test
    public void testAddRoomType() {

        // Ensure first that the database does not contain testRoomType
        dbm.rdb.deleteRoomType(testRoomType1);
        dbm.rdb.addRoomType(testRoomType1);
        boolean successfulUpdate = false;

        List<RoomType> roomTypes = dbm.rdb.findAllRoomTypes();
        for (RoomType roomType : roomTypes) {
            if (testRoomType1.getT_name().equals(roomType.getT_name())) {
                successfulUpdate = true;
                break;
            }
        }
        assertTrue(successfulUpdate);
    }

    /**
     * Tests 'deleteRoomType()' method, by use of 'findAllRoomTypes()'
     *
     * @see RoomsDB
     */
    @Test
    public void testDeleteRoomType() {
        dbm.rdb.addRoomType(testRoomType1);
        dbm.rdb.deleteRoomType(testRoomType1);
        boolean successfulUpdate = true; // Note: 'true'

        List<RoomType> roomTypes = dbm.rdb.findAllRoomTypes();
        for (RoomType roomType : roomTypes) {
            if (roomType.getT_name().equals(testRoomType1.getT_name())) {
                successfulUpdate = false;
                break;
            }
        }
        assertTrue(successfulUpdate);
    }

    /**
     * Tests the 'addRoom()' method, by use of the 'findAllRooms()' method
     *
     * @see RoomsDB
     */
    @Test
    public void testAddRoom() {
        // Have to ensure that testRoom does not already exist in the database
        dbm.rdb.deleteRoom(testRoom1);
        dbm.rdb.addRoom(testRoom1);
        boolean successfulUpdate = false;

        List<Room> rooms = dbm.rdb.findAllRooms();
        for (Room room : rooms) {
            if (room.getR_num() == testRoom1.getR_num()) {
                successfulUpdate = true;
                break;
            }
        }
        assertTrue(successfulUpdate);
    }

    /**
     * Tests 'deleteRoom()' method, by use of 'findAllRooms()' method
     *
     * @see RoomsDB
     */
    @Test
    public void testDeleteRoom() {
        // Have to ensure that testRoom does exist in the database; hence the reverse order compared to previous test
        dbm.rdb.deleteRoom(testRoom1);
        dbm.rdb.addRoom(testRoom1);
        dbm.rdb.deleteRoom(testRoom1);
        boolean successfulUpdate = true;

        List<Room> rooms = dbm.rdb.findAllRooms();
        for (Room room : rooms) {
            if (room.getR_num() == testRoom1.getR_num()) {
                successfulUpdate = false;
                break;
            }
        }
        assertTrue(successfulUpdate);
    }

    /**
     * Tests 'updateRoomType()' method, by use of 'findAllRoomTypes()'
     *
     * @see RoomsDB
     */
    @Test
    public void testUpdateRoomType() {
        dbm.rdb.deleteRoomType(testRoomType1);
        dbm.rdb.addRoomType(testRoomType1);

        // Update testRoomType1 object, from has bed (1) to does not have bed (0)
        testRoomType1.setBeds(0);

        // test: update the database with new information on 'beds'
        dbm.rdb.updateRoomType(testRoomType1, testRoomType1.getT_name());

        boolean successfulUpdate = false;

        List<RoomType> roomTypes = dbm.rdb.findAllRoomTypes();
        for (RoomType roomType : roomTypes) {
            if (testRoomType1.getT_name().equals(roomType.getT_name())) {
                if (testRoomType1.getBeds() == roomType.getBeds()) {
                    successfulUpdate = true;
                }
                break;
            }
        }
        assertTrue(successfulUpdate);
    }

    /**
     * Tests 'updateRoom()' method, by use of 'findAllRooms()' method
     *
     * @see RoomsDB
     */
    @Test
    public void testUpdateRoom() {
        dbm.rdb.deleteRoom(testRoom1);
        dbm.rdb.addRoom(testRoom1);

        // Update testRoom1, from has r_type "Hexagonal" r_type "Heptagonal"
        testRoom1.setR_type("Heptagonal");

        // test
        dbm.rdb.updateRoom(testRoom1, testRoom1.getR_num());

        boolean successfulUpdate = false;

        List<Room> rooms = dbm.rdb.findAllRooms();
        for (Room room : rooms) {
            if (testRoom1.getR_num() == room.getR_num()) {
                if (testRoom1.getR_type().equals(room.getR_type())) {
                    successfulUpdate = true;
                }
                break;
            }
        }
        assertTrue(successfulUpdate);
    }

    /**
     * Tests 'viewRoomDetails()' method
     *
     * @see RoomsDB
     */
    @Test
    public void testViewRoomDetails() {
        dbm.rdb.deleteRoom(testRoom1); // To avoid: SQLIntegrityConstraintViolationException: Duplicate entry '666' for key 'room.PRIMARY'
        dbm.rdb.addRoom(testRoom1);
        Hashtable<String, String> roomDetails = dbm.rdb.viewRoomDetails(testRoom1);

        assertEquals(testRoom1.getR_type(), roomDetails.get("t_name"));
    }

    /**
     * Tests 'roomTypeExists()' method
     *
     * @see RoomsDB
     */

    @Test
    public void testRoomTypeExists() {
        dbm.rdb.deleteRoomType(testRoomType1);
        dbm.rdb.addRoomType(testRoomType1);
        assertTrue(dbm.rdb.roomTypeExists(testRoomType1));
    }

    @Test
    public void testRoomExists() {
        dbm.rdb.deleteRoom(testRoom1);
        dbm.rdb.addRoom(testRoom1);
        assertTrue(dbm.rdb.roomExists(testRoom1));
    }


}