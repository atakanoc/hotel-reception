package hotelproject;

import hotelproject.controllers.db.DatabaseManager;
import hotelproject.controllers.db.HotelData;
import hotelproject.controllers.objects.*;
import hotelproject.views.*;
import hotelproject.views.UpdateInfoView.Change;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HotelProject extends Application {

    private final DatabaseManager dbm = new DatabaseManager();
    private final HotelData hdata = dbm.createDBObjects();
    private User connectedUser;
    private Stage mainPageStage;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        credentialsDisplay(primaryStage, false);
    }

    /**
     * @param primaryStage stage on which the login view is displayed
     * @param onlyPwd      if true this method displays the password input,
     *                     otherwise the login page
     * @brief Displays the login stage or the password input when the user wants to change
     * their personal info
     */
    private void credentialsDisplay(Stage primaryStage, boolean onlyPwd) {
        LoginView loginView = new LoginView(onlyPwd);
        Stage credentialScene = new Stage();

        // SET ON ENTER KEY PRESSED //

        loginView.getPassword().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginView.getTestLoginButton().fire();
            }
        });

        loginView.getUsername().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginView.getTestLoginButton().fire();
            }
        });

        // SET BUTTONS ON ACTION //

        loginView.getTestLoginButton().setOnAction(e -> {
            User userTest = new User(loginView.getUsername().getText().trim(), loginView.getPassword().getText());
            try {
                if (!onlyPwd) { // login
                    if (dbm.udb.userExists(userTest)) { // test if the user exist in the database and has correct password
                        connectedUser = new User(loginView.getUsernameString(), loginView.getPasswordString(), dbm.udb.getU_is_admin(userTest));
                        credentialScene.close();
                        mainPageDisplay(primaryStage); // if the user succeeded to login we open the main page of the application
                    } else {
                        loginView.getResult().setText("The username or password you have entered is wrong!");
                    }
                } else { // pwd input
                    if (connectedUser.getU_password().equals(userTest.getU_password())) {
                        credentialScene.close();
                        updateInfoDisplay(primaryStage, Change.PASSWORD);
                    } else {
                        loginView.getResult().setText("The password you have entered is wrong!");
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        // OPEN THE WINDOW //

        credentialScene.initOwner(primaryStage);
        credentialScene.initModality(Modality.WINDOW_MODAL);
        credentialScene.setScene(loginView.getScene());
        credentialScene.setTitle("Hotel Manager - Login");
        credentialScene.show();
    }

    /**
     * Displays the main page of the application
     *
     * @param primaryStage former open stage to close after showing the main page's
     *                     window
     */
    private void mainPageDisplay(Stage primaryStage) {
        MainPageView mainPageView = new MainPageView(connectedUser);
        mainPageStage = new Stage();

        // SET BUTTONS ON ACTION //

        mainPageView.getMyPageButton().setOnAction(e -> {
            // display user info page
            myPageDisplay();
        });

        mainPageView.getViewBookingsButton().setOnAction(e -> {
            // display window with all bookings with search bar
        });

        mainPageView.getViewCustomers().setOnAction(e -> {
            // display window to see customers in db
        });

        mainPageView.getViewRooms().setOnAction(e -> roomsDisplay());
        mainPageView.getViewBookingsButton().setOnAction(e -> bookingsDisplay());
        mainPageView.getViewCustomers().setOnAction(e -> customersDisplay());

        // only admins can access to this button
        if (connectedUser.getU_is_admin() == 1) {
            mainPageView.getViewUsersButton().setOnAction(e -> {
                // display window to change the information of an user or delete one
                usersDisplay();
            });
        }

        // OPEN THE WINDOW //

        mainPageStage.setScene(mainPageView.getScene());
        mainPageStage.setTitle("Hotel Manager - Menu");
        mainPageStage.show();
        primaryStage.close();
    }

    /**
     * Displays the window where the user can update its pers. info
     *
     * @param myPageStage former open stage to close after showing updateInfo
     *                    stage
     * @param change      stage on which the page to change the user's info will
     *                    appear
     */
    private void updateInfoDisplay(Stage myPageStage, Change change) {
        UpdateInfoView updateInfoPage = new UpdateInfoView(change);
        Stage updateInfoStage = new Stage();
        ArrayList<User> checkUsers = hdata.getUsers();

        // SET ON ENTER KEY PRESSED //

        if (change == Change.USERNAME) {
            updateInfoPage.getFirstUName().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    updateInfoPage.getSave().fire();
                }
            });

            updateInfoPage.getSecondUName().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    updateInfoPage.getSave().fire();
                }
            });
        } else {
            updateInfoPage.getFirstPwd().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    updateInfoPage.getSave().fire();
                }
            });

            updateInfoPage.getSecondPwd().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    updateInfoPage.getSave().fire();
                }
            });
        }

        // SET BUTTONS ON ACTION //

        // to save the modifications
        updateInfoPage.getSave().setOnAction(e -> {
            String oldUsername = connectedUser.getU_name();
            boolean isInfoCorrect = true;

            if (change == Change.USERNAME) {
                String firstUsername = updateInfoPage.getFirstUName().getText();
                String secondUsername = updateInfoPage.getSecondUName().getText();

                boolean unFlag = true;
                for (User u : checkUsers) {
                    if (u.getU_name().equalsIgnoreCase(oldUsername)) {
                        continue;
                    }
                    if (u.getU_name().equalsIgnoreCase(firstUsername)) {
                        unFlag = false;
                        Alert aU = new Alert(AlertType.ERROR);
                        aU.setContentText("Username already exists in database. Choose another!");
                        aU.showAndWait();
                        break;
                    }
                }

                if (!firstUsername.equals("") && firstUsername.equals(secondUsername) && unFlag) {
                    connectedUser.setU_name(firstUsername);
                } else if (firstUsername.equals("") || secondUsername.equals("")) {
                    updateInfoPage.setOutput("Please write in the fields!");
                    isInfoCorrect = false;
                } else if (!firstUsername.equalsIgnoreCase("") && !unFlag) {
                    isInfoCorrect = false;
                    Alert aUN = new Alert(AlertType.INFORMATION);
                    aUN.setContentText("Do not write already existing usernames!");
                    aUN.showAndWait();
                } else {
                    updateInfoPage.setOutput("The usernames do not match!");
                    isInfoCorrect = false;
                }
            } else {
                String firstPassword = updateInfoPage.getFirstPwd().getText();
                String secondPassword = updateInfoPage.getSecondPwd().getText();

                if (!firstPassword.equals("") && firstPassword.equals(secondPassword)) {
                    connectedUser.setU_password(firstPassword);
                } else if (firstPassword.equals("") || secondPassword.equals("")) {
                    updateInfoPage.setOutput("Please write in the fields!");
                    isInfoCorrect = false;
                } else {
                    updateInfoPage.setOutput("The passwords do not match!");
                    isInfoCorrect = false;
                }
            }

            if (isInfoCorrect || (isInfoCorrect && connectedUser.getU_name().equalsIgnoreCase(oldUsername))) {
                // update db
                try {
                    hdata.updateUserInformation(connectedUser, oldUsername);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                updateInfoStage.close();
                myPageStage.close();
                myPageDisplay();
            }

        });

        // OPEN THE WINDOW //

        updateInfoStage.initOwner(myPageStage);
        updateInfoStage.initModality(Modality.WINDOW_MODAL);

        updateInfoStage.setScene(updateInfoPage.getScene());
        updateInfoStage.setTitle("Hotel Manager - Update personal information");
        updateInfoStage.show();
    }

    /**
     * Displays the user's page (with pers. info)
     */
    private void myPageDisplay() {
        MyPageView myPage = new MyPageView(connectedUser);
        Stage myPageStage = new Stage();

        // SET BUTTONS ON ACTION //

        myPage.getChPwd().setOnAction(e -> credentialsDisplay(myPageStage, true));
        myPage.getChUser().setOnAction(e -> updateInfoDisplay(myPageStage, Change.USERNAME));

        myPage.getLogout().setOnAction(e -> logout(myPageStage));

        // OPEN THE WINDOW //

        myPageStage.setScene(myPage.getScene());
        myPageStage.setTitle("Hotel Manager - My Page");
        myPageStage.show();
    }

    /**
     * Display the page to add a room type in the database (only for admins)
     *
     * @param newRoomStage former stage to close when the new stage is displayed
     */
    private void addRoomTypeDisplay(HotelData hdata, Stage newRoomStage) {
        ArrayList<RoomType> allRoomTypes = hdata.getRoomTypes();
        AddRoomTypeView addRoomTypePage = new AddRoomTypeView();
        Stage addTypeStage = new Stage();

        addRoomTypePage.getSubmit().setDisable(addRoomTypePage.getNbBeds().getText().equals("")
                || addRoomTypePage.getName().getText().equals("")
                || addRoomTypePage.getRoomSize().getText().equals(""));

        addRoomTypePage.getName().textProperty().addListener((observable, oldValue, newValue) -> {
            if (addRoomTypePage.getName().getText().equals("")) {
                addRoomTypePage.getSubmit().setDisable(true);
            } else {
                if (!addRoomTypePage.getName().getText().equals("")
                        && !addRoomTypePage.getNbBeds().getText().equals("")
                        && !addRoomTypePage.getRoomSize().getText().equals("")) {
                    addRoomTypePage.getSubmit().setDisable(false);
                }
            }
        });

        addRoomTypePage.getNbBeds().textProperty().addListener((observable, oldValue, newValue) -> {
            if (addRoomTypePage.getNbBeds().getText().equals("")) {
                addRoomTypePage.getSubmit().setDisable(true);
            } else {
                if (!addRoomTypePage.getNbBeds().getText().equals("")
                        && !addRoomTypePage.getName().getText().equals("")
                        && !addRoomTypePage.getRoomSize().getText().equals("")) {
                    addRoomTypePage.getSubmit().setDisable(false);
                }
            }
        });

        addRoomTypePage.getRoomSize().textProperty().addListener((observable, oldValue, newValue) -> {
            if (addRoomTypePage.getRoomSize().getText().equals("")) {
                addRoomTypePage.getSubmit().setDisable(true);
            } else {
                if (!addRoomTypePage.getRoomSize().getText().equals("")
                        && !addRoomTypePage.getName().getText().equals("")
                        && !addRoomTypePage.getNbBeds().getText().equals("")) {
                    addRoomTypePage.getSubmit().setDisable(false);
                }
            }
        });

        // SET BUTTONS ON ACTION //

        // add the new room type to the database
        addRoomTypePage.getSubmit().setOnAction(e -> {
            String typeName = addRoomTypePage.getName().getText();
            String capTypeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);

            int nbBeds = Integer.parseInt(addRoomTypePage.getNbBeds().getText());
            int rSize = Integer.parseInt(addRoomTypePage.getRoomSize().getText());
            int hasView = addRoomTypePage.getHasView();
            int hasKitchen = addRoomTypePage.getHasKitchen();
            int hasBathroom = addRoomTypePage.getHasBathroom();
            int hasWorksp = addRoomTypePage.getHasWorksp();
            int hasTv = addRoomTypePage.getHasTv();
            int hasCoffeeMkr = addRoomTypePage.getHasCoffeeMkr();

            boolean rtFlag = true;
            RoomType newRoomType = new RoomType(capTypeName, nbBeds, rSize, hasView, hasKitchen, hasBathroom, hasWorksp,
                    hasTv, hasCoffeeMkr);

            for (RoomType rT : allRoomTypes) {
                if (rT.getT_name().equals(newRoomType.getT_name())) {
                    rtFlag = false;

                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Room type already exists. Choose another one!");
                    a.showAndWait();

                    break;
                }
            }
            if (rtFlag) {
                hdata.addRoomType(newRoomType);
                addTypeStage.close();
                newRoomStage.close();
                newRoomDisplay(addTypeStage);
            }
        });

        addRoomTypePage.getCancel().setOnAction(e -> addTypeStage.close());

        // OPEN THE WINDOW //

        addTypeStage.initOwner(newRoomStage);
        addTypeStage.initModality(Modality.WINDOW_MODAL);

        addTypeStage.setScene(addRoomTypePage.getScene());
        addTypeStage.setTitle("Add a new room type");
        addTypeStage.show();
    }

    /**
     * Displays the page to add a new room in the database (only for admins)
     *
     * @param formerStage to close when the new stage is showed
     */
    private void newRoomDisplay(Stage formerStage) {
        ArrayList<Room> rooms = hdata.getRooms();
        NewRoomView newRoomViewPage = new NewRoomView(hdata);
        Stage newRoomStage = new Stage();

        // ERROR HANDLING //

        newRoomViewPage.getSubmit().setDisable(newRoomViewPage.getNumRoom().getText().equals("") ||
                newRoomViewPage.getFloor().getText().equals("") ||
                newRoomViewPage.getRoomType().getValue() == null);

        newRoomViewPage.getNumRoom().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newRoomViewPage.getNumRoom().getText().equals("")) {
                newRoomViewPage.getSubmit().setDisable(true);
            } else if (!newRoomViewPage.getNumRoom().getText().equals("")
                    && !newRoomViewPage.getFloor().getText().equals("")
                    && newRoomViewPage.getRoomType().getValue() != null) {
                newRoomViewPage.getSubmit().setDisable(false);
            }
        });

        newRoomViewPage.getFloor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newRoomViewPage.getFloor().getText().equals("")) {
                newRoomViewPage.getSubmit().setDisable(true);
            } else if (!newRoomViewPage.getFloor().getText().equals("")
                    && !newRoomViewPage.getNumRoom().getText().equals("")
                    && newRoomViewPage.getRoomType().getValue() != null) {
                newRoomViewPage.getSubmit().setDisable(false);
            }
        });

        newRoomViewPage.getRoomType().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newRoomViewPage.getRoomType().getValue() == null) {
                newRoomViewPage.getSubmit().setDisable(true);
            } else if (!newRoomViewPage.getNumRoom().getText().equals("") &&
                    !newRoomViewPage.getFloor().getText().equals("") &&
                    newRoomViewPage.getRoomType().getValue() != null) {
                newRoomViewPage.getSubmit().setDisable(false);
            }
        });

        // SET BUTTONS ON ACTION //

        newRoomViewPage.getAddRoomType().setOnAction(e -> addRoomTypeDisplay(hdata, newRoomStage));

        newRoomViewPage.getSubmit().setOnAction(e -> {
            int roomNb = Integer.parseInt(newRoomViewPage.getNumRoom().getText());
            int roomFloor = Integer.parseInt(newRoomViewPage.getFloor().getText());
            String roomType = newRoomViewPage.getRoomType().getValue();

            Room newRoom = new Room(roomNb, roomFloor, roomType);

            boolean flag = true;

            for (Room r : rooms) {
                if (r.getR_num() == newRoom.getR_num()) {
                    flag = false;

                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Room already exists. Add a different one or update existing!");
                    a.showAndWait();

                    break;
                }
                // else do nothing
            }
            if (flag) {
                hdata.addRoom(newRoom);
                newRoomStage.close();
                formerStage.close();
                roomsDisplay();
            }
        });

        newRoomViewPage.getCancel().setOnAction(e -> newRoomStage.close());

        // OPEN THE WINDOW //

        // Specifies the modality for new window and the owner of window
        newRoomStage.initOwner(formerStage);
        newRoomStage.initModality(Modality.WINDOW_MODAL);
        newRoomStage.setScene(newRoomViewPage.getScene());
        newRoomStage.setTitle("Hotel Manager - New Room");
        newRoomStage.show();
    }

    /**
     * Displays the page to add a new user in the database (only for admins)
     *
     * @param formerStage to close when the new stage is showed
     */
    private void newUserDisplay(Stage formerStage) {
        NewUserView newUserViewPage = new NewUserView();
        Stage newUserStage = new Stage();
        ArrayList<User> allUsers = hdata.getUsers();

        // ERROR HANDLING //

        newUserViewPage.getSubmit().setDisable(newUserViewPage.getUserName().getText().equals("") ||
                newUserViewPage.getUserPassWord().getText().equals(""));

        newUserViewPage.getUserName().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newUserViewPage.getUserName().getText().equals("") &&
                    !newUserViewPage.getUserPassWord().getText().equals("")) {
                newUserViewPage.getSubmit().setDisable(false);
            }
        });

        newUserViewPage.getUserPassWord().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newUserViewPage.getUserName().getText().equals("") &&
                    !newUserViewPage.getUserPassWord().getText().equals("")) {
                newUserViewPage.getSubmit().setDisable(false);
            }
        });

        // SET BUTTONS ON ACTION //

        newUserViewPage.getSubmit().setOnAction(e -> {
            String userN = newUserViewPage.getUserName().getText();
            String userP = newUserViewPage.getUserPassWord().getText();
            int userIA = 0;
            if (newUserViewPage.getAdmin().isSelected()) {
                userIA = 1;
            }
            User newUser = new User(userN, userP, userIA);

            boolean uFlag = true;

            for (User u : allUsers) {
                if (u.getU_name().equalsIgnoreCase(newUser.getU_name())) {
                    uFlag = false;

                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Username already exists. Add a different one or update existing!");
                    a.showAndWait();

                    break;
                }
                // else do nothing
            }
            if (uFlag) {
                hdata.addUser(newUser);
                newUserStage.close();
                formerStage.close();
                usersDisplay();
            }
        });

        newUserViewPage.getCancel().setOnAction(e -> {
            newUserStage.close();
            formerStage.close();
            usersDisplay();
        });

        // OPEN THE WINDOW //

        newUserStage.initOwner(formerStage);
        newUserStage.initModality(Modality.WINDOW_MODAL);
        newUserStage.setScene(newUserViewPage.getScene());
        newUserStage.setTitle("Hotel Manager - New User");
        newUserStage.show();
    }

    /**
     * Displays the page to add a new booking in the database (only for admins)
     *
     * @param formerStage to close when the new stage is showed
     */
    private void newBookingDisplay(Stage formerStage) {
        NewBookingView newBookingViewPage = new NewBookingView(dbm, hdata);
        Stage newBookingStage = new Stage();

        int MIN_ROOM_NUMBER = 1;
        Room lastRoomAdded = hdata.getRooms().get(hdata.getRooms().size() - 1);
        int MAX_ROOM_NUMBER = lastRoomAdded.getR_num();
        int MIN_BOOKING_FEE = 0;
        int MAX_BOOKING_FEE = 1000000;

        Alert warningBookingFee = new Alert(AlertType.WARNING, "Enter a number greater than or equal to 0 and smaller than or equal to 1000000!");

        // ERROR HANDLING //

        newBookingViewPage.getSubmit().setDisable(newBookingViewPage.getNumRoom().getValue() == null ||
                newBookingViewPage.getBookingFee().getText().equals("") ||
                newBookingViewPage.getCheckIn().getValue() == null ||
                newBookingViewPage.getCheckOut().getValue() == null ||
                newBookingViewPage.getCustomer().getValue() == null);

        newBookingViewPage.getNumRoom().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue < MIN_ROOM_NUMBER ||
                    newValue > MAX_ROOM_NUMBER) {
                newBookingViewPage.getSubmit().setDisable(true);
            } else {
                if (newBookingViewPage.getNumRoom().getValue() != null &&
                        !newBookingViewPage.getBookingFee().getText().equals("") &&
                        isNumeric(newBookingViewPage.getBookingFee().getText()) &&
                        Integer.parseInt(newBookingViewPage.getBookingFee().getText()) >= MIN_BOOKING_FEE &&
                        Integer.parseInt(newBookingViewPage.getBookingFee().getText()) <= MAX_BOOKING_FEE &&
                        newBookingViewPage.getCheckIn().getValue() != null &&
                        newBookingViewPage.getCheckOut().getValue() != null &&
                        newBookingViewPage.getCustomer().getValue() != null) {
                    newBookingViewPage.getSubmit().setDisable(false);
                }
            }
        });

        newBookingViewPage.getBookingFee().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNumeric(newValue) ||
                    Integer.parseInt(newValue) < MIN_BOOKING_FEE ||
                    Integer.parseInt(newValue) > MAX_BOOKING_FEE) {
                newBookingViewPage.getSubmit().setDisable(true);
            } else {
                if (newBookingViewPage.getNumRoom().getValue() != null &&
                        !newBookingViewPage.getBookingFee().getText().equals("") &&
                        newBookingViewPage.getNumRoom().getValue() >= MIN_ROOM_NUMBER &&
                        newBookingViewPage.getNumRoom().getValue() <= MAX_ROOM_NUMBER &&
                        newBookingViewPage.getCheckIn().getValue() != null &&
                        newBookingViewPage.getCheckOut().getValue() != null &&
                        newBookingViewPage.getCustomer().getValue() != null) {
                    newBookingViewPage.getSubmit().setDisable(false);
                }
            }
            if ((!isNumeric(newValue) && isNumeric(oldValue)) ||
                    (!isNumeric(newValue) && oldValue.equals("")) ||
                    (isNumeric(newValue) && isNumeric(oldValue) && (Integer.parseInt(newValue) < MIN_BOOKING_FEE || Integer.parseInt(newValue) > MAX_BOOKING_FEE) &&
                            (Integer.parseInt(oldValue) >= MIN_BOOKING_FEE && Integer.parseInt(oldValue) <= MAX_BOOKING_FEE))) {
                warningBookingFee.showAndWait();
            }
        });

        newBookingViewPage.getCheckIn().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newBookingViewPage.getNumRoom().getValue() != null &&
                    !newBookingViewPage.getBookingFee().getText().equals("") &&
                    newBookingViewPage.getNumRoom().getValue() >= MIN_ROOM_NUMBER &&
                    newBookingViewPage.getNumRoom().getValue() <= MAX_ROOM_NUMBER &&
                    isNumeric(newBookingViewPage.getBookingFee().getText()) &&
                    Integer.parseInt(newBookingViewPage.getBookingFee().getText()) >= MIN_BOOKING_FEE &&
                    Integer.parseInt(newBookingViewPage.getBookingFee().getText()) <= MAX_BOOKING_FEE &&
                    newBookingViewPage.getCheckOut().getValue() != null &&
                    newBookingViewPage.getCustomer().getValue() != null) {
                newBookingViewPage.getSubmit().setDisable(false);
            }
        });

        newBookingViewPage.getCheckOut().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newBookingViewPage.getNumRoom().getValue() != null &&
                    !newBookingViewPage.getBookingFee().getText().equals("") &&
                    newBookingViewPage.getNumRoom().getValue() >= MIN_ROOM_NUMBER &&
                    newBookingViewPage.getNumRoom().getValue() <= MAX_ROOM_NUMBER &&
                    isNumeric(newBookingViewPage.getBookingFee().getText()) &&
                    Integer.parseInt(newBookingViewPage.getBookingFee().getText()) >= MIN_BOOKING_FEE &&
                    Integer.parseInt(newBookingViewPage.getBookingFee().getText()) <= MAX_BOOKING_FEE &&
                    newBookingViewPage.getCheckIn().getValue() != null &&
                    newBookingViewPage.getCustomer().getValue() != null) {
                newBookingViewPage.getSubmit().setDisable(false);
            }
        });

        newBookingViewPage.getCustomer().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newBookingViewPage.getNumRoom().getValue() != null &&
                    !newBookingViewPage.getBookingFee().getText().equals("") &&
                    newBookingViewPage.getNumRoom().getValue() >= MIN_ROOM_NUMBER &&
                    newBookingViewPage.getNumRoom().getValue() <= MAX_ROOM_NUMBER &&
                    isNumeric(newBookingViewPage.getBookingFee().getText()) &&
                    Integer.parseInt(newBookingViewPage.getBookingFee().getText()) >= MIN_BOOKING_FEE &&
                    Integer.parseInt(newBookingViewPage.getBookingFee().getText()) <= MAX_BOOKING_FEE &&
                    newBookingViewPage.getCheckIn().getValue() != null &&
                    newBookingViewPage.getCheckOut().getValue() != null) {
                newBookingViewPage.getSubmit().setDisable(false);
            }
        });

        // SET BUTTONS ON ACTION //

        newBookingViewPage.getSubmit().setOnAction(e -> {
            int bookingID = hdata.getBookingAutoID();
            int roomNb = newBookingViewPage.getNumRoom().getValue();
            int paidByCard = 0;
            if (newBookingViewPage.getPaidByCard().isSelected()) {
                paidByCard = 1;
            }
            // Getting the datepicker dates
            DatePicker checkInDP = newBookingViewPage.getCheckIn();
            LocalDate datePicked = checkInDP.getValue();
            String formattedDate = datePicked.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date sqlDate = java.sql.Date.valueOf(formattedDate);

            DatePicker checkOutDP = newBookingViewPage.getCheckOut();

            LocalDate secondDatePicked = checkOutDP.getValue();
            String secondFormattedDate = secondDatePicked.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date secondSQLDate = java.sql.Date.valueOf(secondFormattedDate);

            int bookingFee = Integer.parseInt(newBookingViewPage.getBookingFee().getText());
            int isPaid = 0;
            if (newBookingViewPage.getIsPaid().isSelected()) {
                isPaid = 1;
            }

            int c_ss_number = Integer.parseInt(newBookingViewPage.getCustomer().getValue().split(" - ")[1].trim());
            Booking newBooking = new Booking(bookingID, roomNb, paidByCard, sqlDate, secondSQLDate, bookingFee, isPaid, c_ss_number);
            hdata.addBooking(newBooking);

            newBookingStage.close();
            formerStage.close();
            bookingsDisplay();
        });

        newBookingViewPage.getCancel().setOnAction(e -> {
            newBookingStage.close();
            formerStage.close();
            bookingsDisplay();
        });

        newBookingStage.initOwner(formerStage);
        newBookingStage.initModality(Modality.WINDOW_MODAL);
        newBookingStage.setScene(newBookingViewPage.getScene());
        newBookingStage.setTitle("Hotel Manager - New Booking");
        newBookingStage.show();
    }

    /**
     * A page to update the bookings for reception staff and admin
     *
     * @param formerStage to close when the new stage is showed
     */
    private void updateBookingDisplay(Stage formerStage, Booking booking) {
        UpdateBookingView updateBookingViewPage = new UpdateBookingView(dbm, hdata);
        Stage updateRoomStage = new Stage();

        // default values
        updateBookingViewPage.getNewRoom().setValue(booking.getR_num());
        updateBookingViewPage.getNewPayment().setSelected(booking.getPaid_by_card() == 1);
        updateBookingViewPage.getNewFromDate().setValue(booking.getB_from().toLocalDate());
        updateBookingViewPage.getNewTillDate().setValue(booking.getB_till().toLocalDate());
        updateBookingViewPage.getNewFee().setText(String.valueOf(booking.getB_fee()));
        updateBookingViewPage.getNewIsPaid().setSelected(booking.getB_is_paid() == 1);
        updateBookingViewPage.getCustomer().setValue(dbm.cdb.getCustomerName(booking.getC_ss_number()) + " - " + booking.getC_ss_number());

        // SET BUTTONS ON ACTION //

        updateBookingViewPage.getSubmit().setOnAction(e -> {
            int newRoom = updateBookingViewPage.getNewRoom().getValue();

            int newPayment = 0;
            if (updateBookingViewPage.getNewPayment().isSelected()) {
                newPayment = 1;
            }

            // Getting the datepicker dates
            DatePicker checkInDP = updateBookingViewPage.getNewFromDate();
            LocalDate datePicked = checkInDP.getValue();
            String formattedDate = datePicked.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date sqlDate = java.sql.Date.valueOf(formattedDate);

            DatePicker checkOutDP = updateBookingViewPage.getNewTillDate();

            LocalDate secondDatePicked = checkOutDP.getValue();
            String secondFormattedDate = secondDatePicked.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date secondSQLDate = java.sql.Date.valueOf(secondFormattedDate);

            int bookingFee = Integer.parseInt(updateBookingViewPage.getNewFee().getText());
            int isPaid = 0;
            if (updateBookingViewPage.getNewIsPaid().isSelected()) {
                isPaid = 1;
            }

            int new_C_ss_number = Integer.parseInt(updateBookingViewPage.getCustomer().getValue().split(" - ")[1].trim());

            booking.setR_num(newRoom);
            booking.setPaid_by_card(newPayment);
            booking.setB_from(sqlDate);
            booking.setB_till(secondSQLDate);
            booking.setB_fee(bookingFee);
            booking.setB_is_paid(isPaid);
            booking.setC_ss_number(new_C_ss_number);

            hdata.updateBooking(booking);
            updateRoomStage.close();
            formerStage.close();
            bookingsDisplay();
        });

        updateBookingViewPage.getCancel().setOnAction(e -> updateRoomStage.close());

        // OPEN THE WINDOW //

        updateRoomStage.setScene(updateBookingViewPage.getScene());
        updateRoomStage.setTitle("Hotel Manager - Updating Booking");

        // Specifies the modality for new window and the owner of window
        updateRoomStage.initOwner(formerStage);
        updateRoomStage.initModality(Modality.WINDOW_MODAL);

        updateRoomStage.show();
    }

    /**
     * Displays the page with the hotel's bookings table
     */
    private void bookingsDisplay() {
        List<Booking> bookings = hdata.getBookings();
        BookingsView bookingsViewPage = new BookingsView(connectedUser, bookings, dbm);
        Stage bookingsStage = new Stage();

        // SET BUTTONS ON ACTION //

        // admins can add a booking
        if (connectedUser.getU_is_admin() == 1 || connectedUser.getU_is_admin() == 0) {
            bookingsViewPage.getAddBooking().setOnAction(e -> newBookingDisplay(bookingsStage));
        }

        bookingsViewPage.getBookingsTable().setRowFactory(tableView -> {
            final TableRow<Booking> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            MenuItem updateItem = new MenuItem("Update");
            updateItem.setOnAction(event -> {
                Booking b = bookingsViewPage.getBookingsTable().getSelectionModel().getSelectedItem();
                updateBookingDisplay(bookingsStage, b);
            });

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event -> {
                Booking bD = bookingsViewPage.getBookingsTable().getSelectionModel().getSelectedItem();
                deleteBookingDisplay(bookingsStage, bD);
            });

            rowMenu.getItems().addAll(updateItem, deleteItem);

            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu).otherwise((ContextMenu) null));
            return row;
        });

        // OPEN THE WINDOW //

        bookingsStage.setScene(bookingsViewPage.getScene());
        bookingsStage.setTitle("Hotel Manager - Bookings");
        bookingsStage.show();
    }

    /**
     * Displays the details of a room
     *
     * @param roomsViewPage roomsView instance which contains the info of the room
     *                      concerned
     * @param roomsStage    the Stage of the previous window
     */
    private void roomDetailsDisplay(RoomsView roomsViewPage, Stage roomsStage) {
        Stage newWindow = new Stage();
        GridPane secBLayout = new GridPane();
        secBLayout.getStyleClass().add("details-pane");

        Label title = new Label("The room is :");
        title.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 20));
        title.setStyle("-fx-font-weight: bold;");
        title.setTextFill(Paint.valueOf("white"));
        secBLayout.add(title, 0, 0);

        Room rD = roomsViewPage.getRoomsTable().getSelectionModel().getSelectedItem();
        Hashtable<String, String> roomsDetails = hdata.viewDetails(rD);

        secBLayout.add(new Label("  \u2022  Of type " + roomsDetails.get("t_name")), 0, 1);
        secBLayout.add(new Label("  \u2022  With " + roomsDetails.get("beds") + " bed/s"), 0, 2);
        secBLayout.add(new Label("  \u2022  With a surface of " + roomsDetails.get("r_size") + " square meters"), 0, 3);

        if (roomsDetails.get("has_view").equals("1")) {
            secBLayout.add(new Label("  \u2022  With view: yes"), 0, 4);
        } else if (roomsDetails.get("has_view").equals("0")) {
            secBLayout.add(new Label("  \u2022  With view: no"), 0, 4);
        }

        if (roomsDetails.get("has_kitchen").equals("1")) {
            secBLayout.add(new Label("  \u2022  Kitchen integrated: yes"), 0, 5);
        } else if (roomsDetails.get("has_kitchen").equals("0")) {
            secBLayout.add(new Label("  \u2022  Kitchen integrated: no"), 0, 5);
        }

        if (roomsDetails.get("has_bathroom").equals("1")) {
            secBLayout.add(new Label("  \u2022  Bathroom integrated: yes"), 0, 6);
        } else if (roomsDetails.get("has_bathroom").equals("0")) {
            secBLayout.add(new Label("  \u2022  Bathroom integrated: no"), 0, 6);
        }

        if (roomsDetails.get("has_workspace").equals("1")) {
            secBLayout.add(new Label("  \u2022  Workspace integrated: yes"), 0, 7);
        } else if (roomsDetails.get("has_workspace").equals("0")) {
            secBLayout.add(new Label("  \u2022  Workspace integrated: no"), 0, 7);
        }

        if (roomsDetails.get("has_tv").equals("1")) {
            secBLayout.add(new Label("  \u2022  With TV: yes"), 0, 8);
        } else if (roomsDetails.get("has_tv").equals("0")) {
            secBLayout.add(new Label("  \u2022  With TV: no"), 0, 8);
        }

        if (roomsDetails.get("has_coffee_maker").equals("1")) {
            secBLayout.add(new Label("  \u2022  With coffee maker: yes"), 0, 9);
        } else if (roomsDetails.get("has_view").equals("0")) {
            secBLayout.add(new Label("  \u2022  With coffee maker: no"), 0, 9);
        }

        secBLayout.setVgap(10);
        secBLayout.setPadding(new Insets(10, 10, 10, 10));
        Scene secondScene = new Scene(secBLayout, 280, 280);
        secondScene.getStylesheets().add("file:assets/css/Stylesheet.css");

        newWindow.setTitle("Details");
        newWindow.setScene(secondScene);
        newWindow.initOwner(roomsStage);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.show();
    }

    /**
     * Displays the page with the hotel's rooms table
     */
    private void roomsDisplay() {
        ArrayList<Room> rooms = hdata.getRooms();
        RoomsView roomsViewPage = new RoomsView(connectedUser, rooms, hdata);
        Stage roomsStage = new Stage();

        // SET CONTEXT MENU & BUTTONS ON ACTION //

        // admins can add, update or delete a room through the context menu
        if (connectedUser.getU_is_admin() == 1) {
            roomsViewPage.getAddRoom().setOnAction(e -> newRoomDisplay(roomsStage));
            roomsViewPage.getRoomsTable().setRowFactory(tableView -> {
                final TableRow<Room> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();

                MenuItem updateItem = new MenuItem("Update");
                updateItem.setOnAction(event -> {
                    Room u = roomsViewPage.getRoomsTable().getSelectionModel().getSelectedItem();
                    updateRoomDisplay(roomsStage, u);
                });

                MenuItem deleteItem = new MenuItem("Delete");
                deleteItem.setOnAction(event -> {
                    Room r = roomsViewPage.getRoomsTable().getSelectionModel().getSelectedItem();
                    deleteRoomDisplay(roomsStage, r);
                });

                MenuItem viewItem = new MenuItem("View details");
                viewItem.setOnAction(event -> roomDetailsDisplay(roomsViewPage, roomsStage));

                rowMenu.getItems().addAll(updateItem, deleteItem, viewItem);

                // only display context menu for non-null items:
                row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu)
                        .otherwise((ContextMenu) null));
                return row;
            });
        }

        // reception staff can only view details through the context menu
        if (connectedUser.getU_is_admin() == 0) {
            roomsViewPage.getRoomsTable().setRowFactory(tableView -> {
                final TableRow<Room> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();

                MenuItem viewItem = new MenuItem("View details");
                viewItem.setOnAction(event -> roomDetailsDisplay(roomsViewPage, roomsStage));

                rowMenu.getItems().addAll(viewItem);

                // only display context menu for non-null items:
                row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu)
                        .otherwise((ContextMenu) null));
                return row;
            });
        }

        // OPEN THE WINDOW //

        roomsStage.setScene(roomsViewPage.getScene());
        roomsStage.setTitle("Hotel Manager - Rooms");
        roomsStage.show();
    }

    /**
     * Displays the page to update a room in the database (only for admins)
     *
     * @param formerStage to close when the new stage is showed
     */
    private void updateRoomDisplay(Stage formerStage, Room room) {
        ArrayList<Room> rooms = hdata.getRooms();
        UpdateRoomView updateRoomViewPage = new UpdateRoomView(hdata);
        Stage updateRoomStage = new Stage();

        // Default values
        String currentRoomNumber = String.valueOf(room.getR_num());
        String currentFloor = String.valueOf(room.getR_floor());

        updateRoomViewPage.getNumRoom().setText(currentRoomNumber);
        updateRoomViewPage.getFloor().setText(currentFloor);
        updateRoomViewPage.getRoomType().setValue(room.getR_type());

        // ERROR HANDLING //

        updateRoomViewPage.getSubmit().setDisable(updateRoomViewPage.getNumRoom().getText().equals("")
                || updateRoomViewPage.getFloor().getText().equals("") || updateRoomViewPage.getRoomType().getValue() == null);

        updateRoomViewPage.getNumRoom().textProperty().addListener((observable, oldValue, newValue) -> {
            if (updateRoomViewPage.getNumRoom().getText().equals("")) {
                updateRoomViewPage.getSubmit().setDisable(true);
            } else if (!updateRoomViewPage.getNumRoom().getText().equals("")
                    && !updateRoomViewPage.getFloor().getText().equals("")
                    && updateRoomViewPage.getRoomType().getValue() != null) {
                updateRoomViewPage.getSubmit().setDisable(false);
            }
        });

        updateRoomViewPage.getFloor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (updateRoomViewPage.getFloor().getText().equals("")) {
                updateRoomViewPage.getSubmit().setDisable(true);
            } else if (!updateRoomViewPage.getFloor().getText().equals("")
                    && !updateRoomViewPage.getNumRoom().getText().equals("")
                    && updateRoomViewPage.getRoomType().getValue() != null) {
                updateRoomViewPage.getSubmit().setDisable(false);
            }
        });

        updateRoomViewPage.getRoomType().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (updateRoomViewPage.getRoomType().getValue() == null) {
                updateRoomViewPage.getSubmit().setDisable(true);
            } else if (!updateRoomViewPage.getNumRoom().getText().equals("")
                    && !updateRoomViewPage.getFloor().getText().equals("")
                    && updateRoomViewPage.getRoomType().getValue() != null) {
                updateRoomViewPage.getSubmit().setDisable(false);
            }
        });

        // SET BUTTONS ON ACTION //

        updateRoomViewPage.getSubmit().setOnAction(e -> {
            int roomNb = Integer.parseInt(updateRoomViewPage.getNumRoom().getText());
            int roomFloor = Integer.parseInt(updateRoomViewPage.getFloor().getText());
            String roomType = updateRoomViewPage.getRoomType().getValue();

            Room updateRoom = new Room(roomNb, roomFloor, roomType);

            boolean flag = true;

            for (Room r : rooms) {
                if (r.getR_num() == room.getR_num()) {
                    continue;
                }
                if (r.getR_num() == updateRoom.getR_num()) {
                    flag = false;

                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Room number already exists. Add a different one or update existing room!");
                    a.showAndWait();
                    break;
                }
            }
            if (updateRoom.getR_num() == room.getR_num() || flag) {
                hdata.updateRoom(updateRoom, room.getR_num());
                updateRoomStage.close();
                formerStage.close();
                roomsDisplay();
            }

        });

        updateRoomViewPage.getCancel().setOnAction(e -> updateRoomStage.close());

        // OPEN THE WINDOW //

        updateRoomStage.setScene(updateRoomViewPage.getScene());
        updateRoomStage.setTitle("Hotel Manager - Updating Room");
        // Specifies the modality for new window and the owner of window
        updateRoomStage.initOwner(formerStage);
        updateRoomStage.initModality(Modality.WINDOW_MODAL);
        updateRoomStage.show();
    }

    /**
     * Display to delete the selected room
     */
    private void deleteRoomDisplay(Stage formerStage, Room room) {
        DeleteRoomView deleteRoomViewPage = new DeleteRoomView();
        Stage deleteRoomStage = new Stage();

        // SET BUTTONS ON ACTION //

        deleteRoomViewPage.getSubmit().setOnAction(e -> {
            hdata.deleteRoom(room);
            deleteRoomStage.close();
            formerStage.close();
            roomsDisplay();
        });

        deleteRoomViewPage.getCancel().setOnAction(e -> deleteRoomStage.close());

        // OPEN THE WINDOW //

        // Specifies the modality for new window and the owner of window
        deleteRoomStage.initOwner(formerStage);
        deleteRoomStage.initModality(Modality.WINDOW_MODAL);
        deleteRoomStage.setScene(deleteRoomViewPage.getScene());
        deleteRoomStage.setTitle("Hotel Manager - Deleting Room");
        deleteRoomStage.show();
    }

    /**
     * Display to delete the selected booking
     */
    private void deleteBookingDisplay(Stage formerStage, Booking booking) {
        DeleteBookingView deleteBookingView = new DeleteBookingView();
        Stage deleteBookingStage = new Stage();

        // SET BUTTONS ON ACTION //

        deleteBookingView.getSubmit().setOnAction(e -> {
            hdata.deleteBooking(booking);
            deleteBookingStage.close();
            formerStage.close();
            bookingsDisplay();
        });

        deleteBookingView.getCancel().setOnAction(e -> deleteBookingStage.close());

        // OPEN THE WINDOW //

        // Specifies the modality for new window and the owner of window
        deleteBookingStage.initOwner(formerStage);
        deleteBookingStage.initModality(Modality.WINDOW_MODAL);
        deleteBookingStage.setScene(deleteBookingView.getScene());
        deleteBookingStage.setTitle("Hotel Manager - Deleting Booking");
        deleteBookingStage.show();
    }

    /**
     * Display to delete the selected customer
     */
    private void deleteCustomerDisplay(Stage formerStage, Customer customer) {
        DeleteCustomerView deleteCustomerView = new DeleteCustomerView();
        Stage deleteCustomerStage = new Stage();

        // SET BUTTONS ON ACTION //

        deleteCustomerView.getSubmit().setOnAction(e -> {
            hdata.deleteCustomer(customer);
            deleteCustomerStage.close();
            formerStage.close();
            customersDisplay();
        });

        deleteCustomerView.getCancel().setOnAction(e -> deleteCustomerStage.close());

        // OPEN THE WINDOW //

        // Specifies the modality for new window and the owner of window
        deleteCustomerStage.initOwner(formerStage);
        deleteCustomerStage.initModality(Modality.WINDOW_MODAL);
        deleteCustomerStage.setScene(deleteCustomerView.getScene());
        deleteCustomerStage.setTitle("Hotel Manager - Deleting Customer");
        deleteCustomerStage.show();
    }

    private void usersDisplay() {
        List<User> users = dbm.udb.getAllUsers();
        UsersView usersViewPage = new UsersView(connectedUser, users);
        Stage usersStage = new Stage();

        // SET BUTTONS ON ACTION //

        // admins can add a user
        if (connectedUser.getU_is_admin() == 1) {
            usersViewPage.getAddUser().setOnAction(e -> newUserDisplay(usersStage));
        }

        // OPEN THE WINDOW //

        usersStage.setScene(usersViewPage.getScene());
        usersStage.setTitle("Hotel Manager - Users");
        usersStage.show();
    }

    private void customersDisplay() {
        List<Customer> customers = hdata.getCustomers();
        CustomersView customersViewPage = new CustomersView(customers);
        Stage customerStage = new Stage();

        // SET BUTTONS ON ACTION //

        customersViewPage.getAddCustomer().setOnAction(e -> newCustomerDisplay(customerStage));

        customersViewPage.getCustomersTable().setRowFactory(tableView -> {
            final TableRow<Customer> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            MenuItem updateItem = new MenuItem("Update");
            updateItem.setOnAction(event -> {
                Customer c = customersViewPage.getCustomersTable().getSelectionModel().getSelectedItem();
                updateCustomerDisplay(customerStage, c);
            });

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event -> {
                Customer cD = customersViewPage.getCustomersTable().getSelectionModel().getSelectedItem();
                deleteCustomerDisplay(customerStage, cD);
            });

            rowMenu.getItems().addAll(updateItem, deleteItem);

            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu).otherwise((ContextMenu) null));
            return row;
        });

        // OPEN THE WINDOW //

        customerStage.setScene(customersViewPage.getScene());
        customerStage.setTitle("Hotel Manager - Customers");
        customerStage.show();
    }

    private void newCustomerDisplay(Stage formerStage) {
        NewCustomerView newCustomerViewPage = new NewCustomerView();
        Stage newCustomerStage = new Stage();

        // ERROR HANDLING //

        newCustomerViewPage.getSubmit().setDisable(newCustomerViewPage.getCSSNum().getText().equals("") ||
                newCustomerViewPage.getCAddress().getText().equals("") ||
                newCustomerViewPage.getCFullName().getText().equals("") ||
                newCustomerViewPage.getCPhoneNum().getText().equals("") ||
                newCustomerViewPage.getCEmail().getText().equals(""));

        newCustomerViewPage.getCSSNum().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNumeric(newValue)) {
                newCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (!newCustomerViewPage.getCAddress().getText().equals("") &&
                        !newCustomerViewPage.getCFullName().getText().equals("") &&
                        isNumeric(newCustomerViewPage.getCPhoneNum().getText()) &&
                        !newCustomerViewPage.getCEmail().getText().equals("")) {
                    newCustomerViewPage.getSubmit().setDisable(false);
                }
            }
            if ((!isNumeric(newValue) && isNumeric(oldValue)) ||
                    (!isNumeric(newValue) && oldValue.equals(""))) {
                Alert nCA = new Alert(AlertType.ERROR);
                nCA.setContentText("8 digits please!");
                nCA.showAndWait();
            }
        });

        newCustomerViewPage.getCAddress().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                newCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (isNumeric(newCustomerViewPage.getCSSNum().getText()) &&
                        !newCustomerViewPage.getCFullName().getText().equals("") &&
                        isNumeric(newCustomerViewPage.getCPhoneNum().getText()) &&
                        !newCustomerViewPage.getCEmail().getText().equals("")) {
                    newCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        newCustomerViewPage.getCFullName().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                newCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (isNumeric(newCustomerViewPage.getCSSNum().getText()) &&
                        !newCustomerViewPage.getCAddress().getText().equals("") &&
                        isNumeric(newCustomerViewPage.getCPhoneNum().getText()) &&
                        !newCustomerViewPage.getCEmail().getText().equals("")) {
                    newCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        newCustomerViewPage.getCPhoneNum().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNumeric(newValue)) {
                newCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (isNumeric(newCustomerViewPage.getCSSNum().getText()) &&
                        !newCustomerViewPage.getCAddress().getText().equals("") &&
                        !newCustomerViewPage.getCFullName().getText().equals("") &&
                        !newCustomerViewPage.getCEmail().getText().equals("")) {
                    newCustomerViewPage.getSubmit().setDisable(false);
                }
            }
            if ((!isNumeric(newValue) && isNumeric(oldValue)) ||
                    (!isNumeric(newValue) && oldValue.equals(""))) {
                Alert nCAnd = new Alert(AlertType.ERROR);
                nCAnd.setContentText("9 digits please!");
                nCAnd.showAndWait();
            }
        });

        newCustomerViewPage.getCEmail().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                newCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (isNumeric(newCustomerViewPage.getCSSNum().getText()) &&
                        !newCustomerViewPage.getCAddress().getText().equals("") &&
                        !newCustomerViewPage.getCFullName().getText().equals("") &&
                        isNumeric(newCustomerViewPage.getCPhoneNum().getText())) {
                    newCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        // SET BUTTONS ON ACTION //

        newCustomerViewPage.getSubmit().setOnAction(e -> {
            int cSSNum = Integer.parseInt(newCustomerViewPage.getCSSNum().getText());
            String cAddress = newCustomerViewPage.getCAddress().getText();
            String cFullName = newCustomerViewPage.getCFullName().getText();
            int cPhoneNum = Integer.parseInt(newCustomerViewPage.getCPhoneNum().getText());
            String cEmail = newCustomerViewPage.getCEmail().getText();

            Customer newCustomer = new Customer(cSSNum, cAddress, cFullName, cPhoneNum, cEmail);
            if (!dbm.cdb.customerExists(newCustomer)) {
                hdata.addCustomer(newCustomer);

                newCustomerStage.close();
                formerStage.close();
                customersDisplay();
            } else {
                Alert errorCustomerExists = new Alert(AlertType.ERROR, "A customer with this social security number already exists in the database!");
                errorCustomerExists.showAndWait();
            }

        });

        newCustomerViewPage.getCancel().setOnAction(e -> {
            newCustomerStage.close();
            formerStage.close();
            customersDisplay();
        });

        // OPEN THE WINDOW //

        // Specifies the modality for new window and the owner of window
        newCustomerStage.initOwner(formerStage);
        newCustomerStage.initModality(Modality.WINDOW_MODAL);
        newCustomerStage.setScene(newCustomerViewPage.getScene());
        newCustomerStage.setTitle("Hotel Manager - New Customer");
        newCustomerStage.show();
    }

    /**
     * Displays the page to update a customer in the database
     *
     * @param formerStage to close when the new stage is showed
     */
    private void updateCustomerDisplay(Stage formerStage, Customer customer) {
        UpdateCustomerView updateCustomerViewPage = new UpdateCustomerView();
        Stage updateCustomerStage = new Stage();
        ArrayList<Customer> allCustomers = hdata.getCustomers();

        //default values
        updateCustomerViewPage.getCSSNum().setText(String.valueOf(customer.getC_ss_number()));
        updateCustomerViewPage.getCAddress().setText(customer.getC_address());
        updateCustomerViewPage.getCFullName().setText(customer.getC_full_name());
        updateCustomerViewPage.getCPhoneNum().setText(String.valueOf(customer.getC_phone_num()));
        updateCustomerViewPage.getCEmail().setText(customer.getC_email());


        updateCustomerViewPage.getSubmit().setDisable(updateCustomerViewPage.getCSSNum().getText().equals("")
                || updateCustomerViewPage.getCAddress().getText().equals("")
                || updateCustomerViewPage.getCFullName().getText().equals("")
                || updateCustomerViewPage.getCPhoneNum().getText().equals("")
                || updateCustomerViewPage.getCEmail().getText().equals(""));

        updateCustomerViewPage.getCSSNum().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() != 8) {
                updateCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (!updateCustomerViewPage.getCAddress().getText().equals("")
                        && !updateCustomerViewPage.getCFullName().getText().equals("")
                        && updateCustomerViewPage.getCPhoneNum().getText().length() == 9
                        && !updateCustomerViewPage.getCEmail().getText().equals("")) {
                    updateCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        updateCustomerViewPage.getCAddress().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                updateCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (updateCustomerViewPage.getCSSNum().getText().length() == 8
                        && !updateCustomerViewPage.getCFullName().getText().equals("")
                        && updateCustomerViewPage.getCPhoneNum().getText().length() == 9
                        && !updateCustomerViewPage.getCEmail().getText().equals("")) {
                    updateCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        updateCustomerViewPage.getCFullName().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                updateCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (updateCustomerViewPage.getCSSNum().getText().length() == 8
                        && !updateCustomerViewPage.getCAddress().getText().equals("")
                        && updateCustomerViewPage.getCPhoneNum().getText().length() == 9
                        && !updateCustomerViewPage.getCEmail().getText().equals("")) {
                    updateCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        updateCustomerViewPage.getCPhoneNum().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() != 9) {
                updateCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (updateCustomerViewPage.getCSSNum().getText().length() == 8
                        && !updateCustomerViewPage.getCAddress().getText().equals("")
                        && !updateCustomerViewPage.getCFullName().getText().equals("")
                        && !updateCustomerViewPage.getCEmail().getText().equals("")) {
                    updateCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        updateCustomerViewPage.getCEmail().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                updateCustomerViewPage.getSubmit().setDisable(true);
            } else {
                if (updateCustomerViewPage.getCSSNum().getText().length() == 8
                        && !updateCustomerViewPage.getCAddress().getText().equals("")
                        && !updateCustomerViewPage.getCFullName().getText().equals("")
                        && updateCustomerViewPage.getCPhoneNum().getText().length() == 9) {
                    updateCustomerViewPage.getSubmit().setDisable(false);
                }
            }
        });

        // SET BUTTONS ON ACTION //

        updateCustomerViewPage.getSubmit().setOnAction(e -> {
            int cSSNum = Integer.parseInt(updateCustomerViewPage.getCSSNum().getText());
            String cAddress = updateCustomerViewPage.getCAddress().getText();
            String cFullName = updateCustomerViewPage.getCFullName().getText();
            int cPhoneNum = Integer.parseInt(updateCustomerViewPage.getCPhoneNum().getText());
            String cEmail = updateCustomerViewPage.getCEmail().getText();

            Customer updatedCustomer = new Customer(cSSNum, cAddress, cFullName, cPhoneNum, cEmail);

            boolean cFlag = true;

            for (Customer c : allCustomers) {
                if (c.getC_ss_number() == customer.getC_ss_number()) {
                    continue;
                }
                if (c.getC_ss_number() == updatedCustomer.getC_ss_number()) {
                    cFlag = false;

                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Social security number already exists. Add a different one or update existing!");
                    a.showAndWait();
                    break;
                }
            }
            if (updatedCustomer.getC_ss_number() == customer.getC_ss_number() || cFlag) {
                hdata.updateCustomer(updatedCustomer, customer.getC_ss_number());
                updateCustomerStage.close();
                formerStage.close();
                customersDisplay();
            }

        });

        updateCustomerViewPage.getCancel().setOnAction(e -> updateCustomerStage.close());

        // OPEN THE WINDOW //

        updateCustomerStage.setScene(updateCustomerViewPage.getScene());
        updateCustomerStage.setTitle("Hotel Manager - Updating Customer");
        // Specifies the modality for new window and the owner of window
        updateCustomerStage.initOwner(formerStage);
        updateCustomerStage.initModality(Modality.WINDOW_MODAL);
        updateCustomerStage.show();
    }

    /**
     * Logout the user and display the login page
     *
     * @param myPageStage the main application's page to close after showing login
     *                    page
     */
    private void logout(Stage myPageStage) {
        mainPageStage.close();
        Stage loginStage = new Stage();
        credentialsDisplay(loginStage, false);
        myPageStage.close();
    }
}
