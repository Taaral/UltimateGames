/*
 * This file is part of UltimateGames.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.arenas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.UltimateGames;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnpointManager implements Manager {
    
    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private Map<Arena, List<SpawnPoint>> spawnPoints = new HashMap<Arena, List<SpawnPoint>>();

    public SpawnpointManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }
    
    @Override
    public boolean load() {
        loaded = true;
        return true;
    }

    @Override
    public boolean reload() {
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        spawnPoints.clear();
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Checks if an arena has a spawnpoint at the index.
     * @param arena The arena.
     * @param index The index.
     * @return If the arena has a spawnpoint at the index or not.
     */
    public Boolean hasSpawnPointAtIndex(Arena arena, Integer index) {
        return spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() >= index;
    }

    /**
     * Adds a spawnpoint to the manager.
     * @param spawnPoint The spawnpoint.
     */
    public void addSpawnPoint(SpawnPoint spawnPoint) {
        if (spawnPoints.containsKey(spawnPoint.getArena())) {
            spawnPoints.get(spawnPoint.getArena()).add(spawnPoint);
        } else {
            List<SpawnPoint> spawn = new ArrayList<SpawnPoint>();
            spawn.add(spawnPoint);
            spawnPoints.put(spawnPoint.getArena(), spawn);
        }
        ultimateGames.getServer().getPluginManager().registerEvents(spawnPoint, ultimateGames);
    }

    /**
     * Creates a spawnpoint.
     * The spawnpoint is added to the manager and config.
     * @param arena The Arena.
     * @param location The location.
     * @param locked If the spawnpoint prevents the player from moving off of it.
     * @return The spawnpoint created.
     */
    public SpawnPoint createSpawnPoint(Arena arena, Location location, Boolean locked) {
        List<String> newSpawnPoint = new ArrayList<String>();
        newSpawnPoint.add(String.valueOf(location.getX()));
        newSpawnPoint.add(String.valueOf(location.getY()));
        newSpawnPoint.add(String.valueOf(location.getZ()));
        newSpawnPoint.add(String.valueOf(location.getPitch()));
        newSpawnPoint.add(String.valueOf(location.getYaw()));
        newSpawnPoint.add(String.valueOf(locked));
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        String path = "Arenas." + arena.getGame().getName() + "." + arena.getName() + ".SpawnPoints";
        if (ultimateGames.getConfigManager().getArenaConfig().getConfig().contains(path)) {
            @SuppressWarnings("unchecked")
            List<List<String>> arenaSpawnPoints = (ArrayList<List<String>>) arenaConfig.getList(path);
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        } else {
            List<List<String>> arenaSpawnPoints = new ArrayList<List<String>>();
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        }
        ultimateGames.getConfigManager().getArenaConfig().saveConfig();
        SpawnPoint spawnPoint = new SpawnPoint(ultimateGames, arena, location, locked);
        addSpawnPoint(spawnPoint);
        return spawnPoint;
    }

    /**
     * Get a specific spawnpoint of an arena.
     * @param arena The arena.
     * @param index The spawnpoint index.
     * @return The spawnpoint.
     */
    public SpawnPoint getSpawnPoint(Arena arena, Integer index) {
        if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() >= index) {
            return spawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Get a random spawnpoint of an arena.
     * @param arena The arena.
     * @return The spawnpoint.
     */
    public SpawnPoint getRandomSpawnPoint(Arena arena) {
        if (spawnPoints.containsKey(arena)) {
            Random generator = new Random();
            Integer index = generator.nextInt(spawnPoints.get(arena).size());
            return spawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Get a random spawnpoint of an arena at or above the minIndex.
     * Useful if there are certain spawnpoints only used for specific things.
     * @param arena The arena.
     * @param minIndex The minimum index.
     * @return The spawnpoint.
     */
    public SpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex) {
        if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() > minIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(spawnPoints.get(arena).size() - minIndex) + minIndex;
            return spawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Get a random spawnpoint of an arena within the minIndex and maxIndex.
     * Useful if there are certain spawnpoints only used for specific things.
     * @param arena The arena.
     * @param minIndex The minimum index.
     * @param maxIndex The maximum index.
     * @return The spawnpoint.
     */
    public SpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex, Integer maxIndex) {
        if (spawnPoints.containsKey(arena) && minIndex < maxIndex && spawnPoints.get(arena).size() > maxIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(maxIndex - minIndex + 1) + minIndex;
            return spawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Get a certain amount of spawnpoints distributed evenly among the available spawnpoints.
     * Example: You have 8 spawn points. You ask for 4. It gives you the spawnpoints at indexes 0, 2, 4, and 6.
     * Example 2: You have 16 spawn points. You ask for 2. It gives you the spawnpoints at indexes 0 and 8.
     * Useful for spawnpoint setups similar to survival games where players need to be spaced out.
     * @param arena The arena.
     * @param amount The amount of spawnpoints to get.
     * @return The spawnpoints.
     */
    public List<SpawnPoint> getDistributedSpawnPoints(Arena arena, Integer amount) {
        if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() >= amount) {
            List<SpawnPoint> distributedSpawnPoints = new ArrayList<SpawnPoint>();
            Integer size = spawnPoints.get(arena).size();
            Double multiple = (double) size / (double) amount;
            for (int i = 0; i < amount; i++) {
                Integer index = (int) Math.round(i * multiple);
                distributedSpawnPoints.add(spawnPoints.get(arena).get(index));
            }
            return distributedSpawnPoints;
        }
        return null;
    }

    /**
     * Gets all the spawnpoints of an arena.
     * @param arena The arena.
     * @return The spawnpoints.
     */
    public List<SpawnPoint> getSpawnPointsOfArena(Arena arena) {
        if (spawnPoints.containsKey(arena)) {
            return spawnPoints.get(arena);
        } else {
            return null;
        }
    }

    /**
     * Removes the spawnpoint at a certain index.
     * @param arena The arena.
     * @param index The index.
     */
    public void removeSpawnPoint(Arena arena, Integer index) {
        if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() >= index) {
            spawnPoints.get(arena).remove(index);
            //TODO: Remove spawnpoint from arena config.
        }
    }

    /**
     * Removes the spawnpoints at the indexes.
     * @param arena The arena.
     * @param indexes The indexes.
     */
    public void removeSpawnPoints(Arena arena, Integer... indexes) {
        if (spawnPoints.containsKey(arena)) {
            ArrayList<SpawnPoint> remove = new ArrayList<SpawnPoint>();
            for (Integer index : indexes) {
                if (spawnPoints.get(arena).size() >= index) {
                    remove.add(spawnPoints.get(arena).get(index));
                }
            }
            if (remove != null) {
                spawnPoints.get(arena).removeAll(remove);
            }
            //TODO: Remove spawnpoints from arena config.
        }
    }

    /**
     * Removes all the spawnpoints of an arena.
     * @param arena The arena.
     */
    public void removeAllSpawnPoints(Arena arena) {
        if (spawnPoints.containsKey(arena)) {
            spawnPoints.remove(arena);
            //TODO: Remove spawnpoints from arena config.
        }
    }
    
}
