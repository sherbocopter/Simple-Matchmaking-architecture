package networkUtils;

/**
 *
 * @author SHerbocopter
 */
public class MMRange extends SerData {
    public int minLevel;
    public int maxLevel;
    
    public MMRange() {
        minLevel = 0;
        maxLevel = 0;
        System.out.println("I am MMRange!");
    }
    
    public MMRange(int minLevel, int maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }
}
