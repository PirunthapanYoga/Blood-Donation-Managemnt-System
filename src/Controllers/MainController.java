package Controllers;

import Controllers.SqlConnections.SqlConnection;
import Controllers.User.UserSignInController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public Connection connection;
    public Text quotesTxt;
    public ImageView imgBack;
    public TextField adminPassword;
    public TextField adminUsername;
    public TextField userLogInPassword;
    public TextField userLogInName;
    public TextField bloodBankPassword;
    public TextField bloodBankUserName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgBack.setImage(new Image("Images/blood img.jpg"));
        quotesTxt.setText("Do you feel you don’t have much to offer? You have the most precious resource of all: the ability to save a life by donating blood! Help share this invaluable gift with someone in need.");
    }

    public void bloodBankSignIn() throws SQLException {
        Stage bloodBankUI = (Stage) imgBack.getScene().getWindow();
        String password = null;
        String query =  "SELECT PASSWORD\n" +
                        "FROM BB_LOGIN " +
                        "WHERE USER_NAME = '" + bloodBankUserName.getText() + "'";
        connection = SqlConnection.getConnection();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()) {
            password = rs.getString(1);
        }

        if (bloodBankPassword.getText().equals(password)) {
            SqlConnection.set("INSERT INTO BB_LOGIN_LOG\n" +
                    "VALUES ('" + bloodBankUserName.getText() + "' , '" + LocalDate.now() + "' , '" + LocalTime.now() + "')\n");
            bloodBankUI.setScene(new Scene(Main.getRootBankWindow()));
            bloodBankUI.centerOnScreen();
            bloodBankUI.setResizable(false);


        } else {
            showPopUp();
        }
    }

    public void newUserSignIn() throws SQLException {
        Stage bloodBankUI = (Stage) imgBack.getScene().getWindow();
        String password = null;
        String query =  "SELECT COUNT(1)\n" +
                        "FROM USER_LOGIN "  +
                        "WHERE ID = '" + userLogInName.getText() + "'" + "AND PASSWORD = '" + userLogInPassword.getText() + "'";

        connection = SqlConnection.getConnection();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(query);

        while (rs.next()) {
            password = rs.getString(1);
        }

        assert password != null;
        if (password.equals("1")) {
            SqlConnection.set(  "INSERT INTO USER_LOGIN_LOG\n" +
                                "VALUES ('" + userLogInName.getText() + "' , '" + LocalDate.now() + "' , '" + LocalTime.now() + "')\n");
            UserSignInController.id = userLogInName.getText();
            bloodBankUI.setScene(new Scene(Main.getRootUserUI()));
            bloodBankUI.centerOnScreen();
            bloodBankUI.setResizable(false);
        } else {
            showPopUp();
        }
    }

    public void NewUserSignUp() throws IOException {
        Parent rootSignUp  = FXMLLoader.load(getClass().getResource("/FXML/Registration.fxml"));
        Stage newUserSignUp = (Stage) imgBack.getScene().getWindow();
        newUserSignUp.setScene(new Scene(rootSignUp));
        newUserSignUp.centerOnScreen();
        newUserSignUp.resizableProperty().set(false);
    }

    public void adminLogIn() {
        if(adminUsername.getText().equals("Rex1997") && adminPassword.getText().equals("12345@Rex")){
            Stage adminLogIn = (Stage) imgBack.getScene().getWindow();
            adminLogIn.setScene(new Scene(Main.getRootAdminUI()));
            adminLogIn.centerOnScreen();
            adminLogIn.resizableProperty().set(false);
        }else{
            showPopUp();
            adminPassword.setText("");
            adminUsername.setText("");
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
        Label text = new Label("Invalid User Name / Password ");
        btn.setOnAction(e -> popup.close());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(text, btn);
        vbox.setAlignment(Pos.CENTER);

        popup.setScene(new Scene(vbox));
        popup.showAndWait();

    }

}
