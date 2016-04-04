package p2pUtils;

import javax.swing.JFrame;
import networkUtils.Match;

/**
 *
 * @author SHerbocopter
 */
public class P2PGame {
    public P2PChatGUI p2pChatGUI;
    public P2PServer p2pServer;
    public P2PClient p2pClient;
    public Match match;
    
    public P2PGame(Match match) throws InterruptedException {
        this.match = match;
        
        //openChatWindow();
        startP2PServer();
        startP2PClient();
    }
    
    private void openChatWindow() {
        p2pChatGUI = new P2PChatGUI(match);
        
        p2pChatGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p2pChatGUI.setTitle("Chat with: " + match.pc.username);
        p2pChatGUI.setSize(500, 500);
        p2pChatGUI.setResizable(false);
        
        p2pChatGUI.setLocationRelativeTo(null);
        
        p2pChatGUI.initUI();
        
        p2pChatGUI.setVisible(true);
    }
    
    private void startP2PServer() {
        p2pServer = new P2PServer(match);
        p2pServer.start();
    }
    
    private void startP2PClient() {
        p2pClient = new P2PClient(match);
        p2pClient.start();
    }
}
