/*
 * This file is part of UltimateGames.
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames. If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.scoreboards;

import java.util.HashMap;
import java.util.Map;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.arenas.Arena;

public class ScoreboardManager implements Manager {

    private boolean loaded = false;
    private Map<Arena, ArenaScoreboard> scoreboards = new HashMap<Arena, ArenaScoreboard>();

    @Override
    public boolean load() {
        loaded = true;
        return true;
    }

    @Override
    public boolean reload() {
        scoreboards.clear();
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        scoreboards.clear();
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Gets the ArenaScoreboard of an arena.
     * @param arena The arena.
     * @return The arena's ArenaScoreboards.
     */
    public ArenaScoreboard getArenaScoreboard(Arena arena) {
        return scoreboards.containsKey(arena) ? scoreboards.get(arena) : null;
    }

    /**
     * Creates an ArenaScoreboard for an arena.
     * @param arena The arena.
     * @param name Name of the ArenaScoreboard.
     * @return The ArenaScoreboard created.
     */
    public ArenaScoreboard createArenaScoreboard(Arena arena, String name) {
        ArenaScoreboard scoreboard = new ArenaScoreboard(name);
        return scoreboards.put(arena, scoreboard);
    }

    /**
     * Removes an Arena's ArenaScoreboard from the manager.
     * @param arena The arena.
     */
    public void removeArenaScoreboard(Arena arena) {
        if (scoreboards.containsKey(arena)) {
            ArenaScoreboard scoreboard = scoreboards.get(arena);
            scoreboard.reset();
            scoreboards.remove(arena);
        }
    }

}
