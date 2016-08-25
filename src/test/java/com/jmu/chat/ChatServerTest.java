package com.jmu.chat;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JApplet;
import org.junit.Test;
import junit.framework.TestCase;

public class ChatServerTest  {

	@Test
	public void testWillFailWhenChatServerIsNOTRunning() {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 3000);
			ChatGUI client = new ChatGUI("localhost","user1");
			client.init();
			client.start();
			client.close();
			
		} catch (UnknownHostException e) {
			assertTrue(socket == null);
		} catch (IOException e) {
			assertTrue(socket == null);
		}
		
		assertTrue(socket == null);
	}


	@Test
	public void testWillNOTFailWhenChatServerIsRunning() {
		
		Thread t1 = new Thread() {
			public void run() {
				ChatServer server = new ChatServer();
			}
		};
		t1.start();

		Socket socket = null;
		try {
			socket = new Socket("localhost", 3000);
			ChatGUI client = new ChatGUI("localhost","user1");
			client.init();
			client.start();
			client.close();
			
		} catch (UnknownHostException e) {
			assertTrue(socket == null);
		} catch (IOException e) {
			assertTrue(socket == null);
		}
		
		assertTrue(socket != null);
	}

}
