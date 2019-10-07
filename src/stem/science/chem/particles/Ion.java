package stem.science.chem.particles;

import stem.science.chem.ElementDatabase;

public class Ion extends MolecularIdentity {
    protected Ion(Atom... a) {
        super(a);
    }



    public int getCharge() {
        int charge = 0;
        for (Atom x:atoms)
            charge+= x.getCharge();
        return charge;
    }

    private static final Ion
            AMMONIUM =
                    new Ion(
                            new Atom(ElementDatabase.DATABASE.find("H").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("H").getAtomicNumber(), -1),
                            new Atom(ElementDatabase.DATABASE.find("H").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("H").getAtomicNumber(), -1),
                            new Atom(ElementDatabase.DATABASE.find("H").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("H").getAtomicNumber(), -1),
                            new Atom(ElementDatabase.DATABASE.find("H").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("H").getAtomicNumber(), -1),
                            new Atom(ElementDatabase.DATABASE.find("N").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("N").getAtomicNumber(), 5));

    private static final Ion
            NITRATE =
                    new Ion(
                            new Atom(ElementDatabase.DATABASE.find("N").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("N").getAtomicNumber(), -7),
                            new Atom(ElementDatabase.DATABASE.find("O").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("O").getAtomicNumber(), 2),
                            new Atom(ElementDatabase.DATABASE.find("O").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("O").getAtomicNumber(), 2),
                            new Atom(ElementDatabase.DATABASE.find("O").getAtomicNumber(),
                                    ElementDatabase.DATABASE.find("O").getAtomicNumber(), 2));



    public static void main(String[] args) {
        System.out.println(AMMONIUM.getCharge());
    }
}
