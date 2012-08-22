package com.github.otrebor4.simulator.waypoints;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.github.otrebor4.simulator.resources.SimulatorNPC;

public class Waypoint {
	private final Location waypoint;
	private final List<WaypointModifier> modifiers = new ArrayList<WaypointModifier>();
	private int delay;

	public Waypoint(Location waypoint) {
		this.waypoint = waypoint;
	}

	public void onReach(SimulatorNPC npc) {
		for (WaypointModifier modifier : modifiers) {
			modifier.onReach(npc);
		}
	}

	public void addModifier(WaypointModifier effect) {
		this.modifiers.add(effect);
	}

	public List<WaypointModifier> getModifiers() {
		return this.modifiers;
	}

	public Location getLocation() {
		return waypoint;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}