package de.odinoxin.aiddesk;

import javafx.application.Application;
import javafx.stage.Stage;

public class AidDesk extends Application {

    public static void main(String[] args) {
        AidDesk.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
        new Login();
    }
}
