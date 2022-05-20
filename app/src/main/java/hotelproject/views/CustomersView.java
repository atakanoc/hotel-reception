package hotelproject.views;

import hotelproject.controllers.objects.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.List;

/**
 * This view is used to present all hotel customers.
 */
public class CustomersView extends View {

    public final TableView<Customer> customersTable = new TableView<>();
    private final ObservableList<Customer> customers;
    private Button addCustomer;

    /**
     * The constructor is used for initiating all available customers in the database.
     *
     * @param customers list to store all the customer objects.
     */
    public CustomersView(List<Customer> customers) {
        this.customers = FXCollections.observableList(customers);
        createScene();
    }

    /**
     * Create scene for this view.
     */
    @Override
    void createScene() {
        GridPane bodyPane = createBody();
        GridPane.setHalignment(bodyPane, javafx.geometry.HPos.CENTER);

        bodyPane.getStyleClass().add("body-pane");
        customersTable.getStyleClass().add("table-view");

        scene = new Scene(bodyPane);
        scene.getStylesheets().add("file:assets/css/Stylesheet.css");
    }

    /**
     * Create a body pane to contain all customers' information.
     *
     * @return the instance of the GridPane.
     */
    @Override
    GridPane createBody() {
        final String IDLE_ADD_CUSTOMER = "file:assets/img/ui_dev_pack/customer_menu/idle_button_new_customer.png";
        final String HOVER_ADD_CUSTOMER = "file:assets/img/ui_dev_pack/customer_menu/hover_button_new_customer.png";

        GridPane pane = createPane();

        Label title = new Label("Hotel customers");
        title.setFont(Font.loadFont("file:assets/font/SF_Pro.ttf", 25));
        title.setStyle("-fx-font-weight: bold;");
        title.setTextFill(Paint.valueOf("bb86fc"));

        customersTable.setEditable(true);

        // Create column in the table
        TableColumn<Customer, Integer> cSSNumCol = new TableColumn<>("Social security number");
        cSSNumCol.setMinWidth(200);
        cSSNumCol.setCellValueFactory(new PropertyValueFactory<>("c_ss_number"));

        TableColumn<Customer, String> cAddressCol = new TableColumn<>("Address");
        cAddressCol.setMinWidth(200);
        cAddressCol.setCellValueFactory(new PropertyValueFactory<>("c_address"));

        TableColumn<Customer, String> cFullNameCol = new TableColumn<>("Full name");
        cFullNameCol.setMinWidth(200);
        cFullNameCol.setCellValueFactory(new PropertyValueFactory<>("c_full_name"));

        TableColumn<Customer, Integer> cNumCol = new TableColumn<>("Phone number");
        cNumCol.setMinWidth(150);
        cNumCol.setCellValueFactory(new PropertyValueFactory<>("c_phone_num"));

        TableColumn<Customer, String> cEmailCol = new TableColumn<>("Email");
        cEmailCol.setMinWidth(200);
        cEmailCol.setCellValueFactory(new PropertyValueFactory<>("c_email"));

        // Create a filtered list to put the rooms as items in the table
        FilteredList<Customer> flCustomer = new FilteredList<>(customers, p -> true);
        customersTable.setItems(flCustomer);
        customersTable.getColumns().addAll(cSSNumCol, cAddressCol, cFullNameCol, cNumCol, cEmailCol);

        // Create choice box so the user can choose on the column he's searching in
        ChoiceBox<String> whatToSearch = new ChoiceBox<>();
        whatToSearch.getItems().addAll("Full name", "Phone number", "Social security number");
        whatToSearch.setValue("Full name"); // default search
        whatToSearch.setStyle("-fx-pref-width: 183;");

        // Create search bar with listener to update according to the user's input
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here");
        searchBar.setStyle("-fx-pref-width: 183;");
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (whatToSearch.getValue().equals("Social security number")) {
                flCustomer.setPredicate(p -> String.valueOf(p.getC_ss_number()).contains(newValue.toLowerCase().trim()));
            } else if (whatToSearch.getValue().equals("Full name")) {
                flCustomer.setPredicate(p -> p.getC_full_name().toLowerCase().contains(newValue.toLowerCase().trim()));
            } else if (whatToSearch.getValue().equals("Phone number")) {
                flCustomer.setPredicate(p -> String.valueOf(p.getC_phone_num()).contains(newValue.toLowerCase().trim()));
            }
        });

        //When the new choice is selected we reset
        whatToSearch.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                -> {
            if (newVal != null) {
                searchBar.setText("");
            }
        });

        HBox search = new HBox(whatToSearch, searchBar);
        search.setAlignment(Pos.CENTER);
        search.getStyleClass().add("search");

        pane.add(title, 0, 0);
        GridPane.setHalignment(title, javafx.geometry.HPos.CENTER);
        pane.add(search, 0, 2);
        pane.add(customersTable, 0, 4);
        addCustomer = createButton(35, IDLE_ADD_CUSTOMER, HOVER_ADD_CUSTOMER);
        pane.add(addCustomer, 0, 5);
        GridPane.setHalignment(addCustomer, javafx.geometry.HPos.CENTER);

        return pane;
    }

    /**************************Getter**********************/

    public Button getAddCustomer() {
        return addCustomer;
    }

    public TableView<Customer> getCustomersTable() {
        return customersTable;
    }
}   
