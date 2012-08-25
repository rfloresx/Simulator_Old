package com.github.otrebor4.simulator.properties.settings;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;


import org.bukkit.craftbukkit.*;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;

import com.github.otrebor4.simulator.SPSettings;
import com.github.otrebor4.simulator.SP.SimulatedPlayer;
import com.github.otrebor4.simulator.properties.SPNode;
import com.github.otrebor4.simulator.properties.Storage;
import com.github.otrebor4.simulator.util.StringUtils;

public class BasicProperties {
	
	private static WorldServer getWorldServer(World world) {
        return ((CraftWorld) world).getHandle();
    }
	
	private static MinecraftServer getMinecraftServer(Server server) {
        return ((CraftServer) server).getServer();
    }
	
	private static void saveLocation(SPNode node, Location loc){
		String locale = loc.getWorld().getName() + ","  + loc.getX() + "," + loc.getY() + "," + loc.getZ() + ","
                + loc.getYaw() + "," + loc.getPitch();
		Storage file = node.getFile();
		file.setString("world.location", locale);
		file.save();
	}
	
	private static void saveInventory(SPNode node, PlayerInventory inv){
		StringBuilder save = new StringBuilder();
		int count = 0;
		for (ItemStack i : inv.getContents()) {
			if (i == null || i.getType() == Material.AIR) {
				++count;
			} else {
				if (count > 0) {
					save.append("AIR*" + count + ",");
					count = 0;
				}
				save.append(i.getTypeId())
						.append("/")
						.append(i.getAmount())
						.append("/")
						.append(i.getDurability())
						.append("/")
						.append((i.getData() == null) ? 0 : i.getData()
								.getData()).append(",");
			}
		}
		if (count > 0) {
			save.append("AIR*" + count + ",");
			count = 0;
		}
		
		Storage file = node.getFile();
		file.setString("items.inventory", save.toString());
		file.save();
		
	}
	
	public static void saveState(SimulatedPlayer SP  ){
		saveLocation( SPSettings.getData(SP.getName()), SP.getLocation() );
		saveInventory(SPSettings.getData(SP.getName()), SP.getBukkitEntity().getInventory());
	}
	
	public static SimulatedPlayer loadState( String name ){
		MinecraftServer server = getMinecraftServer( Bukkit.getServer() );
		//load data from file
		Location loc = loadLocation( SPSettings.getData( name ) );
		ItemStack [] items = loadInventory( SPSettings.getData( name ) );
		
		//
		WorldServer ws = getWorldServer(loc.getWorld());
		ItemInWorldManager manager = new ItemInWorldManager( ws  );
		SimulatedPlayer SP = new SimulatedPlayer(server, ws, name, manager);
		
		//set data
		SP.SetValues(name, loc);
		SP.SetInventory(items);
		
		return SP;
	}
	
	public static Location loadLocation( SPNode node ){
		Storage file = node.getFile();
		String[] values = file.getString( "world.location" ).split( ",");
		if(values.length != 6){
			return Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
		}
		else{
			return new Location(Bukkit.getServer().getWorld(values[0]), Double.parseDouble(values[1]),
                    Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]),
                    Float.parseFloat(values[5]));
		}
		
	}
	
	public static ItemStack [] loadInventory( SPNode node){
		List<ItemStack> items = new LinkedList<ItemStack>();
		Storage file = node.getFile();
		String val = file.getString("items.inventory");
		if(!val.isEmpty()){
			for(String s : val.split(",")){
				String[] split = s.split("/");
				if (!split[0].contains("AIR") && !split[0].equals("0")) {
					if (split.length == 4) {
						ItemStack newStack = new ItemStack(
							StringUtils.parse(split[0]),
							StringUtils.parse(split[1]),
							(short) StringUtils.parse(split[2]),
							(byte) StringUtils.parse(split[3]));
						newStack.setDurability((short) StringUtils.parse(split[2]));
						items.add(newStack);
					} else {
						items.add(new ItemStack(StringUtils.parse(split[0]),
							StringUtils.parse(split[1]), (short) 0,
							(byte) StringUtils.parse(split[2])));
					}
				} else {
					if (split[0].equals("AIR")) {
						items.add(null);
					} else {
						int count = Integer.parseInt(split[0].split("\\*")[1]);
						while (count != 0) {
							items.add(null);
							--count;
						}
					}
				}
			}
		}
		PlayerInventory inv = new CraftInventoryPlayer(
				new net.minecraft.server.PlayerInventory(null));
		ItemStack[] stacks = inv.getContents();
		
		return items.toArray(stacks);
	}

}
