package hotelproject.controllers.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DatabaseManagerTest {


    private final DatabaseManager dbm = new DatabaseManager();
    private ArrayList<String> log;
    private String tableName;
    private String body;

    /**
     * Setting up variables need in test methods
     *
     * @see DatabaseManager
     */
    @Before
    public void setUp() {
        log = new ArrayList<>();
        body = "test_column varchar(255)";
    }

    /**
     * Ensures there are no remnants in the database after test session
     *
     * @see DatabaseManager
     */
    @After
    public void tearDown() {
        dbm.dropTable("test_table", log); // test
    }

    /**
     * Tests the 'tableExists()' method, by testing if table 'room_type', known to exist, exists in the SQL database
     *
     * @see DatabaseManager
     */
    @Test
    public void testTableExist() {
        tableName = "room_type"; // Exists
        assertTrue(dbm.tableExists(tableName, log));
    }

    /**
     * Tests 'createTable()' method, by use of the 'tableExists()' method on table 'test_table'
     *
     * @see DatabaseManager
     */
    @Test
    public void testCreateTable() {
        tableName = "test_table";
        dbm.createTable(tableName, body, log); // test
        assertTrue(dbm.tableExists(tableName, log));
        dbm.dropTable(tableName, log); // test
    }

    /**
     * Tests 'dropTable()' method, by use of 'tableExists()' method on table 'test_table'
     *
     * @see DatabaseManager
     */
    @Test
    public void testDropTable() {
        tableName = "test_table";
        dbm.createTable(tableName, body, log);
        dbm.dropTable(tableName, log); // test
        assertFalse(dbm.tableExists(tableName, log));
    }
}
