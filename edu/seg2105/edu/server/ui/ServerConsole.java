package edu.seg2105.edu.server.ui;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF {

	EchoServer server;
	Scanner fromConsole;
	
	ServerConsole(int port) {
		
		server = new EchoServer(port, this);
		
		fromConsole = new Scanner(System.in); 
	}
	
	
	
	
	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println("> " + message);
	}
	
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	      }
	      
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console! " + ex.getMessage());
	    }
	  }
	
	public static void main(String[] args)  {
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	     port = EchoServer.DEFAULT_PORT; //Set port to 5555
	    }
	    
	    ServerConsole serverC = new ServerConsole(port);
	    serverC.accept(); //wait for messages
	    
	}

}
