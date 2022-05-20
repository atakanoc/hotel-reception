package hotelproject.views;

import hotelproject.controllers.db.DatabaseManager;
import hotelproject.controllers.db.HotelData;
import hotelproject.controllers.objects.Customer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create update booking information view.
 */
public class UpdateBookingView extends View {

    final String IDLE_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
    final String HOVER_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
    final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
    final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";
    private final DatabaseManager dbm;
    private final HotelData hdata;
    private final ComboBox<Integer> newRoom = new ComboBox<>();
    private final CheckBox newPayment = new CheckBox("Paid by card?");
    private final DatePicker newCheckIn = new DatePicker();
    private final DatePicker newCheckOut = new DatePicker();
    private final TextField newFee = new TextField();
    private final CheckBox newIsPaid = new CheckBox("Is paid?");
    private final ComboBox<String> customer = new ComboBox<>();
    private final Button submit = createButton(35, IDLE_BUTTON_SUBMIT, HOVER_BUTTON_SUBMIT);
    private final Button cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);
    List<Integer> availableRooms;

    /**
     * Constructor for this view.
     *
     * @param dbm   the instance of DatabaseManager for creating connection.
     * @param hdata the instance of the actual database.
     */
    public UpdateBookingView(DatabaseManager dbm, HotelData hdata) {
        this.dbm = dbm;
        this.hdata = hdata;
        createScene();
    }

    /**
     * Create scene for update booking view.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        VBox header = createHeader("Update booking", "Enter booking specifics to update");

        pane.add(newCheckIn, 0, 1);
        pane.add(newCheckOut, 1, 1);

        Label newRoomL = changeLabelDesign(new Label("New room number: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(newRoomL, 0, 2);
        pane.add(newRoom, 1, 2);

        Label newFeeL = changeLabelDesign(new Label("New fee: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(newFeeL, 0, 3);
        pane.add(newFee, 1, 3);

        Label bookingCSSL = changeLabelDesign(new Label("New customer number: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(bookingCSSL, 0, 4);

        List<Customer> customers = dbm.cdb.findAllCustomers();
        for (Customer value : customers) {
            customer.getItems().add(value.getC_full_name() + " - " + value.getC_ss_number());
        }

        pane.add(customer, 1, 4);

        newPayment.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        newPayment.setTextFill(Paint.valueOf("white"));
        pane.add(newPayment, 0, 5);

        newIsPaid.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        newIsPaid.setTextFill(Paint.valueOf("white"));
        pane.add(newIsPaid, 0, 6);

        newCheckIn.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newCheckOut.getValue() != null && newCheckOut.getValue().isBefore(newCheckIn.getValue())) {
                newCheckOut.setValue(newCheckIn.getValue());
            }
            if (newCheckOut.getValue() != null) {
                LocalDate leftEndpoint = newCheckIn.getValue();
                LocalDate rightEndpoint = newCheckOut.getValue();

                Date datePicked = Date.valueOf(leftEndpoint);
                Date secondDatePicked = Date.valueOf(rightEndpoint);

                availableRooms = hdata.availableRooms(datePicked, secondDatePicked).stream().sorted().distinct().collect(Collectors.toList());
                newRoom.getSelectionModel().clearSelection();
                newRoom.getItems().clear();
                availableRooms.forEach(value -> newRoom.getItems().add(value));
            }
        });
        newCheckOut.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newCheckIn.getValue() != null && newCheckIn.getValue().isAfter(newCheckOut.getValue())) {
                newCheckIn.setValue(newCheckOut.getValue());
            }
            if (newCheckOut.getValue() != null) {
                LocalDate leftEndpoint = newCheckIn.getValue();
                LocalDate rightEndpoint = newCheckOut.getValue();

                Date datePicked = Date.valueOf(leftEndpoint);
                Date secondDatePicked = Date.valueOf(rightEndpoint);

                availableRooms = hdata.availableRooms(datePicked, secondDatePicked).stream().sorted().distinct().collect(Collectors.toList());
                newRoom.getSelectionModel().clearSelection();
                newRoom.getItems().clear();
                availableRooms.forEach(value -> newRoom.getItems().add(value));
            }
        });

        newCheckIn.valueProperty().addListener((observable, oldValue, newValue) -> {
            final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(newCheckIn.getValue())) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };
            newCheckOut.setDayCellFactory(dayCellFactory);
        });

        pane.setVgap(15);

        GridPane paneTwo = new GridPane();
        paneTwo.add(header, 0, 0);
        paneTwo.add(pane, 0, 1);
        VBox footer = createFooter(submit, cancel);
        paneTwo.add(footer, 0, 2);

        GridPane.setHalignment(header, javafx.geometry.HPos.CENTER);
        paneTwo.getStyleClass().add("body-pane");
        paneTwo.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        paneTwo.setVgap(15);

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

    /*****************************Getters*********************************/
    public Button getSubmit() {
        return submit;
    }

    public Button getCancel() {
        return cancel;
    }

    public ComboBox<Integer> getNewRoom() {
        return newRoom;
    }

    public CheckBox getNewPayment() {
        return newPayment;
    }

    public DatePicker getNewFromDate() {
        return newCheckIn;
    }

    public DatePicker getNewTillDate() {
        return newCheckOut;
    }

    public TextField getNewFee() {
        return newFee;
    }

    public CheckBox getNewIsPaid() {
        return newIsPaid;
    }

    public ComboBox<String> getCustomer() {
        return customer;
    }
}
