package Controllers.User;

import Controllers.BloodBank.BloodBankNameList;
import Controllers.Main;
import Controllers.SqlConnections.SqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserSignUpController implements Initializable {

    public ImageView imgBack;
    public TextField regName;
    public TextField regId;
    public ComboBox<String> regGender;
    public ComboBox<String> regBloodGroup;
    public DatePicker regDateOfBirth;
    public TextField regContactNo;
    public TextField regAge;
    public ImageView imgId;
    public TextField newIDNumber;
    public TextField oldIdNumber;
    public Text validity;
    public TextField pass;
    public ComboBox<String> regAddress;
    public Button registerBtn;

    ObservableList<String> gender = FXCollections.observableArrayList("M", "F");
    ObservableList<String> bloodGroupList = FXCollections.observableArrayList("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgBack.setImage(new Image("Images/blood img.jpg"));
        imgId.setImage(new Image("Images/idConv.jpg"));
        regGender.setItems(gender);
        regBloodGroup.setItems(bloodGroupList);
        regAddress.setItems(BloodBankNameList.getList());
        registerBtn.setDisable(true);

        regId.setOnKeyReleased(e->{
                check();
        });
    }

    public void check(){
        String query = String.format("SELECT COUNT(1)\n FROM USER_DONOR\n WHERE ID = '%s'", regId.getText());
        try {
            Connection con = SqlConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(query);

            while(rs.next()){
                if(!rs.getString(1).equals("1") && regId.getText().length()==12){
                    registerBtn.setDisable(false);
                }else{
                    registerBtn.setDisable(true);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void register() {
        String id = regId.getText();
        if (id.contains("v") || id.contains("V") || id.length() <= 10) {
            showPopUp();
        } else {
            String s1 = "INSERT INTO USER_DONOR(ID,NAME,BLOOD_GROUP,DATE_OF_BIRTH,AGE,GENDER,CONTACT_NO,ADDRESS) " + "VALUES (" +
                    "'" + regId.getText() + "'," +
                    "'" + regName.getText() + "'," +
                    "'" + regBloodGroup.getValue() + "'," +
                    "'" + regDateOfBirth.getValue() + "'," +
                    "'" + regAge.getText() + "'," +
                    "'" + regGender.getValue() + "'," +
                    "'" + regContactNo.getText() + "'," +
                    "'" + regAddress.getValue().split(" : ")[1] + "')";
            SqlConnection.set(s1);

            String s2 = "INSERT INTO USER_LOGIN(ID ,PASSWORD) \n VALUES (" +
                    "'" + regId.getText() + "'," +
                    "'" + pass.getText() + "')";

            SqlConnection.set(s2);
            showPopUpSuccess();
        }
    }

    public void showPopUp() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Try Again");
        popup.setResizable(false);
        popup.setMinWidth(150);
        popup.setMinHeight(50);

        Button btn = new Button("OK");
        btn.setStyle("-fx-background-color: #FF0000");
        Label text = new Label("Invalid ID ");
        btn.setOnAction(e -> popup.close());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(text, btn);
        vbox.setAlignment(Pos.CENTER);

        popup.setScene(new Scene(vbox));
        popup.centerOnScreen();
        popup.showAndWait();

    }

    public void showPopUpSuccess() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Success");
        popup.setResizable(false);
        popup.setMinWidth(150);
        popup.setMinHeight(50);

        Button btn = new Button("Done");
        btn.setStyle("-fx-background-color: #FF0000");
        Label text = new Label("User Registered");
        btn.setOnAction(e -> popup.close());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(text, btn);
        vbox.setAlignment(Pos.CENTER);

        popup.setScene(new Scene(vbox));
        popup.centerOnScreen();
        popup.showAndWait();

        Stage st=(Stage) imgBack.getScene().getWindow();
        st.setScene(Main.getMainScene());
        st.resizableProperty().set(false);
        st.centerOnScreen();
    }

    public void convertID() {
        if ((oldIdNumber.getText().contains("v") || oldIdNumber.getText().contains("V")) && oldIdNumber.getText().length() == 10) {
            validity.setText("Valid ID!");
            String s = oldIdNumber.getText().substring(0, 9);
            s = "19".concat(s.substring(0, 5)).concat("0").concat(s.substring(5));
            newIDNumber.editableProperty().set(false);
            newIDNumber.setText(s);
            regId.setText(s);
            check();
        } else {
            validity.setText("Invalid ID!");
        }
    }
}
