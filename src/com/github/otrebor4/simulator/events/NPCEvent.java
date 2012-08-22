package com.github.otrebor4.simulator.events;

import com.github.otrebor4.simulator.resources.SimulatorNPC;



public abstract class NPCEvent extends SimulatorEvents {
    private final SimulatorNPC npc;

    protected NPCEvent(SimulatorNPC npc) {
        this.npc = npc;
    }

    /**
     * Get the npc involved in the event.
     * 
     * @return the npc involved in the event
     */
    public SimulatorNPC getNPC() {
        return npc;
    }
}