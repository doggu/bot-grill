package stem.math.matrix;

public class Fraction {
    private int n, d;

    public Fraction(int n, int d) {
        if (d==0)
            throwDivByZeroError();

        this.n = n;
        this.d = d;
    }

    public Fraction(int numerator) {
        this.n = numerator;
        this.d = 1;
    }


    public Fraction add(int n) {
        n *= d;
        this.n += n;

        return this;
    }
    public Fraction add(Fraction f) {
        f.n *= this.d;
        this.n *= f.d;

        this.n += f.n;
        this.d *= f.d;

        return this;
    }

    @SuppressWarnings("unused")
    public Fraction subtract(int n) {
        add(-1*n);

        return this;
    }

    @SuppressWarnings("unused")
    public Fraction subtract(Fraction f) {
        f.multiplyBy(-1);
        add(f);

        return this;
    }

    public Fraction multiplyBy(int m) {
        n *= m;

        return this;
    }

    @SuppressWarnings("")
    public Fraction multiplyBy(Fraction m) {
        n *= m.n;
        d *= m.d;

        if (this.d<0) {
            this.n*=-1;
            this.d*=-1;
        }

        return this;
    }

    public Fraction divideBy(int d) {
        if (d==0) throwDivByZeroError();
        this.d *= d;

        if (this.d<0) {
            this.n*=-1;
            this.d*=-1;
        }

        return this;
    }
    public Fraction divideBy(Fraction d) {
        if (d.matches(0)) throwDivByZeroError();
        this.n *= d.d;
        this.d *= d.n;

        return this;
    }

    public Fraction reduce() {
        if (n==0) {
            d = 1;
            return this;
        }
        for (int i = 2; i<=Math.abs(n) && i<=d; i++) {
            while ((n%i==0 && d%i==0)) {
                n /= i;
                d /= i;
            }
        }

        return this;
    }


    public int getNumerator() { return n; }

    public int getDenominator() { return d; }

    //to remind me to put casting in wherever
    // it's used to not confuse myself later
    @Deprecated
    public int intValue() { return n/d; }
    public double doubleValue() { return (double) n/d; }

    public boolean matches(int n) {
        return this.n%d==0 && this.n/d==n;
    }

    public boolean matches(Fraction f) {
        Fraction f1 = f.duplicate().reduce();
        Fraction t = this.duplicate().reduce();

        return f1.n==t.n && f1.d==t.d;
    }

    Fraction duplicate() {
        return new Fraction(n, d);
    }



    private void throwDivByZeroError() {
        throw new ArithmeticException("/ by zero");
    }



    @Override
    public String toString() {
        return n+(d==1 ? "":"/"+d);
    }
}
