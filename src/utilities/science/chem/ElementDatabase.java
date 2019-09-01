package utilities.science.chem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utilities.WebCache;

import java.io.IOException;
import java.util.ArrayList;

public class ElementDatabase {
    public static final ArrayList<ChemicalElement> ELEMENTS = getList();

    private static final String PERIODIC_TABLE_URL = "https://en.wikipedia.org/wiki/List_of_chemical_elements";



    /*
    from left to right:

    other non-metal (hydrogen)
    alkali metal
    alkaline earth metal
    lanthanide
    actinide
    transition metal
    unknown (Mt, Ds, Rg, Uut, Fl, Uup, Lv, Uus)
    post-transition metal
    metalloid
    other non-metal (right)
    halogen
    noble gas

    oth N-mtl
    alk mtl
    alk E-mtl
    lanthanide
    actinide
    tr mtl
    unknown
    post-tr
    metalloid
    oth N-mtl (right)
    halogen
    noble gas
     */



    private static ArrayList<ChemicalElement> getList() {
        ArrayList<ChemicalElement> elements = new ArrayList<>();

        WebCache elementCache = new WebCache(PERIODIC_TABLE_URL, "/chem/");

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
                String  atomicNumber = element.get(0).text();
                String  symbol = element.get(1).text();
                String  name = element.get(2).text();
                String  origin = element.get(3).text();
                String  group = element.get(4).text();
                String  period = element.get(5).text();
                String  atomicWeight = element.get(6).text();
                String  density = element.get(7).text();
                String  meltingPoint = element.get(8).text();
                String  boilingPoint = element.get(9).text();
                String  C = element.get(10).text();
                String  electronegativity = element.get(11).text();
                String  earthAbundane = element.get(12).text();


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
                        earthAbundane);

                elements.add(e);
            }
        } catch (Exception f) {
            f.printStackTrace();
        }



        return elements;
    }
}
