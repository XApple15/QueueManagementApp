package ug.queuesystem.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ug.queuesystem.Logic.SimulationManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SimulationManagerApp implements Initializable {


    int timeLimit;
    int maxProcessingTime;
    int minProcessingTime;
    int numberOfServers;
    int numberOfClients;
    int minArrivalTime;
    int maxArrivalTime;
    static SimulationManager simulationManager;

    @FXML
    TextField timeLimitField = new TextField();
    @FXML
    TextField maxProcessingTimeField = new TextField();
    @FXML
    TextField minProcessingTimeField = new TextField();
    @FXML
    TextField numberOfServersField = new TextField();
    @FXML
    TextField numberOfClientsField = new TextField();
    @FXML
    TextField minArrivalTimeField = new TextField();
    @FXML
    TextField maxArrivalTimeField = new TextField();

    @FXML
    TextArea waitingClientsArea;
    @FXML
    TextArea queueArea;
    @FXML
    TextArea result;
    @FXML
    Button runButton;
    @FXML
    TextArea time;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        runButton.setOnAction(e -> {
            timeLimit = Integer.parseInt(timeLimitField.getText());
            maxProcessingTime = Integer.parseInt(maxProcessingTimeField.getText());
            minProcessingTime = Integer.parseInt(minProcessingTimeField.getText());
            numberOfServers = Integer.parseInt(numberOfServersField.getText());
            numberOfClients = Integer.parseInt(numberOfClientsField.getText());
            minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
            maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());

            simulationManager = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime, numberOfServers,
                    numberOfClients, minArrivalTime, maxArrivalTime, waitingClientsArea, queueArea,result,time);
            new Thread(simulationManager).start();
        });
    }
}