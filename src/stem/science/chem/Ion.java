package stem.science.chem;

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
                    new Ion(new Atom(ElementDatabase.DATABASE.find("H"), -1),
                    new Atom(ElementDatabase.DATABASE.find("H"), -1),
                    new Atom(ElementDatabase.DATABASE.find("H"), -1),
                    new Atom(ElementDatabase.DATABASE.find("H"), -1),
                    new Atom(ElementDatabase.DATABASE.find("N"), 5));

    private static final Ion
            NITRATE =
                    new Ion(new Atom(ElementDatabase.DATABASE.find("N"), -7),
                    new Atom(ElementDatabase.DATABASE.find("O"), 2),
                    new Atom(ElementDatabase.DATABASE.find("O"), 2),
                    new Atom(ElementDatabase.DATABASE.find("O"), 2));



    public static void main(String[] args) {
        System.out.println(AMMONIUM.getCharge());
    }
}
