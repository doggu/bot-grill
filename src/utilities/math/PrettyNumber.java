package utilities.math;

import java.math.BigDecimal;
import java.math.MathContext;

public class PrettyNumber {
    private static final char TIMES = '×';
    private static final char SUPERSCRIPT_MINUS = '⁻';
    private static final char[] SUPERSCRIPT_DIGITS = {
            '⁰','¹','²','³','⁴','⁵','⁶','⁷','⁸','⁹',
    };

    private static final int SN_CUTOFF = 6;



    private final BigDecimal n;
    private final boolean ginormo;



    public PrettyNumber(BigDecimal n) {
        this.n = n;
        this.ginormo = true;
    }
    public PrettyNumber(Number n) {
        this(new BigDecimal((float) n));
    }

    public PrettyNumber(BigDecimal n, boolean ginormo) {
        this.n = n;
        this.ginormo = ginormo;
    }
    public PrettyNumber(Number n, boolean ginormo) {
        this(new BigDecimal((float) n), ginormo);
    }



    public String toString() {
        StringBuilder number = new StringBuilder(n.round(new MathContext(16)).toPlainString());
        int decimal = number.indexOf(String.valueOf('.'));
        if (decimal < 0) decimal = number.length();

        if (ginormo) {
            for (int i = decimal - 3; i > 0; i -= 3) {
                number.insert(i, ',');
            }
        } else {
            StringBuilder newN;

            int roughlyN = n.compareTo(new BigDecimal(1))+1;
            if (roughlyN>1) {
                if (n.compareTo(new BigDecimal(Math.pow(10, SN_CUTOFF)))>0) {
                    newN = new StringBuilder(String.valueOf(number.charAt(0))).append('.');

                    for (int i = 1; i < 4 && i < number.length(); i++) {
                        if (number.charAt(i) == '.') continue;
                        newN.append(number.charAt(i));
                    }

                    newN.append("×10");
                    String superscript = String.valueOf(decimal - 1);
                    for (int i = 0; i < superscript.length(); i++)
                        newN.append(SUPERSCRIPT_DIGITS[superscript.charAt(i) - '0']);
                } else
                    newN = null;
            } else if (roughlyN<1) {
                //get leading zeroes
                int lZ;
                for (lZ=0; (number.charAt(lZ)=='0'||number.charAt(lZ)=='.')&&lZ<number.length(); lZ++);
                lZ--; //period

                if (lZ>SN_CUTOFF) {
                    newN = new StringBuilder(number.substring(lZ)+2);

                    newN.insert(1,'.');
                    newN.delete(5, newN.length());

                    newN.append(TIMES).append("10").append(SUPERSCRIPT_MINUS);

                    String superscript = String.valueOf(lZ);
                    for (int i=0; i<superscript.length(); i++)
                        newN.append(SUPERSCRIPT_DIGITS[superscript.charAt(i)-'0']);
                } else
                    newN = null;
            } else {
                return "seriously the number is 0 omg get a life";
            }

            if (newN==null) newN = new StringBuilder(n.round(new MathContext(3)).toString());

            number = newN;
        }

        return number.toString();
    }


    public static void main(String[] args) {
        System.out.println(new PrettyNumber(new BigDecimal(8957298457245.22458), false));
        float f = Float.MAX_VALUE/4340564067380546565L/434046565L;
        System.out.println(new PrettyNumber(new BigDecimal(f)));
    }
}
