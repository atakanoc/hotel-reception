package hotelproject.views;


import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * This view is used to add a new room type for a typical room.
 * This view will present when users add a new room in the database.
 */
public class AddRoomTypeView extends View {

    // The scene's nodes
    private final CheckBox hasView = new CheckBox("with view");
    private final CheckBox hasKitchen = new CheckBox("with kitchen");
    private final CheckBox hasBathroom = new CheckBox("with bathroom");
    private final CheckBox hasWorksp = new CheckBox("with a workspace");
    private final CheckBox hasTv = new CheckBox("with TV");
    private final CheckBox hasCoffeeMkr = new CheckBox("with a coffee maker");
    private final TextField name = new TextField();
    private final TextField nbBeds = new TextField();
    private final TextField roomSize = new TextField();

    private final String IDLE_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
    private final String HOVER_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
    private final Button submit = createButton(35, IDLE_BUTTON_SUBMIT, HOVER_BUTTON_SUBMIT);

    private final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
    private final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";
    private final Button cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);

    /**
     * No parameter for this constructor.
     */
    public AddRoomTypeView() {
        createScene();
    }

    /**
     * This scene includes type name, number of beds, room size, with view, with kitchen, with bathroom, with a workplace, with TV, and with Coffee maker.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        //labels and text fields
        VBox header = createHeader("New room type", "Please enter the new room type parameters");

        Label nameL = changeLabelDesign(new Label("Type name: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(nameL, 0, 2);
        pane.add(name, 1, 2);

        name.setPromptText("Alphabetical letters only"); // to set the hint text
        name.getParent().requestFocus();

        // force the field to be numeric only
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                Platform.runLater(name::clear);
            }
        });

        Label nbBedsL = changeLabelDesign(new Label("Number of beds: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(nbBedsL, 0, 3);
        pane.add(nbBeds, 1, 3);

        nbBeds.setPromptText("1-10"); // to set the hint text
        nbBeds.getParent().requestFocus();

        // force the field to be numeric only
        nbBeds.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|10")) {
                Platform.runLater(nbBeds::clear);
            }
        });

        Label roomSizeL = changeLabelDesign(new Label("Room size: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(roomSizeL, 0, 4);
        pane.add(roomSize, 1, 4);

        roomSize.setPromptText("Digits up to 300 only"); // to set the hint text
        roomSize.getParent().requestFocus();

        // force the field to be numeric only
        roomSize.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-9]|[1-9][0-9]|[12][0-9]{2}|300")) {
                Platform.runLater(roomSize::clear);
            }
        });

        Label sizeUnit = changeLabelDesign(new Label("m\u00b2"), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(sizeUnit, 2, 4);

        //checkboxes
        hasView.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        hasView.setTextFill(Paint.valueOf("white"));
        pane.add(hasView, 0, 5);

        hasKitchen.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        hasKitchen.setTextFill(Paint.valueOf("white"));
        pane.add(hasKitchen, 0, 6);

        hasBathroom.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        hasBathroom.setTextFill(Paint.valueOf("white"));
        pane.add(hasBathroom, 0, 7);

        hasWorksp.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        hasWorksp.setTextFill(Paint.valueOf("white"));
        pane.add(hasWorksp, 0, 8);

        hasTv.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        hasTv.setTextFill(Paint.valueOf("white"));
        pane.add(hasTv, 0, 9);

        hasCoffeeMkr.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        hasCoffeeMkr.setTextFill(Paint.valueOf("white"));
        pane.add(hasCoffeeMkr, 0, 10);

        GridPane mainPain = createPane();
        mainPain.add(header, 0, 0);
        pane.setVgap(15);
        mainPain.add(pane, 0, 1);
        mainPain.add(submit, 0, 2);
        mainPain.add(cancel, 0, 3);
        GridPane.setHalignment(submit, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(cancel, javafx.geometry.HPos.CENTER);
        scene = new Scene(mainPain);
        mainPain.getStyleClass().add("body-pane");
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

    public int getHasBathroom() {
        int isSelected = 0;
        if (hasBathroom.isSelected()) {
            isSelected = 1;
        }
        return isSelected;
    }

    public int getHasCoffeeMkr() {
        int isSelected = 0;
        if (hasCoffeeMkr.isSelected()) {
            isSelected = 1;
        }
        return isSelected;
    }

    public int getHasKitchen() {
        int isSelected = 0;
        if (hasKitchen.isSelected()) {
            isSelected = 1;
        }
        return isSelected;
    }

    public int getHasTv() {
        int isSelected = 0;
        if (hasTv.isSelected()) {
            isSelected = 1;
        }
        return isSelected;
    }

    public TextField getName() {
        return name;
    }

    public int getHasView() {
        int isSelected = 0;
        if (hasView.isSelected()) {
            isSelected = 1;
        }
        return isSelected;
    }

    public int getHasWorksp() {
        int isSelected = 0;
        if (hasWorksp.isSelected()) {
            isSelected = 1;
        }
        return isSelected;
    }

    public TextField getNbBeds() {
        return nbBeds;
    }

    public TextField getRoomSize() {
        return roomSize;
    }

}
