package com.github.otrebor4.simulator.SP.actinions;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.inventory.ItemStack;

import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Vector3;

public class WorldManipulationAction extends Action{
	public enum PLACE_BLOCK_ERR{NONE, OCUPPIED, NOTALLOWED,}
	public enum BREAK_BLOCK_ERR{NONE, NOBLOCK, NOTALLOWED, } 
	
	private World world;
	
	public WorldManipulationAction(CraftSP npc) {
		super(npc);
		world = npc.getBukkitEntity().getWorld();
	}
	
	public PLACE_BLOCK_ERR placeBlock( Block block, Vector3 pos ){
		Chunk chunk = world.getChunkAt(npc.getBukkitEntity().getLocation());
		Block destBlock = chunk.getBlock(pos.x, pos.y, pos.z);
		
		if(destBlock.isEmpty()){
			destBlock.setTypeId( block.getTypeId());
			return PLACE_BLOCK_ERR.NONE;
		}
		else{
			return PLACE_BLOCK_ERR.OCUPPIED;
		}
	}
	
	public PLACE_BLOCK_ERR placeBlock( int blockid, Vector3 pos ){
		Block destBlock = world.getBlockAt(pos.x, pos.y, pos.z);
		
		if(destBlock.isEmpty()){
			destBlock.setTypeId( blockid);
			return PLACE_BLOCK_ERR.NONE;
		}
		else{
			return PLACE_BLOCK_ERR.OCUPPIED;
		}
	}
	
	public BREAK_BLOCK_ERR breakBlock(Vector3 pos){
		if(pos == null){
			return BREAK_BLOCK_ERR.NOBLOCK;
		}
		//Location loc =  npc.getBukkitEntity().getLocation();
		//Chunk chunk = world.getChunkAt( loc);
		Block target = world.getBlockAt(pos.x, pos.y, pos.z);
		ItemStack tool = this.npc.getBukkitEntity().getItemInHand();
		if(!target.isEmpty()){
			if(target.breakNaturally( tool)){
				return BREAK_BLOCK_ERR.NONE;
			}else{
				return BREAK_BLOCK_ERR.NOTALLOWED;
			}
		}
		else{
			
			return BREAK_BLOCK_ERR.NOBLOCK;
		}
	}
	
	public List<CraftItem> getWorldItems( Location pos, float range ){
		List<CraftItem> stack = new LinkedList<CraftItem>();
		Collection<CraftItem> items = world.getEntitiesByClass( CraftItem.class);
		for(CraftItem item : items){
			if(item.getLocation().distance( pos) <= range){
				stack.add(item);
			}
		}
		return stack;
	}
	
	public List<ItemStack> pickUpItems(Location pos, float range){
		List<CraftItem> worldItems = getWorldItems(pos, range);
		List<ItemStack> items = new LinkedList<ItemStack>();
		for(CraftItem item : worldItems){
			items.add( item.getItemStack());
			//world.getEntities().remove(item);
			item.remove();
		}
		
		return items;
	}
	
	public BlockInteraction newInteration( Block block, ItemStack item){
		return new BlockInteraction(block, item, this.npc);
	}
	
	public static Block getFirstBlockInDirection(Location loc1, Location loc2 ){
		World world = loc1.getWorld();
		if(world != loc2.getWorld()){
			return null;
		}
		Vector3 pos1 = new Vector3(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ());
		Vector3 pos2 = new Vector3(loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
		Vector3 dif = pos2.Sub(pos1);
		List<Vector3> points = new LinkedList<Vector3>();
		if( pos1 == pos2){
			return world.getBlockAt( loc2);
		}

		int dir = 1;
		if( dif.x < 0)
			dir = -1;
		int diry = 1;
		if(dif.y < 0)
			diry = -1;
		int dirz = 1;
		if( dif.z < 0)
			dirz = -1;
		float oldxy = 0;
		float curxy = 0;
		float oldxz = 0;
		float curxz = 0;
		
		for( int x = 0; Math.abs( x ) <= Math.abs(dif.x); x += dir){
			oldxz = curxz;
			if( dif.z != 0)
				curxz = x*(dif.x/ dif.z);
			else
				curxz = x*dif.x;
			
			oldxy = curxy;
			if( dif.y != 0)
				curxy = x*(dif.x / dif.y);
			else
				curxy = x*dif.x;
			
			if( dif.y == 0 && dif.z == 0){
				points.add(new Vector3( pos1.x + x, pos1.y, pos1.z ));
			}
			else if( dif.y == 0){
				for(int j = getLowInt(oldxz); Math.abs(j) <= Math.abs(getInt(curxz)); j += dirz ){
					points.add(new Vector3( pos1.x + x, pos1.y, pos1.z + j  ));
				}
			}
			else if( dif.z == 0){
				for( int i = getLowInt(oldxy); Math.abs(i) <= Math.abs(getInt(curxy)); i += diry){
					points.add( new Vector3( pos1.x + x, pos1.y + i, pos1.z));
				}
			}
			else{
				for(int j = getLowInt(oldxz); Math.abs(j) <= Math.abs(getInt(curxz) ); j += dirz ){
					for( int i = getLowInt(oldxy); Math.abs(i) <= Math.abs(getInt(curxy) ); i += diry){
						points.add( new Vector3( pos1.x + x, pos1.y + i, pos1.z + j  ));
					}
				}
			}
		}
		for(Vector3 pos : points){
			Block block = world.getBlockAt( pos.x, pos.y, pos.z);
			if( !block.isEmpty() ){
				return block;
			}
		}
		
		return world.getBlockAt( loc2);
	}
	
	static int getInt(float d){
		int temp = Math.round( d);
		if(d > temp)
			temp++;
		return temp;
	}
	
	static int getLowInt(float f){
		int temp = Math.round( f);
		if(temp > f)
			temp--;
		return temp;
	}
}


