package com.vperse.sasa.editor;

import com.vperse.sasa.util.Direction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileEditorController {

    @FXML
    private Button bottomConnectorAddButton;

    @FXML
    private TextField bottomConnectorIDTextField;

    @FXML
    private Rectangle bottomConnectorRectangle;

    @FXML
    private Button bottomConnectorRemoveButton;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button leftConnectorAddButton;

    @FXML
    private TextField leftConnectorIDTextField;

    @FXML
    private Rectangle leftConnectorRectangle;

    @FXML
    private Button leftConnectorRemoveButton;

    @FXML
    private Button rightConnectorAddButton;

    @FXML
    private TextField rightConnectorIDTextField;

    @FXML
    private Rectangle rightConnectorRectangle;

    @FXML
    private Button rightConnectorRemoveButton;

    @FXML
    private TextField tileIDTextField;

    @FXML
    private Rectangle tileRectangle;

    @FXML
    private Button topConnectorAddButton;

    @FXML
    private TextField topConnectorIDTextField;

    @FXML
    private Rectangle topConnectorRectangle;

    @FXML
    private Button topConnectorRemoveButton;

    @FXML
    private void addConnector(ActionEvent event)
    {
        Button target = (Button) event.getSource();
        String id = target.getId();
        Direction dir = getDirection(id);
        addConnector(dir);
    }

    public void addConnector(Direction dir)
    {
        getConnectorAddBtn(dir).setVisible(false);
        getConnectorRect(dir).setVisible(true);
        getConnectorRmvBtn(dir).setVisible(true);
        getConnectorIdTxtField(dir).setVisible(true);
    }

    @FXML
    private void rmvConnector(ActionEvent event)
    {
        Button target = (Button) event.getSource();
        String id = target.getId();
        Direction dir = getDirection(id);
        rmvConnector(dir);
    }

    public void rmvConnector(Direction dir)
    {
        getConnectorRmvBtn(dir).setVisible(false);
        getConnectorIdTxtField(dir).setVisible(false);
        getConnectorIdTxtField(dir).setText("");
        getConnectorRect(dir).setVisible(false);
        getConnectorRect(dir).setFill(Color.DODGERBLUE);
        getConnectorAddBtn(dir).setVisible(true);
    }

    @FXML
    private void changeColor(MouseEvent event)
    {
        Rectangle target = (Rectangle) event.getSource();
        colorPicker.setOnAction(ActionEvent -> target.setFill(colorPicker.getValue()));
        colorPicker.show();
    }

    @FXML
    private void desaturate(MouseEvent event)
    {
        Rectangle target = (Rectangle) event.getSource();
        target.setFill(((Color)target.getFill()).desaturate());
    }

    @FXML
    private void saturate(MouseEvent event)
    {
        Rectangle target = (Rectangle) event.getSource();
        target.setFill(((Color)target.getFill()).saturate());
    }

    public Button getConnectorAddBtn(Direction dir)
    {
        return switch (dir)
        {
            case LEFT -> this.leftConnectorAddButton;
            case TOP -> this.topConnectorAddButton;
            case RIGHT -> this.rightConnectorAddButton;
            case BOTTOM -> this.bottomConnectorAddButton;
        };
    }

    public Button getConnectorRmvBtn(Direction dir)
    {
        return switch (dir)
        {
            case LEFT -> this.leftConnectorRemoveButton;
            case TOP -> this.topConnectorRemoveButton;
            case RIGHT -> this.rightConnectorRemoveButton;
            case BOTTOM -> this.bottomConnectorRemoveButton;
        };
    }

    public Rectangle getTileRectangle()
    {
        return this.tileRectangle;
    }

    public Rectangle getConnectorRect(Direction dir)
    {
        return switch (dir)
        {
            case LEFT -> this.leftConnectorRectangle;
            case TOP -> this.topConnectorRectangle;
            case RIGHT -> this.rightConnectorRectangle;
            case BOTTOM -> this.bottomConnectorRectangle;
        };
    }

    public TextField getIdTxtField()
    {
        return this.tileIDTextField;
    }

    public TextField getConnectorIdTxtField(Direction dir)
    {
        return switch (dir)
        {
            case LEFT -> this.leftConnectorIDTextField;
            case TOP -> this.topConnectorIDTextField;
            case RIGHT -> this.rightConnectorIDTextField;
            case BOTTOM -> this.bottomConnectorIDTextField;
        };
    }

    public Direction getDirection(String id)
    {
        if (id.startsWith("left")) return Direction.LEFT;
        if (id.startsWith("top")) return Direction.TOP;
        if (id.startsWith("right")) return Direction.RIGHT;
        return Direction.BOTTOM;
    }

}
