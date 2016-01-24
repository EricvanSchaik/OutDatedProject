package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.*;
import java.util.*;
import Client.*;
import Game.*;

public class ServerPeer implements Runnable {
    public static final String EXIT = "exit";

    protected ServerSocket ssock;
    public boolean isRunning = true;
    private List<ClientPeer> clientpeers;
    public List<Game> waiting;
    public List<Game> running;
    
    public ServerPeer(ServerSocket sockArg) throws IOException {
    	ssock = sockArg;
    	clientpeers = new ArrayList<ClientPeer>();
    }
    
    public void run() {
    	while (isRunning) {
    		try {
    			Socket clientsock = ssock.accept();
    			ClientPeer clientpeer = new ClientPeer(clientsock, this);
    			clientpeers.add(clientpeer);
    			clientpeer.run();
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    		if (!isRunning) {
    			shutDown();
    		}
    	}
    }
    
    
    public void shutDown() {
    	try {
    		ssock.close();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /*static public String print(String tekst) {
        System.out.print(tekst);
        String antw = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            antw = in.readLine();
        } catch (IOException e) {
        }
        return (antw == null) ? "" : antw;
    }*/
}	
	