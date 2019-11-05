package stem.science.chem.particles;

import stem.AleksPrintable;

public interface MolecularIdentity extends AleksPrintable {
    int getProtons();
    int getNeutrons();
    int getElectrons();
    int getCharge();
    int getAtomicMass();

    boolean isIon();
}
