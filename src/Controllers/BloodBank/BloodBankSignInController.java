package Controllers.BloodBank;

import Controllers.Details;
import Controllers.SqlConnections.SqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class BloodBankSignInController implements Initializable {

    public ImageView backImg;
    public TableColumn<Details, String> contactNoCol;
    public TableColumn<Details, String> cityCol;
    public TableColumn<Details, String> bloodGroupCol;
    public TableColumn<Details, String> nameCol;
    public TableColumn<Details, String> lastDonationDateCol;
    public TableView<Details> tableView;

    public TextField bloodBankField;
    public TextField clusterField;
    public ComboBox<String> bloodGroup;
    public Button search;


    ObservableList<String> bloodGroupList = FXCollections.observableArrayList("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bloodGroup.setItems(bloodGroupList);
    }

    public void search() throws SQLException {

        String query = "SELECT NAME,BLOOD_GROUP,LAST_DATE_OF_DONATION,ADDRESS,CONTACT_NO \n" +
                "FROM USER_DONOR";

        if (bloodGroup.getValue() != null && (!clusterField.getText().isEmpty() || !bloodBankField.getText().isEmpty())) {
            query = "SELECT NAME,BLOOD_GROUP,LAST_DATE_OF_DONATION,ADDRESS,CONTACT_NO \n" +
                    "FROM USER_DONOR \n" +
                    "WHERE BLOOD_GROUP IN ( SELECT CAN_GIVE_TO \n" +
                    "FROM BLOOD_MATCHES \n" +
                    "WHERE CAN_RECEIVE_FROM = '" + bloodGroup.getValue() + "') AND ";

            if (!bloodBankField.getText().isEmpty()) {
                query = query.concat("NAME IN   (SELECT NAME \n" +
                        "FROM USER_DONOR \n" +
                        "WHERE ADDRESS  = '" + bloodBankField.getText() + "');");

            } else if (!clusterField.getText().isEmpty()) {
                query = query.concat("NAME IN  (SELECT NAME \n" +
                        "FROM USER_DONOR \n" +
                        "WHERE ADDRESS  IN  (SELECT BANK \n " +
                        "FROM BB_DETAILS \n" +
                        "WHERE CLUSTER = '" + clusterField.getText() + "'))");
            }

        } else if (bloodGroup.getValue() != null) {

            query = "SELECT NAME,BLOOD_GROUP,LAST_DATE_OF_DONATION,ADDRESS,CONTACT_NO \n" +
                    "FROM USER_DONOR \n" +
                    "WHERE BLOOD_GROUP IN ( SELECT CAN_GIVE_TO \n" +
                    "FROM BLOOD_MATCHES \n" +
                    "WHERE CAN_RECEIVE_FROM = " +
                    "'" + bloodGroup.getValue() + "')";

        } else if (bloodGroup.getValue() == null) {
            if (!bloodBankField.getText().isEmpty()) {
                query = "SELECT NAME,BLOOD_GROUP,LAST_DATE_OF_DONATION,ADDRESS,CONTACT_NO \n" +
                        "FROM USER_DONOR \n" +
                        "WHERE ADDRESS  = '" + bloodBankField.getText() + "'";
            } else if (!clusterField.getText().isEmpty()) {
                query = "SELECT NAME,BLOOD_GROUP,LAST_DATE_OF_DONATION,ADDRESS,CONTACT_NO \n" +
                        "FROM USER_DONOR \n" +
                        "WHERE ADDRESS  IN  (SELECT BANK \n " +
                        "FROM BB_DETAILS \n" +
                        "WHERE CLUSTER = '" + clusterField.getText() + "')";
            }
        }

        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameOrId"));
        bloodGroupCol.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        lastDonationDateCol.setCellValueFactory(new PropertyValueFactory<>("lastDateOfDonation"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        Connection connection = SqlConnection.getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        tableView.setItems(getList(rs));

    }

    public ObservableList<Details> getList(ResultSet rs) throws SQLException {
        ObservableList<Details> list = FXCollections.observableArrayList();

        while (rs.next()) {
            list.add(new Details(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return list;
    }
}
