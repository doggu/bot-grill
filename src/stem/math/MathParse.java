package stem.math;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.function.Function;

public class MathParse {
    private static final char
            PLUS = '+', MINUS = '-', TIMES = '*', DIVIDE = '/', POWER = '^', MODULO = '%',
            SIN = '∿', COS = 'Ϲ', TAN = 't',  //that is NOT a C
            ASIN = 'Ѕ', ACOS = 'Ͻ', ATAN = 'T',
            SINH = 'Ѓ', COSH = 'Ͼ', TANH = 'ţ',
            LOG = '㏒', LN = '㏑', SQRT = '√', FLOOR = 'f', CEIL = 'r',
            VAR = 'x',
            PI = 'π', PHI = 'p', E = 'e', SoL = 'c', AVO = 'Ä', P_CONST = 'ל';

    private static final double
            PHI_N = (1+Math.sqrt(5))/2,
            SoL_N = 299792458.0, //m/s
            AVO_N = 6.02214086E23,
            PLA_N = 6.62607004E-34;

    private static final char[][] OoO /*order of operations*/ = {
            //special functions come first, since they are the equivalent of 1*[fn](arg)
            { SIN, COS, TAN, ASIN, ACOS, ATAN, SINH, COSH, TANH, LOG, LN, SQRT, FLOOR, CEIL },
            //pEmdas
            { POWER },
            //peMDas (multiplication and modulo)
            { TIMES, DIVIDE, MODULO },
            //pemdAS
            { PLUS, MINUS },
    };



    private char[] f;
    private int i = 0;
    private ArrayList<Function<Double,Double>> fxns = new ArrayList<>();
    private ArrayList<Character> ops = new ArrayList<>();



    public MathParse(String problem) {
        this.f = problem
                //hyperbolics
                .replaceAll("sinh", String.valueOf(SINH))
                .replaceAll("cosh", String.valueOf(COSH))
                .replaceAll("tanh", String.valueOf(TANH))
                //inverse trig functions (so the regular ones don't override them)
                //(even though i could just have it replace "∿h" or something later)
                .replaceAll("a(rc)?sin", String.valueOf(ASIN))
                .replaceAll("a(rc)?cos", String.valueOf(ACOS))
                .replaceAll("a(rc)?tan", String.valueOf(ATAN))
                //trigonometric functions
                .replaceAll("sin", String.valueOf(SIN))
                .replaceAll("cos", String.valueOf(COS))
                .replaceAll("tan", String.valueOf(TAN))
                //logarithmic functions
                .replaceAll("log", String.valueOf(LOG))
                .replaceAll("ln", String.valueOf(LN))
                .replaceAll("sqr?t", String.valueOf(SQRT))
                .replaceAll("floor", String.valueOf(FLOOR))
                .replaceAll("ceil", String.valueOf(CEIL))
                //constants
                .replaceAll("pi", String.valueOf(PI))
                .replaceAll("phi", String.valueOf(PHI))
                .replaceAll("N\\(A\\)", String.valueOf(AVO))
                .replaceAll("h", String.valueOf(P_CONST))
                .toCharArray();
    }
    //for recursive calls which have already replaced functions with special characters
    private MathParse(char[] problem) { this.f = problem; }



    public Function<Double,Double> getFunction() throws Error {
        //todo: make input follow rules more rigorously
        for (i=0; i<f.length; i++) {
            char c = f[i];
            switch(c) {
                case '0': case '1': case '2': case '3':
                case '4': case '5': case '6': case '7':
                case '8': case '9': case '.':
                    addVal(getNum());
                    break;
                case '(':
                    insertImplTimes();
                    int pBal = 1;
                    int start = i+1;
                    for (i = start; i<f.length; i++) {
                        c = f[i];
                        if (c=='(') pBal++;
                        if (c==')') pBal--;
                        if (pBal==0) break;
                    }
                    if (pBal!=0) {
                        System.out.println("imbalanced parentheses!");
                        throw new Error();
                    }
                    fxns.add(new MathParse(Arrays.copyOfRange(f, start, i)).getFunction());
                    break;
                case VAR:
                    insertImplTimes();
                    fxns.add(x -> x);
                    break;
                case PI: case E: case PHI: case SoL: case AVO: case P_CONST:
                    addVal(c);
                    break;
                case MINUS: //special for negative numbers
                    if (fxns.size()==0||ops.size()==fxns.size()) {
                        addVal(getNum());
                        break;
                    } //else it's an operator
                case PLUS: case TIMES: case DIVIDE: case POWER: case MODULO:
                case SIN: case COS: case TAN:
                case ASIN: case ACOS: case ATAN:
                case SINH: case COSH: case TANH:
                case LOG: case LN:
                case SQRT:
                case FLOOR: case CEIL:
                    if (ops.size()==fxns.size())
                        fxns.add(x -> 1.0);
                    ops.add(c);
                    break;
                default:
                    throw new Error("unknown char: "+c+" ("+i+")");
            }
        }

        for (char[] operations:OoO) {
            for (i=0; i<ops.size(); i++) {
                for (char operation:operations) {
                    if (ops.get(i)==operation) {
                        performAction();
                        break;
                    }
                }
            }
        }

        return fxns.get(0);
    }

    private void insertImplTimes() {
        if (fxns.size()>ops.size())
            ops.add(TIMES);
    }

    private double getNum() {
        String num = "";
        char val = f[i];
        boolean negN = false;
        if (val == '-') {
            negN = true;
            num+= val;
            i++;
        }
        for (;i<f.length; i++) {
            val = f[i];
            if ((val-'0'>=0 && val-'0'<=9) || val=='.') {
                num += val;
            } else {
                i--;
                break;
            }
        }

        try {
            return Double.parseDouble(num);
        } catch (NumberFormatException f) {
            if (negN) return -1;
            else throw new Error();
        }
    }

    private void addVal(double n) {
        if (fxns.size()>ops.size())
            ops.add(TIMES);
        fxns.add(x -> n);
    }
    private void addVal(char c) {
        double n;
        switch(c) {
            case PHI: n = PHI_N; break;
            case E: n = Math.E; break;
            case PI: n = Math.PI; break;
            case SoL: n = SoL_N; break;
            case AVO: n = AVO_N; break;
            case P_CONST: n = PLA_N; break;
            default:
                //PANIC
                throw new Error();
        }
        addVal(n);
    }

    private void performAction() {
        Function<Double,Double> a = fxns.get(i), b = fxns.get(i+1);
        char op = ops.get(i);
        Function<Double,Double> newF;
        switch (op) {
            case PLUS: newF = x -> a.apply(x)+b.apply(x); break;
            case MINUS: newF = x -> a.apply(x)-b.apply(x); break;
            case TIMES: newF = x -> a.apply(x)*b.apply(x); break;
            case DIVIDE: newF = x -> a.apply(x)/b.apply(x); break;
            case MODULO: newF = x -> a.apply(x)%b.apply(x); break;
            case POWER: newF = x -> Math.pow(a.apply(x),b.apply(x)); break;
            case SIN: newF = x -> a.apply(x)*Math.sin(b.apply(x)); break;
            case COS: newF = x -> a.apply(x)*Math.cos(b.apply(x)); break;
            case TAN: newF = x -> a.apply(x)*Math.tan(b.apply(x)); break;
            case ASIN: newF = x -> a.apply(x)*Math.asin(b.apply(x)); break;
            case ACOS: newF = x -> a.apply(x)*Math.acos(b.apply(x)); break;
            case ATAN: newF = x -> a.apply(x)*Math.atan(b.apply(x)); break;
            case SINH: newF = x -> a.apply(x)*Math.sinh(b.apply(x)); break;
            case COSH: newF = x -> a.apply(x)*Math.cosh(b.apply(x)); break;
            case TANH: newF = x -> a.apply(x)*Math.tanh(b.apply(x)); break;
            case LOG: newF = x -> a.apply(x)*Math.log10(b.apply(x)); break;
            case LN: newF = x -> a.apply(x)*Math.log(b.apply(x)); break;
            case SQRT: newF = x -> a.apply(x)*Math.pow(b.apply(x),0.5); break;
            case FLOOR: newF = x -> a.apply(x)*Math.floor(b.apply(x)); break;
            case CEIL: newF = x -> a.apply(x)*Math.ceil(b.apply(x)); break;
            default:
                System.out.println("invalid operator detected");
                throw new Error();
        }
        fxns.remove(i);
        fxns.remove(i);
        ops.remove(i);
        fxns.add(i, newF);
        i--;
    }



    public static void main(String[] arg) {
        double[] TV = {0,1,2,3,4,5,6,7,Math.PI,Math.E};
        double[] OV = new double[TV.length];

        System.out.print("enter a function: ");
        Scanner input = new Scanner(System.in);
        String function = input.nextLine();
        while (!function.equals("quit")) {

            try {
                Function<Double,Double> f = new MathParse(function).getFunction();
                for (int i=0; i<TV.length; i++)
                    OV[i] = f.apply(TV[i]);

                StringBuilder table = new StringBuilder("|  I  |  O ");
                for (int i=0; i<OV.length; i++) {
                    table.append("\n+-----+----------------")
                            .append("\n| ");
                    if (TV[i]==Math.PI) table.append(" π ");
                    else if (TV[i]==Math.E) table.append(" e ");
                    else table.append(TV[i]);
                    table.append(" | ").append(OV[i]);
                }

                System.out.println(table);
            } catch (Error format) {
                System.out.println("incorrect format!");
            }

            System.out.print("enter a function: ");
            function = input.nextLine();
        }
    }
}