package p2pUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import networkUtils.Match;
import networkUtils.Message;
import networkUtils.Message.MsgType;

/**
 *
 * @author SHerbocopter
 */
public class P2PClient extends Thread {
    public Socket clientSocket;
    public ObjectInputStream streamFromServer;
    public ObjectOutputStream streamToServer;
    public Match match;
    public int pollingTime = 3000;
    
    public P2PClient(Match match) {
        this.match = match;
    }
    
    @Override
    public void run() {
        try {
            boolean foundServer = false;
            while (!foundServer) {
                try {
                    clientSocket = new Socket(match.address, match.portClient);
                    foundServer = true;
                }
                catch (Exception ex) {
                    System.out.format(">Peer not found, trying again in %d seconds\n",
                                        pollingTime / 1000);
                    foundServer = false;
                    Thread.sleep(pollingTime);
                }
            }
            
            streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
            
            while (true) {
                sendTextMessage();
            }
        }
        catch (Exception ex) {
            System.out.println();
        }
    }

    private void sendTextMessage() throws IOException {
        System.out.print("You: ");
        Scanner scan = new Scanner(System.in);
        String line;
        
        line = scan.nextLine();
        
        Message msg = new Message();
        msg.type = MsgType.MSG_TEXT;
        msg.data = null;
        msg.text = line;
        
        streamToServer.writeObject(msg);
    }
}
