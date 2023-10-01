package com.vperse.sasa;

import com.vperse.sasa.logic.TileType;
import com.vperse.sasa.mainmenu.MainMenuSceneController;
import com.vperse.sasa.util.ResourceLoader;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SASAApplication extends Application
{
    private static SASAApplication SASAAppInstance;
    public static final int MAX_TOTAL = 1000;
    public Map<String, TileType> tileTypes;
    public int total;

    @Override
    public void init()
    {
        SASAAppInstance = this;
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        URL iconUrl = SASAApplication.class.getResource("images/sasa-logo.png");
        Image icon = new Image(Objects.requireNonNull(iconUrl).openStream());
        stage.getIcons().add(icon);

        stage.setTitle("SASA - Simulating Algorithmic Self Assembly");
        stage.setResizable(false);

        ResourceLoader.loadScene(
                "views/main-menu-view.fxml", stage,
                MainMenuSceneController.WIDTH,
                MainMenuSceneController.HEIGHT
        );
    }

    public static void main(String[] args)
    {
        launch();
    }

    public static SASAApplication getApplicationInstance()
    {
        return SASAAppInstance;
    }


    public void printTypes()
    {
        for (String id : tileTypes.keySet())
        {
            tileTypes.get(id).print();
        }
    }

}