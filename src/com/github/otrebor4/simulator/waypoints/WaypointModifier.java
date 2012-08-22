package com.github.otrebor4.simulator.waypoints;


import org.bukkit.entity.Player;

import com.github.otrebor4.simulator.properties.Storage;
import com.github.otrebor4.simulator.resources.SimulatorNPC;
import com.github.otrebor4.simulator.util.ConversationUtils.ChatType;
import com.github.otrebor4.simulator.util.ConversationUtils.ConversationMessage;
import com.github.otrebor4.simulator.util.ConversationUtils.Converser;





public abstract class WaypointModifier extends Converser {
	protected Waypoint waypoint;

	public WaypointModifier(Waypoint waypoint) {
		this.waypoint = waypoint;
	}

	public abstract void onReach(SimulatorNPC npc);

	public abstract void parse(Storage storage, String root);

	public abstract void save(Storage storage, String root);

	public abstract WaypointModifierType getType();

	public boolean converse(Player player, ConversationMessage message) {

		return false;
	}

	public boolean special(Player player, ChatType type) {

		return false;
	}

	public static WaypointModifierType valueOf(String string) {

		return null;
	}
}