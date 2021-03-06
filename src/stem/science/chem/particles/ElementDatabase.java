package stem.science.chem.particles;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.IOException;
import java.util.ArrayList;

public class ElementDatabase extends Database<ChemicalElement> {
    private static final boolean DEBUG = false;



    public static ElementDatabase DATABASE = new ElementDatabase();
    public static ArrayList<ChemicalElement> ELEMENTS = DATABASE.getList();



    private static final String PERIODIC_TABLE_URL =
            "https://en.wikipedia.org/wiki/List_of_chemical_elements";

    private static final WebCache PERIODIC_TABLE_FILE =
            new WebCache(
                    "https://en.wikipedia.org/wiki/List_of_chemical_elements",
                    "/chem");

    @Override
    protected WebCache[] getOnlineResources() {
        return new WebCache[] { PERIODIC_TABLE_FILE };
    }



    protected ArrayList<ChemicalElement> getList() {
        ArrayList<ChemicalElement> elements = new ArrayList<>();

        WebCache elementCache =
                new WebCache(PERIODIC_TABLE_URL, "/chem/");

        Document file;
        try {
            file = Jsoup.parse(elementCache, "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("no elements will be had");
            return null;
        }

        org.jsoup.nodes.Element table = file.select("table").get(0);
        Elements rows = table.select("tr");
        ArrayList<Elements> data = new ArrayList<>();
        for (org.jsoup.nodes.Element row:rows) {
            data.add(row.select("td"));
        }

        try {
            for (Elements element : data) {
                if (element.size()<13) continue;
                int     atomicNumber = Integer.parseInt(
                                sanitize(element.get(0).text()));
                String  symbol = element.get(1).text();
                String  name = element.get(2).text();
                String  origin = element.get(3).text();

                int   group;
                try {
                    group = Integer.parseInt(
                            sanitize(element.get(4).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("group "+name);
                    group = 0;
                }

                int     period = Integer.parseInt(
                        sanitize(element.get(5).text()));

                double  atomicWeight;
                try {
                    atomicWeight = Double.parseDouble(
                            sanitize(element.get(6).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("atomicWeight "+name);
                    atomicWeight = 0;
                }

                double  density;
                try {
                    density = Double.parseDouble(
                            sanitize(element.get(7).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("density "+name);
                    density = 0;
                }

                double  meltingPoint;
                try {
                    meltingPoint = Double.parseDouble(
                            sanitize(element.get(8).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("meltingPoint "+name);
                    meltingPoint = 0;
                }

                double  boilingPoint;
                try {
                    boilingPoint = Double.parseDouble(
                            sanitize(element.get(9).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("boilingPoint "+name);
                    boilingPoint = 0;
                }

                double  C;
                try {
                    C = Double.parseDouble(
                            sanitize(element.get(10).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("C "+name);
                    C = 0;
                }

                double  electronegativity;
                try {
                    electronegativity = Double.parseDouble(
                            sanitize(element.get(11).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("electronegativity "+name);
                    electronegativity = 0;
                }

                double  earthAbundance;
                try {
                    earthAbundance = Double.parseDouble(
                            sanitize(element.get(12).text()));
                } catch (NumberFormatException nfe) {
                    if (DEBUG)
                        System.out.println("earthAbundance "+name);
                    earthAbundance = 0;
                }


                ChemicalElement e = new ChemicalElement(
                        atomicNumber,
                        symbol,
                        name,
                        origin,
                        group,
                        period,
                        atomicWeight,
                        density,
                        meltingPoint,
                        boilingPoint,
                        C,
                        electronegativity,
                        earthAbundance);

                elements.add(e);
            }
        } catch (Exception f) {
            f.printStackTrace();
        }



        return elements;
    }

    private static String sanitize(String input) {
        //todo: make more rigorous/correct some day
        int i;
        for (i=0; i<input.length(); i++) {
            switch (input.charAt(i)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '-':
                case '.':
                    continue;
            }
            break;
        }

        return input.substring(0, i);
    }



    @Override
    public ArrayList<ChemicalElement> findAll(String input) {
        ArrayList<ChemicalElement> elements = new ArrayList<>();
        for (ChemicalElement element:ELEMENTS) {
            if (element.getSymbol().equals(input)) elements.add(element);
        }

        return elements;
    }

    @Override
    public ChemicalElement getRandom() {
        return ELEMENTS.get(ELEMENTS.size()-1);
    }

    public static void main(String[] args) {
        //initiate
    }
}
