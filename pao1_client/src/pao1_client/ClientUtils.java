package pao1_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkUtils.BooleanMessage;
import networkUtils.MMRange;
import networkUtils.Match;
import networkUtils.Message;
import networkUtils.Message.MsgType;
import networkUtils.PlayerCredentials;
import p2pUtils.P2PGame;

/**
 *
 * @author SHerbocopter
 */
public class ClientUtils {
    private static ClientUtils instance = new ClientUtils();
    private ClientUtils() { }
    public static ClientUtils getInstance() { return instance; }
    
    private String      serverAddress = "localhost";
    private int         port = 9090;
    private Socket      clientSocket;
    ObjectOutputStream  streamToServer;
    ObjectInputStream   streamFromServer;
    
    private PlayerCredentials pc;
    private boolean loggedIn = false;
    
    public void connect() throws IOException {
        System.out.println(">Establishing connection...");
        clientSocket = new Socket(serverAddress, port);
        
        streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
    }
    
    public void readCredentials() {
        pc = new PlayerCredentials();
        Scanner scan = new Scanner(System.in);
        String line;
        
        System.out.println(">Enter credentials");
        
        System.out.print("\tUsername: ");
        line = scan.nextLine();
        pc.username = line;
        
        System.out.print("\tLevel: ");
        line = scan.nextLine();
        pc.level = Integer.parseInt(line);
    }
    
    public boolean login() throws IOException, ClassNotFoundException, Exception {
        if (loggedIn == true) {
            System.out.println(">Already logged in");
        }
        
        if (pc == null) {
            readCredentials();
        }
        
        System.out.println(">Attempting login");
        
        Message req = new Message();
        req.type = MsgType.REQ_LOGIN;
        req.data = pc;
        
        streamToServer.writeObject(req);
        
        Message res = (Message) streamFromServer.readObject();
        
        if (res.type != MsgType.RES_LOGIN) {
            throw new Exception();
        }
        
        //is this safe?
        BooleanMessage b = (BooleanMessage) res.data;
        loggedIn = b.value;
        return b.value;
    }
    
    public Match sendMMRequest() throws IOException, ClassNotFoundException, Exception {
        if (loggedIn == false) {
            try {
                login();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        System.out.println(">Enter MM specs");
        
        Scanner scan = new Scanner(System.in);
        String line;
        int minLevel;
        int maxLevel;
        
        System.out.print("\tminLevel: ");
        line = scan.nextLine();
        minLevel = Integer.parseInt(line);
        
        System.out.print("\tmaxLevel: ");
        line = scan.nextLine();
        maxLevel = Integer.parseInt(line);
        
        System.out.println(">Attempting MM");
        
        MMRange mmRange = new MMRange(minLevel, maxLevel);
        
        Message req = new Message();
        req.type = MsgType.REQ_MM;
        req.data = mmRange;
        
        streamToServer.writeObject(req);
        
        System.out.println(">Queuing for match");
        
        Message res = (Message) streamFromServer.readObject();
        
        if (res.type != MsgType.RES_MM) {
            throw new Exception();
        }
        
        Match match = (Match) res.data;
        System.out.format(">Matched with %s(%d)"
                        + "\n\tportClient: %d"
                        + "\n\tIP: %s"
                        + "\n\tportServer: %d\n",
                                match.pc.username, match.pc.level,
                                match.portClient,
                                match.address, match.portServer);
        
        return match;
    }
    
    public P2PGame startP2PGame(Match match) throws InterruptedException {
        return new P2PGame(match);
    }
}
