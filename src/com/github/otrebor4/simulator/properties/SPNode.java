package com.github.otrebor4.simulator.properties;

import java.util.HashMap;
import java.util.Map;

public class SPNode {
	
	private final String name;
	private static Map<String, Node> Nodes = new HashMap<String, Node>();
	
	public SPNode( String name){
		this.name = name;
	}
	
	public Storage getFile() {
		return SettingsProperties.getData(name);
	}
	
	public void addNode(String name, SettingsType type ,String path, Object value){
		Node temp = new Node( name, type ,path ,value);
		Nodes.put(name, temp);
	}
	
	public void addNode( Node n){
		Nodes.put( n.getName(), n);
	}
	
	public Node getNode( String key ){
		if( Nodes.containsKey(key)){
			return Nodes.get(key);
		}
		return null;
	}
	
	public String getName(){ return name;}
	
	
	
}
