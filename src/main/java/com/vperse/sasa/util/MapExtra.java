package com.vperse.sasa.util;

import java.util.*;

public class MapExtra
{

    public static <K, V> Map<K, V> clone(Map<K, V> map)
    {
        Map<K, V> clone = new HashMap<>();

        for (K key : map.keySet())
        {
            clone.put(key, map.get(key));
        }

        return clone;
    }

    public static <K, V> Map.Entry<K, V> getRandomEntry(Map<K, V> map)
    {
        if (map.isEmpty()) return null;

        Random rand = new Random();
        Set<Map.Entry<K, V>> entries = map.entrySet();

        int index = rand.nextInt(entries.size());
        int i = 0;

        for(Map.Entry<K, V> entry : entries)
        {
            if (i == index) return entry;
            i++;
        }

        return null;
    }

}
