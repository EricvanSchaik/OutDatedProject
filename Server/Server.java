package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Client.ClientPeer;
import Game.Game;

import java.util.*;

public class Server extends Thread {
	
    private static final String USAGE
        = "usage: " + Server.class.getName() + "<port>";
    protected ServerSocket servsock;
    public boolean isRunning = true;
    private List<ServerPeer> clientpeers;
    public List<Game> waiting;
    public List<Game> running;
    
    public static void main(String[] args) {
    	if (args.length != 1) {
    		System.out.println(USAGE);
    		System.exit(0);
    	}
    	ServerSocket servsock = null;
    	try {
    		servsock = new ServerSocket(Integer.parseInt(args[1]));
    		Server server = new Server(servsock);
    		server.run();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public Server (ServerSocket servsock) {
    	this.servsock = servsock;
    	clientpeers = new ArrayList<ServerPeer>();
    }
    
    public void run() {
    	
    }
    
    public void shutDown() {
    	try {
    		servsock.close();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    

}
