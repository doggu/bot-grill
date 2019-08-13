package feh;

import utilities.WebCache;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class FEHeroesCache extends WebCache {
    private static final String
            FEHEROES_URL = "https://feheroes.gamepedia.com/",
            FEHEROES_SUBDIR = "feh";



    //TODO: replace old utilities with new jsoupy ones
    public FEHeroesCache(String wiki, String subdir) {
        super(FEHEROES_URL+wiki, FEHEROES_SUBDIR+(subdir!=null?subdir:""));
    }
    public FEHeroesCache(String wiki) {
        this(wiki, null);
    }

    public ArrayList<ArrayList<String>> getTables() {
        Scanner input;
        try {
            input = new Scanner(this);
        } catch (FileNotFoundException fnfe) {
            throw new Error("could not find file at "+getPath());
        }
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> table = new ArrayList<>();
        String line;
        boolean print = false;

        while (input.hasNextLine()) {
            line = input.nextLine();
            if (line.contains("<table class=\"cargoTable noMerge sortable\">")) {
                print = true;
            }
            if (print) {
                ArrayList<String> datum = getItems(line.chars());
                if (datum.size()>0) table.addAll(datum);
            }

            if (line.contains("</tbody></table>")) {
                print = false;
                //apparently i DONT need to clone it
                if (table.size()>0) data.add(/*(ArrayList<String>) */table/*.clone()*/);
                table = new ArrayList<>();
            }
        }

        return data;
    }

    private static ArrayList<String> getItems(IntStream source) {
        ArrayList<String> data = new ArrayList<>();
        int[] chars = source.toArray();

        StringBuilder datum = new StringBuilder();
        boolean isData = false;
        for (int i:chars) {
            char c = (char) i;
            if (c=='<') {
                isData = false;
                if (datum.length()>0) {
                    data.add(datum.toString());
                    datum = new StringBuilder();
                }
                continue;
            }
            if (isData) datum.append(c);
            if (c=='>') {
                isData = true;
            }
        }

        return data;
    }
}
