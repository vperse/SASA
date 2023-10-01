package com.vperse.sasa.util;

import com.vperse.sasa.SASAApplication;
import com.vperse.sasa.logic.Connector;
import com.vperse.sasa.logic.TileType;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TAMManager
{
    private SASAApplication sasaApplication;
    private File file;
    private final BufferedReader reader;

    public TAMManager(File file) throws IOException
    {
        this.sasaApplication = SASAApplication.getApplicationInstance();
        this.file = file;
        this.reader = new BufferedReader(new FileReader(file));
    }

    public void readFile() throws IOException
    {
        // TODO: Add checkers!
        Map<String, TileType> tileTypes = new HashMap<>();
        int total = 0;

        while (reader.ready())
        {
            String id = reader.readLine();
            int amount = Integer.parseInt(reader.readLine());
            Color color = Color.web(reader.readLine().toUpperCase());

            Connector left = readConnector();
            Connector top = readConnector();
            Connector right = readConnector();
            Connector bottom = readConnector();

            tileTypes.put(id, new TileType(id, color, amount, left, top, right, bottom));
            total += amount;

            reader.readLine();
        }

        sasaApplication.tileTypes = tileTypes;
        sasaApplication.total = total;
    }

    private Connector readConnector() throws IOException
    {
        String[] data = reader.readLine().split(" ");
        if (data[0].equals("null")) return null;
        return new Connector(data[0], Color.web(data[1]));
    }

}
