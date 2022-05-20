package hotelproject.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
 * Create new user view based on abstract class view.
 * Only user with 'admin' authority can see this view.
 */
public class NewUserView extends View {

    private final TextField userName = new TextField();
    private final TextField userPassword = new TextField();
    private final CheckBox userIA = new CheckBox("Is admin?");
    private Button submit;
    private Button cancel;

    /**
     * No parameter is needed for this constructor.
     */
    public NewUserView() {
        createScene();
    }

    /**
     * This scene includes username, password text fields and submit and cancel buttons.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        VBox header = createHeader("New user", "Enter the new user details");

        Label userNameL = changeLabelDesign(new Label("Username: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(userNameL, 0, 2);
        pane.add(userName, 1, 2);
        Label userPassL = changeLabelDesign(new Label("Password: "), "file:assets/font/SF_Pro.ttf", 17, "white");
        pane.add(userPassL, 0, 3);
        pane.add(userPassword, 1, 3);
        userIA.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        userIA.setTextFill(Paint.valueOf("white"));
        pane.add(userIA, 0, 4);

        GridPane paneTwo = new GridPane();

        paneTwo.add(header, 0, 0);
        GridPane.setHalignment(header, javafx.geometry.HPos.CENTER);

        pane.setVgap(15);
        paneTwo.add(pane, 0, 1);

        VBox footer = createFooter();
        paneTwo.add(footer, 0, 2);

        pane.getStyleClass().add("body-pane");
        paneTwo.getStyleClass().add("body-pane");

        scene = new Scene(paneTwo);
        scene.getStylesheets().add("file:assets/css/Stylesheet.css");
    }

    /**
     * Create the instance of VBox for containing submit and cancel buttons.
     *
     * @return an instance of VBox.
     */
    private VBox createFooter() {
        final String IDLE_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
        final String HOVER_BUTTON_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
        final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
        final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";

        submit = createButton(35, IDLE_BUTTON_SUBMIT, HOVER_BUTTON_SUBMIT);
        cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);

        VBox footer = new VBox(submit, cancel);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        footer.setSpacing(15);

        return footer;
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

    public CheckBox getAdmin() {
        return userIA;
    }

    public TextField getUserName() {
        return userName;
    }

    public TextField getUserPassWord() {
        return userPassword;
    }

    public Button getCancel() {
        return cancel;
    }

}
