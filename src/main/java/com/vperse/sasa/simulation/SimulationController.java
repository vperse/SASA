package com.vperse.sasa.simulation;

import com.vperse.sasa.logic.Simulation;
import com.vperse.sasa.logic.TileType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SimulationController implements Initializable
{

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static final int CANVAS_WIDTH = 1000;
    public static final int CANVAS_HEIGHT = 1000;

    private ZoomableScrollPane canvasHolderPane;
    public Canvas canvas;
    public GraphicsContext ctx;
    public List<TileType[][]> frames;
    public int frameIndex;

    @FXML
    private BorderPane simulationPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.ctx = canvas.getGraphicsContext2D();

        this.canvasHolderPane = new ZoomableScrollPane(canvas);
        simulationPane.setCenter(canvasHolderPane);

        Simulation simulation = new Simulation();
        simulation.start();
        frames = simulation.getFrames();
        frameIndex = frames.size() - 1;

        draw();
    }

    public void draw()
    {
        PixelWriter pixelWriter = ctx.getPixelWriter();
        TileType[][] frame = frames.get(frameIndex);

        for (int y = 0; y < frame.length; y++)
        {
            for (int x = 0; x < frame[y].length; x++)
            {
                if (frame[y][x] != null) pixelWriter.setColor(x, y, frame[y][x].color);
            }
        }
    }

}
