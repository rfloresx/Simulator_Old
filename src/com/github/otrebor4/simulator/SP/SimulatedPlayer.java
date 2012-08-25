package com.github.otrebor4.simulator.SP;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.otrebor4.simulator.SP.actinions.CollectItem;
import com.github.otrebor4.simulator.SP.tasks.Task;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Timer;


public class SimulatedPlayer extends CraftSP {
	private String myname;
	private Location location;
	private Task action;
	Timer healthTimer = new Timer();
	
	
	public SimulatedPlayer(MinecraftServer minecraftserver, World world,
			String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);

	}

	public void SetValues(String name, Location loc) {
		this.name = name;
		this.location = loc;
	}
	
	public void SetInventory( ItemStack [] items){
		PlayerInventory PI = this.getBukkitEntity().getInventory();
		PI.setContents(items );
	}
	
	public void SetEquipment(){
		
	}

	public String getName() {
		return myname;
	}

	public Location getLocation() {
		return location;
	}
	
	public void Update(){
		super.Update();
		if( !this.isAlive()){
			return;
		}
		CollectItem.Update(this);
		
		if( healthTimer.getTimeSecons() > 5f && this.getBukkitEntity().getHealth() < this.getBukkitEntity().getMaxHealth() && this.getBukkitEntity().getFoodLevel() >= 18){
			this.getBukkitEntity().setHealth( this.getBukkitEntity().getHealth() + 1 );
			healthTimer.start();
		}
		
		if(action == null){
			action = (new TasksManager(this)).getTask();
			return;
		}
		
		action.Update();
		//update action to safe task
		if(this.getHealth() <= this.getMaxHealth()/3 && !action.isHightPriority( 3)){
			action = new TasksManager(this).getTask();
		}
		
		if(action.done()){
			action = null;
		}
	}
	
	
	public void stopActions(){ action = null;}
}
