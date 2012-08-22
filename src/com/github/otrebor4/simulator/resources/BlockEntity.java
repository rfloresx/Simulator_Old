package com.github.otrebor4.simulator.resources;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.WorldServer;

public class BlockEntity extends Entity {

	private static WorldServer getWorldServer(World world) {
        return ((CraftWorld) world).getHandle();
    }
	
	public BlockEntity(Location loc) {
		super( getWorldServer(loc.getWorld()));
		this.locX = loc.getX();
		this.locY = loc.getY();
		this.locZ = loc.getZ();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void a() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void a(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void b(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
