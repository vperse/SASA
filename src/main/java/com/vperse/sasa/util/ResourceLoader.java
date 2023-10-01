package com.vperse.sasa.util;

import com.vperse.sasa.SASAApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ResourceLoader
{

    public static void loadScene(String path, Stage stage, int width, int height)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(SASAApplication.class.getResource(path));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), width, height);
        } catch (IOException e)
        {
            System.out.println("Could not load: " + path + "!");
            e.printStackTrace();
            System.exit(-1); // Error type -1: Could not load resource!
        }

        stage.setScene(scene);
        stage.show();
    }

}
