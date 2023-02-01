package Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminLogInController implements Initializable {
    public TextArea resultField;
    public TextArea queryField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void executeQuery() {
        if (!queryField.getText().isEmpty()) {
            result(queryField.getText());
        }
    }

    private void result(String query) {
        String connectionUrl =
                "jdbc:sqlserver://REX-PC:1433;"
                        + "database=BLOOD_DONATION_SYSTEM;"
                        + "user=REX2;"
                        + "password=123456789;"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {

            Statement st = connection.createStatement();
            String s = "";
            ResultSet rs = st.executeQuery(query);
            int column = rs.getMetaData().getColumnCount();

            while (rs.next()) {

                for (int i = 1; i <= column; i++) {
                    if (rs.getString(i) == null) {
                        s = s.concat(rs.getMetaData().getColumnName(i));
                        s = s.concat(" : " + rs.getString(i) + "\n");
                    } else {
                        s = s.concat(rs.getMetaData().getColumnName(i));
                        s = s.concat(" : " + rs.getString(i).replaceAll("\n\n", ", ") + "\n");
                    }
                }
                s = s.concat("\n");

            }
            resultField.setText(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
