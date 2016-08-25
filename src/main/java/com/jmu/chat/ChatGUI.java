package com.jmu.chat;

import java.net.*;

import javax.swing.JApplet;

import java.io.*;
import java.awt.*;

public class ChatGUI extends JApplet {
	private Socket socket = null;
	private DataOutputStream streamOut = null;
	private ChatClientThread client = null;
	private TextArea display = new TextArea();
	private TextField input = new TextField();
	private TextField host = new TextField();
	private TextField user = new TextField();
	
	private Button send = new Button("Send"); 
	PrintWriter printWriter = null;

	public ChatGUI(String host, String user) {
		this.host.setText(host);
		this.user.setText(user);
	}
 
	public void init() {
		Panel panel0 = new Panel();
		panel0.setLayout(new BorderLayout());
		//panel0.add("North",host);
		//panel0.add("South",user);
		

		Panel panel1 = new Panel();
		panel1.setLayout(new BorderLayout());
		panel1.add("North", panel0);
		
		Panel panel2 = new Panel();
		panel2.setLayout(new BorderLayout());
		panel2.add(panel1);
		panel2.add("North",input);
		panel2.add("South", send);
		
		setLayout(new BorderLayout());
		
		add("Center", display);
		add("South", panel2);

		display.setForeground(Color.BLUE);
		connect(host.getText(), 3000);
	}

	public boolean action(Event e, Object o) {
	   	send();
		input.requestFocus();
		return true;
	}

	public void connect(String serverName, int serverPort) {
		try {
			socket = new Socket(serverName, serverPort);
			open();
		} catch (UnknownHostException uhe) {
			println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			println("Unexpected exception: " + ioe.getMessage());
		}
	}

	private void send() {
		printWriter.println("[" +  user.getText()+ "] : " + input.getText());
		input.setText("");
	}

	public void displayMsg(String msg) {
		if (msg.equals("bye")) {
			printWriter.println(msg);
			close();
		} else
			println(msg);
	}

	public void open() {
		try {
			streamOut = new DataOutputStream(socket.getOutputStream());
			client = new ChatClientThread(this, socket);

			println("Hi " + user.getText() + ". Welcome to the Chatroom");
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println(user.getText());

		} catch (IOException ioe) {
			println("Error opening output stream: " + ioe);
		}
	}

	public void close() {
		try {
			if (streamOut != null)
				streamOut.close();
			if (socket != null)
				socket.close();
		} catch (IOException ioe) {
			println("ERROR");
		}
		client.close();
		client.interrupt();
	}

	private void println(String msg) {
		display.appendText(msg + "\n");
	}

}
