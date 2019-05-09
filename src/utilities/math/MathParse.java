package utilities.math;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;



public class MathParse {
    public static Function<Double,Double> parseProblem(String f) throws Error {
        ArrayList<Function<Double,Double>> fxns = new ArrayList<>();
        ArrayList<Character> ops = new ArrayList<>();

        String num = "";
        for (int i=0; i<f.length(); i++) {
            char c = f.charAt(i);

            //declare final value for lambda expression
            Double v;
            try {
                v = Double.parseDouble(num);
            } catch (NumberFormatException g) {
                v = null;
            }
            final Double val = v;

            switch(c) {
                case '(':
                    int pbalance = 0;
                    int start = i+1;
                    for (; i<f.length(); i++) {
                        if (f.charAt(i)=='(') pbalance++;
                        if (f.charAt(i)==')') pbalance--;
                        if (pbalance==0) break;
                    }
                    if (pbalance!=0) {
                        System.out.println("imbalanced parentheses!");
                        throw new Error();
                    }
                    fxns.add(parseProblem(f.substring(start, i)));
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                    num+= c;
                    if (i==f.length()-1) {
                        try {
                            final double n = Double.parseDouble(num);
                            fxns.add(x -> n);
                        } catch (NumberFormatException g) {
                            System.out.println("incorrect number at the end somehow idk figure it out");
                            throw new Error();
                        }

                    }
                    break;
                case 'x':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add('*');
                    }
                    fxns.add(x -> x);
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    if (val!=null) {
                        fxns.add(x -> val);
                    }
                    num = "";
                    ops.add(c);
            }
        }
        for (int i=0; i<fxns.size(); i++) {
            System.out.println(fxns.get(i));
            if (i<ops.size()) System.out.println(ops.get(i));
        }

        System.out.println(fxns);

        for (int i=0; i<ops.size(); i++) {
            if (ops.get(i)=='^') {
                final Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        Math.pow(a.apply(y), b.apply(y));

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            }
        }

        //peMDas
        for (int i=0; i<ops.size(); i++) {
            if (ops.get(i)=='*') {
                final Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)*b.apply(y);

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            } else if (ops.get(i)=='/') {
                final Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)/b.apply(y);

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            }
        }

        //pemdAS
        for (int i=0; i<ops.size(); i++) {
            if (ops.get(i)=='+') {
                final Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)+b.apply(y);

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            } else if (ops.get(i)=='-') {
                final Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)-b.apply(y);

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            }
        }

        return fxns.get(0);
    }



    public static void main(String[] args) {
        double[] TV = {0,1,2,3,4,5,6,7};
        double[] OV = new double[8];

        System.out.print("enter a function: ");
        Scanner input = new Scanner(System.in);
        String function = input.nextLine();
        while (!function.equals("quit")) {
            try {
                Function<Double,Double> f = parseProblem(function);
                for (int i=0; i<TV.length; i++) OV[i] = f.apply(TV[i]);

                System.out.println("evaulations for test values:");

                String divisor = "+-----+-----------\n";
                String table = divisor +
                        "|  I  |  O\n";
                for (int i=0; i<TV.length; i++)
                    table+= divisor +
                            "| "+TV[i]+" | "+OV[i]+"\n";

                System.out.println(table);
            } catch (Error format) {
                System.out.println("incorrect format!");
            }

            System.out.print("enter a function: ");
            function = input.nextLine();
        }
    }
}
