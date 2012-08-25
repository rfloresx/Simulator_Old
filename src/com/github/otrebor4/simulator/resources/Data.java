package com.github.otrebor4.simulator.resources;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Data {
	private String name;
	private int UID = -1;
	private Location location;
	private ChatColor colour = ChatColor.WHITE;
	private List<ItemData> items = new ArrayList<ItemData>();
	private Deque<String> texts = new ArrayDeque<String>();


	// Acts as a container for various npc data.
	public Data(String name, int UID, Location loc, ChatColor colour,
			List<ItemData> items, Deque<String> texts) {
		this.setName(name);
		this.setUID(UID);
		this.setLocation(loc);
		this.setColour(colour);
		this.setItems(items);
		this.setTexts(texts);
	}
    
    public Data(){}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUID(int UID) {
		this.UID = UID;
	}

	public int getUID() {
		return UID;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setColour(ChatColor code) {
		this.colour = code;
	}

	public ChatColor getColour() {
		return colour;
	}

	public void setItems(List<ItemData> items) {
		this.items = items;
	}

	public List<ItemData> getItems() {
		return items;
	}

	public void setTexts(Deque<String> text) {
		this.texts = text;
	}

	public Deque<String> getTexts() {
		return texts;
	}
}