package com.vperse.sasa.logic;

import javafx.scene.paint.Color;

public class Connector
{

    public String id;
    public Color color;

    public Connector(String id, Color color)
    {
        this.id = id;
        this.color = color;
    }

    public boolean connects(Connector connector)
    {
        return this.id.equals(connector.id);
    }

    public void print()
    {
        System.out.println(id + " " + color.toString());
    }

}
