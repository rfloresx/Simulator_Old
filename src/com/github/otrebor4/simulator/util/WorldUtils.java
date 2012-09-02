package com.github.otrebor4.simulator.util;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WorldUtils {
	
	public static List<Block> getBlocksOfId(Location loc, int blockid, int range ){
		List<Block> blocks = new LinkedList<Block>();
		World world = loc.getWorld();
		Vector3 pos1 = new Vector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).Sub( new Vector3(range/2));
		Vector3 pos2 = new Vector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).add( new Vector3(range/2));
		for(int x = pos1.x; x < pos2.x; x++){
			for(int y = pos1.y; y < pos2.y; y++){
				for(int z = pos1.z; z < pos2.z; z++){
					Block b = world.getBlockAt(x, y, z);
					if(b != null && b.getTypeId() == blockid){
						blocks.add(b);
					}
				}
			}
		}
		return blocks;
	}
}
