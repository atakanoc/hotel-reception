package hotelproject.controllers.db;

import hotelproject.controllers.objects.User;
import hotelproject.controllers.utils.PasswordAuth;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class UserDBTest {

    private final String u_name_admin = "IsAdmin";
    private final String u_name_staff = "IsStaff";
    private final DatabaseManager dbm = new DatabaseManager();
    private final User userIsAdmin = new User(u_name_admin, "admin123", 1);
    private final User userIsStaff = new User(u_name_staff, "staff123", 0);
    private final PasswordAuth passwordAuth = new PasswordAuth();


    /**
     * This user already exists in the database and the result should be true.
     */
    @Test
    public void test_001_IsUserExist() {
        try {
            assertTrue(dbm.udb.userExists(new User("admin", "root", 1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * These two user objects should be added in the users table of database. The result should be true.
     */
    @Test
    public void test_002_addUser() {
        dbm.udb.addUser(userIsAdmin);
        dbm.udb.addUser(userIsStaff);
        try {
            assertTrue(dbm.udb.userExists(userIsAdmin));
            assertTrue(dbm.udb.userExists(userIsStaff));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The user 'userIsStaff' should return '0'.
     * The user 'userIsAdmin' should return '1'.
     */
    @Test
    public void test_003_GetU_is_admin() {
        try {
            int isAdmin = dbm.udb.getU_is_admin(userIsAdmin);
            int isStaff = dbm.udb.getU_is_admin(userIsStaff);
            Assert.assertEquals(isAdmin, 1);
            Assert.assertEquals(isStaff, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * All four users should be obtained from the database. The result should return true.
     */
    @Test
    public void test_004_GetAllUsers() {
        List<User> allUsersForTest = new ArrayList<>();
        allUsersForTest.add(new User("admin", "root", 1));
        allUsersForTest.add(new User("rstaff", "rstaff", 0));
        allUsersForTest.add(new User("IsAdmin", "admin123", 1));
        allUsersForTest.add(new User("IsStaff", "staff123", 0));
        List<User> allUserInDatabase;
        int count = 0;
        allUserInDatabase = dbm.udb.getAllUsers();
        for (User userInDatabase : allUserInDatabase) {
            for (User userForTest : allUsersForTest) {
                if (userInDatabase.getU_name().equals(userForTest.getU_name()) &&
                        passwordAuth.authenticate(userForTest.getU_password(), userInDatabase.getU_password()) &&
                        userInDatabase.getU_is_admin() == userForTest.getU_is_admin()) {
                    count++;
                }
            }
        }
        Assert.assertEquals(count, 4);
    }

    /**
     * The user object's attributes should be updated accordingly and should return true.
     */
    @Test
    public void test_005_UpdateUserInformation() {
        boolean isUpdate = false;
        try {
            String old_username = userIsAdmin.getU_name();
            userIsAdmin.setU_name("userIsBoss");
            userIsAdmin.setU_password("boss123");
            dbm.udb.updateUserInformation(userIsAdmin, old_username);
            List<User> users = dbm.udb.getAllUsers();
            for (User user : users) {
                isUpdate = user.getU_name().equals("userIsBoss") &&
                        passwordAuth.authenticate("boss123", user.getU_password());
            }
            assertTrue(isUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The user object's attributes should be updated accordingly and should return true.
     */
    @Test
    public void test_006_UpdateUserInformation2() {

        boolean isUpdate = false;
        String old_username = userIsStaff.getU_name();
        userIsStaff.setU_name("userIsWorker");
        try {
            dbm.udb.updateUserInformation(userIsStaff, old_username);
            List<User> users = dbm.udb.getAllUsers();
            for (User user : users) {
                isUpdate = user.getU_name().equals("userIsWorker") && passwordAuth.authenticate("staff123", user.getU_password());
            }
            assertTrue(isUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The user 'userIsWorker' should be found and deleted in the database. The result should return true.
     */
    @Test
    public void test_007_DeleteUser() {
        boolean isDeleted = true;
        User userForDelete = new User("userIsWorker", "staff123", 0);
        dbm.udb.deleteUser(userForDelete);
        List<User> allUsersInDatabase = dbm.udb.getAllUsers();
        for (User user : allUsersInDatabase) {
            if (user.getU_name().equals("userIsWorker")) {
                isDeleted = false;
                break;
            }
        }
        assertTrue(isDeleted);
    }

    /**
     * The user 'userIsBoss' should be found and deleted in the database. The result should return true.
     */
    @Test
    public void test_008_DeleteUser2() {
        boolean isDeleted = true;
        dbm.udb.deleteUser(new User("userIsBoss", "boss123", 1));
        List<User> allUsersInDatabase = dbm.udb.getAllUsers();
        for (User user : allUsersInDatabase) {
            if (user.getU_name().equals("userIsBoss")) {
                isDeleted = false;
                break;
            }
        }
        assertTrue(isDeleted);
    }

}
