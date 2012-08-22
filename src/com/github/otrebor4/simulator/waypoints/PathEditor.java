package com.github.otrebor4.simulator.waypoints;

import org.bukkit.block.Block;

public interface PathEditor extends Editor {
	void addModifier(WaypointModifier modifier);

	void onLeftClick(Block clicked);

	void onRightClick(Block clicked);

	void setIndex(int index);
}
