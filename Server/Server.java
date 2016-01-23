package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server {
	
    private static final String USAGE
        = "usage: " + Server.class.getName() + " <name> <port>";

    /** Starts a Server-application. */
    public static void main(String[] args) {
    	if (args.length != 2) {
    		System.out.println(USAGE);
    		System.exit(0);
    	}
    	
    	String name = args[0];
    	ServerSocket servsock = null;
    	try {
    		servsock = new ServerSocket(Integer.parseInt(args[1]));
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	try {
    		Socket sock = servsock.accept();
    		Peer serverpeer = new Peer(name, sock);
    		Thread streamInputHandler = new Thread(serverpeer);
    		streamInputHandler.start();
    		serverpeer.handleTerminalInput();
    		serverpeer.shutDown();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }

} // end of class Server
