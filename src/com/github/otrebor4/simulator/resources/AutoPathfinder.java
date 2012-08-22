package com.github.otrebor4.simulator.resources;

import net.minecraft.server.PathEntity;

public interface AutoPathfinder {
	PathEntity find(PathNPC npc);
}
