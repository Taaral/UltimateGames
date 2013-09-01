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
package me.ampayne2.ultimategames.scoreboards;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ArenaScoreboard {
    
    private Scoreboard scoreboard;
    private String name;

    /**
     * Creates an ArenaScoreboard.
     * @param name The name of the ArenaScoreboard.
     */
    public ArenaScoreboard(String name) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.scoreboard = scoreboard;
        this.name = name;
    }

    /**
     * Gets the name of an ArenaScoreboard.
     * @return The name of the ArenaScoreboard.
     */
    public String getName() {
        return name;
    }

    /**
     * Lets a player see an ArenaScoreboard.
     * @param playerName The name of the player.
     */
    public void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

    /**
     * Lets multiple players see an ArenaScoreboard.
     * @param playerNames The names of the players.
     */
    public void addPlayers(List<Player> players) {
        for (Player player : players) {
            player.setScoreboard(scoreboard);
        }
    }

    /**
     * Hides an ArenaScoreboard from a player.
     * @param playerName The name of the player.
     */
    public void removePlayer(Player player) {
        resetPlayerColor(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /**
     * Hides an ArenaScoreboard from multiple players.
     * @param playerNames The names of the players.
     */
    public void removePlayers(List<Player> players) {
        for (Player player : players) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
    
    /**
     * Sets a player's name to a certain color.
     * @param playerName The player.
     * @param chatColor The color.
     */
    public void setPlayerColor(Player player, ChatColor chatColor) {
        Team team;
        team = scoreboard.getTeam(player.getName());
        if (team != null) {
            team.setPrefix(chatColor + "");
        } else {
            team = scoreboard.registerNewTeam(player.getName());
            team.setPrefix(chatColor + "");
            team.addPlayer(player);
        }
    }
    
    /**
     * Resets a player's name's color.
     * @param playerName The player.
     */
    public void resetPlayerColor(Player player) {
        Team team = scoreboard.getTeam(player.getName());
        if (team != null) {
            team.removePlayer(player);
        }
    }

    /**
     * Resets an ArenaScoreboard.
     */
    public void reset() {
        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }
        scoreboard.getObjective(name).unregister();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Sets the visibility of an ArenaScoreboard.
     * @param visible Whether or not the ArenaScoreboard should be visible.
     */
    public void setVisible(Boolean visible) {
        if (visible) {
            scoreboard.getObjective(name).setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    /**
     * Gets the score of a scoreboard element.
     * @param name Name of the element.
     * @return The score.
     */
    public int getScore(String name) {
        return scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name)).getScore();
    }

    /**
     * Sets the score of a scoreboard element.
     * @param name Name of the element.
     */
    public void setScore(String name, Integer score) {
        scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name)).setScore(score);
    }

    /**
     * Resets the score of a scoreboard element.
     * @param name Name of the element.
     */
    public void resetScore(String name) {
        scoreboard.resetScores(Bukkit.getOfflinePlayer(name));
    }
}
