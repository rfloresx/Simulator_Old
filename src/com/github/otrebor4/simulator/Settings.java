package com.github.otrebor4.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.otrebor4.simulator.properties.Node;
import com.github.otrebor4.simulator.properties.SettingsType;
import com.github.otrebor4.simulator.properties.Storage;


public class Settings {

	private static List<Node> nodes = new ArrayList<Node>();
    private static Map<String, Node> loadedNodes = new HashMap<String, Node>();
    
	public static String getPath(String key) {
        return loadedNodes.get(key).getPath();
    }

    public static boolean getBoolean(String name) {
        try {
            return (Boolean) loadedNodes.get(name).getValue();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getInt(String name) {
        try {
            return (Integer) loadedNodes.get(name).getValue();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getString(String name) {
        try {
            return (String) loadedNodes.get(name).getValue();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static double getDouble(String name) {
        try {
            Object value = loadedNodes.get(name).getValue();
            if (value instanceof Float) {
                return (Float) value;
            } else if (value instanceof Double) {
                return (Double) value;
            } else {
                return (Integer) value;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setupVariables() {

        Storage local = null;
        // Only load settings for loaded NPC types
        for (Node node : nodes) {
            local = node.getFile();
            if (!local.keyExists(node.getPath())) {
                local.setRaw(node.getPath(), node.getValue());
            } else {
                node.set(local.getRaw(node.getPath()));
            }
            loadedNodes.put(node.getName(), node);
            local.save();
        }
    }
   
    static {
        // citizens.yml "DebugMode"
    	nodes.add(new Node("UseSimulator", SettingsType.CONFIG ,"simulator.use-simulator", true));
    	nodes.add(new Node("DebugMode", SettingsType.CONFIG ,"simulator.DebugMode", true));
    	nodes.add(new Node("Names", SettingsType.PLAYERS, "simulate-player.names", "Otrebor"));
    }
    
}
