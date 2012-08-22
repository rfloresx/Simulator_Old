package com.github.otrebor4.simulator.properties;

public class Node {
	private final String name;
	private final String path;
	private final SettingsType type;
	private Object value;
	
	public Storage getFile() {
		switch( this.getType()){
		case CONFIG:
			return SettingsProperties.getConfig();
		case PLAYERS:
			return SettingsProperties.getPlayers();
		default:
			break;
		}
		return null;
	}
	
	public Node( String name, SettingsType type ,String path, Object value){
		this.name = name;
		this.type = type;
		this.path = path;
		this.value = value;
	}
	
	public SettingsType getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public Object getValue() {
		return this.value;
	}

	public void set(Object value) {
		this.value = value;
	}
	
}
