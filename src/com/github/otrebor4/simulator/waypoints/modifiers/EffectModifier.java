package com.github.otrebor4.simulator.waypoints.modifiers;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.otrebor4.simulator.properties.Storage;
import com.github.otrebor4.simulator.resources.SimulatorNPC;
import com.github.otrebor4.simulator.util.ConversationUtils.ChatType;
import com.github.otrebor4.simulator.util.ConversationUtils.ConversationMessage;
import com.github.otrebor4.simulator.util.EffectUtils;
import com.github.otrebor4.simulator.util.EffectUtils.EffectData;
import com.github.otrebor4.simulator.util.EffectUtils.Effects;
import com.github.otrebor4.simulator.util.EffectUtils.Effects.Effect;
import com.github.otrebor4.simulator.util.EffectUtils.Effects.IEffect;
import com.github.otrebor4.simulator.util.StringUtils;
import com.github.otrebor4.simulator.waypoints.Waypoint;
import com.github.otrebor4.simulator.waypoints.WaypointModifier;
import com.github.otrebor4.simulator.waypoints.WaypointModifierType;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class EffectModifier extends WaypointModifier {
	private IEffect inProgress;
	private final List<EffectData> effects = new ArrayList<EffectData>();

	public EffectModifier(Waypoint waypoint) {
		super(waypoint);
	}

	@Override
	public void onReach(SimulatorNPC npc) {
		for (EffectData data : effects) {
			EffectUtils.playSound(data.getEffect(), waypoint.getLocation(),
					data.getData());
		}
	}

	@Override
	public void parse(Storage storage, String root) {
		String[] innerSplit;
		for (String effect : Splitter.on(";").split(
				storage.getString(root + ".effects"))) {
			innerSplit = effect.split(",");
			effects.add(new EffectData(Effects.getByIdentifier(Integer
					.parseInt(innerSplit[0])), Integer.parseInt(innerSplit[1])));
		}
	}

	@Override
	public void save(Storage storage, String root) {
		StringBuilder builder = new StringBuilder();
		for (EffectData data : effects) {
			builder.append(data.getEffect().getIdentifier() + ","
					+ data.getData() + ";");
		}
		storage.setString(root + ".effects", builder.toString());
	}

	@Override
	public WaypointModifierType getType() {
		return WaypointModifierType.EFFECT;
	}

	@Override
	public void begin(Player player) {
		player.sendMessage(ChatColor.GREEN + "An " + StringUtils.wrap("effect")
				+ " is a sound or visual cue that can be played at a location.");
		player.sendMessage(ChatColor.GREEN + "Possible values are "
				+ Joiner.on(", ").join(Effects.getFormattedEffects()) + ".");
		player.sendMessage(ChatColor.GREEN + "Enter "
				+ StringUtils.wrap("finish")
				+ " at any time to finish editing.");
	}

	@Override
	public boolean converse(Player player, ConversationMessage message) {
		super.resetExit();
		if (Effects.getByName(message.getMessage()) == null) {
			player.sendMessage("Invalid effect name.");
		} else {
			if (inProgress == null) {
				IEffect effect = Effects.getByName(message.getMessage());
				if (effect instanceof Effect) {
					inProgress = effect;
					switch ((Effect) effect) {
					case DISPENSER_PARTICLE_SPAWN:
						player.sendMessage(ChatColor.GREEN
								+ "Enter a direction as a number from 1-8.");
						break;
					case DIG:
						player.sendMessage(ChatColor.GREEN
								+ "Enter the block ID of the particle to spawn.");
						break;
					case RECORD_PLAY:
						player.sendMessage(ChatColor.GREEN
								+ "Enter the record to play (green or yellow).");
						break;
					default:
					}
				} else {
					add(player, new EffectData(effect));
				}
			} else {
				if (inProgress instanceof Effect) {
					int data = 0;
					switch ((Effect) inProgress) {
					case DISPENSER_PARTICLE_SPAWN:
						data = message.getInteger(0);
						if (data > 8 || data < 1) {
							player.sendMessage(ChatColor.GRAY
									+ "Direction must be a number from 1-8.");
							return false;
						}
						player.sendMessage(getMessage("particle direction",
								data));
						break;
					case DIG:
						data = message.getInteger(0);
						if (data > 255 || Material.getMaterial(data) == null) {
							player.sendMessage(ChatColor.GRAY
									+ "Invalid block ID entered.");
							return false;
						}
						player.sendMessage(super.getMessage("block ID",
								StringUtils.format(Material.getMaterial(data))));
						break;
					case RECORD_PLAY:
						if (message.getMessage().equalsIgnoreCase("green")) {
							data = 2257;
						} else if (message.getMessage().equalsIgnoreCase(
								"yellow")) {
							data = 2256;
						} else {
							data = -1;
						}
						if (data != 2256 && data != 2257) {
							player.sendMessage(ChatColor.GRAY
									+ "Not a valid record type.");
						}
						break;
					}
					add(player, new EffectData(inProgress, data));
					inProgress = null;
				}
			}
		}
		return false;
	}

	private void add(Player player, EffectData data) {
		effects.add(data);
		player.sendMessage(ChatColor.GREEN
				+ "Added effect "
				+ StringUtils.wrap(StringUtils.format((Enum<?>) data
						.getEffect())) + ".");
	}

	@Override
	public boolean allowExit() {
		return effects.size() > 0;
	}

	@Override
	public boolean special(Player player, ChatType type) {
		if (type == ChatType.RESTART) {
			effects.clear();
		} else if (type == ChatType.UNDO && allowExit()) {
			effects.remove(effects.size() - 1);
		}
		return super.special(player, type);
	}

	@Override
	protected int getUndoStep() {
		return 1;
	}

	@Override
	protected void onExit() {
		waypoint.addModifier(this);
	}
/*
	@Override
	public boolean converse(Player player,
			net.citizensnpcs.utils.ConversationUtils.ConversationMessage message) {

		return false;
	}*/
}