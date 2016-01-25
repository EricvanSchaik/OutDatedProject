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

public class ServerPeer extends Thread {
    
	protected String name;
    protected Socket sock;
    protected BufferedReader in;
    protected BufferedWriter out;
    private Server server;
    
    public ServerPeer(Socket sockArg, Server server) throws IOException {
    	this.sock = sockArg;
    	in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    	out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    	this.server = server;
    }
    
    public void run() {
    	String input = null;
    	while (server.isRunning) {
    		try {
    			input = in.readLine();
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	try {
    		sock.close();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void shutDown() {
    	try {
    		sock.close();
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
	