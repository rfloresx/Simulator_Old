package com.github.otrebor4.simulator.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.otrebor4.simulator.Settings;
import com.github.otrebor4.simulator.Simulator;
import com.github.otrebor4.simulator.SP.SimulatedPlayer;
import com.google.common.base.Splitter;

public class Messaging {
    private static final Logger log = Logger.getLogger("Minecraft");
    private final static String[] colours = { "black", "dblue", "dgreen", "daqua", "dred", "dpurple", "gold", "gray",
            "dgray", "blue", "green", "aqua", "red", "lpurple", "yellow", "white" };
    private static final int DELAY_STR_LENGTH = "<delay".length();

    public static void send(CommandSender sender, String message) {
        send(sender, null, message);
    }

    public static void delay(final Runnable runnable, String messages) {
        int index = messages.indexOf("<delay");
        if (index == -1) {
            runnable.run();
            return;
        }
        int totalDelay = 0;
        while (index != -1) {
            index += DELAY_STR_LENGTH;
            String assignment = messages.substring(index, messages.indexOf(">", index));
            totalDelay += assignment.length() <= 1 ? 1 : Integer.parseInt(assignment.substring(1, assignment.length()));
            messages = messages.substring(index + 1 + assignment.length(), messages.length());
            index = messages.indexOf("<delay");
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Simulator.plugin, new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }, totalDelay);
    }

    public static void send(final CommandSender sender, final SimulatedPlayer SP, String messages) {
        int index = messages.indexOf("<delay");
        if (index != -1) {
            index += DELAY_STR_LENGTH;
            String assignment = messages.substring(index, messages.indexOf(">", index));
            int delay = assignment.length() <= 1 ? 1 : Integer.parseInt(assignment.substring(1, assignment.length()));
            send(sender, SP, messages.substring(0, index - DELAY_STR_LENGTH));
            final String substr = messages.substring(index + 1 + assignment.length(), messages.length());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Simulator.plugin, new Runnable() {
                @Override
                public void run() {
                    send(sender, SP, substr);
                }
            }, delay);
            return;
        }
        for (String message : Splitter.on("<br>").omitEmptyStrings().split(messages)) {
            if (sender instanceof Player) {
                Player player = ((Player) sender);
                message = message.replace("<h>", "" + player.getHealth());
                message = message.replace("<name>", player.getName());
                message = message.replace("<world>", player.getWorld().getName());
            }
            message = colourise(StringUtils.colourise(message));
            if (SP != null) {
                message = message.replace("<SP>", SP.getName());
            }
            sender.sendMessage(message);
        }
    }

    public static void log(Object... messages) {
        StringBuilder builder = new StringBuilder();
        for (Object string : messages) {
            builder.append(string == null ? "null " : string.toString() + " ");
        }
        log(builder.toString(), Level.INFO);
    }

    public static void log(Object message, Level level) {
        log.log(level, "[Simulator] " + message );
    }

    public static void log(Object message) {
        log(message, Level.INFO);
    }

    public static void debug(Object message) {
        if (Settings.getBoolean("DebugMode")) {
            log(message);
        }
    }

    public static void debug(Object... messages) {
        if (Settings.getBoolean("DebugMode")) {
            log(messages);
        }
    }

    public static void sendError(Player player, String error) {
        send(player, null, ChatColor.RED + error);
    }

    public static void sendError(CommandSender sender, String error) {
        send(sender, null, ChatColor.RED + error);
    }

    public static void sendUncertain(String name, String message) {
        Player player = Bukkit.getServer().getPlayer(name);
        if (player != null) {
            send(player, null, message);
        }
    }

    private static String colourise(String message) {
        String format = "<%s>";
        char index = '0';
        for (String colour : colours) {
            message = message.replaceAll(String.format(format, colour), "" + ChatColor.getByChar(index));
            ++index;
        }
        for (char colour = '0'; colour < 16; ++colour) {
            String chatColour = ChatColor.getByChar(colour).toString();
            message = message.replaceAll(String.format(format, colour), chatColour).replaceAll(
                    String.format(format, Integer.toHexString(colour)), chatColour);
        }
        message = message.replaceAll("<g>", "" + ChatColor.GREEN);
        message = message.replaceAll("<y>", "" + ChatColor.YELLOW);
        return message;
    }

    public static void dualSend(CommandSender sender, String string) {
        log(string);
        send(sender, StringUtils.join(string));
    }
}