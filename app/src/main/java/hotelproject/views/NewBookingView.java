package hotelproject.views;

import hotelproject.controllers.db.DatabaseManager;
import hotelproject.controllers.db.HotelData;
import hotelproject.controllers.objects.Customer;
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
 * This view aims for adding a new booking to the database.
 * All users have access to this view.
 * User can add a new booking by fulfilling all the selections.
 */
public class NewBookingView extends View {

    final String IDLE_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
    final String HOVER_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
    final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
    final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";
    private final DatabaseManager dbm;
    private final HotelData hdata;
    private final ComboBox<Integer> numRoom = new ComboBox<>();
    private final CheckBox paidByCard = new CheckBox("Paid by card?");
    private final DatePicker checkIn = new DatePicker();
    private final DatePicker checkOut = new DatePicker();
    private final TextField bookingFee = new TextField();
    private final ComboBox<String> customer = new ComboBox<>();
    private final CheckBox isPaid = new CheckBox("Is paid?");
    private final Button submit = createButton(35, IDLE_BUTTON_SUBMIT, HOVER_BUTTON_SUBMIT);
    private final Button cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);
    List<Integer> availableRooms;

    /**
     * Constructor  for initiating related data to a new booking view.
     *
     * @param dbm   create connection with database.
     * @param hdata this instance to encapsulate all related operations for the database of the hotel.
     */
    public NewBookingView(DatabaseManager dbm, HotelData hdata) {
        this.dbm = dbm;
        this.hdata = hdata;
        createScene();
    }

    /**
     * Create scene for adding a new booking.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        VBox header = createHeader("New booking", "Enter the new booking specifications");
        GridPane bodyPane = createBody();
        VBox footer = createFooter(submit, cancel);

        pane.add(header, 0, 0);
        pane.add(bodyPane, 0, 1);
        pane.add(footer, 0, 2);
        GridPane.setHalignment(header, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(footer, javafx.geometry.HPos.CENTER);
        pane.getStyleClass().add("body-pane");

        scene = new Scene(pane);
        scene.getStylesheets().add("file:assets/css/Stylesheet.css");
    }

    /**
     * Create body pane to contain new booking related information.
     *
     * @return an instance of Grid pane.
     */
    @Override
    GridPane createBody() {
        GridPane pane = createPane();

        checkIn.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (checkOut.getValue() != null && checkOut.getValue().isBefore(checkIn.getValue())) {
                checkOut.setValue(checkIn.getValue());
            }
            if (checkOut.getValue() != null) {
                LocalDate leftEndpoint = checkIn.getValue();
                LocalDate rightEndpoint = checkOut.getValue();

                Date datePicked = Date.valueOf(leftEndpoint);
                Date secondDatePicked = Date.valueOf(rightEndpoint);

                availableRooms = hdata.availableRooms(datePicked, secondDatePicked).stream().sorted().distinct().collect(Collectors.toList());
                numRoom.getSelectionModel().clearSelection();
                numRoom.getItems().clear();
                availableRooms.forEach(value -> numRoom.getItems().add(value));
            }
        });
        checkOut.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (checkIn.getValue() != null && checkIn.getValue().isAfter(checkOut.getValue())) {
                checkIn.setValue(checkOut.getValue());
            }
            if (checkIn.getValue() != null) {
                LocalDate leftEndpoint = checkIn.getValue();
                LocalDate rightEndpoint = checkOut.getValue();

                Date datePicked = Date.valueOf(leftEndpoint);
                Date secondDatePicked = Date.valueOf(rightEndpoint);

                availableRooms = hdata.availableRooms(datePicked, secondDatePicked).stream().sorted().distinct().collect(Collectors.toList());
                numRoom.getSelectionModel().clearSelection();
                numRoom.getItems().clear();
                availableRooms.forEach(value -> numRoom.getItems().add(value));
            }
        });

        checkIn.valueProperty().addListener((observable, oldValue, newValue) -> {
            final Callback<DatePicker, DateCell> dayCellFactory =
                    new Callback<>() {
                        @Override
                        public DateCell call(final DatePicker datePicker) {
                            return new DateCell() {
                                @Override
                                public void updateItem(LocalDate item, boolean empty) {
                                    super.updateItem(item, empty);

                                    if (item.isBefore(
                                            checkIn.getValue())
                                    ) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                }
                            };
                        }
                    };
            checkOut.setDayCellFactory(dayCellFactory);
        });

        pane.add(checkIn, 0, 1);
        pane.add(checkOut, 1, 1);

        Label numRoomL = changeLabelDesign(new Label("Room number: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(numRoomL, 0, 2);
        pane.add(numRoom, 1, 2);

        Label bookingFeeL = changeLabelDesign(new Label("Booking fee: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(bookingFeeL, 0, 3);
        pane.add(bookingFee, 1, 3);

        Label bookingCSSL = changeLabelDesign(new Label("Customer number: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(bookingCSSL, 0, 4);

        List<Customer> customers = dbm.cdb.findAllCustomers();
        for (Customer value : customers) {
            customer.getItems().add(value.getC_full_name() + " - " + value.getC_ss_number());
        }

        pane.add(customer, 1, 4);

        paidByCard.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        paidByCard.setTextFill(Paint.valueOf("white"));
        pane.add(paidByCard, 0, 5);

        isPaid.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        isPaid.setTextFill(Paint.valueOf("white"));
        pane.add(isPaid, 0, 6);

        pane.setVgap(15);
        return pane;
    }

    /************************** Getter **********************/
    public Button getSubmit() {
        return submit;
    }

    public ComboBox<Integer> getNumRoom() {
        return numRoom;
    }

    public CheckBox getPaidByCard() {
        return paidByCard;
    }

    public DatePicker getCheckIn() {
        return checkIn;
    }

    public DatePicker getCheckOut() {
        return checkOut;
    }

    public TextField getBookingFee() {
        return bookingFee;
    }

    public CheckBox getIsPaid() {
        return isPaid;
    }

    public Button getCancel() {
        return cancel;
    }

    public ComboBox<String> getCustomer() {
        return customer;
    }
}