package com.github.otrebor4.simulator.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


import com.github.otrebor4.simulator.SP.SimulatedPlayerManager;
import com.github.otrebor4.simulator.resources.SimulatorNPC;
import com.github.otrebor4.simulator.util.Messaging;

public class SimulatorListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		
		
	}
	
	@EventHandler (ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
		
        SimulatorNPC npc = SimulatedPlayerManager.get(event.getEntity());
        if (npc != null) {
            npc.callDamageEvent(event);
            Messaging.log("Damage to " + npc.getName());
        }
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (npc != null) {
                if (e.getDamager() instanceof Player) {
                    Player player = (Player) e.getDamager();
                    npc.callLeftClick(player, npc);
                }
            } else if (e.getDamager() instanceof Player) {
              //  CreatureTask.onDamage(e.getEntity(), event);
            }
        }
    }
    
	
	@EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
		
	}
	
	@EventHandler (ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
		SimulatorNPC npc = SimulatedPlayerManager.get(event.getEntity());
        if (npc != null) {
        	Messaging.log("Death to " + npc.getName());
        	SimulatedPlayerManager.reSpawn(npc);
            //npc.callDeathEvent(event);
           // Messaging.log("Death to " + npc.getName());
        }
	}
		
	@EventHandler (ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
		
	}
	
	 @EventHandler (ignoreCancelled = true)
	 public void onChunkLoad(ChunkLoadEvent event) {
		 
	}
	 
	 @EventHandler (ignoreCancelled = true)
	 public void onPlayerLogin(PlayerLoginEvent event) {
		 
	 }
	 
	 @EventHandler (ignoreCancelled = true)
	 public void onPlayerQuit(PlayerQuitEvent event) {
		 
	 }
	 
	 @EventHandler (ignoreCancelled = true)
	 public void onPlayerInteract(PlayerInteractEvent event) {
		 
	 }
	 
	 @EventHandler (ignoreCancelled = true)
	 public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		 
	 }
	 
	@EventHandler (ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		 
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onItemSpawnEvent( ItemSpawnEvent event ){
		Messaging.log("ItemSpawnEvent");
	}
	 
}
