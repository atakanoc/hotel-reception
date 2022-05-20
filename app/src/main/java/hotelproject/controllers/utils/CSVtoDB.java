package hotelproject.controllers.utils;

import hotelproject.controllers.db.DatabaseManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is used for reading all the csv data to the database.
 * All initial data of this application is firstly stored in csv files then read to the database.
 */
public class CSVtoDB {
    private final Connection conn;
    private final PasswordAuth passwordAuth = new PasswordAuth();
    private final String FILE_PATH = "instances";

    /**
     * This constructor is used for building connection with the database.
     *
     * @param conn database connection
     */
    public CSVtoDB(Connection conn) {
        this.conn = conn;
    }

    public static void main(String[] args) {
        Connection conn = DatabaseManager.checkAndGetConnection("app/app.properties");
        CSVtoDB csvdb = new CSVtoDB(conn);

        // order is important due to foreign keys of the architecture of the db
        csvdb.roomTypeQuery();
        csvdb.roomQuery();
        csvdb.customerQuery();
        csvdb.bookingQuery();
        csvdb.userQuery();
    }

    /**
     * Read all rooms' data from csv file and insert them to the room table in the database.
     */
    public void roomQuery() {
        int batchSize = 20; //optimize the size of the batch
        try {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO `hotel`.`room` (`r_num`, `r_floor`, `r_type`) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(FILE_PATH + "/room.csv"));
            String lineText;
            int count = 0;
            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                int r_num = Integer.parseInt(data[0]);
                int r_floor = Integer.parseInt(data[1]);
                String r_type = data[2];

                count++;

                statement.setInt(1, r_num);
                statement.setInt(2, r_floor);
                statement.setString(3, r_type);

                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            conn.commit();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read all room types' data from csv file and insert them to the room table in the database.
     */
    public void roomTypeQuery() {
        int batchSize = 20; //optimization
        try {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO `hotel`.`room_type` (`t_name`, `beds`, `r_size`, `has_view`, `has_kitchen`, `has_bathroom`, `has_workspace`, `has_tv`, `has_coffee_maker`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(FILE_PATH + "/room_type.csv"));
            String lineText;
            int count = 0;
            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String t_name = data[0];
                int beds = Integer.parseInt(data[1]);
                int r_size = Integer.parseInt(data[2]);
                int has_view = Integer.parseInt(data[3]);
                int has_kitchen = Integer.parseInt(data[4]);
                int has_bathroom = Integer.parseInt(data[5]);
                int has_workspace = Integer.parseInt(data[6]);
                int has_tv = Integer.parseInt(data[7]);
                int has_coffee_maker = Integer.parseInt(data[8]);

                count++;

                statement.setString(1, t_name);
                statement.setInt(2, beds);
                statement.setInt(3, r_size);
                statement.setInt(4, has_view);
                statement.setInt(5, has_kitchen);
                statement.setInt(6, has_bathroom);
                statement.setInt(7, has_workspace);
                statement.setInt(8, has_tv);
                statement.setInt(9, has_coffee_maker);

                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            conn.commit();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read all users' data from csv file and insert them to the user table in the database.
     */
    public void userQuery() {
        int batchSize = 20; //optimization
        try {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO `hotel`.`users` (`u_name`, `u_password`, `u_is_admin`) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(FILE_PATH + "/users.csv"));
            String lineText;
            int count = 0;
            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String u_name = data[0];
                String u_password = data[1];
                int u_is_admin = Integer.parseInt(data[2]);

                count++;

                statement.setString(1, u_name);
                statement.setString(2, passwordAuth.hash(u_password));
                statement.setInt(3, u_is_admin);

                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            conn.commit();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read all customers' data from csv file and insert them to the customer table in the database.
     */
    public void customerQuery() {
        int batchSize = 20; //optimization
        try {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO `hotel`.`customer` (`c_ss_number`, `c_address`, `c_full_name`, `c_phone_num`, `c_email`) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(FILE_PATH + "/customer.csv"));
            String lineText;
            int count = 0;
            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                int c_ss_number = Integer.parseInt(data[0]);
                String c_address = data[1];
                String c_full_name = data[2];
                int c_phone_num = Integer.parseInt(data[3]);
                String c_email = data[4];

                count++;

                statement.setInt(1, c_ss_number);
                statement.setString(2, c_address);
                statement.setString(3, c_full_name);
                statement.setInt(4, c_phone_num);
                statement.setString(5, c_email);

                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            conn.commit();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read all bookings' data from csv file and insert them to the booking table in the database.
     */
    public void bookingQuery() {
        int batchSize = 20; //optimization
        try {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO `hotel`.`booking` (`b_id`, `r_num`, `paid_by_card`, `b_from`, `b_till`, `b_fee`, `b_is_paid`, `c_ss_number`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(FILE_PATH + "/booking.csv"));
            String lineText;
            int count = 0;
            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                int b_id = Integer.parseInt(data[0]);
                int r_num = Integer.parseInt(data[1]);
                int paid_by_card = Integer.parseInt(data[2]);
                Date b_from = Date.valueOf(data[3]);
                Date b_till = Date.valueOf(data[4]);
                int b_fee = Integer.parseInt(data[5]);
                int b_is_paid = Integer.parseInt(data[6]);
                int c_ss_number = Integer.parseInt(data[7]);

                count++;

                statement.setInt(1, b_id);
                statement.setInt(2, r_num);
                statement.setInt(3, paid_by_card);
                statement.setDate(4, b_from);
                statement.setDate(5, b_till);
                statement.setInt(6, b_fee);
                statement.setInt(7, b_is_paid);
                statement.setInt(8, c_ss_number);

                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            conn.commit();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}