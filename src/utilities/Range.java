package utilities;

public class Range<T extends Comparable<T>> {
    private T min, max;



    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }



    public T getMin() { return min; }
    public T getMax() { return max; }

    public void updateRange(T newVal) {
        if (newVal==null) return;
        min = (min.compareTo(newVal)>0?newVal:min);
        max = (max.compareTo(newVal)<0?newVal:max);
    }

    public boolean infintesimalRange() { return min.compareTo(max)==0; }

    public boolean inThisRange(T val) {
        return min.compareTo(val)<=0 && max.compareTo(val)>=0;
    }



    public String toString() {
        return (min+" - "+max);
    }
}
