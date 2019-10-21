package stem.science.chem.particles;

public class Atom extends ChemicalElement {
    private int p, n, e;



    public Atom(int p, int n, int e) {
        super(p);
        this.p = p;
        this.n = n;
        this.e = e;
    }



    public void changeN(int n) { this.n+= n; }
    public void changeE(int e) { this.e+= e; }



    public int getP() { return p; }
    public int getN() { return n; }
    public int getE() { return e; }
    public int getCharge() { return p-e; }
    public double getAtomicMass() { return p+n; };



    public String toString() {
        return super.getName()+"-"+(p+n);
    }
}
