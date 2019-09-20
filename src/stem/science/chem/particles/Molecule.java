package stem.science.chem.particles;

import java.util.ArrayList;
import java.util.Arrays;

public class Molecule extends ArrayList<Molecule> implements Polyatomic {
    private final ArrayList<Atom> atoms;
    private final ArrayList<Molecule> ions;



    //todo: rework relationship between ions and molecules
    // are lone atoms always ions?
    public Molecule(Atom... a) {
        this.atoms = (ArrayList<Atom>) Arrays.asList(a);
        this.ions = null;
    }



    public void give(Atom... a) {
        atoms.addAll(Arrays.asList(a));
    }



    public double getMolarMass() {
        double mass = 0;
        for (Atom x:atoms) {
            mass+=x.getMolarMass();
        }

        return mass;
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
