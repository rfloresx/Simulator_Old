package com.github.otrebor4.simulator.resources;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.otrebor4.simulator.SP.SimulatedPlayer;
import com.github.otrebor4.simulator.resources.Animator.Animation;
import com.github.otrebor4.simulator.util.Messaging;


public class SimulatorNPC {
	private final SimulatedPlayer mcEntity;
    private Data data = new Data();
    private double balance;
	private final String name;
	private final int UID;

	public SimulatorNPC(SimulatedPlayer eh, int uID, String name) {

		this.name = ChatColor.stripColor(name);
		this.UID = uID;
		this.mcEntity = eh;
        this.mcEntity.npc = this;
	}

	public String getName() {
		return this.name;
	}

	public int getUID() {
		return this.UID;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(UID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		SimulatorNPC other = (SimulatorNPC) obj;
		return UID == other.UID;
	}

    public void doTick() {
    	this.mcEntity.Update();
    }

    public double getBalance() {
        return this.balance;
    }

    public int getChunkX() {
        return this.getLocation().getBlockX() >> 4;
    }

    public int getChunkZ() {
        return this.getLocation().getBlockZ() >> 4;
    }
    
    public SimulatedPlayer getHandleSP(){
    	return this.mcEntity;
    }
    
    public CraftSP getHandle() {
        return this.mcEntity;
    }

    public PlayerInventory getInventory() {
        return this.getPlayer().getInventory();
    }

    public ItemStack getItemInHand() {
        return this.getPlayer().getItemInHand();
    }

    public Location getLocation() {
        return this.getPlayer().getLocation();
    }

    public Data getData() {
        return data;
    }

    public Player getPlayer() {
        return this.mcEntity.getBukkitEntity();
    }

    public void performAction(Animation action) {
        this.mcEntity.performAction(action);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setItemInHand(ItemStack item) {
        this.getPlayer().setItemInHand(item);
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void teleport(double x, double y, double z, float yaw, float pitch) {
        this.mcEntity.setLocation(x, y, z, yaw, pitch);
    }

    public void teleport(Location loc) {
        boolean multiworld = loc.getWorld() != this.getPlayer().getWorld();
        this.getPlayer().teleport(loc);
        if (multiworld) {
            ((CraftServer) Bukkit.getServer()).getHandle().players
                    .remove(this.mcEntity);
        }
    }
    
    public World getWorld(){
    	return this.getPlayer().getWorld();
    }

	public void callDamageEvent(EntityDamageEvent event) {
		
	}
	
	public void callLeftClick(Player player, SimulatorNPC npc) {

	}
	
	public void callDeathEvent(EntityDeathEvent event) {

	}
	
	public Location getSpawnPoint(){
		Location dest = this.getPlayer().getBedSpawnLocation();
		if (dest == null)
			dest = this.getPlayer().getWorld().getSpawnLocation();
		return dest;
	}
	
	public void resotreValues(){
		this.getHandle().dead = false;
		this.getHandle().setHealth( this.getHandle().getMaxHealth());
		this.mcEntity.stopActions();
		this.getHandleSP().setRun(false);
		this.getHandle().setAirTicks( this.getHandle().maxAirTicks);
		Messaging.log("restoring values");
	}
}
