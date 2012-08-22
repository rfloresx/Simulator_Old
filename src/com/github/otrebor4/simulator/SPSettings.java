package com.github.otrebor4.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.otrebor4.simulator.properties.Node;
import com.github.otrebor4.simulator.properties.SPNode;
import com.github.otrebor4.simulator.properties.SettingsType;
import com.github.otrebor4.simulator.properties.Storage;

public class SPSettings {
	private static Map<String, SPNode> SPNodes = new HashMap<String, SPNode>();
	
	public static SPNode getData(String key){
		if(SPNodes.containsKey(key))
			return SPNodes.get(key);
		
		return null;
	}
	
	public static void setupVariables() {

        Storage local = null;
        // Only load settings for loaded NPC types
        String [] names = Settings.getString("Names").split( ",");
        
        for (String name : names) {
        	SPNode sp = new SPNode( name);
        	
            local = sp.getFile();
            for( Node node : getNodes()){
            	if (!local.keyExists(node.getPath())) {
                    local.setRaw(node.getPath(), node.getValue());
                } else {
                    node.set(local.getRaw(node.getPath()));
                }
            	sp.addNode(node);
            	local.save();
            }
            
            SPNodes.put( name, sp);
            
            local.save();
        }
    }
	
	private static List<Node> getNodes(){
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("Name", SettingsType.DATA, "simulate-player.name", "Otrebor"));
		nodes.add(new Node("location", SettingsType.DATA, "world.location", "null" ));
		nodes.add(new Node("inventory", SettingsType.DATA, "items.inventory", "AIR*36,"));
		return nodes;
	}
	
}
