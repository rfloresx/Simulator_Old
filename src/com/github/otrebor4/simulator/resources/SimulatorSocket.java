package com.github.otrebor4.simulator.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SimulatorSocket extends Socket {

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(new byte[10]);
	}

	@Override
	public OutputStream getOutputStream() {
		return new ByteArrayOutputStream();
	}
}