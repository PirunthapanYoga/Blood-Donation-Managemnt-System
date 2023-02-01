package Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Parent rootBankWindow;
    private static Parent rootUserUI;
    private static Parent rootAdminUI;
    private static Parent rootLogIn;
    private static Scene mainScene;

    public static Parent getRootUserUI() {
        return rootUserUI;
    }

    public static Parent getRootBankWindow() {
        return rootBankWindow;
    }

    public static Parent getRootAdminUI() {
        return rootAdminUI;
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        rootLogIn   = FXMLLoader.load(getClass().getResource("/FXML/UserLogIn.fxml"));
        rootUserUI  = FXMLLoader.load(getClass().getResource("/FXML/UserUI.fxml"));

        rootBankWindow = FXMLLoader.load(getClass().getResource("/FXML/Blood_Bank_UI.fxml"));
        rootAdminUI = FXMLLoader.load(getClass().getResource("/FXML/AdminUI.fxml"));

        mainScene=new Scene(rootLogIn, 791, 518);
        primaryStage.setTitle("It’s not how much we give, but how much love we put into giving");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
