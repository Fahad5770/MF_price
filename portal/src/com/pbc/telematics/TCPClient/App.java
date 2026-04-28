package com.pbc.telematics.TCPClient;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
	
	public static void main(String[] args) {
		try {

			
			System.out.println("Process started New");
			String clientSentence;
			ServerSocket welcomeSocket = new ServerSocket(8091);
			while (true) {
				Socket connectionSocket = welcomeSocket.accept();
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				// DataOutputStream outToClient = new
				// DataOutputStream(connectionSocket.getOutputStream());

				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);

				// InetAddress IPAddress = welcomeSocket.getInetAddress();
				

				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
