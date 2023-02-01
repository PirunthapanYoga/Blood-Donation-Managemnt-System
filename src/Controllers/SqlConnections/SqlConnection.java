package Controllers.SqlConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnection {

    private static final String connectionUrl =
            "jdbc:sqlserver://REX-PC:1433;"
                    + "database=BLOOD_DONATION_SYSTEM;"
                    + "user=REX2;"
                    + "password=123456789;"
                    + "encrypt=false;"
                    + "trustServerCertificate=false;"
                    + "loginTimeout=30;";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }

    public static void set(String query) {

        try (
                Connection connection = DriverManager.getConnection(connectionUrl)) {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }
}
