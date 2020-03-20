package events.commands.campus;

public class TimeOfDay implements Comparable<TimeOfDay> {
    private final int h, m;
    private final float s;


    /**
     * 24-hour format only.
     * @param hour a value from 0 - 23,
     *             representing the hour of a day.
     * @param minute a value from 0 - 59,
     *               representing the minute of the hour.
     * @param second a value from 0 - <60,
     *               representing the second of the minute.
     */
    TimeOfDay(int hour, int minute, float second)
            throws NumberFormatException {
        if ((hour>23||hour<0) ||
                (minute<0||minute>59) ||
                (second<0||second>=60))
            throw new NumberFormatException();
        this.h = hour;
        this.m = minute;
        this.s = second;
    }


    public int compareTo(TimeOfDay t) {
        int     h = Integer.compare(t.h, this.h),
                m = Integer.compare(t.m, this.m),
                s = Float.compare(t.s, this.s);

        if (h!=0) return h;
        if (m!=0) return m;

        System.out.println(s+"\t"+this+"\t"+t);
        return s;
    }



    public String toString() {
        return (h/10>0?"":"0")+h +
                ":"+(m/10>0?"":"0")+m +
                ":"+(s/10>0?"":"0")+s;
    }
}
