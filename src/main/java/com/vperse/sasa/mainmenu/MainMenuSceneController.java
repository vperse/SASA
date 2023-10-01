package com.vperse.sasa.mainmenu;

import com.vperse.sasa.SASAApplication;
import com.vperse.sasa.editor.EditorSceneController;
import com.vperse.sasa.logic.TileType;
import com.vperse.sasa.util.ResourceLoader;
import com.vperse.sasa.util.TAMManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainMenuSceneController {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    @FXML
    private void newEditorScene(ActionEvent event)
    {
        SASAApplication sasaApplication = SASAApplication.getApplicationInstance();
        sasaApplication.tileTypes = new HashMap<>();
        sasaApplication.tileTypes.put("SEED", new TileType());
        sasaApplication.total = 1;

        ResourceLoader.loadScene(
                "views/editor-view.fxml",
                (Stage) ((Node) event.getSource()).getScene().getWindow(),
                EditorSceneController.WIDTH,
                EditorSceneController.HEIGHT
        );
    }

    @FXML
    private void loadEditorScene(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = getTAMFile(stage);

        if (selectedFile == null) return;

        TAMManager tamManager = new TAMManager(selectedFile);
        tamManager.readFile();

        ResourceLoader.loadScene(
                "views/editor-view.fxml",
                (Stage) ((Node) event.getSource()).getScene().getWindow(),
                EditorSceneController.WIDTH,
                EditorSceneController.HEIGHT
        );
    }

    public File getTAMFile(Stage stage)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Simulation");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Tile Assembly Model Files", "*.tam")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void exit(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
