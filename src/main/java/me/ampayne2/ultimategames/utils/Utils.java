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
package me.ampayne2.ultimategames.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class Utils {

    private UltimateGames ultimateGames;
    private static final int LAST_DIGIT_DIVIDER = 10;
    private static final int LAST_TWO_DIGIT_DIVIDER = 100;
    private static final int LAST_TWO_DIGITS_ELEVEN = 11;
    private static final int LAST_TWO_DIGITS_TWELVE = 12;
    private static final int LAST_TWO_DIGITS_THIRTEEN = 13;
    private static final long AUTO_RESPAWN_DELAY = 1L;
    private static final String WORD_SEPARATOR = " ";
    private static final double TARGETER_ACCURACY = 0.8;

    public Utils(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Gets the ordinal suffix of an integer.
     * @param value The integer.
     * @return The ordinal suffix.
     */
    public String getOrdinalSuffix(int value) {
        Integer roundedValue = Math.abs(value);
        final int lastDigit = roundedValue % LAST_DIGIT_DIVIDER;
        final int last2Digits = roundedValue % LAST_TWO_DIGIT_DIVIDER;
        switch (lastDigit) {
            case 1:
                return last2Digits == LAST_TWO_DIGITS_ELEVEN ? "th" : "st";
            case 2:
                return last2Digits == LAST_TWO_DIGITS_TWELVE ? "th" : "nd";
            case 3:
                return last2Digits == LAST_TWO_DIGITS_THIRTEEN ? "th" : "rd";
            default:
                return "th";
        }
    }

    /**
     * Gets the name of a ChatColor.
     * @param chatColor The ChatColor.
     * @return The ChatColor's name.
     */
    public String getChatColorName(ChatColor chatColor) {
        String[] nameParts = chatColor.name().split("_");
        String[] capitalizedNameParts = new String[nameParts.length];
        for (int i = 0; i < nameParts.length; i++) {
            String namePart = nameParts[i];
            capitalizedNameParts[i] = Character.toUpperCase(namePart.charAt(0)) + namePart.substring(1).toLowerCase();
        }
        StringBuilder nameBuilder = new StringBuilder(capitalizedNameParts[0]);
        for (int i = 1; i < capitalizedNameParts.length; i++) {
            nameBuilder.append(WORD_SEPARATOR);
            nameBuilder.append(capitalizedNameParts[i]);
        }
        return nameBuilder.toString();
    }

    /**
     * Converts a ChatColor to a Color.
     * @param chatColor The ChatColor.
     * @return The Color.
     */
    public Color chatColorToColor(ChatColor chatColor) {
        Color color;
        switch (chatColor) {
            case BLACK:
                color = Color.BLACK;
                break;
            case DARK_BLUE:
                color = Color.NAVY;
                break;
            case DARK_GREEN:
                color = Color.GREEN;
                break;
            case DARK_AQUA:
                color = Color.AQUA;
                break;
            case DARK_RED:
                color = Color.MAROON;
                break;
            case DARK_PURPLE:
                color = Color.PURPLE;
                break;
            case GOLD:
                color = Color.ORANGE;
                break;
            case GRAY:
                color = Color.SILVER;
                break;
            case DARK_GRAY:
                color = Color.GRAY;
                break;
            case BLUE:
                color = Color.BLUE;
                break;
            case GREEN:
                color = Color.LIME;
                break;
            case AQUA:
                color = Color.TEAL;
            case RED:
                color = Color.RED;
                break;
            case LIGHT_PURPLE:
                color = Color.FUCHSIA;
                break;
            case YELLOW:
                color = Color.YELLOW;
                break;
            case WHITE:
                color = Color.WHITE;
                break;
            default:
                color = Color.WHITE;
        }
        return color;
    }

    /**
     * Creates an instruction book for a game.
     * @param title The book's title.
     * @param author The book's author.
     * @param pages The book's pages.
     * @return The book.
     */
    public ItemStack createInstructionBook(Game game) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(game.getName() + " Instructions");
        meta.setAuthor(game.getAuthor());
        for (String page : game.getInstructionPages()) {
            meta.addPage(ChatColor.translateAlternateColorCodes('&', page));
        }
        book.setItemMeta(meta);
        return book;
    }

    /**
     * Creates purple particles floating around a player pointing to each player on their radar.
     * @param playerName The player.
     * @param playersToScan Players to 'point' to.
     */
    public void radarScan(String playerName, List<String> playersToScan) {
        if (playersToScan != null && playerName != null) {
            Player player = Bukkit.getPlayerExact(playerName);
            Double playerX = player.getLocation().getX();
            Double playerY = player.getEyeLocation().getY();
            Double playerZ = player.getLocation().getZ();
            for (String nextPlayerToScan : playersToScan) {
                Player playerToScan = Bukkit.getPlayerExact(nextPlayerToScan);
                Double playerToScanX = playerToScan.getLocation().getX();
                Double playerToScanZ = playerToScan.getLocation().getZ();
                Double x = playerToScanX - playerX;
                Double z = playerToScanZ - playerZ;
                Double divisor = Math.sqrt((x * x) + (z * z)) / 2;
                Double relativeX = x / divisor;
                Double relativeZ = z / divisor;
                Location particleLocation = new Location(player.getWorld(), playerX + relativeX, playerY + 1, playerZ + relativeZ);
                ParticleEffect particleEffect = ParticleEffect.WITCH_MAGIC;
                particleEffect.play(player, particleLocation, 0, 0, 0, 0, 1);
            }
        }
    }

    /**
     * Closes a player's respawn screen 1 tick after called.
     * @param player The player.
     */
    public void autoRespawn(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ultimateGames, new Runnable() {
            @Override
            public void run() {
                try {
                    Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);

                    Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".Packet205ClientCommand").newInstance();
                    packet.getClass().getField("a").set(packet, 1);

                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                } catch (Exception e) {
                    ultimateGames.getMessageManager().debug(e);
                }
            }
        }, AUTO_RESPAWN_DELAY);
    }

    /**
     * Gets all the entities in a player's line of sight within a certain range.
     * @param player The player.
     * @param range The range.
     * @param maxEntities The maximum amount of entities the targeter will get. 0 for no limit.
     * @param highlightPath Whether or not it should 'highlight' the path with firework spark effects.
     * @param highlightEntities Whether or not it should 'highlight' each entity with an explosion effect.
     * @return The entities.
     */
    public Collection<Entity> getEntityTargets(Player player, double range, int maxEntities, Boolean goThroughWalls, Boolean highlightPath, Boolean highlightEntities) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        List<Entity> entities = player.getNearbyEntities(range, range, range);
        Set<Entity> targetedEntities = new HashSet<Entity>();
        int maxWorldHeight = location.getWorld().getMaxHeight();
        int distance = 0;
        while (distance < range && (maxEntities == 0 || targetedEntities.size() <= maxEntities)) {
            double locationX = location.getX();
            double locationY = location.getY();
            double locationZ = location.getZ();
            if ((goThroughWalls && location.getBlock().getType() != Material.AIR) || locationY < 0 || locationY > maxWorldHeight) {
                break;
            }
            if (highlightPath) {
                ParticleEffect.FIREWORKS_SPARK.play(location, 0, 0, 0, 0, 1);
            }
            for (Entity entity : entities) {
                if (isLocationInEntity(locationX, locationY, locationZ, entity)) {
                    if (targetedEntities.size() < maxEntities) {
                        targetedEntities.add(entity);
                        if (highlightEntities) {
                            ParticleEffect.HUGE_EXPLOSION.play(entity.getLocation(), 0, 0, 0, 0, 1);
                        }
                    } else {
                        break;
                    }
                }
            }
            location.add(direction);
            distance++;
        }
        return targetedEntities;
    }
    
    /**
     * Checks if a location is near or very close to an entity.
     * @param locationX The location's x value.
     * @param locationY The location's y value.
     * @param locationZ The location's z value.
     * @param entity The entity.
     * @return True if the location is within a certain range of the entity.
     */
    public Boolean isLocationInEntity(double locationX, double locationY, double locationZ, Entity entity) {
        Location entityLocation = entity.getLocation();
        double entityYLower = entityLocation.getY();
        double entityYHigher = entityYLower + getEntityHeight(entity);
        return Math.abs(locationX - entityLocation.getX()) <= TARGETER_ACCURACY && (locationY >= entityYLower && locationY <= entityYHigher) && Math.abs(locationZ - entityLocation.getZ()) <= TARGETER_ACCURACY;
    }

    /**
     * Gets an entities height.
     * @param entity The entity.
     * @return The entities height.
     */
    public double getEntityHeight(Entity entity) {
        double y;
        if (entity instanceof Player || entity instanceof Skeleton || entity instanceof Creeper || entity instanceof Zombie || entity instanceof PigZombie || entity instanceof Snowman
                || entity instanceof Villager || entity instanceof Blaze) {
            y = 2.0;
        } else if (entity instanceof Cow || entity instanceof Pig || entity instanceof Sheep || entity instanceof MushroomCow || entity instanceof MagmaCube || entity instanceof Slime) {
            y = 1.5;
        } else if (entity instanceof Chicken || entity instanceof Spider || entity instanceof Wolf) {
            y = 1.0;
        } else if (entity instanceof Ocelot || entity instanceof CaveSpider || entity instanceof Silverfish) {
            y = 0.5;
        } else if (entity instanceof Enderman) {
            y = 2.5;
        } else if (entity instanceof Squid) {
            y = 0.1;
        } else {
            y = 1.0;
        }
        return y;
    }

    /**
     * Colors leather armor.
     * @param itemStack The piece of armor.
     * @param color The color.
     * @return The colored piece of armor.
     */
    public ItemStack colorArmor(ItemStack itemStack, Color color) {
        if (itemStack.getType() == Material.LEATHER_HELMET || itemStack.getType() == Material.LEATHER_CHESTPLATE || itemStack.getType() == Material.LEATHER_LEGGINGS
                || itemStack.getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
            meta.setColor(color);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    /**
     * Checks if a material has physics.
     * @param material The material.
     * @return True if the material has physics, else false.
     */
    public Boolean hasPhysics(Material material) {
        switch (material) {
            case ACTIVATOR_RAIL:
            case ANVIL:
            case CARPET:
            case CACTUS:
            case CROPS:
            case DEAD_BUSH:
            case DRAGON_EGG:
            case FIRE:
            case FLOWER_POT:
            case GRAVEL:
            case IRON_DOOR_BLOCK:
            case LEVER:
            case LONG_GRASS:
            case MELON_STEM:
            case NETHER_WARTS:
            case POWERED_RAIL:
            case RAILS:
            case RED_MUSHROOM:
            case RED_ROSE:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case REDSTONE_WIRE:
            case SAND:
            case SAPLING:
            case STONE_BUTTON:
            case SUGAR_CANE_BLOCK:
            case TORCH:
            case TRAP_DOOR:
            case TRIPWIRE_HOOK:
            case WATER_LILY:
            case WOOD_BUTTON:
            case WOODEN_DOOR:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if a block is attached to a block.
     * @param attached The block that might be attached.
     * @param block The support block.
     * @return True if the block is attached to the block, else false.
     */
    public Boolean isAttachedToBlock(Block attached, Block block) {
        MaterialData data = attached.getState().getData();
        return data instanceof Attachable && attached.getRelative(((Attachable) data).getAttachedFace()).equals(block);
    }

}
