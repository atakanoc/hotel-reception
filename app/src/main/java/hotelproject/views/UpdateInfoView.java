package hotelproject.views;

import hotelproject.controllers.utils.Default;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

/**
 * Create update user information view.
 */
public class UpdateInfoView extends View {

    private final Change change;
    private final Label output = new Label();
    private final Label Lbl1;
    private final Label Lbl2;
    private Button save;
    private TextField firstUName;
    private TextField secondUName;
    private PasswordField firstPwd;
    private PasswordField secondPwd;

    /**
     * Constructor for this view.
     *
     * @param change an instance of Change, can be username or password. The view will change based on the parameter.
     */
    public UpdateInfoView(Change change) {
        this.change = change;

        // The scene's nodes
        String chOption;
        if (change == Change.USERNAME) {
            chOption = "username";
            firstUName = new TextField();
            firstUName.setStyle("-fx-background-color: #323232; -fx-text-inner-color: white; -fx-background-radius: 0;");
            firstUName.setFont(Default.getSFPro(18));
            secondUName = new TextField();
            secondUName.setStyle("-fx-background-color: #323232; -fx-text-inner-color: white; -fx-background-radius: 0;");
            secondUName.setFont(Default.getSFPro(18));
        } else {
            chOption = "password";
            firstPwd = new PasswordField();
            firstPwd.setStyle("-fx-background-color: #323232; -fx-text-inner-color: white; -fx-background-radius: 0;");
            firstPwd.setFont(Default.getSFPro(18));
            secondPwd = new PasswordField();
            secondPwd.setStyle("-fx-background-color: #323232; -fx-text-inner-color: white; -fx-background-radius: 0;");
            secondPwd.setFont(Default.getSFPro(18));
        }

        Lbl1 = new Label(String.format("Please enter your new %s: ", chOption));
        Lbl2 = new Label(String.format("Please enter your new %s again: ", chOption));
        createScene();
    }

    /**
     * Create the scene for update info view.
     */
    @Override
    void createScene() {
        GridPane pane = createBody();
        GridPane.setHalignment(pane, javafx.geometry.HPos.CENTER);

        pane.setStyle("-fx-background-color: #1e1e1e; -fx-alignment: center;");
        scene = new Scene(pane, 370, 230);
    }

    /**
     * Create text fields for the update info view
     *
     * @param field1 first input text field.
     * @param field2 second input text field.
     * @param pane   instance of pane.
     */
    private void createTextFields(Node field1, Node field2, GridPane pane) {
        Lbl1.setFont(Default.getSFPro(20));
        Lbl1.setTextFill(Paint.valueOf("white"));

        Lbl2.setFont(Default.getSFPro(20));
        Lbl2.setTextFill(Paint.valueOf("white"));

        pane.add(Lbl1, 0, 0);
        pane.add(field1, 0, 1);

        pane.add(Lbl2, 0, 3);
        pane.add(field2, 0, 4);
    }

    /**
     * Create update info view body based on Change object.
     *
     * @return a Pane object.
     */
    @Override
    GridPane createBody() {
        final String IDLE_BUTTON_SAVE = "file:assets/img/ui_dev_pack/general/idle_button_save.png";
        final String HOVER_BUTTON_SAVE = "file:assets/img/ui_dev_pack/general/hover_button_save.png";
        GridPane pane = createPane();

        //font and color of labels
        output.setFont(Default.getSFPro(17));
        output.setTextFill(Paint.valueOf("cf6679"));

        if (change == Change.USERNAME) {
            createTextFields(firstUName, secondUName, pane);
        } else {
            createTextFields(firstPwd, secondPwd, pane);
        }

        output.setTranslateX(75);
        pane.add(output, 0, 6);

        save = createButton(35, IDLE_BUTTON_SAVE, HOVER_BUTTON_SAVE);
        GridPane.setHalignment(save, javafx.geometry.HPos.CENTER);
        pane.add(save, 0, 7);

        return pane;
    }

    /*****************************Getters*********************************/

    public Button getSave() {
        return save;
    }

    /***************************Setter*******************************/

    public void setOutput(String outputTxt) {
        output.setText(outputTxt);
    }

    public TextField getFirstUName() {
        return firstUName;
    }

    public TextField getSecondUName() {
        return secondUName;
    }

    public PasswordField getFirstPwd() {
        return firstPwd;
    }

    public PasswordField getSecondPwd() {
        return secondPwd;
    }

    public enum Change {USERNAME, PASSWORD}
}
