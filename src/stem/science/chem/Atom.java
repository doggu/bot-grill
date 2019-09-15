package stem.science.chem;

public class Atom extends MolecularIdentity {
    private ChemicalElement me;
    private int p, n, e;



    public Atom(int p, int n, int e) {
        this.me = ElementDatabase.ELEMENTS.get(p-1);
        this.p = p;
        this.n = n;
        this.e = e;
    }
    public Atom(ChemicalElement me, int n, int e) {
        this.me = me;
        this.p = me.getAtomicNumber();
        this.n = n;
        this.e = e;
    }



    public void setP(int p) {
        this.me = ElementDatabase.ELEMENTS.get(p-1);
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
    public double getMolarMass() {
        return me.getAtomicWeight();
    }



    public String toString() {
        return null;
    }


}
