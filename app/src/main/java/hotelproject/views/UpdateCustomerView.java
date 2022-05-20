package hotelproject.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.regex.Pattern;

/**
 * Create update customer information view.
 */
public class UpdateCustomerView extends View {

    final String IDLE_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
    final String HOVER_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
    final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
    final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";
    private final TextField cSSNum = new TextField();
    private final TextField cAddress = new TextField();
    private final TextField cFullName = new TextField();
    private final TextField cPhoneNum = new TextField();
    private final TextField cEmail = new TextField();
    final private Button submit = createButton(35, IDLE_BUTTON_SUBMIT, HOVER_BUTTON_SUBMIT);
    final private Button cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);

    /**
     * Constructor for updating customer view.
     */
    public UpdateCustomerView() {
        createScene();
    }

    /**
     * Create the scene for updating customer view.
     * Including social security number, address, full name, telephone number, and email.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        VBox header = createHeader("Update customer", "Enter customer specifics to update");

        Label cSSNumL = changeLabelDesign(new Label("New social security number (8 digits): "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(cSSNumL, 0, 1);
        pane.add(cSSNum, 1, 1);

        // force the field to be numeric only
        cSSNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|[1-9][0-9]{1,7}")) {
                Platform.runLater(cSSNum::clear);
            }

            // If there numbers and no violations (characters, signs or such), limit to 8 only
            Pattern pattern = Pattern.compile(".{0,8}");
            TextFormatter<String> formatter = new TextFormatter<>(change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
            cSSNum.setTextFormatter(formatter);

        });


        Label cAddressL = changeLabelDesign(new Label("New address: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(cAddressL, 0, 2);
        pane.add(cAddress, 1, 2);

        // force the field to be strings only
        cAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$")) {
                Alert a = new Alert(AlertType.INFORMATION);
                a.setContentText("Address has to have letters and numbers (Space included)!");
                a.showAndWait();
                Platform.runLater(cAddress::clear);
            }
        });

        Label cFullNameL = changeLabelDesign(new Label("New name: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(cFullNameL, 0, 3);
        pane.add(cFullName, 1, 3);

        // force the field to be numeric only
        cFullName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[\\p{L} .'-]+$")) {
                Alert a = new Alert(AlertType.INFORMATION);
                a.setContentText("Name has to have letters (space included)!");
                a.showAndWait();
                Platform.runLater(cFullName::clear);
            }
        });

        Label cPhoneNumL = changeLabelDesign(new Label("New phone number (9 digits): "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(cPhoneNumL, 0, 4);
        pane.add(cPhoneNum, 1, 4);

        // force the field to be numeric only
        cPhoneNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|[1-9][0-9]{1,8}")) {
                Platform.runLater(cPhoneNum::clear);
            }

            // If there numbers and no violations (characters, signs or such), limit to 8
            Pattern pattern = Pattern.compile(".{0,9}");
            TextFormatter<String> formatter = new TextFormatter<>(change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
            cPhoneNum.setTextFormatter(formatter);
        });

        Label cEmailL = changeLabelDesign(new Label("New email: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(cEmailL, 0, 5);
        pane.add(cEmail, 1, 5);

        pane.setVgap(15);

        GridPane paneTwo = new GridPane();
        paneTwo.add(header, 0, 0);
        paneTwo.add(pane, 0, 1);
        VBox footer = createFooter(submit, cancel);
        paneTwo.add(footer, 0, 2);
        paneTwo.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        paneTwo.setVgap(15);

        GridPane.setHalignment(header, javafx.geometry.HPos.CENTER);
        paneTwo.getStyleClass().add("body-pane");

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

    /*****************************Getters*********************************/
    public Button getSubmit() {
        return submit;
    }

    public Button getCancel() {
        return cancel;
    }

    public TextField getCSSNum() {
        return cSSNum;
    }

    public TextField getCAddress() {
        return cAddress;
    }

    public TextField getCFullName() {
        return cFullName;
    }

    public TextField getCPhoneNum() {
        return cPhoneNum;
    }

    public TextField getCEmail() {
        return cEmail;
    }

}
