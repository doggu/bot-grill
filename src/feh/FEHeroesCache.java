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

    /**
     * this one gets a description since it's unclear
     * @param tableFormat the exact form, in plain text, of the table html tag to look for
     * @return the data of the table in a single, linear array (you gotta figure out where the rows end lol)
     */
    public ArrayList<String> getTable(String tableFormat) {
        ArrayList<String> table = new ArrayList<>();

        Scanner file;
        try {
            file = new Scanner(this);
        } catch (FileNotFoundException fnfe) {
            throw new Error("could not find file "+getName());
        }
        String line;
        while (file.hasNextLine()) {
            line = file.nextLine();
            if (line.contains(tableFormat))
                table = getItems(line.chars());
        }

        return table;
    }

    //for BannerDatabase
    public String getLongAssLine(int threshold) {
        Scanner input;
        try {
            input = new Scanner(this);
        } catch (FileNotFoundException fnfe) {
            throw new Error("could not find file at "+getPath());
        }

        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.length()>threshold)
                return line;
        }

        return null;
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



    public static void main(String[] args) {
        FEHeroesCache test = new FEHeroesCache("Weapon_Refinery");
        ArrayList<String> table = test.getTable("<table style=\"display:inline-table;border:1px solid #a2a9b1;border-collapse:collapse;width:24em;margin:0.5em 0;background-color:#f8f9fa\">");

        for (String datum:table) {
            System.out.println(datum.trim());
        }
    }
}
