package p2pUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import networkUtils.Match;
import networkUtils.Message;

/**
 *
 * @author SHerbocopter
 */
public class P2PServer extends Thread {
    public ServerSocket serverSocket;
    public Socket clientSocket;
    public ObjectInputStream streamFromClient;
    public ObjectOutputStream streamToClient;
    public Match match;
    
    public P2PServer(Match match) {
        this.match = match;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(match.portServer);
            
            clientSocket = serverSocket.accept();
            
            streamFromClient = new ObjectInputStream(clientSocket.getInputStream());
            streamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            streamToClient.flush();
            
            boolean stillConnected = true;
            while (stillConnected) {
                Message msg = (Message)streamFromClient.readObject();
                switch (msg.type) {
                    case MSG_TEXT: {
                        printTextMessage(msg.text);
                    } break;
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void printTextMessage(String text) {
        System.out.format("%s(%d) : %s\n", match.pc.username, match.pc.level, text);
    }
}
