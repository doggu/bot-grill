package utilities;

public class Misc {
    /**
     * positive numbers only
     * @param array of POSITIVE numbers, might i remind you
     */
    public static int gcf(int[] array) {
        int gcf = min(array);

        boolean satisfied = false;
        while (!satisfied) {
            int i;
            for (i=0; i<array.length&&array[i]%gcf==0; i++);

            if (i==array.length) satisfied = true;
            else gcf--;
        }

        return gcf;
    }

    public static void gcfDiv(int[] array) {
        int gcf = gcf(array);

        for (int i=0; i<array.length; i++) {
            array[i]/= gcf;
        }
    }

    public static int max(int[] array) {
        int max;
        try {
            max = array[0];
        } catch (IndexOutOfBoundsException ioobe) {
            return -1;
        }

        for (int i:array)
            if (i>max) max = i;

        return max;
    }
    public static int min(int[] array) {
        int min;
        try {
            min = array[0];
        } catch (IndexOutOfBoundsException ioobe) {
            return -1;
        }

        for (int i:array)
            if (i<min) min = i;

        return min;
    }

    public static int[] combine(int[] a, int[] b) {
        if (a.length!=b.length)
            return null;

        int[] c = new int[a.length];
        for (int i=0; i<a.length; i++) {
            c[i] = a[i]+b[i];
        }

        return c;
    }





    public static void main(String[] args) {
        int[] arr = {12, 16, 28, 24};

        System.out.println(gcf(arr));
    }
}
