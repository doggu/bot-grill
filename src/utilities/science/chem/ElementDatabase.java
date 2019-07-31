package utilities.science.chem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElementDatabase {
    public static final ArrayList<Element> ELEMENTS = getList();

    private static final String PERIODIC_TABLE_FILEPATH = "./src/utilities/science/chem/periodicTable_basic.txt";




    private static ArrayList<Element> getList() {
        ArrayList<Element> elements = new ArrayList<>();

        Scanner file;
        try {
            file = new Scanner(new File(PERIODIC_TABLE_FILEPATH));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        while (file.hasNextLine()) {
            String[] atom = file.nextLine().split("\t");

            try {
                elements.add(new Element(atom[2], atom[1], Integer.parseInt(atom[0])));
            } catch (IndexOutOfBoundsException ioobe) {
                throw new Error("some data was missing from an atom");
            } catch (NumberFormatException nfe) {
                throw new Error("incorrect atomic number");
            }
        }

        return elements;
    }
}
