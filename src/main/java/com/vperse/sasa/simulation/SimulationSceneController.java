package com.vperse.sasa.simulation;

import com.vperse.sasa.SASAApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SimulationSceneController implements Initializable
{
    public static final int WIDTH = 1250;
    public static final int HEIGHT = 850;

    private SimulationController sequentialSimulationController;
    private SimulationController parallelSimulationController;
    private SimulationController distributedSimulationController;

    @FXML
    private Pane distributedPane;

    @FXML
    private Pane parallelPane;

    @FXML
    private Pane sequentialPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader sequentialSimulationFXMLLoader =
                new FXMLLoader(SASAApplication.class.getResource("components/simulation.fxml"));

        FXMLLoader parallelSimulationFXMLLoader =
                new FXMLLoader(SASAApplication.class.getResource("components/simulation.fxml"));

        FXMLLoader distributedSimulationFXMLLoader =
                new FXMLLoader(SASAApplication.class.getResource("components/simulation.fxml"));

        try
        {
            BorderPane sequentialSimulationScrollPane = sequentialSimulationFXMLLoader.load();
            BorderPane parallelSimulationScrollPane = parallelSimulationFXMLLoader.load();
            BorderPane distributedSimulationScrollPane = distributedSimulationFXMLLoader.load();

            sequentialPane.getChildren().add(sequentialSimulationScrollPane);
            parallelPane.getChildren().add(parallelSimulationScrollPane);
            distributedPane.getChildren().add(distributedSimulationScrollPane);

            sequentialSimulationController = sequentialSimulationFXMLLoader.getController();
            parallelSimulationController = parallelSimulationFXMLLoader.getController();
            distributedSimulationController = distributedSimulationFXMLLoader.getController();
        } catch (IOException e)
        {
            System.out.println("Could not load simulation.fxml!");
            e.printStackTrace();
        }

    }


}
