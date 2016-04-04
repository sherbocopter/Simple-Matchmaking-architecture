package pao1_server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import networkUtils.PlayerCredentials;

/**
 *
 * @author SHerbocopter
 */
public class Server {
    public static ServerSocket serverSocket;
    public static ArrayList<ClientHandler> clients;
    public static ArrayList<ClientHandler> clientsInQueue;

    private static final Object clientsLock = new Object();
    private static final Object queueLock = new Object();
    
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(9090);
            clients = new ArrayList<>();
            clientsInQueue = new ArrayList<>();
            
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler ch = new ClientHandler(socket);
                
                addClient(ch);
                
                ch.start();
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private static synchronized void addClient(ClientHandler ch) {
            clients.add(ch);
    }
    
    private static synchronized void removeClient(ClientHandler ch) {
        clients.remove(ch);
    }
    
    private static void addClientInQueue(ClientHandler ch) {
        synchronized (queueLock) {
            ch.inQueue = true;
            clientsInQueue.add(ch);
        }
    }
    
    private static void removeClientFromQueue(ClientHandler ch) {
        synchronized (queueLock) {
            ch.inQueue = false;
            clientsInQueue.remove(ch);
        }
    }
    
    public static synchronized ClientHandler findMatch(ClientHandler ch) {
        synchronized (queueLock) {
            Iterator<ClientHandler> it = clientsInQueue.iterator();
            
            while (it.hasNext()) {
                ClientHandler match = it.next();
                
                if (playersMatch(ch, match)) {
                    it.remove();
                    
                    return match;
                }
            }
            
            addClientInQueue(ch);
            
            return null;
        }
    }
    
    private static boolean inInterval(int a, int l, int r) {
        return (l <= a && a <= r);
    }
    
    private static boolean playersMatch(ClientHandler ch1, ClientHandler ch2) {
        int level1 = ch1.pc.level;
        int min1 = ch1.mmRange.minLevel;
        int max1 = ch1.mmRange.maxLevel;
        
        int level2 = ch2.pc.level;
        int min2 = ch2.mmRange.minLevel;
        int max2 = ch2.mmRange.maxLevel;
        
        return (inInterval(level1, min2, max2) && inInterval(level2, min1, max1));
    }
}
