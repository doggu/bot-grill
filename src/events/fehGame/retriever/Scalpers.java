package events.fehGame.retriever;

import java.util.ArrayList;

public class Scalpers extends ArrayList<Scalper> {
    /**
     * applies scalpers contained in this until either one comes up true or all
     * have been applied and returned false.
     *
     * @param s the string to be analyzed
     * @return true if a scalper returned true, false otherwise
     */
    public boolean apply(String s) {
        for (Scalper scalper : this) {
            if (scalper.apply(s)) {
                return true;
            }
        }
        return false;
    }
}
