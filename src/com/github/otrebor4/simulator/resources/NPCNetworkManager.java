package com.github.otrebor4.simulator.resources;

import java.lang.reflect.Field;
import java.net.Socket;
import java.security.PrivateKey;

import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;

public class NPCNetworkManager extends NetworkManager {
	private static Field THREAD_STOPPER;
	
	static{
		try{
			 THREAD_STOPPER = NetworkManager.class.getDeclaredField("m");
			 THREAD_STOPPER.setAccessible(true);
		} catch (Exception ex) {
            THREAD_STOPPER = null;
		}
	}
	
	public NPCNetworkManager(Socket paramSocket, String paramString,
			NetHandler paramNetHandler, PrivateKey key) {
		
		super(paramSocket, paramString, paramNetHandler, key);

		if(THREAD_STOPPER != null){
			try {
				THREAD_STOPPER.set(this,  false);
			} catch(Exception e) {}
		}
	}

	@Override
	public void a(NetHandler nethandler) {
	}

	@Override
	public void queue(Packet packet) {
	}

	@Override
	public void a(String s, Object... aobject) {
	}

	@Override
	public void a() {
	}

	@Override
	public void b() {
	}

	@Override
	public void d() {
	}

	@Override
	public int e() {
		return 0;
	}
}