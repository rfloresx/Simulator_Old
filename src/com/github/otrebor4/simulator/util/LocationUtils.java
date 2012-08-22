package com.github.otrebor4.simulator.util;



import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.otrebor4.simulator.properties.Storage;
import com.google.common.base.Joiner;

public class LocationUtils {

	/**
	 * Checks whether two locations are within range of each other.
	 * 
	 * @param loc
	 * @param pLoc
	 * @param range
	 * @return
	 */
	public static boolean withinRange(Location loc, Location pLoc, double range) {
		if (loc == null || pLoc == null || loc.getWorld() != pLoc.getWorld()) {
			return false;
		}
		return Math.pow(range, 2) > loc.distanceSquared(pLoc);
	}

	public static Location loadLocation(Storage storage, String path,
			boolean shortened) {
		String world;
		double x, y, z;
		float pitch, yaw;
		if (shortened) {
			String[] read = storage.getString(path + ".location").split(",");
			world = read[0];
			x = Double.parseDouble(read[1]);
			y = Double.parseDouble(read[2]);
			z = Double.parseDouble(read[3]);
			pitch = Float.parseFloat(read[4]);
			yaw = Float.parseFloat(read[5]);
		} else {
			world = storage.getString(path + ".location.world");
			x = storage.getDouble(path + ".location.x");
			y = storage.getDouble(path + ".location.y");
			z = storage.getDouble(path + ".location.z");
			pitch = (float) storage.getDouble(path + ".location.pitch");
			yaw = (float) storage.getDouble(path + ".location.yaw");
		}
		return new Location(Bukkit.getServer().getWorld(world), x, y, z, pitch,
				yaw);
	}

	public static void saveLocation(Storage storage, Location loc, String path,
			boolean shortened) {
		if (shortened) {
			storage.setString(
					path + ".location",
					Joiner.on(",").join(
							new Object[] { loc.getWorld().getName(),
									loc.getX(), loc.getY(), loc.getZ(),
									loc.getPitch(), loc.getYaw() }));
		} else {
			storage.setString(path + ".location.world", loc.getWorld()
					.getName());
			storage.setDouble(path + ".location.x", loc.getX());
			storage.setDouble(path + ".location.y", loc.getY());
			storage.setDouble(path + ".location.z", loc.getZ());
			storage.setDouble(path + ".location.pitch", loc.getPitch());
			storage.setDouble(path + ".location.yaw", loc.getYaw());
		}
	}
}