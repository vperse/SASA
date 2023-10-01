package com.vperse.sasa.logic;

import com.vperse.sasa.util.Direction;
import javafx.scene.paint.Color;

public class TileType
{

    public String id;
    public Color color;
    public int amount;
    public Connector leftConnector, topConnector, rightConnector, bottomConnector;

    public TileType()
    {
        this.id = "SEED";
        this.color = Color.DODGERBLUE;
        this.amount = 1;
    }

    public TileType(String id, Color color, int amount,
                    Connector leftConnector, Connector topConnector,
                    Connector rightConnector, Connector bottomConnector)
    {
        this.id = id;
        this.color = color;
        this.amount = amount;
        this.leftConnector = leftConnector;
        this.topConnector = topConnector;
        this.rightConnector = rightConnector;
        this.bottomConnector = bottomConnector;
    }

    public boolean hasConnector(Direction direction)
    {
        return switch (direction)
        {
            case LEFT -> this.leftConnector != null;
            case TOP -> this.topConnector != null;
            case RIGHT -> this.rightConnector != null;
            case BOTTOM -> this.bottomConnector != null;
        };
    }

    public boolean connects(TileType tileType, Direction direction)
    {
        return switch (direction) {
            case LEFT ->
                    hasConnector(Direction.LEFT)
                            && tileType.hasConnector(Direction.RIGHT)
                            && leftConnector.connects(tileType.rightConnector);
            case TOP ->
                    hasConnector(Direction.TOP)
                            && tileType.hasConnector(Direction.BOTTOM)
                            && topConnector.connects(tileType.bottomConnector);
            case RIGHT ->
                    hasConnector(Direction.RIGHT)
                            && tileType.hasConnector(Direction.LEFT)
                            && rightConnector.connects(tileType.leftConnector);
            case BOTTOM ->
                    hasConnector(Direction.BOTTOM)
                            && tileType.hasConnector(Direction.TOP)
                            && bottomConnector.connects(tileType.topConnector);
        };
    }

    public Connector getConnector(Direction direction)
    {
        return switch (direction)
        {
            case LEFT -> this.leftConnector;
            case TOP -> this.topConnector;
            case RIGHT -> this.rightConnector;
            case BOTTOM -> this.bottomConnector;
        };
    }

    public void setConnector(Connector connector, Direction direction)
    {
        switch (direction)
        {
            case LEFT -> this.leftConnector = connector;
            case TOP -> this.topConnector = connector;
            case RIGHT -> this.rightConnector = connector;
            case BOTTOM -> this.bottomConnector = connector;
        }
    }

    public void print()
    {
        System.out.println(id);
        System.out.println(amount);
        System.out.println(color.toString());

        if (hasConnector(Direction.LEFT)) {
            leftConnector.print();
        } else System.out.println("null");

        if (hasConnector(Direction.TOP)) {
            topConnector.print();
        } else System.out.println("null");

        if (hasConnector(Direction.RIGHT)) {
            rightConnector.print();
        } else System.out.println("null");

        if (hasConnector(Direction.BOTTOM)) {
            bottomConnector.print();
        } else System.out.println("null");
    }

}
