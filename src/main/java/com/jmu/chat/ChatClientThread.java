package com.jmu.chat;

import java.net.*;
import java.io.*;

public class ChatClientThread extends Thread {
	private Socket socket = null;
	private ChatGUI client = null;
	private DataInputStream streamIn = null;

	public ChatClientThread(ChatGUI _client, Socket _socket) {
		client = _client;
		socket = _socket;
		try {
			streamIn = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client.stop();
		}
		start();
	}

	public void close() {
		if (streamIn != null)
			try {
				streamIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	public void run() {
		while (true) {
			try {
				streamIn = new DataInputStream(socket.getInputStream());
				String msg = streamIn.readUTF();
				client.displayMsg(msg);
			} catch (IOException ioe) {
				client.stop();
			}
		}
	}
}
