package hotelproject.views;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Create view for deleting a room.
 */
public class DeleteBookingView extends View {

    final String IDLE_SUBMIT = "file:assets/img/ui_dev_pack/general/idle_button_submit.png";
    final String HOVER_SUBMIT = "file:assets/img/ui_dev_pack/general/hover_button_submit.png";
    final String IDLE_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/idle_button_cancel.png";
    final String HOVER_BUTTON_CANCEL = "file:assets/img/ui_dev_pack/general/hover_button_cancel.png";

    private final Button submit = createButton(35, IDLE_SUBMIT, HOVER_SUBMIT);
    private final Button cancel = createButton(35, IDLE_BUTTON_CANCEL, HOVER_BUTTON_CANCEL);

    /**
     * No parameter is needed for this constructor.
     */
    public DeleteBookingView() {
        createScene();
    }

    /**
     * Create scene for this view.
     */
    @Override
    void createScene() {
        GridPane pane = createPane();

        VBox header = createHeader("Delete selected booking?", "Confirm your action!");

        pane.setAlignment(Pos.CENTER);

        GridPane paneTwo = new GridPane();
        paneTwo.getStyleClass().add("body-pane");
        paneTwo.add(header, 0, 0);
        paneTwo.add(pane, 0, 1);
        GridPane.setHalignment(header, HPos.CENTER);

        paneTwo.add(createFooter(submit, cancel), 0, 2);

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

    /***************************** Getters *********************************/
    public Button getSubmit() {
        return submit;
    }

    public Button getCancel() {
        return cancel;
    }

}