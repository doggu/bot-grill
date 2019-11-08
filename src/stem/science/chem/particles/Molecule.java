package stem.science.chem.particles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToIntFunction;

public class Molecule extends ArrayList<Molecule> implements MolecularIdentity, Polyatomic {
    private final ArrayList<Atom> atoms;
    private final ArrayList<Molecule> ions;



    //todo: rework relationship between ions and molecules
    // are lone atoms always ions?
    public Molecule(Atom... a) {
        this.atoms = new ArrayList<>(Arrays.asList(a));
        this.ions = new ArrayList<>();
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

    private int summate(ToIntFunction<MolecularIdentity> t) {
        int sum = 0;
        for (Atom atom : atoms)
            sum += t.applyAsInt(atom);

        for (Molecule ion : ions)
            sum += t.applyAsInt(ion);


        return sum;
    }

    public int getProtons() { return summate(MolecularIdentity::getProtons); }
    public int getNeutrons() { return summate(MolecularIdentity::getNeutrons); }
    public int getElectrons() { return summate(MolecularIdentity::getElectrons); }
    public int getCharge() { return summate(MolecularIdentity::getCharge); }
    public int getAtomicMass() { return summate(MolecularIdentity::getAtomicMass); }
    public boolean isIon() { return summate(MolecularIdentity::getCharge)!=0; }

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
        if (!f.isIon()) System.out.println("wow what a fuckin surprise");
    }
}
