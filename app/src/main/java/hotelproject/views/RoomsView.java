package hotelproject.views;

import hotelproject.controllers.db.HotelData;
import hotelproject.controllers.objects.Room;
import hotelproject.controllers.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Create rooms view based on abstract class view.
 * This view presents detailed information of a room.
 */
public class RoomsView extends View {

    // The scene's nodes

    // The user connected to the application
    private final User user;
    private final HotelData hdata;
    // Observable list with all the hotel's rooms
    private final ObservableList<Room> rooms;
    private final TableView<Room> roomsTable = new TableView<>();
    private final DatePicker startDatePicker = new DatePicker();
    private final DatePicker endDatePicker = new DatePicker();
    private Button addRoom;

    /**
     * To initiate all rooms' data in the view.
     *
     * @param user  the user with or without 'admin' identity has different view.
     * @param rooms the rooms saved in list.
     * @param hdata this instance to encapsulate all related operations for the database of the hotel.
     */
    public RoomsView(User user, List<Room> rooms, HotelData hdata) {
        this.user = user;
        this.rooms = FXCollections.observableList(rooms);
        this.hdata = hdata;
        createScene();
    }

    /**
     * Create scene for rooms view.
     */
    @Override
    void createScene() {
        GridPane bodyPane = createBody();
        GridPane.setHalignment(bodyPane, HPos.CENTER);

        bodyPane.getStyleClass().add("body-pane");
        roomsTable.getStyleClass().add("table-view");

        scene = new Scene(bodyPane);
        scene.getStylesheets().add("file:assets/css/Stylesheet.css");

        // Disable horizontal scroll bar in TableView
        scene.getStylesheets().add("file:assets/css/JavaFx2HideTableViewHScrollBar.css");
    }

    /**
     * Rooms view body contain all rooms in the database and present in a table.
     *
     * @return an instance of Grid pane.
     */
    @Override
    GridPane createBody() {
        final String IDLE_ADD_ROOM = "file:assets/img/ui_dev_pack/room_menu/idle_button_new_room.png";
        final String HOVER_ADD_ROOM = "file:assets/img/ui_dev_pack/room_menu/hover_button_new_room.png";

        GridPane pane = createPane();

        Label title = new Label("Hotel rooms");
        title.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 25));
        title.setStyle("-fx-font-weight: bold;");
        title.setTextFill(Paint.valueOf("bb86fc"));

        roomsTable.setEditable(true);
        // Create column in the table
        TableColumn<Room, Integer> roomNbCol = new TableColumn<>("Room number");
        roomNbCol.setPrefWidth(120.0F);
        roomNbCol.setCellValueFactory(new PropertyValueFactory<>("r_num"));

        TableColumn<Room, Integer> roomFloorCol = new TableColumn<>("Floor");
        roomFloorCol.setPrefWidth(100.0F);
        roomFloorCol.setCellValueFactory(new PropertyValueFactory<>("r_floor"));

        TableColumn<Room, String> roomTypeCol = new TableColumn<>("Room type");
        roomTypeCol.setPrefWidth(100.0F);
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("r_type"));


        // Create a filtered list to put the rooms as items in the table
        FilteredList<Room> flRoom = new FilteredList<>(rooms, p -> true);
        roomsTable.setItems(flRoom);
        roomsTable.getColumns().addAll(roomNbCol, roomFloorCol, roomTypeCol);

        // Create choice box so the user can choose on the column he's searching in
        ChoiceBox<String> whatToSearch = new ChoiceBox<>();
        whatToSearch.getItems().addAll("Room number", "Floor", "Room type");
        whatToSearch.setValue("Room number"); // default search
        whatToSearch.setStyle("-fx-pref-width: 183;");

        // Create search bar with listener to update according to the user's input
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here");
        searchBar.setStyle("-fx-pref-width: 183;");
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (whatToSearch.getValue().equals("Room number")) {
                flRoom.setPredicate(p -> String.valueOf(p.getR_num()).contains(newValue.toLowerCase().trim()));
            } else if (whatToSearch.getValue().equals("Floor")) {
                flRoom.setPredicate(p -> String.valueOf(p.getR_floor()).contains(newValue.toLowerCase().trim()));
            } else if (whatToSearch.getValue().equals("Room type")) {
                flRoom.setPredicate(p -> p.getR_type().toLowerCase().contains(newValue.toLowerCase().trim()));
            }
        });

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(startDatePicker.getValue())) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };
            endDatePicker.setDayCellFactory(dayCellFactory);
        });

        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (startDatePicker.getValue() != null && startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
                startDatePicker.setValue(endDatePicker.getValue());
            }
            if (startDatePicker.getValue() != null) {
                flRoom.setPredicate(item -> {
                    // If filter text is empty, display all items.
                    LocalDate leftEndpoint = startDatePicker.getValue();
                    String firstFormattedDate = leftEndpoint.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Date datePicked = java.sql.Date.valueOf(firstFormattedDate);

                    LocalDate rightEndpoint = endDatePicker.getValue();
                    String secondFormattedDate = rightEndpoint.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Date secondDatePicked = java.sql.Date.valueOf(secondFormattedDate);

                    ArrayList<Integer> availableRooms = hdata.availableRooms(datePicked, secondDatePicked);

                    for (int rNum : availableRooms) {
                        if (rNum == item.getR_num()) {
                            return true;
                        }
                    }
                    return false;
                });
            }
        });

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (endDatePicker.getValue() != null && endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
                endDatePicker.setValue(startDatePicker.getValue());
            }
            if (endDatePicker.getValue() != null) {
                flRoom.setPredicate(item -> {
                    // If filter text is empty, display all items.
                    LocalDate leftEndpoint = startDatePicker.getValue();
                    String firstFormattedDate = leftEndpoint.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Date datePicked = java.sql.Date.valueOf(firstFormattedDate);

                    LocalDate rightEndpoint = endDatePicker.getValue();
                    String secondFormattedDate = rightEndpoint.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Date secondDatePicked = java.sql.Date.valueOf(secondFormattedDate);

                    ArrayList<Integer> availableRooms = hdata.availableRooms(datePicked, secondDatePicked);

                    for (int rNum : availableRooms) {
                        if (rNum == item.getR_num()) {
                            return true;
                        }
                    }
                    return false;
                });
            }
        });

        // When the new choice is selected we reset
        whatToSearch.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                searchBar.setText("");
            }
        });

        HBox search = new HBox(whatToSearch, searchBar);
        search.setAlignment(Pos.CENTER);
        search.getStyleClass().add("search");

        pane.add(title, 0, 0);
        GridPane.setHalignment(title, HPos.CENTER);
        pane.add(search, 0, 2);

        HBox dateRange = new HBox(startDatePicker, endDatePicker);
        dateRange.setAlignment(Pos.CENTER);
        dateRange.getStyleClass().add("date-range");
        pane.add(dateRange, 0, 3);
        pane.add(roomsTable, 0, 4);

        addRoom = createButton(35, IDLE_ADD_ROOM, HOVER_ADD_ROOM);
        addRoom.setVisible(false);

        if (user.getU_is_admin() == 1) {
            pane.add(addRoom, 0, 5);
            addRoom.setVisible(true);
            GridPane.setHalignment(addRoom, javafx.geometry.HPos.CENTER);
        }

        return pane;
    }

    /************************** Getter **********************/

    public Button getAddRoom() {
        return addRoom;
    }

    public TableView<Room> getRoomsTable() {
        return roomsTable;
    }
}
