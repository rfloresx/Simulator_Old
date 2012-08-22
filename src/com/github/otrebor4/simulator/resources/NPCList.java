package com.github.otrebor4.simulator.resources;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class NPCList extends ConcurrentHashMap<Integer, SimulatorNPC> {
	private static final long serialVersionUID = 7208318521278059987L;

	public boolean containsBukkitEntity(Entity entity) {
		return getNPC(entity) != null;
	}

	public SimulatorNPC getNPC(Entity entity) {
		if (entity == null) {
			return null;
		}
		net.minecraft.server.Entity mcEntity = ((CraftEntity) entity)
				.getHandle();
		if (mcEntity instanceof CraftNPC) {
			SimulatorNPC npc = ((CraftNPC) mcEntity).npc;
			if (npc == null)
				return null;
			// Compare object references to eliminate conflicting UIDs.
			if (get(npc.getUID()) == npc) {
				return npc;
			}
		}
		return null;
	}
}