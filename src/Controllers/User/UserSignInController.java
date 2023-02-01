package Controllers.User;

import Controllers.BloodBank.BloodBankNameList;
import Controllers.Details;
import Controllers.SqlConnections.SqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class UserSignInController implements Initializable {

    public static String id;
    public boolean isLoaded = false;
    public ImageView imgBack;
    public TableView<Details> tableView;
    public TableColumn<Details, String> addressCol;
    public TableColumn<Details, String> iDCol;
    public TableColumn<Details, String> contactNoCol;
    public TableColumn<Details, String> bloodGCol;
    public ComboBox<String> reqBloodBank;
    public ComboBox<String> reqBloodGroup;
    public DatePicker lastDonationUpdate;
    public TextArea diseaseHistoryUpdate;
    public Text lastDateOfDonationProfile;
    public Text addressProfile;
    public Text contactNoProfile;
    public Text genderProfile;
    public Text dateOfBirthProfile;
    public Text bloodGroupProfile;
    public Text ageProfile;
    public Text nameProfile;
    public Text idProfile;
    public TableView<Details> bloodBankView;
    public TableColumn<Details, String> bloodBankCol;
    public TableColumn<Details, String> contactNumberCol;
    public Text updateDateValidity;
    public TableView<DiseaseHistory> diseaseHistoryView;
    public TableView<DonationDetails> lastDonationView;
    public TableColumn<DiseaseHistory,String> diseaseHistoryDateCol;
    public TableColumn<DiseaseHistory,String> diseaseHistoryDetailsCol;
    public TableColumn<DonationDetails,String> donationHistoryDateCol;
    ObservableList<String> bloodGroupList = FXCollections.observableArrayList("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgBack.setImage(new Image("Images/blood img.jpg"));
        try {
            loadBloodBankDetails();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void load() throws SQLException {
        idProfile.setText(id);
        String userDetails = String.format("SELECT * FROM [USER_DONOR] WHERE ID = '%s'", id);

        String userDonationHistory = String.format("SELECT DONATION_UPDATE \nFROM USER_DONATION_HISTORY \nWHERE ID = '%s'\nORDER BY DONATION_UPDATE DESC", id);

        String userDiseaseHistory = String.format("SELECT DISEASE_UPDATE , DATE \nFROM USER_DISEASE_HISTORY \nWHERE ID = '%s'\nORDER BY DATE DESC", id);

        Connection connection = SqlConnection.getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(userDetails);

        while (rs.next()) {

            idProfile.setText(rs.getString(1));
            nameProfile.setText(rs.getString(2));
            bloodGroupProfile.setText(rs.getString(3));
            dateOfBirthProfile.setText(rs.getString(4));
            ageProfile.setText(rs.getString(5));
            genderProfile.setText(rs.getString(6));
            contactNoProfile.setText(rs.getString(7));
            addressProfile.setText(rs.getString(8));
            lastDateOfDonationProfile.setText(rs.getString(9));
        }

        rs = st.executeQuery(userDonationHistory);
        donationHistoryDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        lastDonationView.setItems(getListDonHistory(rs));

        rs = st.executeQuery(userDiseaseHistory);
        diseaseHistoryDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        diseaseHistoryDetailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));
        diseaseHistoryView.setItems(getListDisHistory(rs));

        reqBloodGroup.setItems(bloodGroupList);
        reqBloodBank.setItems(BloodBankNameList.getList());
        isLoaded = true;
    }

    public void loadProfile() throws SQLException {
        if (!isLoaded) {
            load();
        }
    }

    public void profileUpdate() throws SQLException {
        if (!isLoaded) {
            load();
        }
    }

    public void request() throws SQLException {
        if (!isLoaded) {
            load();
        }
    }

    private void loadBloodBankDetails() throws SQLException {
        bloodBankCol.setCellValueFactory(new PropertyValueFactory<>("nameOrId"));
        contactNumberCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        String query =  "SELECT BANK , CONTACT_NO " +
                        "FROM BB_DETAILS";
        Connection connection = SqlConnection.getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        bloodBankView.setItems(getListBB(rs));
    }

    public void search() throws SQLException {
        String query = String.format("SELECT ID,BLOOD_GROUP,ADDRESS,CONTACT_NO \nFROM USER_DONOR \nWHERE BLOOD_GROUP IN ( SELECT CAN_GIVE_TO \nFROM BLOOD_MATCHES \nWHERE CAN_RECEIVE_FROM = '%s')", reqBloodGroup.getValue());

        if (!reqBloodBank.getValue().isEmpty()) {
            query = query.concat(String.format("AND  ID IN   (SELECT ID \nFROM USER_DONOR \nWHERE ADDRESS  = '%s');", reqBloodBank.getValue().split(" : ")[1].trim()));
        }

        iDCol.setCellValueFactory(new PropertyValueFactory<>("nameOrId"));
        bloodGCol.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("ContactNumber"));

        Connection connection = SqlConnection.getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        tableView.setItems(getList(rs));

        String logQu = "INSERT INTO USER_SEARCH_LOG " +
                "VALUES('" + id + "','" + reqBloodBank.getValue() + "','" + LocalDate.now() + "','" + LocalTime.now() + "')";
        SqlConnection.set(logQu);

    }

    public void updateLastDonation() throws SQLException, ParseException {
        String query =  "INSERT INTO USER_DONATION_HISTORY\n" +
                "VALUES ('" + id + "','" + lastDonationUpdate.getValue() + "','"  + LocalDate.now() + "','" +  LocalTime.now()  + "')";

        String update = "UPDATE USER_DONOR\n" +
                        "SET LAST_DATE_OF_DONATION = '" + lastDonationUpdate.getValue() + "'\n" +
                        "WHERE ID = '" + idProfile + "'";


        LocalDate d2=lastDonationUpdate.getValue();
        LocalDate d3=LocalDate.now();

        if(!lastDateOfDonationProfile.getText().isEmpty()){
            LocalDate d1=LocalDate.parse(lastDateOfDonationProfile.getText()).plusMonths(4);
            if(d1.compareTo(d2) < 0  && d3.compareTo(d2) >= 0){
                SqlConnection.set(query);
                SqlConnection.set(update);
                updateDateValidity.setText("");
                load();
            }else{
                updateDateValidity.setText("INVALID DATE");
            }
        }else{
            if(d3.compareTo(d2) >= 0){
                SqlConnection.set(query);
                SqlConnection.set(update);
                updateDateValidity.setText("");
                load();
            }else{
                updateDateValidity.setText("INVALID DATE");
            }
        }
    }

    public void updateDiseaseHistory() throws SQLException {
        String query =  "INSERT INTO USER_DISEASE_HISTORY\n" +
                "VALUES ('" + id + "','" + diseaseHistoryUpdate.getText() + "','"  + LocalDate.now() + "','" +  LocalTime.now()  + "')";
        SqlConnection.set(query);
        load();
    }

    public ObservableList<Details> getList(ResultSet rs) throws SQLException {
        ObservableList<Details> list = FXCollections.observableArrayList();

        while (rs.next()) {
            list.add(new Details(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        return list;
    }

    public ObservableList<Details> getListBB(ResultSet rs) throws SQLException {
        ObservableList<Details> list = FXCollections.observableArrayList();
        while (rs.next()) {
            list.add(new Details(rs.getString(1), rs.getString(2)));
        }
        return list;
    }

    public ObservableList<DonationDetails> getListDonHistory(ResultSet rs) throws SQLException {
        ObservableList<DonationDetails> list = FXCollections.observableArrayList();
        while (rs.next()) {
            list.add(new DonationDetails(rs.getString(1)));
        }
        return list;
    }

    public ObservableList<DiseaseHistory> getListDisHistory(ResultSet rs) throws SQLException {
        ObservableList<DiseaseHistory> list = FXCollections.observableArrayList();
        while (rs.next()) {
            list.add(new DiseaseHistory(rs.getString(1), rs.getString(2)));
        }
        return list;
    }

}
