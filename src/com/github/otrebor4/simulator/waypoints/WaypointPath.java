package com.github.otrebor4.simulator.waypoints;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.otrebor4.simulator.Settings;
import com.github.otrebor4.simulator.Simulator;
import com.github.otrebor4.simulator.resources.SimulatorNPC;
import com.github.otrebor4.simulator.util.PathUtils;

public class WaypointPath {
	private final List<Waypoint> points = new ArrayList<Waypoint>();
	private boolean started = false;
	private int index = 0;

	public void resetWaypoints() {
		this.points.clear();
		this.index = 0;
	}

	public void setIndex(int waypointIndex) {
		this.index = waypointIndex;
	}

	public Waypoint current() {
		if (points.size() == 0)
			return null;
		return this.get(currentIndex());
	}

	public int currentIndex() {
		if (index >= points.size())
			index = 0;
		return index;
	}

	public void setStarted(boolean waypointStarted) {
		this.started = waypointStarted;
	}

	public boolean isStarted() {
		return started;
	}

	public List<Waypoint> getWaypoints() {
		return this.points;
	}

	public void setPoints(List<Waypoint> waypoints) {
		this.points.clear();
		this.points.addAll(waypoints);
	}

	public Waypoint get(int index) {
		return this.points.get(index);
	}

	public void insert(Waypoint location, int index) {
		this.points.add(index, location);
	}

	public void remove(int index) {
		this.points.remove(index);
	}

	public int size() {
		return points.size();
	}

	public Waypoint getLast() {
		if (points.size() == 0) {
			return null;
		}
		return points.get(points.size() - 1);
	}

	public void onReach(SimulatorNPC npc) {
		if (current() != null)
			current().onReach(npc);
	}

	public void scheduleNext(SimulatorNPC npc) {
		schedule(npc, currentIndex());
	}

	public void schedule(SimulatorNPC npc, int index) {
		this.setStarted(true);
		WaypointScheduler scheduler = new WaypointScheduler(npc, points.get(
				index).getLocation());
		if (points.get(index).getDelay() > 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Simulator.plugin,
					scheduler, points.get(index).getDelay());
		} else {
			scheduler.run();
		}
	}

	public void scheduleDelay(SimulatorNPC npc, Location target, int delay) {
		RestartPathTask task = new RestartPathTask(npc, target);
		npc.setPaused(true);
		if (delay > 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Simulator.plugin,
					new RestartPathTask(npc, target), delay);
		} else {
			task.run();
		}
	}

	private static class WaypointScheduler implements Runnable {
		private final SimulatorNPC npc;
		private final Location target;

		public WaypointScheduler(SimulatorNPC npc, Location target) {
			this.npc = npc;
			this.target = target;
		}

		@Override
		public void run() {
			PathUtils.createPath(npc, target, -1, -1,
					Settings.getDouble("PathfindingRange"));
		}
	}

	private static class RestartPathTask implements Runnable {
		private final Location point;
		private final SimulatorNPC npc;

		public RestartPathTask(SimulatorNPC npc, Location point) {
			this.npc = npc;
			this.point = point;
		}

		@Override
		public void run() {
			PathUtils.createPath(npc, point, -1, -1,
					Settings.getDouble("PathfindingRange"));
			npc.setPaused(false);
		}
	}
}