package com.github.otrebor4.simulator.SP.actinions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.github.otrebor4.simulator.resources.PathNPC;
import com.github.otrebor4.simulator.util.Messaging;
import com.github.otrebor4.simulator.util.Timer;

public class CollectItem extends Action {
	List<Integer> ids;
	boolean all;
	float range;
	static double MIN_DIST = 1.75;
	static Timer delay = new Timer();
	public CollectItem(PathNPC npc, List<Integer> targetIds, float range) {
		super(npc);
		ids = targetIds;
		all = false;
		this.range = range;
	}
	
	public CollectItem(PathNPC npc, List<Integer> targetIds, float range, boolean all) {
		super(npc);
		ids = targetIds;
		this.all = all;
		this.range = range;
	}
	
	public CollectItem(PathNPC npc, float range){
		super(npc);
		this.range = range;
		this.all = true;
	}
	
	public void Update(){
		Item targ = null;
		if(all){
			targ =getClosestItem(npc, range);
		}
		else{
			targ = getClosestItemOfIds( npc, range, ids);
		}
		if(targ != null){
			double dis = targ.getLocation().distance(npc.getBukkitEntity().getLocation());
			Messaging.log("distance is " + dis);
			if( dis < MIN_DIST  ){
				if(!collectItem(targ)){
					this.finished = true;
				}
			}
			else{
				Location loc = targ.getLocation();
				npc.MoveToPos( loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			}
		}
		else{
			finished = true;
		}
	}
	
	public static void Update(PathNPC npc){
		Item targ = null;
		targ = getClosestItem(npc, 5);
		if(targ != null){
			double dis = targ.getLocation().distance(npc.getBukkitEntity().getLocation());
			if(dis < 1.33 ){
				collectItem(targ, npc);
				
			}
		}
	}
	
	private boolean collectItem(Item obj){
		if(delay.getTimeSecons() > 1){
			delay.start();
			ItemStack item = obj.getItemStack();
			PlayerInventory inv = this.npc.getBukkitEntity().getInventory();
			HashMap<Integer, ItemStack> left = inv.addItem( item );
			if(left.isEmpty()){
				obj.remove();
				Messaging.log("collecting item");
				return true;
			}
		}
		return false;
	}
	
	private static void collectItem(Item obj, PathNPC npc){
		if(delay.getTimeSecons() < 1)
			return;
		delay.start();
		ItemStack item = obj.getItemStack();
		PlayerInventory inv = npc.getBukkitEntity().getInventory();
		HashMap<Integer, ItemStack> left = inv.addItem( item );
		if(left.isEmpty()){
			obj.remove();
			Messaging.log("collecting item");
		}
	}
	
	public static Item getClosestItemOfIds(PathNPC npc, float range, List<Integer> ids){
		if(npc == null)
			return null;
		Location l = npc.getBukkitEntity().getLocation();
		
		Item item = null;
		List<Item> list = getItemOnRange(npc, range);
		double dist = range;
		for(Item i: list){
			double temp = i.getLocation().distance(l);
			for(int itemId : ids){
				if(i.getItemStack().getTypeId() == itemId && temp < dist && npc.isInSight( (CraftEntity) i )){
					item = i;
					temp = dist;
					continue;
				}
			}
		}
		return item;
	}
	
	public static Item getClosestItemOf(PathNPC npc, float range, int itemId){
		Location l = npc.getBukkitEntity().getLocation();
		Item item = null;
		List<Item> list = getItemOnRange(npc, range);
		double dist = range;
		for(Item i: list){
			double temp = i.getLocation().distance(l);
			if(i.getItemStack().getTypeId() == itemId && temp < dist && npc.isInSight( (CraftEntity) i ) ){
				item = i;
				temp = dist;
			}
		}
		return item;
	}
	
	public static List<Item> getItemOnRange(PathNPC npc, float range){
		if(npc == null)
			return null;
		Location l = npc.getBukkitEntity().getLocation();

		Collection<Item> list = l.getWorld().getEntitiesByClass(Item.class);
		List<Item> items = new LinkedList<Item>();
		for(Item item : list){
			if(item.getLocation().distance( l ) < range && npc.isInSight( (CraftEntity) item ) ){
				items.add( item);
			}
		}
		return items;
	}
	
	public static Item getClosestItem(PathNPC npc, float range){
		if(npc == null)
			return null;
		Location l = npc.getBukkitEntity().getLocation();
		Item item = null;
		List<Item>  items = getItemOnRange(npc,range);
		double dist = range;
		
		for(Item i: items){
			double temp = i.getLocation().distance(l);
			if(temp < dist && npc.isInSight( (CraftEntity) i ) ){
				item = i;
				dist = temp;
			}
		}
		
		return item;
	}
	
}
