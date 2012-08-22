package com.github.otrebor4.simulator.properties;


public class SettingsProperties {
	
	private static final String path = "plugins/Simulator/players/";
	
	private static final Storage config = new ConfigurationHandler(
			"plugins/Simulator/simulator.yml");
	private static final Storage players = new ConfigurationHandler(
			"plugins/Simulator/players.yml");
	
	public static void load() {
		config.load();
		players.load();
	}

	public static Storage getConfig() {
		return config;
	}
	
	public static Storage getPlayers(){
		return players;
	}
	
	public static Storage getData( String name){
		return new ConfigurationHandler( path + name + ".yml" );
	}

}
