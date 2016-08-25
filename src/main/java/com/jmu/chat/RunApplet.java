package com.jmu.chat;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class RunApplet {
	public static void main(String args[]) {
		if (args.length != 2) {
			System.out.println("Usage: java -jar RunApplet.jar <username> <host>");
		}
		else {
		   JFrame frame = new JFrame("Chat with " + args[0]);
		   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   frame.setSize(500, 400);
		   JApplet applet = new ChatGUI(args[1],args[0]);
		   applet.init();
		   applet.start();
		   frame.add(applet);
		   frame.setVisible(true);
		}
	}
}
