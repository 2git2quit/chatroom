package com.jmu.chat;

import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.*;

public class ChatServer {

	private Socket socket = null;
	private ServerSocket server = null;
	private static final int PORT = 3000;
	private List<ChatServerThread> connectedClients = new ArrayList<ChatServerThread>();
	public static List<String> users = new ArrayList<String>();

	public class ChatServerThread extends Thread {
		Socket socket;
		String currentUser;
		
		public Socket getSocket() {
			return socket;
		}

		ChatServerThread(Socket socket, String username) {
			this.socket = socket;
			currentUser = username;
		}

		public void run() {
			String message = null;
			message = "User " + currentUser + " has joined";
			message = (new Date()).toLocaleString() + "  " + message;
			sendMessageToAll(message);
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// Waiting for message from clients
				while ((message = bufferedReader.readLine()) != null) {
					if (("bye".equals(message)) || (message == null)) {
						message = currentUser + " has left.";
						System.out.println("Received client message : " + message);
						sendMessageToAll(message);
						closeThisSocket(socket);
						Thread.currentThread().stop();
						return;
					}
					message = (new Date()).toLocaleString() + "  " + message;
					sendMessageToAll(message); // Broadcast the message
				}
				socket.close();
			} catch (IOException e) {
				message = currentUser + " has left.";
				message = (new Date()).toLocaleString() + "  " + message;
				sendMessageToAll(message);
				closeThisSocket(socket);
			}
		}

		// Close leaving user and its socket
		public synchronized void closeThisSocket(Socket socket) {
			int position = 0;
			for (ChatServerThread thread : connectedClients) {
				if (thread.getSocket().getPort() == socket.getPort()) {
					break;
				}
				position++;
			}

			try {
				connectedClients.get(position).getSocket().close();
				connectedClients.remove(position);
				System.out.println("Number of connected users : " + connectedClients.size());
			} catch (IOException e) {
				// socket closed
			}

			int position2 = 0;
			for (String user : users) {
				if (user.equals(currentUser))
					break;
				position2++;
			}
			System.out.println("User " + users.get(position2) + "  has left.");
			users.remove(position2);

		}

		// Send message from a user to all users
		public synchronized void sendMessageToAll(String message)  {
			
			for (ChatServerThread thread : connectedClients) {
				//if (thread.getSocket().getPort() == socket.getPort())
				//	message = "<=== " + message;
				try {
					DataOutputStream streamOut = new DataOutputStream(
							new BufferedOutputStream(thread.getSocket().getOutputStream()));
					streamOut.writeUTF(message);
					streamOut.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

	public ChatServer() {
		try {
			System.out.println("Binding to port " + PORT);
			server = new ServerSocket(PORT);
			System.out.println("Accepting clients ...");
			String username;
			while (true) {
				socket = server.accept();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// get username
				username = bufferedReader.readLine();
				System.out.println("User connected : " + username);
				DataOutputStream streamOut = new DataOutputStream(
						new BufferedOutputStream(socket.getOutputStream()));
				if (users.contains(username)) {
					System.out.println("User " + username + " has already logged in");
					streamOut.writeUTF("User " + username + " has already logged in");
					streamOut.flush();
					socket.close();
					return;
				}
				users.add(username);

				connectedClients.add(new ChatServerThread(socket, username));
				System.out.println("Client accepted: " + socket);
				// Start a new chat server thread
				new ChatServerThread(socket, username).start();

			} // while true

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	public static void main(String args[]) {
		ChatServer server = new ChatServer();
	}

}