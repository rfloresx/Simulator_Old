package com.github.otrebor4.simulator.resources;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetServerHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet102WindowClick;
import net.minecraft.server.Packet106Transaction;
import net.minecraft.server.Packet10Flying;
import net.minecraft.server.Packet130UpdateSign;
import net.minecraft.server.Packet14BlockDig;
import net.minecraft.server.Packet15Place;
import net.minecraft.server.Packet16BlockItemSwitch;
import net.minecraft.server.Packet255KickDisconnect;
import net.minecraft.server.Packet28EntityVelocity;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet51MapChunk;

public class MyNetHandler extends NetServerHandler {
    public MyNetHandler(MinecraftServer minecraftserver, EntityPlayer entityplayer, NetworkManager netMgr) {
        super(minecraftserver, netMgr, entityplayer);
    }

    @Override
    public void a(String s, Object[] aobject) {
    }

    @Override
    public void sendPacket(Packet packet) {
    }

    @Override
    public void onUnhandledPacket(Packet packet) {
    }

    @Override
    public void a(Packet3Chat packet3chat) {
    }

    @Override
    public void a(Packet10Flying packet10flying) {
    }

    @Override
    public void a(Packet14BlockDig packet14blockdig) {
    }

    @Override
    public void a(Packet15Place packet15place) {
    }

    @Override
    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
    }

    @Override
    public void a(Packet28EntityVelocity packet28entityvelocity) {
    }

    @Override
    public void a(Packet51MapChunk packet50mapchunk) {
    }

    @Override
    public void a(Packet102WindowClick packet102windowclick) {
    }

    @Override
    public void a(Packet106Transaction packet106transaction) {
    }

    @Override
    public void a(Packet255KickDisconnect packet255kickdisconnect) {
    }

    @Override
    public void a(Packet130UpdateSign packet130updatesign) {
    }
}