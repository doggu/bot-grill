package stem.science.chem;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MolecularIdentity {
    protected final ArrayList<Atom> atoms;



    protected MolecularIdentity(Atom... a) {
        atoms = new ArrayList<>(Arrays.asList(a));
    }



    public double getMolarMass() {
        double mm = 0;
        for (Atom x:atoms)
            mm+= x.getMolarMass();
        return mm;
    }
}
