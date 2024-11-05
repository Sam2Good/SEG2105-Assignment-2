package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
//import edu.seg2105.edu.server.ui.ServerConsole;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
    
//May Move 
    try 
    {
      listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
      System.out.println(ex.getMessage());
    }
    
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	String message = msg.toString();
	if (message.contains("#login")) { //#login message handling 
		if (client.getInfo("loginID") == null) { //first #login
			//System.out.println("login message ");
		
			String login = message.split(" ")[1];
		
			//System.out.println("LoginID: " + login);
		
			serverUI.display("Message received: " + message + " from " + client.getInfo("loginID"));
			client.setInfo("loginID", login);
			message = client.getInfo("loginID") +  " has logged on.";
			serverUI.display(client.getInfo("loginID") +  " has logged on.");
		} else {	 //not first #login
			try 
			{
				client.sendToClient("Erorr: loginID has already been set terminating connectiong");
				client.close();
			}
			catch (Exception e) {}
			
		}
	} else {  //Normal message
		//System.out.println("Normal message ");
		
		serverUI.display("Message received: " + message + " from " + client.getInfo("loginID"));
		message =  client.getInfo("loginID") + "> " + message;
		//this.sendToAllClients(message);
	}
	this.sendToAllClients(message);
  }
  
//TODO document
  public void handleMessageFromServerUI(String message) {
	  //prefix message 
	  if (message.startsWith("#")) {
		  handleCommand(message);
	  }
	  else {
		  sendToAllClients("SERVER MSG> " + message);
	  }
  }
  
//TODO document
    
  private void handleCommand(String command) {
	  if (command.equals("#quit")) {
		  serverUI.display("Quiting Server program");	  
		  try 
		  {
			  close();
		  } 
		  catch (IOException e) {}
		  System.exit(0);
		  
	  } else if (command.equals("#stop")) {
		  stopListening();  
	  } else if (command.contains("#close")) { 
		  try {
			close();
		} catch (IOException e) {}
	  } else if (command.contains("#setport")) {
		  String port = command.split(" ")[1];
		  if (port != null && !port.equals("")) {
			  setPort(Integer.parseInt(port));
			  serverUI.display("Port succesfully changed to: " + Integer.toString(getPort()));
		  } else { 
			  serverUI.display("Please try again with port value");
		  }
	  } else if (command.equals("#start")) {
		  if (!isListening()) {
			  try {
				listen();
			} catch (Exception e) {}
			  
		  }else {
			  serverUI.display("Server is already listening");
		  }
		 
	  } else if (command.equals("#getport")) {
		  serverUI.display(Integer.toString(getPort()));		  
	  } else {
		  serverUI.display("Command not Recognized");
	  }
  }
  
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the "SERVER MSG> starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
 
//TODO !!TEST!! never called?  
  /**
   * Implements the hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *  
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  String login = client.getInfo("loginID").toString();
	  serverUI.display(login + " has Disconnected");
  }

  /**
   * Implements the hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  serverUI.display("A new client has connected to the server.");
	  //serverUI.display("Client Connected: " + client.toString());  
  }
  
  //Class methods ***************************************************
  
//  /**
//   * This method is responsible for the creation of 
//   * the server instance (there is no UI in this phase).
//   *
//   * @param args[0] The port number to listen on.  Defaults to 5555 
//   *          if no argument is entered.
//   */
//  public static void main(String[] args) 
//  {
//    int port = 0; //Port to listen on
//
//    try
//    {
//      port = Integer.parseInt(args[0]); //Get port from command line
//    }
//    catch(Throwable t)
//    {
//      port = DEFAULT_PORT; //Set port to 5555
//    }
//	
//    EchoServer sv = new EchoServer(port);
//    
//    try 
//    {
//      sv.listen(); //Start listening for connections
//    } 
//    catch (Exception ex) 
//    {
//      System.out.println("ERROR - Could not listen for clients!");
//    }
//  }

}
//End of EchoServer class
