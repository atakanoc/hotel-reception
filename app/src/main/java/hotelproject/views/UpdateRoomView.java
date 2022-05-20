package hotelproject.views;

import hotelproject.controllers.db.HotelData;
import hotelproject.controllers.objects.RoomType;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Create an update room view based on abstract class view.
 */
public class UpdateRoomView extends View {

    final String IDLE_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
    final String HOVER_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
    final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
    final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";
    private final HotelData hdata;
    private final TextField numRoom = new TextField();
    private final TextField floor = new TextField();
    private final ComboBox<String> roomType = new ComboBox<>();
    final private Button submit = createButton(35, IDLE_BUTTON_SUBMIT, HOVER_BUTTON_SUBMIT);
    final private Button cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);

    /**
     * Constructor for update room view.
     *
     * @param hdata the original data of the room.
     */
    public UpdateRoomView(HotelData hdata) {
        this.hdata = hdata;
        createScene();
    }

    /**
     * Create the scene for the update room view.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        VBox header = createHeader("Update room", "Enter room specifics to update");

        Label numRoomL = changeLabelDesign(new Label("New room number: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(numRoomL, 0, 1);
        pane.add(numRoom, 1, 1);

        numRoom.setPromptText("1-256"); // to set the hint text
        numRoom.getParent().requestFocus();

        // force the field to be numeric only
        numRoom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]")) {
                Platform.runLater(numRoom::clear);
            }
        });

        Label floorL = changeLabelDesign(new Label("New floor: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(floorL, 0, 2);
        pane.add(floor, 1, 2);

        floor.setPromptText("1-10"); // to set the hint text
        floor.getParent().requestFocus();

        // force the field to be numeric only
        floor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|10")) {
                Platform.runLater(floor::clear);
            }
        });

        Label type = changeLabelDesign(new Label("New room type: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(type, 0, 3);

        List<RoomType> roomTypes = hdata.getRoomTypes();
        for (RoomType value : roomTypes) {
            roomType.getItems().add(value.getT_name());
        }
        roomType.setValue("Single");

        pane.add(roomType, 1, 3);

        pane.setVgap(15);

        GridPane paneTwo = new GridPane();
        paneTwo.add(header, 0, 0);
        paneTwo.add(pane, 0, 1);
        VBox footer = createFooter(submit, cancel);
        paneTwo.add(footer, 0, 2);

        GridPane.setHalignment(header, HPos.CENTER);
        paneTwo.getStyleClass().add("body-pane");
        paneTwo.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        paneTwo.setVgap(15);

        scene = new Scene(paneTwo);
        scene.getStylesheets().add("file:assets/css/Stylesheet.css");
    }

    /**
     * No body need to be created in this view.
     *
     * @return null
     */
    @Override
    GridPane createBody() {
        return null;
    }

    /**************************Getter**********************/

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

}
