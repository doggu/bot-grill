package utilities;

import java.math.BigDecimal;
import java.math.MathContext;

public class Stopwatch {
    private long start = 0, stop = 0;


    public void start() { start = System.nanoTime(); }
    public void stop() { stop = System.nanoTime(); }
    private long nanosecondsElapsed() { return stop-start; }

    public double timeInSeconds() {
        if (start==0) return 0L;
        if (stop==0) stop();

        return BigDecimal.valueOf(nanosecondsElapsed() / 1000000000.0)
                .round(new MathContext(3)).doubleValue();
    }

    /**
     * prints out the result of the timer, thereby also stopping it.
     * @return a string formatted as: "done ([double] s)!"
     */
    public String presentResult() {
        stop();
        return "done ("+BigDecimal.valueOf(timeInSeconds())
                .round(new MathContext(3))+")!";
    }

    public double split() {
        long stop = nanosecondsElapsed();
        this.stop = 0;
        return BigDecimal.valueOf(stop / 1000000000.0)
                .round(new MathContext(3)).doubleValue();
    }
}
