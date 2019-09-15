package stem.science.chem;

public class Ion extends MolecularIdentity {


    @Override
    public double getMolarMass() {
        return 0;
    }

    private static final Ion
            AMMONIUM = new Ion();
}
