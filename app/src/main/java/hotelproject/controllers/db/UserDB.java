package hotelproject.controllers.db;

import hotelproject.controllers.objects.User;
import hotelproject.controllers.utils.PasswordAuth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * This class contains all the database operations related to users.
 * Include if user exist, is user admin, update user information, get all users, delete user, and add a new user.
 */
public class UserDB {
    private final Connection conn;
    private final PasswordAuth passwordAuth = new PasswordAuth();

    public UserDB(Connection conn) {
        this.conn = conn;
    }

    /**
     * Checks if a user exists in the `users` table in the database.
     *
     * @param user User object whose existence will be checked in the database.
     * @return boolean regarding the existence of the user.
     * @throws SQLException thrown sql exception.
     */
    public boolean userExists(User user) throws SQLException {
        String sql = "SELECT * FROM `users`";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            if (rs.getString("u_name").equals(user.getU_name()) && passwordAuth.authenticate(user.getU_password(), rs.getString("u_password"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a user is an administrator in the `users` table in the database.
     *
     * @param user User object whose admin status will be checked.
     * @return int that returns 1 if user is admin and 0 if not.
     * @throws SQLException thrown sql exception.
     */
    public int getU_is_admin(User user) throws SQLException {
        String sql = "SELECT `u_is_admin` FROM `users` WHERE u_name = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.getU_name());

        ResultSet rs = stmt.executeQuery();
        rs.next();
        return parseInt(rs.getString("u_is_admin"));
    }

    /**
     * Updates a row in the `user` table in the database.
     *
     * @param user         User object whose information will be updated in the database.
     * @param old_username the old_username of the user whose information will be changed.
     * @throws SQLException thrown sql exception.
     */
    public void updateUserInformation(User user, String old_username) throws SQLException {
        String sql = "UPDATE `users` SET `u_name` = ?, `u_password` = ? WHERE `u_name` = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, user.getU_name());
        stmt.setString(2, passwordAuth.hash(user.getU_password()));
        stmt.setString(3, old_username);

        stmt.executeUpdate();
    }

    /**
     * Find all current users in the database.
     *
     * @return a list of User objects from all users in the database.
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM `users`";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new User(rs.getString(1), rs.getString(2), rs.getInt(3));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Delete a user according to the user name.
     *
     * @param user object of the user who will be deleted from the database.
     */
    public void deleteUser(User user) {
        try {
            String sql = "DELETE FROM `users` WHERE `u_name` = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getU_name());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new user to the database.
     *
     * @param user object of the user who will be added to the database.
     */
    public void addUser(User user) {
        try {
            String sql = "INSERT INTO `users` (u_name, u_password, u_is_admin) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getU_name());
            stmt.setString(2, passwordAuth.hash(user.getU_password()));
            stmt.setInt(3, user.getU_is_admin());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
