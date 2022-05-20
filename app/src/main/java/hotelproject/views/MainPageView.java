package hotelproject.views;

import hotelproject.controllers.objects.User;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * This is the main view for this application.
 * The presentation of this view may different for different users.
 * The users with 'admin' authority can see USERS selection and can add new users.
 * The users without 'admin' authority cannot see USERS selection.
 */
public class MainPageView extends View {

    // The user connected to the application
    private final User user;

    // The scene's nodes
    private final ImageView imgMyPage = new ImageView(
            new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_user_settings.png"));
    private final ImageView imgRooms = new ImageView(
            new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_rooms.png"));
    private final ImageView imgBookings = new ImageView(
            new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_bookings.png"));
    private final ImageView imgCustomers = new ImageView(
            new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_customers.png"));
    private final ImageView imgUsers = new ImageView(
            new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_users.png"));
    private final Button myPage = new Button("My Page");
    private final Button viewRooms = new Button("View rooms");
    private final Button viewBookings = new Button("View bookings");
    private final Button viewCustomers = new Button("View customers");
    private final Button viewUsers = new Button("View users");

    /**
     * Constructor for initiating MainPageView.
     *
     * @param user the login user
     */
    public MainPageView(User user) {
        myPage.setCursor(Cursor.HAND);
        viewBookings.setCursor(Cursor.HAND);
        viewCustomers.setCursor(Cursor.HAND);
        viewRooms.setCursor(Cursor.HAND);

        this.user = user;
        createScene();
    }

    /**
     * Create scene based on user's authority.
     */
    @Override
    void createScene() {
        if (user.getU_is_admin() == 1) {
            viewUsers.setCursor(Cursor.HAND);
        }

        GridPane scenePane = createPane();

        HBox header = createHeader();
        scenePane.add(header, 0, 0);

        GridPane body = createBody();
        scenePane.add(body, 0, 1);

        scenePane.setStyle("-fx-background-color: #121212; -fx-alignment: center;");
        scene = new Scene(scenePane, 775, 658);
    }

    /**
     * Present hotel logo
     *
     * @return an instance of HBox
     */
    private HBox createHeader() {
        Image logoImg = new Image("file:assets/img/ui_dev_pack/main_menu/logo_hotel.png");
        ImageView logo = new ImageView(logoImg);
        logo.setPreserveRatio(true);
        logo.setFitWidth(150.0);
        logo.setFitHeight(175.0);

        myPage.setCursor(Cursor.HAND);

        // mouse hovering
        myPage.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> imgMyPage
                .setImage(new Image("file:assets/img/ui_dev_pack/main_menu/hover_button_user_settings.png")));
        myPage.addEventHandler(MouseEvent.MOUSE_EXITED, e -> imgMyPage
                .setImage(new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_user_settings.png")));

        StackPane stack = createButton(imgMyPage, myPage, 176, 47);
        stack.setAlignment(Pos.BOTTOM_RIGHT);

        HBox header = new HBox(logo, stack);
        header.setAlignment(Pos.CENTER);

        header.getChildren().get(0).setTranslateX(100);
        header.getChildren().get(1).setTranslateX(194);

        return header;
    }

    /**
     * Create body pane to contain all four selections.
     *
     * @return an instance of Grid pane.
     */
    @Override
    GridPane createBody() {
        GridPane pane = createPane();

        viewUsers.setCursor(Cursor.HAND);
        viewBookings.setCursor(Cursor.HAND);
        viewCustomers.setCursor(Cursor.HAND);
        viewRooms.setCursor(Cursor.HAND);

        StackPane roomsStack = createButton(imgRooms, viewRooms, 342, 191);
        StackPane customersStack = createButton(imgCustomers, viewCustomers, 342, 191);
        HBox roomsCustomersButtons = new HBox(roomsStack, customersStack);
        roomsCustomersButtons.setSpacing(30);

        StackPane bookingsStack = createButton(imgBookings, viewBookings, 342, 191);
        HBox bookingsUsersButtons = new HBox(bookingsStack);
        if (user.getU_is_admin() == 1) {
            StackPane usersStack = createButton(imgUsers, viewUsers, 342, 191);
            bookingsUsersButtons.getChildren().add(usersStack);
        }
        bookingsUsersButtons.setSpacing(30);
        bookingsUsersButtons.setAlignment(Pos.CENTER);

        VBox menuButtons = new VBox(roomsCustomersButtons, bookingsUsersButtons);
        menuButtons.setSpacing(30);
        pane.add(menuButtons, 0, 2);

        // mouse hovering buttons

        // rooms button
        viewRooms.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> imgRooms.setImage(new Image("file:assets/img/ui_dev_pack/main_menu/hover_button_rooms.png")));
        viewRooms.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> imgRooms.setImage(new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_rooms.png")));

        // customers button
        viewCustomers.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> imgCustomers
                .setImage(new Image("file:assets/img/ui_dev_pack/main_menu/hover_button_customers.png")));
        viewCustomers.addEventHandler(MouseEvent.MOUSE_EXITED, e -> imgCustomers
                .setImage(new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_customers.png")));

        // bookings button
        viewBookings.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> imgBookings
                .setImage(new Image("file:assets/img/ui_dev_pack/main_menu/hover_button_bookings.png")));
        viewBookings.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> imgBookings.setImage(new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_bookings.png")));

        // users button
        viewUsers.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> imgUsers.setImage(new Image("file:assets/img/ui_dev_pack/main_menu/hover_button_users.png")));
        viewUsers.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> imgUsers.setImage(new Image("file:assets/img/ui_dev_pack/main_menu/idle_button_users.png")));

        return pane;
    }

    /***************************** Getters *********************************/

    public Button getMyPageButton() {
        return myPage;
    }

    public Button getViewBookingsButton() {
        return viewBookings;
    }

    public Button getViewCustomers() {
        return viewCustomers;
    }

    public Button getViewUsersButton() {
        return viewUsers;
    }

    public Button getViewRooms() {
        return viewRooms;
    }
}