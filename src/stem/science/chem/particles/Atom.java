package stem.science.chem.particles;

public class Atom extends ChemicalElement implements MolecularIdentity {
    private int p, n, e;



    public Atom(int p, int n, int e) {
        /*
        the current structure here would require any user to generate
        a new atom every time the amount of protons is changed.

        idk when that will affect me (in nuclear shit, for one),
        but it's something to keep in mind as i write things with this.
         */
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
    public int getAtomicMass() { return p+n; }


    @Override
    public boolean isIon() { return p!=e; }

    public String toString() {
        if (super.getName().equals("Hydrogen")) {
            //if (n==0) return "Protium";
            if (n==1) return "Deuterium";
            if (n==2) return "Tritium";
        }
        return super.getName()+"-"+(getAtomicMass());
    }
    public String toAleksString() {
        return "\\chem["+getSymbol()+"]";
    }
}
