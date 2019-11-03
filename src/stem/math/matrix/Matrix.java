package stem.math.matrix;

import utilities.Misc;

public class Matrix {
    private Fraction[][] matrix;


    @SuppressWarnings("static-access")
    public Matrix(Fraction[][] matrix) {
        this.matrix = matrix;
    }

    public Matrix(int[][] matrix) {
        this(constructMatrix(matrix));
    }

    private static Fraction[][] constructMatrix(int[][] values) {
        Fraction[][] matrix = new Fraction[values.length][values[0].length];
        for (int i = 0; i<values.length; i++) {
            for (int j = 0; j<values[i].length; j++)
                matrix[i][j] = new Fraction(values[i][j]);
        }

        return matrix;
    }

    public Fraction[] getRow(int r) {
        return matrix[r];
    }

    public Fraction[] getColumn(int c) {
        Fraction[] column = new Fraction[matrix.length];
        for (int i = 0; i<matrix.length; i++) {
            column[i] = matrix[i][c];
        }

        return column;
    }

    public Matrix add(int r1, int r2, int m) {
        for (int i = 0; i<matrix[r1].length; i++) {
            matrix[r1][i].add(matrix[r2][i].duplicate().multiplyBy(m));
        }

        return this;
    }

    public Matrix add(int r1, int r2, Fraction m) {
        for (int i = 0; i<matrix[r1].length; i++) {
            matrix[r1][i].add(matrix[r2][i].duplicate().multiplyBy(m));
        }

        return this;
    }

    public Matrix subtract(int r1, int r2, Fraction m) {
        return add(r1, r2, m.multiplyBy(-1));
    }

    public Matrix subtract(int r1, int r2, int m) {
        return add(r1, r2, -1*m);
    }

    public Matrix multiply(int r, int m) {
        for (int i = 0; i<matrix[r].length; i++) {
            matrix[r][i].multiplyBy(m);
        }

        return this;
    }

    public Matrix divide(int r, int d) {
        for (int i = 0; i<matrix[r].length; i++) {
            matrix[r][i].divideBy(d);
        }

        return this;
    }

    public Matrix swap(int r1, int r2) {
        Fraction[] temp = matrix[r1];
        matrix[r1] = matrix[r2];
        matrix[r2] = temp;

        return this;
    }

    public Matrix reducedRowEchelonForm() {
        //interpreted psuedocode from:
        //https://rosettacode.org/wiki/Reduced_row_echelon_form
        //it looks obvious now that i've translated it
        //but maybe that's why it's good code and i'm not
        int lead = 0;
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int r = 0; r<rows; r++) {
            if (cols<=lead)
                break;

            int i = r;
            while (matrix[i][lead].matches(0)) {
                i++;
                if (rows==i) {
                    i = r;
                    lead++;
                    if (lead==cols) return this;
                }
            }

            this.swap(i, r);

            if (!matrix[r][lead].matches(0)) {
                Fraction l = matrix[r][lead].duplicate();
                this.multiply(r, l.getDenominator())
                        .divide(r, l.getNumerator());
            }

            for (i = 0; i<rows; i++) {
                if (i==r) continue;
                this.subtract(i, r, matrix[i][lead].duplicate());
            }

            lead++;
        }

        return this;
    }

    public Matrix reduce() {
        for (Fraction[] fractions : matrix)
            for (Fraction fraction : fractions)
                fraction.reduce();

        return this;
    }


    public String toString() {
        StringBuilder m = new StringBuilder();

        for (Fraction[] row : matrix) {
            m.append("| ");
            for (Fraction fraction : row) {
                m.append(fraction).append(' ');
            }
            m.append("|\n");
        }

        return m.substring(0, m.length()-1);
    }

    @SuppressWarnings("unused")
    public Matrix whole() {
        for (Fraction[] row : matrix) {
            int[] denominators = new int[row.length];
            for (int i = 0; i<row.length; i++) {
                denominators[i] = row[i].getDenominator();
            }

            int lcm = Misc.lcm(denominators);
            for (int i = 0; i<row.length; i++) {
                row[i].multiplyBy(lcm);
            }
        }

        return this.reduce();
    }


    public static void main(String[] args) {
        Matrix m = new Matrix(new int[][]{
                //C3H8O + O2 -> H2O + CO2
                //{3,0,-0,-1},
                //{8,0,-1,-0},
                //{1,2,-1,-2},
                //CH4 + O2 -> CO2 + H2O
                //{1,0,-1,0},
                //{4,0,0,-2},
                //{0,2,-2,-1},
                //KI + KClO3 + HCl > I2 + H2O + KCl
                //{1,1,0,0,0,1},
                //{1,0,0,2,0,0},
                //{0,3,0,0,1,0},
                //{0,0,1,0,2,0},
                //{0,1,1,0,0,1},
                //NaOH + H2SO4 > Na2SO4 + H2O
                //{1, 0, 2, 0},
                //{1, 4, 4, 1},
                //{1, 2, 0, 2},
                //C2H4 + O2 > CO2 + H2O
                {2, 0, -1, 0},
                {4, 0, 0, -2},
                {0, 2, -2, -1}
        });

        m.reducedRowEchelonForm()
                .reduce()
                .whole();

        System.out.println(m);
    }
}