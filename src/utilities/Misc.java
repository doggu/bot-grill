package utilities;

import java.util.Scanner;

public class Misc {
    /**
     * positive numbers only
     *
     * @param a an array of POSITIVE numbers, might i remind you
     */
    public static int gcf(int... a) {
        int gcf = min(a);

        boolean satisfied = false;
        while (!satisfied) {
            int i;
            for (i = 0; i<a.length && a[i]%gcf==0; i++) ;

            if (i==a.length) satisfied = true;
            else gcf--;
        }

        return gcf;
    }

    public static void gcfDiv(int... a) {
        int gcf = gcf(a);

        for (int i = 0; i<a.length; i++) {
            a[i] /= gcf;
        }
    }

    public static int lcm(int... a) {
        int max = max(a);

        int lcm = 0;

        boolean solved;
        do {
            lcm += max;
            solved = true;
            for (int value : a) {
                if (lcm%value!=0) {
                    solved = false;
                    break;
                }
            }
        } while (!solved);

        return lcm;
    }

    public static int max(int... a) {
        int max;
        try {
            max = a[0];
        } catch (IndexOutOfBoundsException ioobe) {
            return -1;
        }

        for (int i : a)
            if (i>max) max = i;

        return max;
    }

    public static int min(int... a) {
        int min;
        try {
            min = a[0];
        } catch (IndexOutOfBoundsException ioobe) {
            return -1;
        }

        for (int i : a)
            if (i<min) min = i;

        return min;
    }

    public static int[] combine(int[] a, int[] b) {
        if (a.length!=b.length)
            return null;

        int[] c = new int[a.length];
        for (int i = 0; i<a.length; i++) {
            c[i] = a[i]+b[i];
        }

        return c;
    }


    public static double softGCF(double... a) {
        assert a.length>0 : "no numbers";
        double gcf = a[0];
        for (int i=1; i<a.length; i++) {
            //System.out.println("gcf "+(i-1)+", "+i);
            gcf = softGCF(gcf, a[i]);
        }
        return gcf;
    }
    private static double softGCF(double a, double b /*, double precision*/) {
        //System.out.println(a+", "+b);
        if (Math.abs(a-b)<1) return a;
        if (a>b) {
            return softGCF(a-b, b);
        } else if (b>a) {
            return softGCF(a, b-a);
        } else {
            return a;
        }
    }


    public static void main(String[] args) {
        int[] arr = {12, 16, 28, 24};

        System.out.println(gcf(arr));

        Scanner in = new Scanner(System.in);

        String line;
        while (!(line = in.nextLine()).equalsIgnoreCase("quit")) {
            String[] strs = line.split(" ");
            double[] dbls = new double[strs.length];

            for (int i=0; i<strs.length; i++)
                dbls[i] = Double.parseDouble(strs[i]);

            System.out.println(softGCF(dbls));
        }
    }
}
