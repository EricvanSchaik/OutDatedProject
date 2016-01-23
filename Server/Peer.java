package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * Peer for a simple client-server application
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Peer implements Runnable {
    public static final String EXIT = "exit";

    protected String name;
    protected Socket sock;
    protected BufferedReader in;
    protected BufferedWriter out;
    private String input;
    
    
    /*@
       requires (nameArg != null) && (sockArg != null);
     */
    /**
     * Constructor. creates a peer object based in the given parameters.
     * @param   nameArg name of the Peer-proces
     * @param   sockArg Socket of the Peer-proces
     */
    public Peer(String nameArg, Socket sockArg) throws IOException {
    	name = nameArg;
    	sock = sockArg;
    	in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    	out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }
    
    /**
     * Reads strings of the stream of the socket-connection and
     * writes the characters to the default output.
     */
    public void run() {
    	try {
    		while ((input = in.readLine()) != null) {
    			System.out.println(input);
    		}
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    }
    
    
    /**
     * Reads a string from the console and sends this string over
     * the socket-connection to the Peer process.
     * On Peer.EXIT the method ends
     */
    public void handleTerminalInput() {
    	Scanner scanin = new Scanner(System.in);
    	try {
    		while (!sock.isClosed()) {
    			if (scanin.hasNextLine()) {
    				if ((input = scanin.nextLine()).equals("EXIT")) {
    					shutDown();
    				}
    				else {
    					out.write(input + "\n");
    					out.flush();
    				}
    			}
    		}
    	}
    	catch (IOException e) {
    		if (e instanceof SocketException) {
    			System.out.println("Peer has been closed, closing socket now...");
    			shutDown();
    			System.out.println("Socket has been closed");
    		}
    		else {
    			e.printStackTrace();
    		}
    	}
    	finally {
			scanin.close();
		}
    }
    
    /**
     * Closes the connection, the sockets will be terminated
     */
    public void shutDown() {
    	try {
    		sock.close();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**  returns name of the peer object*/
    public String getName() {
        return name;
    }
    
    /** read a line from the default input */
    static public String readString(String tekst) {
        System.out.print(tekst);
        String antw = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            antw = in.readLine();
        } catch (IOException e) {
        }
        return (antw == null) ? "" : antw;
    }
}	
	