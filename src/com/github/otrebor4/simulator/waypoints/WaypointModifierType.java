package com.github.otrebor4.simulator.waypoints;

import java.lang.reflect.Constructor;

import com.github.otrebor4.simulator.waypoints.modifiers.ChatModifier;
import com.github.otrebor4.simulator.waypoints.modifiers.DelayModifier;
import com.github.otrebor4.simulator.waypoints.modifiers.EffectModifier;
import com.github.otrebor4.simulator.waypoints.modifiers.HealthModifier;
import com.github.otrebor4.simulator.waypoints.modifiers.TeleportModifier;



public enum WaypointModifierType {
	CHAT(ChatModifier.class), DELAY(DelayModifier.class), HEALTH(
			HealthModifier.class), TELEPORT(TeleportModifier.class), EFFECT(
			EffectModifier.class);

	private final Constructor<? extends WaypointModifier> constructor;

	WaypointModifierType(Class<? extends WaypointModifier> clazz) {
		Constructor<? extends WaypointModifier> temp = null;
		try {
			temp = clazz.getConstructor(Waypoint.class);
		} catch (Exception e) {
		}
		this.constructor = temp;
	}

	public WaypointModifier create(Waypoint waypoint) {
		try {
			return constructor.newInstance(waypoint);
		} catch (Exception e) {
			return null;
		}
	}

	public static WaypointModifierType value(String type) {
		try {
			return WaypointModifierType.valueOf(type);
		} catch (IllegalArgumentException excp) {
			return null;
		}
	}
}