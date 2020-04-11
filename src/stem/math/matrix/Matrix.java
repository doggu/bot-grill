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

            reduce();

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


    private static Fraction determinant(Fraction[][] matrix)
            throws NotSquareException {
        //TODONE: my object oriented brain isn't working today
        // need to dereference all these fractions i'm multiplying
        // so that they don't get affected in the original matrix

        //TODO: that's done but it was sort of guess-and-check;
        // clean up and rationalize duplications
        System.out.println(new Matrix(matrix));
        System.out.println();
        if (matrix.length!=matrix[0].length)
            throw new NotSquareException();
        int s = matrix.length;

        if (s==1) return matrix[0][0];
        else if (s==2) {//ad-bc, could go to s = 1 but that's just expensive
            return matrix[0][0].multiplyBy(matrix[1][1])
                    .subtract(matrix[0][1].multiplyBy(matrix[1][0]));
        }

        Fraction determinant = new Fraction(0);

        for (int i=0; i<s; i++) {
            Fraction[][] mini = new Fraction[s-1][s-1];
            int mRow = 0, mCol = 0;
                        //do not do first row
            for (int row=1; row<s; row++) {
                for (int col=0; col<s; col++) {
                    if (col==i) continue;
                    Fraction get = matrix[row][col].duplicate();
                    mini[mRow][mCol] = get;
                    mCol++;
                }
                mRow++;
                mCol = 0;
            }

            mini = new Matrix(mini).reduce().matrix;

            Fraction mDet = matrix[0][i].duplicate()
                    .multiplyBy(determinant(mini));

            determinant.add(mDet.multiplyBy((i%2==0?1:-1)));

            determinant.reduce();
        }

        return determinant;
    }

    public Fraction determinant() throws NotSquareException {
        return determinant(this.matrix);
    }

    private boolean inversible() throws NotSquareException {
        return !determinant().matches(0);}


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

        //m.reducedRowEchelonForm()
        //        .reduce()
        //        .whole();

        Matrix detTest = new Matrix(new int[][]{
                {1, 3, 5, 9},
                {1, 3, 1, 7},
                {4, 3, 9, 7},
                {5, 2, 0, 9}
                //{1, 2, 3},
                //{4, 1, 0},
                //{5, 6, 1}
        });

        try {
            System.out.println(detTest.determinant());
        } catch (NotSquareException nse) {
            System.out.println("houston");
        }
    }
}