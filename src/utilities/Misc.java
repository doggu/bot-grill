package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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




    //everything below this is being used to probe through the source code to
    //determine length, non-space chars, etc.
    private static ArrayList<File> getAllFiles(File topDir) {
        ArrayList<File> files =
                new ArrayList<>(Arrays.asList(topDir.listFiles()));

        for (int i=0; i<files.size(); i++) {
            File file = files.get(i);
            if (file.isDirectory()) {
                files.remove(i);
                i--;
                files.addAll(getAllFiles(file));
            }
        }

        return files;
    }
    public static void main(String[] args) {
        ArrayList<File> files = getAllFiles(new File("./src"));

        for (int i=0; i<files.size(); i++) {
             if (!files.get(i).getName().endsWith(".java")) {
                files.remove(i);
                i--;
            }
        }

        Scanner in;
        int lines = 0;
        int chars = 0;

        for (File file:files) {
            try {
                in = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            while (in.hasNextLine()) {
                String line = in.nextLine();
                for (int i=0; i<line.length(); i++) {
                    if (line.charAt(i)!=' ') {
                        chars++;
                    }
                }
                lines++;
            }
        }

        System.out.println(lines);
        System.out.println(chars);
    }
}
