package com.github.otrebor4.simulator.events;


import org.bukkit.event.HandlerList;

import com.github.otrebor4.simulator.resources.SimulatorNPC;

public class NPCRemoveEvent extends NPCEvent {
    private final NPCRemoveReason reason;

    public NPCRemoveEvent(SimulatorNPC npc, NPCRemoveReason reason) {
        super(npc);
        this.reason = reason;
    }

    /**
     * Get the reason why an NPC despawn
     * 
     * @return reason Reason why NPC despawned
     */
    public NPCRemoveReason getReason() {
        return this.reason;
    }

    public enum NPCRemoveReason {
        /**
         * NPC was killed
         */
        DEATH,
        /**
         * NPC was removed by a command
         */
        COMMAND,
        /**
         * NPC was removed, possibly to be respawned later
         */
        UNLOAD,
        /**
         * NPC was removed by other means
         */
        OTHER;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}