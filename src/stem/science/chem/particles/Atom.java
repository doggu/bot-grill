package stem.science.chem.particles;

public class Atom extends ChemicalElement implements MolecularIdentity {
    private int p, n, e;



    public Atom(int p, int n, int e) {
        super(p);
        this.p = p;
        this.n = n;
        this.e = e;
    }



    public void captureNeutron(int n) { this.n+= n; }
    public void ionize(int e) { this.e+= e; }



    public int getProtons() { return p; }
    public int getNeutrons() { return n; }
    public int getElectrons() { return e; }
    public int getCharge() { return p-e; }
    public int getAtomicMass() { return p+n; };



    public String toString() {
        return super.getName()+"-"+(getAtomicMass());
    }
}
