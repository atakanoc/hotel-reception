package hotelproject.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Abstract class all views will extend to prevent code redundancy.
 */
public abstract class View {

    // the view scene
    Scene scene;

    /**
     * Create the view scene with all it's nodes.
     */
    abstract void createScene();

    /**
     * Create the scene's header, called in createScene.
     *
     * @param title    of the header.
     * @param subtitle of the header.
     * @return the header in the format VBox.
     */
    protected VBox createHeader(String title, String subtitle) { //use createHeader in every view
        Label titleL = new Label(title);
        titleL.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 30));
        titleL.setStyle("-fx-font-weight: bold;");
        titleL.setTextFill(Paint.valueOf("bb86fc"));

        Label subtitleL = new Label(subtitle);
        subtitleL.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 21));
        subtitleL.setTextFill(Paint.valueOf("silver"));

        VBox header = new VBox(titleL, subtitleL);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        header.setSpacing(5.5);

        return header;
    }

    abstract GridPane createBody();

    /**
     * Create the initial GridPane.
     *
     * @return the initial GridPane.
     */
    protected GridPane createPane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.4));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        return pane;
    }

    /**
     * VBox for containing submit and cancel buttons.
     *
     * @param submit as a Button.
     * @param cancel as a Button.
     * @return an instance of VBox.
     */
    protected VBox createFooter(Button submit, Button cancel) {
        VBox footer = new VBox(submit, cancel);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        footer.setSpacing(15);

        return footer;
    }

    /**
     * Create a button with the image associated.
     *
     * @param buttonImgView path of the image associated to the button.
     * @param button        the button concerned.
     * @param minWidth      minimum width of the button.
     * @param minHeight     minimum height of the button.
     * @return a StackPane with the result to add to the scene.
     */
    protected StackPane createButton(ImageView buttonImgView, Button button, double minWidth, double minHeight) {
        StackPane stack = new StackPane();

        button.setStyle("-fx-opacity: 0;");
        button.setMinWidth(minWidth);
        button.setMaxHeight(minHeight);

        buttonImgView.setPreserveRatio(true);

        stack.getChildren().addAll(buttonImgView, button);
        stack.setAlignment(Pos.CENTER);
        return stack;
    }

    /**
     * Create a button with the the height of button, the paths of the idle image and  hover image.
     *
     * @param fitHeight the height of the button.
     * @param idleUrl   the path of the idle image.
     * @param hoverUrl  the path of the hover image.
     * @return a Button object.
     */
    protected Button createButton(double fitHeight, String idleUrl, String hoverUrl) {
        Button button = new Button();
        ImageView imgView = new ImageView(new Image(idleUrl));

        button.setGraphic(imgView);
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
        imgView.setFitHeight(fitHeight);
        imgView.setPreserveRatio(true);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> imgView.setImage(new Image(hoverUrl)));
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> imgView.setImage(new Image(idleUrl)));

        return button;
    }

    /**
     * Change a label design based on original label object, new font, size and color.
     *
     * @param label the original label object.
     * @param font  the font for the new label.
     * @param size  the size for the new label.
     * @param color the color for the new label.
     * @return a Label object.
     */
    protected Label changeLabelDesign(Label label, String font, int size, String color) {
        label.setFont(Font.loadFont(font, size));
        label.setTextFill(Paint.valueOf(color));

        return label;
    }

    /**
     * getter for Scene object.
     */
    public Scene getScene() {
        return scene;
    }
}
