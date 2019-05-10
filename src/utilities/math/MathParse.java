package utilities.math;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;



public class MathParse {
    public static Function<Double,Double> parseProblem(String f) throws Error {
        ArrayList<Function<Double,Double>> fxns = new ArrayList<>();
        ArrayList<Character> ops = new ArrayList<>();

                //constants
        f = f   .replaceAll("pi","π")
                //inverse trig functions (so the regular ones don't override them)
                //(even though i could just have it replace "as" later
                .replaceAll("asin","S")
                .replaceAll("acos","C")
                .replaceAll("atan","T")
                //trigonometric functions
                .replaceAll("sin","s")
                .replaceAll("cos","c")
                .replaceAll("tan","t")
                //logarithmic functions
                .replaceAll("log","l")
                .replaceAll("ln","n");

        String num = "";
        for (int i=0; i<f.length(); i++) {
            char c = f.charAt(i);

            //declare final value for lambda expression
            Double v;
            try {
                v = Double.parseDouble(num);
            } catch (NumberFormatException g) {
                //System.out.println("no num");
                v = null;
            }
            final Double val = v;

            switch(c) {
                case '(':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add('*');
                    } else if (fxns.size()!=ops.size())
                        ops.add('*');
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
                    break;
                case 'x':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add('*');
                    } else if (fxns.size()!=ops.size())
                        ops.add('*');

                    fxns.add(x -> x);
                    break;
                case 'e':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add('*');
                    } else if (fxns.size()!=ops.size())
                        ops.add('*');

                    fxns.add(x -> Math.E);
                    break;
                case 'π':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add('*');
                    } else if (fxns.size()!=ops.size())
                        ops.add('*');

                    fxns.add(x -> Math.PI);
                    break;
                case '-': //special for negative numbers
                    if (fxns.size()==0) {
                        num+= '-';
                        break;
                    } //else it's  an operator
                case '+':
                case '*':
                case '/':
                case '^':
                case '%':
                case 's':
                case 'c':
                case 't':
                case 'S':
                case 'C':
                case 'T':
                case 'l':
                case 'n':
                    if (val!=null) {
                        fxns.add(x -> val);
                    } else if (ops.size()==fxns.size()) {
                        fxns.add(x -> 1.0);
                    }
                    num = "";
                    ops.add(c);
            }
        }
        
        if (num.length()>0) {
            try {
                final double val = Double.parseDouble(num);
                fxns.add(x -> val);
            } catch (NumberFormatException g) {
                System.out.println("encountered incomplete statement at the end i guess");
                throw new Error();
            }
        }
        if (fxns.size()==ops.size()) ops.remove(ops.size()-1);

        /*
        for (int i=0; i<fxns.size(); i++) {
            System.out.println(fxns.get(i));
            if (i<ops.size()) System.out.println(ops.get(i));
        }

        System.out.println(fxns);
        */

        //special functions (sin, cos, ln, etc.)
        for (int i=0; i<ops.size(); i++) {
            final Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
            Function<Double,Double> newF;
            switch(ops.get(i)) {
                case 's':
                    newF = y ->
                            a.apply(y)*Math.sin(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
                case 'c':
                    newF = y ->
                            a.apply(y)*Math.cos(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
                case 't':
                    newF = y ->
                            a.apply(y)*Math.tan(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
                case 'S':
                    newF = y ->
                            a.apply(y)*Math.asin(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
                case 'C':
                    newF = y ->
                            a.apply(y)*Math.acos(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
                case 'T':
                    newF = y ->
                            a.apply(y)*Math.atan(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
                case 'l':
                    newF = y ->
                            a.apply(y)*Math.log10(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
                case 'n':
                    newF = y ->
                            a.apply(y)*Math.log(b.apply(y));

                    fxns.remove(a);
                    fxns.remove(b);
                    ops.remove(i);
                    fxns.add(i,newF);
                    i--;
                    break;
            }
        }

        //pEmdas (parentheses handled recursively)
        for (int i=0; i<ops.size(); i++) {
            if (ops.get(i)=='^') {
                Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        Math.pow(a.apply(y), b.apply(y));

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            }
        }

        //peMDas (multiplication and modulo)
        for (int i=0; i<ops.size(); i++) {
            if (ops.get(i)=='*') {
                Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)*b.apply(y);

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            } else if (ops.get(i)=='/') {
                Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)/b.apply(y);

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            } else if (ops.get(i)=='%') {
                Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)%b.apply(y);

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
                Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
                Function<Double,Double> newF = y ->
                        a.apply(y)+b.apply(y);

                fxns.remove(a);
                fxns.remove(b);
                ops.remove(i);
                fxns.add(i,newF);
                i--;
            } else if (ops.get(i)=='-') {
                Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
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
