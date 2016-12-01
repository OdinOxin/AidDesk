package de.odinoxin.aiddesk;

import javafx.application.Application;
import javafx.stage.Stage;

public class AidDesk extends Application {

    /**
     * Lanches AidDesk
     * @param args Command parameters
     */
    public static void main(String[] args) {
        AidDesk.launch(args);
    }


    /**
     * Starts the Application
     * @param primaryStage The primary stage (unused)
     * @throws Exception May throw an Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Properties to enable logging
//        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
//        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
//        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
//        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
        new Login(); //Start Login plugin
    }
}
