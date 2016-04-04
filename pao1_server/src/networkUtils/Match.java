package networkUtils;

/**
 *
 * @author SHerbocopter
 */
public class Match extends SerData {
    public PlayerCredentials pc;
    public String address;
    public int portClient; //says to player which port should be used as
    public int portServer; //client or, respectively, server

    public Match(PlayerCredentials pc, int portClient, String address, int portServer) {
        this.pc = pc;
        this.portClient = portClient;
        this.portServer = portServer;
        this.address = address;
    }
}
