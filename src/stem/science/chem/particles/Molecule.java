package stem.science.chem.particles;

import java.util.ArrayList;
import java.util.Arrays;

public class Molecule extends ArrayList<Molecule> implements MolecularIdentity, Polyatomic {
    private final ArrayList<Atom> atoms;
    private final ArrayList<Molecule> ions;



    //todo: rework relationship between ions and molecules
    // are lone atoms always ions?
    public Molecule(Atom... a) {
        this.atoms = (ArrayList<Atom>) Arrays.asList(a);
        this.ions = null;
    }



    //todo: easily breakable
    public void give(Atom... a) {
        atoms.addAll(Arrays.asList(a));
    }



    public double getMolarMass() {
        double mass = 0;
        for (Atom x:atoms) {
            mass+=x.getAtomicWeight();
        }

        return mass;
    }



    public String toString() {
        //hill model
        return null;
    }

    @Override
    public int getProtons() {
        int p = 0;
        for (Atom x:atoms) p+= x.getProtons();
        for (Molecule x:this) p+= x.getProtons();
        return p;
    }

    @Override
    public int getNeutrons() {
        int n = 0;
        for (Atom x:atoms) n+= x.getNeutrons();
        for (Molecule x:this) n+= x.getNeutrons();
        return n;
    }

    @Override
    public int getElectrons() {
        int e = 0;
        for (Atom x:atoms) e+= x.getElectrons();
        for (Molecule x:this) e+= x.getElectrons();
        return e;
    }

    @Override
    public int getCharge() {
        return 0;
    }

    @Override
    public int getAtomicMass() {
        return 0;
    }

    @Override
    public boolean isIon() {
        //make the charges add up to zero
        return false;
    }

    public String toAleksString() {
        return null;
    }



    public static void main(String[] args) {
        Atom[] a = {
                new Atom(1,1,1),
                new Atom(1,1,1),
                new Atom(1,1,1),
        };

        Molecule f = new Molecule(a);

        System.out.println(f.getMolarMass());
    }
}
