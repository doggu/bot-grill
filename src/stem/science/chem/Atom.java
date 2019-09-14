package stem.science.chem;

public class Atom {
    private int p, n, e;



    public Atom(int p, int n, int e) {
        this.p = p;
        this.n = n;
        this.e = e;
    }



    public void setP(int p) {
        this.p = p;
    }
    public void setN(int n) {
        this.n = n;
    }
    public void setE(int e) {
        this.e = e;
    }



    public int getP() {
        return p;
    }
    public int getN() {
        return n;
    }
    public int getE() {
        return e;
    }
    public int getCharge() { return p-e; }

    public String toString() {
        return null;
    }


}
