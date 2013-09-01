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
package me.ampayne2.ultimategames;

import java.io.IOException;

import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.games.Game;

public class MetricsManager implements Manager {

    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private Metrics metrics;
    private Graph gamesLoadedGraph;
    private Graph arenasLoadedGraph;
    private Graph totalArenasPlayedGraph;
    private Graph arenasBeingPlayedGraph;
    private Graph totalPlayersInArenasGraph;
    private Graph playersInArenasGraph;

    public MetricsManager(final UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }
    
    @Override
    public boolean load() {
        try {
            metrics = new Metrics(ultimateGames);

            gamesLoadedGraph = metrics.createGraph("Games Loaded");
            arenasLoadedGraph = metrics.createGraph("Arenas Loaded");
            totalArenasPlayedGraph = metrics.createGraph("Total Arenas Played");
            arenasBeingPlayedGraph = metrics.createGraph("Arenas Currently Being Played");
            playersInArenasGraph = metrics.createGraph("Players Currently In Arenas");

            metrics.start();
        } catch (IOException e) {
            ultimateGames.getMessageManager().debug(e);
        }
        loaded = true;
        return true;
    }

    @Override
    public boolean reload() {
        removeTotalPlayersGraph();
        addTotalPlayersGraph();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            removeGame(game);
            addGame(game);
        }
        for (Arena arena : ultimateGames.getArenaManager().getArenas()) {
            removeArena(arena);
            addArena(arena);
        }
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        removeTotalPlayersGraph();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            removeGame(game);
        }
        for (Arena arena : ultimateGames.getArenaManager().getArenas()) {
            removeArena(arena);
        }
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public void addGame(final Game game) {
        gamesLoadedGraph.addPlotter(new Metrics.Plotter(game.getName()) {
            @Override
            public int getValue() {
                return 1;
            }
        });
    }

    public void removeGame(final Game game) {
        gamesLoadedGraph.removePlotter(new Metrics.Plotter(game.getName()) {
            @Override
            public int getValue() {
                return 1;
            }
        });
    }

    public void addTotalPlayersGraph() {
        totalPlayersInArenasGraph = metrics.createGraph("Total Players In Arenas");
        totalPlayersInArenasGraph.addPlotter(new Metrics.Plotter("Total Players In Arenas") {
            @Override
            public int getValue() {
                Integer playersInArenas = 0;
                for (Arena arena : ultimateGames.getArenaManager().getArenas()) {
                    playersInArenas += arena.getPlayers().size();
                }
                return playersInArenas;
            }
        });
    }
    
    public void removeTotalPlayersGraph() {
        totalPlayersInArenasGraph.removePlotter(new Metrics.Plotter("Total Players In Arenas") {
            @Override
            public int getValue() {
                Integer playersInArenas = 0;
                for (Arena arena : ultimateGames.getArenaManager().getArenas()) {
                    playersInArenas += arena.getPlayers().size();
                }
                return playersInArenas;
            }
        });
    }

    public void addArena(final Arena arena) {
        arenasLoadedGraph.addPlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return 1;
            }
        });
        totalArenasPlayedGraph.addPlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getTimesPlayed();
            }
        });
        arenasBeingPlayedGraph.addPlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        playersInArenasGraph.addPlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getPlayers().size();
            }
        });
    }

    public void removeArena(final Arena arena) {
        arenasLoadedGraph.removePlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return 1;
            }
        });
        totalArenasPlayedGraph.removePlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getTimesPlayed();
            }
        });
        arenasBeingPlayedGraph.removePlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        playersInArenasGraph.removePlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getPlayers().size();
            }
        });
    }

}
