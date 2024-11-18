package Server;

import java.io.*;
import java.net.*;

public class Provider
{	
	
    // Main method to start the Provider server
	public static void main(String args[])
	{
		// ServerSocket to listen for incoming client connections
		ServerSocket providerSocket;
		try 
		{
			// Create a ServerSocket and bind it to port 2004
			providerSocket = new ServerSocket(2004);
			
			// Continue accepting client connections indefinitely
			while(true)
			{
				// 1. Wait for connection
				System.out.println("Waiting for connection");
				
				// Accept a client connection, blocking until a connection is established
				Socket connection = providerSocket.accept();
				
				// Create a new ServerThread for each client connection and start it
				ServerThread T1 = new ServerThread(connection);
				T1.start();
			} 
			
		}
		
		// Handle IOException if it occurs during the creation of ServerSocket
		catch (IOException e1) 
		{
			e1.printStackTrace(); // Print the exception trace for debugging
		}	
	}
}
