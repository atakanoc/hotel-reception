package hotelproject.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * User login view used for both initial login and also when user wants to change user information.
 * The login view will be used.
 */
public class LoginView extends View {

    final boolean onlyPwd;
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final Label credentials = new Label("Please enter your password");
    private final Label result = new Label();
    private Button testLogin;

    /**
     * Constructor used for initiating the view scene.
     *
     * @param onlyPwd is used to differ two different kinds of login view.
     */
    public LoginView(boolean onlyPwd) {
        this.onlyPwd = onlyPwd;
        createScene();
    }

    /**
     * Create scene based on the @param onlyPwd.
     * If onlyPwd is true, the scene will be smaller.
     * If OnlyPwd is false, the scene will be larger.
     */
    @Override
    void createScene() {
        GridPane scenePane = new GridPane();
        VBox header = createHeader();
        scenePane.add(header, 0, 0);
        GridPane.setHalignment(header, javafx.geometry.HPos.CENTER);
        GridPane body = createBody();
        scenePane.add(body, 0, 1);
        GridPane.setHalignment(body, javafx.geometry.HPos.CENTER);

        scenePane.setStyle("-fx-background-color: #1e1e1e; -fx-alignment: center;");

        int height;
        if (onlyPwd) {
            height = 250;
        } else {
            height = 610;
        }
        scene = new Scene(scenePane, 476, height);
    }

    /**
     * Create VBox based on the @param onlyPwd.
     * If onlyPwd is true, the view will only present text field for password.
     * If OnlyPwd is false, the view will present hotel logo and welcome images.
     */
    private VBox createHeader() {
        VBox header;
        if (!onlyPwd) {
            Image logoImg = new Image("file:assets/img/ui_dev_pack/main_menu/logo_hotel.png");
            ImageView logo = new ImageView(logoImg);
            logo.setPreserveRatio(true);
            logo.setFitWidth(100.0);
            logo.setFitHeight(125.0);

            Image welcomeImg = new Image("file:assets/img/ui_dev_pack/login_page/text_welcome.png");
            ImageView welcome = new ImageView(welcomeImg);
            welcome.setPreserveRatio(true);

            header = new VBox(logo, welcome);
            header.setPadding(new Insets(10.0, 10.0, 35.0, 10.0));
        } else {
            credentials.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 25));
            credentials.setStyle("-fx-font-weight: bold;");
            credentials.setTextFill(Paint.valueOf("bb86fc"));

            header = new VBox(credentials);
            header.setPadding(new Insets(30.0, 10.0, 5.0, 10.0));
        }
        header.setSpacing(5.5);
        header.setAlignment(Pos.CENTER);

        return header;
    }

    /**
     * Create body pane based on the @param onlyPwd.
     * If onlyPwd is true, the view will only present text field with password.
     * If OnlyPwd is false, the view will present both user name and password.
     */
    @Override
    GridPane createBody() {
        final String IDLE_TEST_LOGIN = "file:assets/img/ui_dev_pack/login_page/idle_button_login.png";
        final String HOVER_TEST_LOGIN = "file:assets/img/ui_dev_pack/login_page/hover_button_login.png";

        GridPane bodyPane = createPane();

        result.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 17));
        result.setTextFill(Paint.valueOf("cf6679"));

        password.setStyle("-fx-background-color: transparent; -fx-text-inner-color: white;");
        password.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 18));
        password.setAlignment(Pos.BASELINE_LEFT);
        password.setMaxWidth(300);
        password.setTranslateX(60);
        password.setTranslateY(1);
        password.setPromptText("Password");

        if (!onlyPwd) {

            bodyPane.add(result, 0, 0);

            StackPane stack = new StackPane();

            username.setStyle("-fx-background-color: transparent; -fx-text-inner-color: white;");
            username.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 18));
            username.setAlignment(Pos.BASELINE_LEFT);
            username.setMaxWidth(300);
            username.setTranslateX(60);
            username.setTranslateY(11);
            username.setPromptText("Username");

            password.setTranslateY(37.5);

            VBox nodes = new VBox(username, password);

            Image loginImg = new Image("file:assets/img/ui_dev_pack/login_page/box_details.png");
            ImageView login = new ImageView(loginImg);
            login.setPreserveRatio(true);

            stack.getChildren().addAll(login, nodes);
            bodyPane.add(stack, 0, 1);
            GridPane.setHalignment(stack, HPos.CENTER);
        } else {
            password.setTranslateY(1);
            bodyPane.add(result, 0, 0);

            Image loginImg = new Image("file:assets/img/ui_dev_pack/login_page/box_password.png");
            ImageView login = new ImageView(loginImg);
            login.setPreserveRatio(true);

            bodyPane.add(login, 0, 1);
            bodyPane.add(password, 0, 1);
        }

        //login button
        testLogin = createButton(55, IDLE_TEST_LOGIN, HOVER_TEST_LOGIN);
        bodyPane.add(testLogin, 0, 2);

        return bodyPane;
    }

    /***************************** Getters *********************************/

    public Button getTestLoginButton() {
        return testLogin;
    }

    public TextField getUsername() {
        return username;
    }

    public String getUsernameString() {
        return username.getText();
    }

    public PasswordField getPassword() {
        return password;
    }

    public String getPasswordString() {
        return password.getText();
    }

    public Label getResult() {
        return result;
    }
}
