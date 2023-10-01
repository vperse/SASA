package com.vperse.sasa.util;

public class Position
{
    public int x;
    public int y;

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void print()
    {
        System.out.print(x + " " + y);
    }

}
