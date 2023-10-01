package com.vperse.sasa.logic;

import com.vperse.sasa.SASAApplication;
import com.vperse.sasa.util.Direction;
import com.vperse.sasa.util.MapExtra;
import com.vperse.sasa.util.Position;

import java.io.IOException;
import java.util.*;

public class Simulation
{

    public static final int ROWS = 1000;
    public static final int COLS = 1000;

    private SASAApplication sasaApplication;

    private final Map<String, TileType> types;
    private final TileType[][] tiles;
    private final Map<Position, TileType> tasks;
    private final List<TileType[][]> frames;

    public Simulation()
    {
        sasaApplication = SASAApplication.getApplicationInstance();
        this.types = MapExtra.clone(sasaApplication.tileTypes);
        this.tiles = new TileType[ROWS][COLS];
        this.tasks = new HashMap<>();
        this.frames = new ArrayList<>();

        TileType seed = types.getOrDefault("SEED", new TileType());
        this.tiles[ROWS / 2][COLS / 2] = seed;
        this.types.remove(seed.id);

        this.tasks.put(new Position(COLS / 2, ROWS / 2), seed);
        this.frames.add(screenshot());
    }

    public void start()
    {
        Random rand = new Random();

        while (!tasks.isEmpty())
        {
            Map.Entry<Position, TileType> task = MapExtra.getRandomEntry(tasks);
            if (task == null) return;

            Map<Position, TileType> candidates = new HashMap<>();

            for (String tpId : types.keySet())
            {
                addCandidate(task.getKey(), types.get(tpId), -1, 0, candidates);
                addCandidate(task.getKey(), types.get(tpId), 0, -1, candidates);
                addCandidate(task.getKey(), types.get(tpId), 1, 0, candidates);
                addCandidate(task.getKey(), types.get(tpId), 0, 1, candidates);
            }

            Map.Entry<Position, TileType> randCandidate = MapExtra.getRandomEntry(candidates);

            if (randCandidate == null)
            {
                tasks.remove(task.getKey());
                continue;
            }

            tiles[randCandidate.getKey().y][randCandidate.getKey().x] = randCandidate.getValue();
            tasks.put(randCandidate.getKey(), randCandidate.getValue());
            if (--types.get(randCandidate.getValue().id).amount == 0) types.remove(randCandidate.getValue().id);
            frames.add(screenshot());
        }
    }

    private void addCandidate(Position pos, TileType tp, int xDir, int yDir, Map<Position, TileType> candidates)
    {
        Position candidatePos = new Position(pos.x + xDir, pos.y + yDir);
        if (fits(candidatePos, tp)) candidates.put(candidatePos, tp);
    }

    private boolean fits(Position pos, TileType tp)
    {
        if (pos.x < 0 || pos.y < 0) return false;
        if (pos.x > COLS - 1 || pos.y > ROWS - 1) return false;
        if (tiles[pos.y][pos.x] != null) return false;

        if (pos.x > 0 &&
                tiles[pos.y][pos.x - 1] != null &&
                !tp.connects(tiles[pos.y][pos.x - 1], Direction.LEFT))
            return false;

        if (pos.y > 0 &&
                tiles[pos.y - 1][pos.x] != null &&
                !tp.connects(tiles[pos.y - 1][pos.x], Direction.TOP))
            return false;

        if (pos.x < COLS - 1 &&
                tiles[pos.y][pos.x + 1] != null &&
                !tp.connects(tiles[pos.y][pos.x + 1], Direction.RIGHT))
            return false;

        return pos.y >= ROWS - 1 ||
                tiles[pos.y + 1][pos.x] == null ||
                tp.connects(tiles[pos.y + 1][pos.x], Direction.BOTTOM);
    }

    private TileType[][] screenshot()
    {
        TileType[][] frame = new TileType[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) System.arraycopy(tiles[i], 0, frame[i], 0, COLS);
        return frame;
    }

    public TileType[][] getTiles()
    {
        return this.tiles;
    }

    public List<TileType[][]> getFrames()
    {
        return this.frames;
    }

    public void print() {
        for (TileType[][] frame : frames)
        {
            printFrame(frame);
        }
    }

    public void printFrame(TileType[][] frame)
    {
        for (int i = 0; i < ROWS; i++)
        {
            for (int j = 0; j < COLS; j++)
            {
                if (frame[i][j] != null)
                {
                    System.out.println(frame[i][j].id + " at x=" + j + ", y=" + i);
                }
            }
        }
        System.out.println();
    }

}
