package stem.science.chem.particles;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MolecularIdentity {
    protected final ArrayList<Atom> atoms;



    protected MolecularIdentity(Atom... a) {
        atoms = new ArrayList<>(Arrays.asList(a));
    }



    public double getAtomicWeight() {
        double mm = 0;
        for (Atom x:atoms)
            mm+= x.getAtomicWeight();
        return mm;
    }
}
