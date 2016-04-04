package pao1_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkUtils.BooleanMessage;
import networkUtils.MMRange;
import networkUtils.Match;
import networkUtils.Message;
import networkUtils.Message.MsgType;
import networkUtils.PlayerCredentials;

/**
 *
 * @author SHerbocopter
 */
public class ClientHandler extends Thread {
    private Socket clientSocket;
    
    public ObjectInputStream streamFromClient;
    public ObjectOutputStream streamToClient;
    public String ipAddress; //not used
    
    public boolean inQueue;
    PlayerCredentials pc;
    MMRange mmRange;
    
    private static int portMatchServer = 9091;
    private static int portMatchClient = 9092;
    
    public ClientHandler(Socket socket) {
        clientSocket = socket;
        
        try {
            streamFromClient = new ObjectInputStream(clientSocket.getInputStream());
            streamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            streamToClient.flush();
            
            ipAddress = getIPAddress();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
    @Override
    public void run() {
        try {
            System.out.println(">Guest connected from ip: " + ipAddress);
            
            boolean stillConnected = true;
            while (stillConnected) {
                Message req = (Message) streamFromClient.readObject();
                Message res = new Message();
                
                switch(req.type) {
                    case MSG_DISCONNECT: {
                        
                    } break;
                    case REQ_LOGIN: {
                        processLogin(req);
                    } break;
                    case REQ_MM: {
                        processMM(req);
                    }
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void processLogin(Message req) {
        try {
            pc = (PlayerCredentials) req.data;
            System.out.format(">Guest logged in as: %s(%d)\n",  pc.username, pc.level);
            Message res = new Message();
            res.type = MsgType.RES_LOGIN;
            res.data = new BooleanMessage(true);
            streamToClient.writeObject(res);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void processMM(Message req) {
        try {
            mmRange = (MMRange) req.data;
            System.out.format(">Player %s(%d) wants to MM (%d, %d)\n",
                    pc.username, pc.level, mmRange.minLevel, mmRange.maxLevel);
            
            ClientHandler match = Server.findMatch(this);
            
            if (match == null) {
                System.out.format(">No immediate match found for %s(%d)(%d, %d)\n",
                        pc.username, pc.level, mmRange.minLevel, mmRange.maxLevel);
            }
            else {
                System.out.format(">Matched %s(%d) with %s(%d)\n",
                        pc.username, pc.level, match.pc.username, match.pc.level);
                this.sendMMResult(match, true);
                match.sendMMResult(this, false);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void sendMMResult(ClientHandler match, boolean portSwitch) throws IOException {
        Message res = new Message();
        res.type = MsgType.RES_MM;
        
        PlayerCredentials pcMatch = match.pc;
        String ipMatch = match.getIPAddress();
        int pClient = portSwitch ? portMatchServer : portMatchClient;
        int pServer = portSwitch ? portMatchClient : portMatchServer;
        Match matchData = new Match(match.pc, pClient, ipMatch, pServer);
        
        res.data = matchData;
        
        streamToClient.writeObject(res);
    }
    
    private String getIPAddress() {
        return clientSocket.getInetAddress().getHostAddress();
    }
}
