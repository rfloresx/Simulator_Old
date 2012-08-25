package com.github.otrebor4.simulator.SP;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.WorldServer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

import com.github.otrebor4.simulator.Simulator;
import com.github.otrebor4.simulator.resources.PacketUtils;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.resources.SimulatorNPC;

public class Spawner {
	private static WorldServer getWorldServer(World world) {
        return ((CraftWorld) world).getHandle();
    }

    private static MinecraftServer getMinecraftServer(Server server) {
        return ((CraftServer) server).getServer();
    }

    public static SimulatorNPC spawnNPC(int UID, String name, final Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return null;
        }
        WorldServer ws = getWorldServer(loc.getWorld());
        
        final SimulatedPlayer eh = new SimulatedPlayer(getMinecraftServer(ws.getServer()), ws, name, new ItemInWorldManager(ws));
        
        eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Simulator.plugin, new Runnable() {
            @Override
            public void run() {
                eh.as = loc.getYaw();
            }
        });
        ws.addEntity(eh);
        ws.players.remove(eh);
        
        return new SimulatorNPC(eh, UID, name);
    }

    public static SimulatorNPC spawnNPC(final Location loc, SimulatedPlayer SP, int id) {
        try {
            String name = SP.name;
            WorldServer ws = getWorldServer(loc.getWorld());
            final SimulatedPlayer eh = SP;
            eh.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Simulator.plugin, new Runnable() {
                @Override
               public void run() {
                   eh.as = loc.getYaw();
                }
            });
            ws.addEntity(eh);
            PacketUtils.sendPacketToOnline(new Packet20NamedEntitySpawn(eh), null  );
            return new SimulatorNPC(eh, id, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SimulatorNPC spawnNPC(final SimulatorNPC npc, final Location loc) {
        WorldServer ws = getWorldServer(loc.getWorld());
        npc.getHandle().setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ws.addEntity(npc.getHandle());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Simulator.plugin, new Runnable() {
            @Override
            public void run() {
               npc.getHandle().as = loc.getYaw();
            }
        });
        return npc;
    }

    public static void despawnNPC(SimulatorNPC npc) {
        if (getWorldServer(npc.getWorld()).getEntity(npc.getPlayer().getEntityId()) != npc.getHandle()
                || npc.getHandle().dead)
            return;
       // Bukkit.getServer().getPluginManager().callEvent(new NPCRemoveEvent(npc, reason));
        PacketUtils.sendPacketToOnline(new Packet29DestroyEntity(npc.getHandle().id), null);
        npc.getHandle().die();
        
    }

    public static void despawnNPC(CraftSP npc) {
        despawnNPC(npc.npc);
    }
    
    
}
