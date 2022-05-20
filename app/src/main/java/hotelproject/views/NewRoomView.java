package hotelproject.views;

import hotelproject.controllers.db.HotelData;
import hotelproject.controllers.objects.RoomType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * This view aims to add a new room to the database.
 * All users have access to this view.
 */
public class NewRoomView extends View {

    final String IDLE_BUTTON_ADD_ROOM = "file:assets/img/ui_dev_pack/room_menu/idle_button_add_type.png";
    final String HOVER_BUTTON_ADD_ROOM = "file:assets/img/ui_dev_pack/room_menu/hover_button_add_type.png";
    final String IDLE_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
    final String HOVER_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
    final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
    final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";
    private final HotelData hdata;
    private final TextField numRoom = new TextField();
    private final TextField floor = new TextField();
    private final ComboBox<String> roomType = new ComboBox<>();
    private final Button submit = createButton(35, IDLE_BUTTON_SUBMIT, HOVER_BUTTON_SUBMIT);
    private final Button cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);
    private Button addRoomType;

    /**
     * Constructor for this view.
     *
     * @param hdata this instance to encapsulate all related operations for the database of the hotel.
     */
    public NewRoomView(HotelData hdata) {
        this.hdata = hdata;
        createScene();
    }

    /**
     * Create scene for adding a new room.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        VBox header = createHeader("New room", "Enter the new room specifications");

        Label numRoomL = changeLabelDesign(new Label("Room number: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(numRoomL, 0, 2);
        pane.add(numRoom, 1, 2);

        numRoom.setPromptText("1-256"); // to set the hint text
        numRoom.getParent().requestFocus();

        // force the field to be numeric only
        numRoom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]")) {
                Platform.runLater(numRoom::clear);
            }
        });

        Label floorL = changeLabelDesign(new Label("Floor: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(floorL, 0, 3);
        pane.add(floor, 1, 3);

        floor.setPromptText("1-10"); // to set the hint text
        floor.getParent().requestFocus();

        // force the field to be numeric only
        floor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|10")) {
                Platform.runLater(floor::clear);
            }
        });

        Label type = changeLabelDesign(new Label("Room type: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(type, 0, 4);

        List<RoomType> roomTypes = hdata.getRoomTypes();
        for (RoomType value : roomTypes) {
            roomType.getItems().add(value.getT_name());
        }
        roomType.setValue("Single");

        pane.add(roomType, 1, 4);

        addRoomType = createButton(25, IDLE_BUTTON_ADD_ROOM, HOVER_BUTTON_ADD_ROOM);
        pane.add(addRoomType, 2, 4);

        VBox footer = createFooter(submit, cancel);
        GridPane paneTwo = new GridPane();

        paneTwo.add(header, 0, 0);
        GridPane.setHalignment(header, javafx.geometry.HPos.CENTER);

        pane.setVgap(15);
        paneTwo.add(pane, 0, 1);

        paneTwo.add(footer, 0, 2);

        paneTwo.getStyleClass().add("body-pane");

        scene = new Scene(paneTwo);
        scene.getStylesheets().add("file:assets/css/Stylesheet.css");
    }

    /**
     * No body pane need to be created in this view.
     *
     * @return null
     */
    @Override
    GridPane createBody() {
        return null;
    }

    /************************** Getter **********************/
    public Button getSubmit() {
        return submit;
    }

    public Button getCancel() {
        return cancel;
    }

    public TextField getFloor() {
        return floor;
    }

    public TextField getNumRoom() {
        return numRoom;
    }

    public ComboBox<String> getRoomType() {
        return roomType;
    }

    public Button getAddRoomType() {
        return addRoomType;
    }

}