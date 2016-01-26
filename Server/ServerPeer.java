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
    private boolean connected;
    public Game game;
    private String[] commands = {"join", "hello", "place", "trade", "help"};
    private List<String> commandslist = Arrays.asList(commands);
    private boolean joined;
    
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
    			String[] command = input.split(" ");
    			if (commandslist.contains(command[0])) {
    				executeCommand(command[0],command[1]);
    			}
    			else {
    				write("error 0");
    			}
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	try {
    		sock.close();
    		connected = false;
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void executeCommand(String command, String specs) {
    	if (command.equals("hello")) {
			if (connected) {
				write("error 0");
			}
			else if (!isValidName(specs)) {
				write("error 2");
			}
			else {
				write("Hello from the other side");
				setName(specs);
				connected = true;
			}
		}
    	else if (command.equals("join")) {
    		if (joined || !(Integer.parseInt(specs) > 1 && Integer.parseInt(specs) < 5)) {
    			write("error 0");
    		}
    		else {
    			join(Integer.parseInt(specs));
    		}
    	}
    }
    
    public void join(int gamesize) {
    	boolean exists = false;
    	for (Map.Entry<Game, List<Player>> e: server.waiting.entrySet()) {
    		if (e.getKey().gameSize() == gamesize) {
    			exists = true;
    			Game game = e.getKey();
    		}
    	}
    	if (exists) {
    		game.addSpeler(new HumanPlayer(this, game));
    		if (game.isRunning) {
    			server.waiting.remove(game);
    			server.running.put(game, game.getSpelers());
    		}
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
    
    public void write(String message) {
    	try {
    		out.write(message + "/n");
    		out.flush();
    	}
    	catch (IOException e) {
    		System.out.println("error 3");
    		server.sendAllClients(getName() + "disconnected");
    		connected = false;
    	}
    }
    
    public boolean isValidName(String name) {
    	if (commandslist.contains(name)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
}	
	