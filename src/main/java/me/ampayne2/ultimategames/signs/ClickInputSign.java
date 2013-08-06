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
package me.ampayne2.ultimategames.signs;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.SignType;

import org.bukkit.block.Sign;
import org.bukkit.event.Event;

public class ClickInputSign extends UGSign {
    private String label;
    private String[] lines = new String[4];

    public ClickInputSign(String label, Sign sign, Arena arena) {
        super(sign, arena);
        this.label = label;
        arena.getGame().getGamePlugin().handleUGSignCreate(this, SignType.getSignTypeFromClass(this.getClass()));
        update();
    }

    @Override
    public void onSignTrigger(Event event) {
        getArena().getGame().getGamePlugin().handleInputSignTrigger(this, SignType.getSignTypeFromClass(this.getClass()), event);
    }

    @Override
    public String[] getUpdatedLines() {
        return lines;
    }

    public void setLines(String[] lines) {
    	this.lines = lines;
    }
    
    public String getLabel() {
    	return label;
    }
}