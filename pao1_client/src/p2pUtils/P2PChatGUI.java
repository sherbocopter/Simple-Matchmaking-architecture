package p2pUtils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import networkUtils.Match;

/**
 *
 * @author SHerbocopter
 */
public class P2PChatGUI extends JFrame {
    public Match match;
    
    private JTextArea logTextArea;
    private JTextField messageTextField;
    private JButton sendButton;
    private Container pane;
    private Insets insets;
    
    public P2PChatGUI(Match match) {
        this.match = match;
    }
    
    public void initUI() {
        pane = this.getContentPane();
        insets = this.getInsets();
        pane.setLayout(null);
        
        //ZERO SHAME HARDCODING
        logTextArea = new JTextArea();
        pane.add(logTextArea);
        logTextArea.setBounds(5, 5, 470, 400);
    }
}
