package utilities.math;

import java.util.*;
import java.util.function.Function;

/**
 * VALUES OF CERTAIN LETTERS
 * π
 * 299792458
 *
 */

public class MathParse {
    private static final char
            PLUS = '+',
            MINUS = '-',
            TIMES = '*',
            DIVIDE = '/',
            POWER = '^',
            MODULO = '%',

            VARIABLE = 'x',

            PI = 'π',
            E = 'e',

            SIN = 's',
            COS = 'c',
            TAN = 't',
            ASIN = 'S',
            ACOS = 'C',
            ATAN = 'T',

            SINH = 'ś',
            COSH = 'ć',
            TANH = 'ţ',
            LOG = 'l',
            LN = 'n';



    private String f;
    private ArrayList<Function<Double,Double>> fxns = new ArrayList<>();
    private ArrayList<Character> ops = new ArrayList<>();



    public MathParse(String problem) {
        this.f = problem;
    }



    public Function<Double,Double> getFunction() throws Error {
                //constants
        f = f   .replaceAll("pi",PI+"")
                //hyperbolics
                .replaceAll("sinh", SINH+"")
                .replaceAll("cosh", COSH+"")
                .replaceAll("tanh", TANH+"")
                //inverse trig functions (so the regular ones don't override them)
                //(even though i could just have it replace "as" later
                .replaceAll("asin",ASIN+"")
                .replaceAll("acos",ACOS+"")
                .replaceAll("atan",ATAN+"")
                //trigonometric functions
                .replaceAll("sin",SIN+"")
                .replaceAll("cos",COS+"")
                .replaceAll("tan",TAN+"")
                //logarithmic functions
                .replaceAll("log",LOG+"")
                .replaceAll("ln",LN+"");

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
                case '(':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add(TIMES);
                    } else if (fxns.size()!=ops.size())
                        ops.add(TIMES);
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
                    fxns.add(new MathParse(f.substring(start, i)).getFunction());
                    break;
                case VARIABLE:
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add(TIMES);
                    } else if (fxns.size()!=ops.size())
                        ops.add(TIMES);

                    fxns.add(x -> x);
                    break;
                case 'e':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add(TIMES);
                    } else if (fxns.size()!=ops.size())
                        ops.add(TIMES);

                    fxns.add(x -> Math.E);
                    break;
                case 'π':
                    if (val!=null) {
                        fxns.add(x -> val);
                        num = "";
                        ops.add(TIMES);
                    } else if (fxns.size()!=ops.size())
                        ops.add(TIMES);

                    fxns.add(x -> Math.PI);
                    break;
                case MINUS: //special for negative numbers
                    if (fxns.size()==0) {
                        num+= MINUS;
                        break;
                    } //else it's  an operator
                case PLUS:
                case TIMES:
                case DIVIDE:
                case POWER:
                case MODULO:
                case SIN:
                case COS:
                case TAN:
                case ASIN:
                case ACOS:
                case ATAN:
                case SINH:
                case COSH:
                case LOG:
                case LN:
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



        //special functions (sin, cos, ln, etc.)
        for (int i=0; i<ops.size(); i++) {
            switch(ops.get(i)) {
                case SIN:
                case COS:
                case TAN:
                case ASIN:
                case ACOS:
                case ATAN:
                case SINH:
                case LOG:
                case LN:
                    performAction(i);
                    i--;
                    break;
            }
        }

        //pEmdas (parentheses handled recursively) (this could be a while loop)
        for (int i=0; i<ops.size(); i++) {
            if (ops.get(i)==POWER) {
                performAction(i);
                i--;
            }
        }

        //peMDas (multiplication and modulo)
        for (int i=0; i<ops.size(); i++) {
            switch (ops.get(i)) {
                case TIMES:
                case DIVIDE:
                case MODULO:
                    performAction(i);
                    i--;
                    break;
            }
        }

        //pemdAS
        for (int i=0; i<ops.size(); i++) {
            switch(ops.get(i)) {
                case PLUS:
                case MINUS:
                    performAction(i);
                    break;
            }
        }



        return fxns.get(0);
    }

    private void performAction(int i) {
        final Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
        char op = ops.get(i);
        Function<Double,Double> newF;
        switch (op) {
            case PLUS: newF = y -> a.apply(y)+b.apply(y); break;
            case MINUS: newF = y -> a.apply(y)-b.apply(y); break;
            case TIMES: newF = y -> a.apply(y)*b.apply(y); break;
            case DIVIDE: newF = y -> a.apply(y)/b.apply(y); break;
            case MODULO: newF = y -> a.apply(y)%b.apply(y); break;
            case POWER: newF = y -> Math.pow(a.apply(y),b.apply(y)); break;
            case SIN: newF = y -> a.apply(y)*Math.sin(b.apply(y)); break;
            case COS: newF = y -> a.apply(y)*Math.cos(b.apply(y)); break;
            case TAN: newF = y -> a.apply(y)*Math.tan(b.apply(y)); break;
            case ASIN: newF = y -> a.apply(y)*Math.asin(b.apply(y)); break;
            case ACOS: newF = y -> a.apply(y)*Math.acos(b.apply(y)); break;
            case ATAN: newF = y -> a.apply(y)*Math.atan(b.apply(y)); break;
            case SINH: newF = y -> a.apply(y)*Math.sinh(b.apply(y)); break;
            case COSH: newF = y -> a.apply(y)*Math.cosh(b.apply(y)); break;
            case TANH: newF = y -> a.apply(y)*Math.tanh(b.apply(y)); break;
            case LOG: newF = y -> a.apply(y)*Math.log10(b.apply(y)); break;
            case LN: newF = y -> a.apply(y)*Math.log(b.apply(y)); break;
            default:
                System.out.println("invalid operator detected");
                throw new Error();
        }
        fxns.remove(a);
        fxns.remove(b);
        fxns.add(i, newF);
        ops.remove(i);
    }



    public static void main(String[] args) {
        System.out.println(SIN+"");
        double[] TV = {0,1,2,3,4,5,6,7};
        double[] OV = new double[8];

        System.out.print("enter a function: ");
        Scanner input = new Scanner(System.in);
        String function = input.nextLine();
        while (!function.equals("quit")) {
            try {
                Function<Double,Double> f = new MathParse(function).getFunction();
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
