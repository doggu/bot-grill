package utilities;

public class Range {
    private int min, max;



    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }



    public int getMin() { return min; }
    public int getMax() { return max; }

    public void updateRange(int newVal) {
        min = (newVal<min?newVal:min);
        max = (newVal>max?newVal:max);
    }

    public boolean infintesimalRange() { return min==max; }
}
