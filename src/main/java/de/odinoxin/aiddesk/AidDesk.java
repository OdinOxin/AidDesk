package de.odinoxin.aiddesk;

import javafx.application.Application;
import javafx.stage.Stage;

public class AidDesk extends Application {

    public static void main(String[] args) {
        AidDesk.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Login();
    }
}
