package com.vperse.sasa.editor;

import com.vperse.sasa.SASAApplication;
import com.vperse.sasa.logic.Connector;
import com.vperse.sasa.logic.TileType;
import com.vperse.sasa.mainmenu.MainMenuSceneController;
import com.vperse.sasa.simulation.SimulationSceneController;
import com.vperse.sasa.util.Direction;
import com.vperse.sasa.util.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// TODO: Unsaved changes alert
// TODO: Resizing and Scaling
// TODO: Menu Bar (Separate controller)

public class EditorSceneController implements Initializable
{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private SASAApplication sasaApplication;
    private int selectedTileIndex;
    private AnchorPane tileEditorPane;
    private TileEditorController tileEditorController;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label tileCountLabel;

    @FXML
    private ListView<String> tileTypeList;

    @FXML
    private TextField amountTxtField;

    @FXML
    private Button removeTileButton;

    @FXML
    private Text errorDisplayText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        sasaApplication = SASAApplication.getApplicationInstance();

        amountTxtField.textProperty().addListener((observable, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                amountTxtField.setText(newVal.replaceAll("\\D", ""));
        });

        tileTypeList.setOnMouseClicked(event -> {
            if (!tileTypeList.getItems().isEmpty())
            {
                selectedTileIndex = tileTypeList.getSelectionModel().getSelectedIndex();
                loadTileEditor();
            }
        });

        selectedTileIndex = 0;
        addTileTypes();

        loadTileEditor();
    }

    @FXML
    private void loadMainMenuScene(ActionEvent event)
    {
        ResourceLoader.loadScene(
                "views/main-menu-view.fxml",
                (Stage) ((Node) event.getSource()).getScene().getWindow(),
                MainMenuSceneController.WIDTH,
                MainMenuSceneController.HEIGHT
        );
    }

    @FXML
    private void loadSimulationScene(ActionEvent event)
    {
        ResourceLoader.loadScene(
                "views/simulation-view.fxml",
                (Stage) ((Node) event.getSource()).getScene().getWindow(),
                SimulationSceneController.WIDTH,
                SimulationSceneController.HEIGHT
        );
    }

    @FXML
    private void newTileEditor(ActionEvent event)
    {
        selectedTileIndex = -1;
        loadTileEditor();
    }

    private void loadTileEditor()
    {
        FXMLLoader fxmlLoader =
                new FXMLLoader(SASAApplication.class.getResource("components/tile-editor.fxml"));

        try {
            tileEditorPane = fxmlLoader.load();
            tileEditorController = fxmlLoader.getController();
        } catch (IOException e)
        {
            System.out.println("Could not load: components/tile-editor.fxml!");
            e.printStackTrace();
            System.exit(-1); // Error type -1: Could not load resource!
        }

        mainPane.setCenter(tileEditorPane);
        amountTxtField.clear();
        errorDisplayText.setText("");
        setDisabledComponents(selectedTileIndex == 0);

        if (selectedTileIndex < 0)
        {
            removeTileButton.setDisable(true);
            return;
        }

        TileType tp = sasaApplication.tileTypes.get(tileTypeList.getItems().get(selectedTileIndex));
        amountTxtField.setText(String.valueOf(tp.amount));

        tileEditorController.getIdTxtField().setText(tp.id);
        tileEditorController.getTileRectangle().setFill(tp.color);

        for (Direction dir : Direction.values())
        {
            if (tp.hasConnector(dir))
            {
                tileEditorController.addConnector(dir);
                tileEditorController.getConnectorIdTxtField(dir).setText(tp.getConnector(dir).id);
                tileEditorController.getConnectorRect(dir).setFill(tp.getConnector(dir).color);
            }
        }
    }

    @FXML
    private void removeTileType(ActionEvent event)
    {
        TileType tp = sasaApplication.tileTypes.get(tileTypeList.getItems().get(selectedTileIndex));
        sasaApplication.tileTypes.remove(tileTypeList.getItems().get(selectedTileIndex));
        sasaApplication.total -= tp.amount;
        tileCountLabel.setText("Currently added: " + sasaApplication.total);
        tileTypeList.getItems().remove(selectedTileIndex);
        selectedTileIndex--;
        loadTileEditor();
    }

    @FXML
    private void saveTileType(ActionEvent event)
    {
        errorDisplayText.setFill(Color.GREEN);
        errorDisplayText.setText("");

        if (checkAmount() < 0) return;
        int amount = Math.min(
                Integer.parseInt(amountTxtField.getText()),
                SASAApplication.MAX_TOTAL - sasaApplication.total
        );

        if (checkTileId() < 0) return;
        String tileId = tileEditorController.getIdTxtField().getText();

        Color tileColor = (Color) tileEditorController.getTileRectangle().getFill();

        if (checkConnectorId(Direction.LEFT) < 0) return;
        if (checkConnectorId(Direction.TOP) < 0) return;
        if (checkConnectorId(Direction.RIGHT) < 0) return;
        if (checkConnectorId(Direction.BOTTOM) < 0) return;

        Connector[] connectors = getConnectorArray();

        TileType tileType = new TileType(
                tileId, tileColor, amount,
                connectors[0], connectors[1],
                connectors[2], connectors[3]
        );

        addTileType(tileType);

        tileCountLabel.setText("Currently added: " + sasaApplication.total);
        errorDisplayText.setText(errorDisplayText.getText() + "Saved!");
        removeTileButton.setDisable(false);
    }

    private int checkAmount()
    {
        if (amountTxtField.getText().isEmpty())
        {
            errorDisplayText.setFill(Color.RED);
            errorDisplayText.setText("You must enter a valid amount!");
            return -1;
        }

        // TODO: Check for new total!

        if (sasaApplication.total == SASAApplication.MAX_TOTAL)
        {
            errorDisplayText.setFill(Color.RED);
            errorDisplayText.setText("Maximum capacity reached!");
            return -1;
        }

        if (Integer.parseInt(amountTxtField.getText()) + sasaApplication.total > SASAApplication.MAX_TOTAL)
        {
            errorDisplayText.setFill(Color.ORANGE);
            errorDisplayText.setText(errorDisplayText.getText()
                    + "Added amount exceeds maximum capacity and was set to "
                    + (SASAApplication.MAX_TOTAL - sasaApplication.total) + "!\n\n");
            return 1;
        }

        return 0;
    }

    private int checkTileId()
    {
        String id = tileEditorController.getIdTxtField().getText();

        if (id.isEmpty())
        {
            errorDisplayText.setFill(Color.RED);
            errorDisplayText.setText("Tile id is not set!");
            return -1;
        }

        if (id.equals("SEED") && selectedTileIndex != 0)
        {
            errorDisplayText.setFill(Color.RED);
            errorDisplayText.setText("Cannot overwrite SEED!");
            return -1;
        }

        if (tileTypeList.getItems().indexOf(id) != selectedTileIndex)
        {
            // TODO: Alert: Confirm overwrite
            errorDisplayText.setFill(Color.ORANGE);
            errorDisplayText.setText(errorDisplayText.getText() + "Tile with this id already exists!\n\n");
        }

        return 0;
    }

    private int checkConnectorId(Direction dir)
    {
        if (tileEditorController.getConnectorIdTxtField(dir).isVisible())
        {
            String id = tileEditorController.getConnectorIdTxtField(dir).getText();

            if (id.isEmpty())
            {
                errorDisplayText.setFill(Color.RED);
                errorDisplayText.setText("Connector id's not set!");
                return -1;
            }

            return 0;
        }

        return 1;
    }

    private String[] getConnectorsIdArray()
    {
        String[] connectorIds = new String[4];
        connectorIds[0] = tileEditorController.getConnectorIdTxtField(Direction.LEFT).getText();
        connectorIds[1] = tileEditorController.getConnectorIdTxtField(Direction.TOP).getText();
        connectorIds[2] = tileEditorController.getConnectorIdTxtField(Direction.RIGHT).getText();
        connectorIds[3] = tileEditorController.getConnectorIdTxtField(Direction.BOTTOM).getText();
        return connectorIds;
    }

    private Color[] getConnectorsColorArray()
    {
        Color[] connectorColors = new Color[4];
        connectorColors[0] = (Color) tileEditorController.getConnectorRect(Direction.LEFT).getFill();
        connectorColors[1] = (Color) tileEditorController.getConnectorRect(Direction.TOP).getFill();
        connectorColors[2] = (Color) tileEditorController.getConnectorRect(Direction.RIGHT).getFill();
        connectorColors[3] = (Color) tileEditorController.getConnectorRect(Direction.BOTTOM).getFill();
        return connectorColors;
    }

    private Connector[] getConnectorArray()
    {
        String[] connectorIds = getConnectorsIdArray();
        Color[] connectorColors = getConnectorsColorArray();
        Connector[] connectors = new Connector[4];
        connectors[0] = (connectorIds[0].isEmpty()) ? null : new Connector(connectorIds[0], connectorColors[0]);
        connectors[1] = (connectorIds[1].isEmpty()) ? null : new Connector(connectorIds[1], connectorColors[1]);
        connectors[2] = (connectorIds[2].isEmpty()) ? null : new Connector(connectorIds[2], connectorColors[2]);
        connectors[3] = (connectorIds[3].isEmpty()) ? null : new Connector(connectorIds[3], connectorColors[3]);
        return connectors;
    }

    private void addTileType(TileType tp)
    {
        if (sasaApplication.tileTypes.containsKey(tp.id))
        {
            sasaApplication.total -= sasaApplication.tileTypes.get(tp.id).amount;
            sasaApplication.total += tp.amount;
            sasaApplication.tileTypes.replace(tp.id, tp);
        } else
        {
            sasaApplication.total += tp.amount;
            sasaApplication.tileTypes.put(tp.id, tp);
        }

        if (!tileTypeList.getItems().contains(tp.id)) tileTypeList.getItems().add(tp.id);

        selectedTileIndex = tileTypeList.getItems().indexOf(tp.id);
    }

    public void setDisabledComponents(boolean disabled)
    {
        tileEditorController.getIdTxtField().setDisable(disabled);
        amountTxtField.setDisable(disabled);
        removeTileButton.setDisable(disabled);
    }

    public void addTileTypes()
    {
        for (String id : sasaApplication.tileTypes.keySet())
        {
            tileTypeList.getItems().add(id);
        }
        tileCountLabel.setText("Currently added: " + String.valueOf(sasaApplication.total));
    }

}
