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
            PHI = 'p',
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

            SQRT = 'q',
            LOG = 'l',
            LN = 'n';

    private static final double PHI_N = (1+Math.pow(5,0.5))/2;



    private static final char[][] OoO /*order of operations*/ = {
            //special functions come first, since they are the equivalent of 1*[fn](arg)
            {SIN, COS, TAN, ASIN, ACOS, ATAN, SINH, LOG, LN, SQRT},
            //pEmdas
            {POWER},
            //peMDas (multiplication and modulo)
            {TIMES, DIVIDE, MODULO},
            //pemdAS
            {PLUS, MINUS},
    };

    private String f;
    private int i = 0;
    private ArrayList<Function<Double,Double>> fxns = new ArrayList<>();
    private ArrayList<Character> ops = new ArrayList<>();



    public MathParse(String problem) {
        this.f = problem
                //constants
                .replaceAll("pi",PI+"")
                .replaceAll("phi",PHI+"")
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
    }



    public Function<Double,Double> getFunction() throws Error {
        for (i=0; i<f.length(); i++) {
            char c = f.charAt(i);
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
                    addVal(getNum());
                    break;
                case '(':
                    if (fxns.size()>ops.size())
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
                    if (fxns.size()>ops.size())
                        ops.add(TIMES);
                    fxns.add(x -> x);
                    break;
                case PI:
                    if (fxns.size()>ops.size())
                        ops.add(TIMES);
                    addVal(Math.PI);
                    break;
                case E:
                    if (fxns.size()>ops.size())
                        ops.add(TIMES);
                    addVal(Math.E);
                    break;
                case PHI:
                    if (fxns.size()>ops.size())
                        ops.add(TIMES);
                    addVal(PHI_N);
                    break;
                case MINUS: //special for negative numbers
                    if (fxns.size()==0) {
                        addVal(getNum());
                        break;
                    } //else it's an operator
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
                case SQRT:
                    if (ops.size()==fxns.size())
                        fxns.add(x -> 1.0);
                    ops.add(c);
            }
        }

        for (char[] operations:OoO) {
            for (i=0; i<ops.size(); i++) {
                for (char operation:operations) {
                    if (ops.get(i)==operation) {
                        performAction();
                        i--;
                        break;
                    }
                }
            }
        }

        return fxns.get(0);
    }

    private double getNum() {
        String num = "";
        char val = f.charAt(i);
        boolean negN = false;
        if (val == '-') {
            negN = true;
            num+= val;
            i++;
        }
        for (;i<f.length(); i++) {
            val = f.charAt(i);
            if ((val - '0' >= 0 && val - '0' <= 9) || val == '.') {
                num += val;
            } else {
                i--;
                break;
            }
        }
        double n;
        try {
            n = Double.parseDouble(num);
        } catch (NumberFormatException f) {
            if (negN) n = -1;
            else throw new Error();
        }
        return n;
    }

    private void addVal(double n) {
        if (fxns.size()>ops.size())
            ops.add(TIMES);
        fxns.add(x -> n);
    }

    private void performAction() {
        Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
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
            case SQRT: newF = y -> a.apply(y)*Math.pow(b.apply(y),0.5); break;
            default:
                System.out.println("invalid operator detected");
                throw new Error();
        }
        fxns.remove(i);
        fxns.remove(i);
        fxns.add(i, newF);
        ops.remove(i);
    }

    public static void main(String[] args) {
        double[] TV = {0,1,2,3,4,5,6,7,Math.PI,Math.E};
        double[] OV = new double[TV.length];

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
