package pao1_client;

import networkUtils.Match;
import p2pUtils.P2PGame;

/**
 *
 * @author SHerbocopter
 */
public class Client {
    public static void main(String[] args) {
        ClientUtils client = ClientUtils.getInstance();
        
        try {
            client.connect();
            
            boolean loginSuccess = false;
            while (!loginSuccess) {
                loginSuccess = client.login();
                if (loginSuccess) {
                    System.out.println(">Login successful");
                }
                else {
                    System.out.println(">Login failed");
                }
            }
            
            Match match = client.sendMMRequest();
            
            P2PGame game = client.startP2PGame(match);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
