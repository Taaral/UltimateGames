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
package me.ampayne2.ultimategames.countdowns;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndingCountdown extends BukkitRunnable {
    
    private UltimateGames ultimateGames;
    private Arena arena;
    private int initialSeconds;
    private int secondsLeft;
    private Boolean expDisplay;
    private final int FINAL_COUNTDOWN_THRESHOLD = 10;
    private final int END_COUNTDOWN_TIME = 0;
    private final long SECOND_LENGTH = 20L;

    public EndingCountdown(UltimateGames ultimateGames, Arena arena, int initialSeconds, int secondsLeft, Boolean expDisplay) {
        this.ultimateGames = ultimateGames;
        this.arena = arena;
        this.initialSeconds = initialSeconds;
        this.secondsLeft = secondsLeft;
        this.expDisplay = expDisplay;
    }

    @Override
    public void run() {
        if (expDisplay) {
            for (String playerName : arena.getPlayers()) {
                Player player = Bukkit.getPlayer(playerName);
                player.setLevel(secondsLeft);
            }
        }
        if (secondsLeft > END_COUNTDOWN_TIME && secondsLeft <= FINAL_COUNTDOWN_THRESHOLD) {
            ultimateGames.getMessageManager().broadcastReplacedMessageToArena(arena, "countdowns.timeleftend", Integer.toString(secondsLeft));
        } else if (secondsLeft == END_COUNTDOWN_TIME) {
        	ultimateGames.getCountdownManager().stopEndingCountdown(arena);
            ultimateGames.getArenaManager().endArena(arena);
        }
        if (ultimateGames.getCountdownManager().isEndingCountdownEnabled(arena)) {
            new EndingCountdown(ultimateGames, arena, initialSeconds, secondsLeft - 1, expDisplay).runTaskLater(ultimateGames, SECOND_LENGTH);
        }
    }
}
