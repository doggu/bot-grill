package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.IntStream;



public abstract class WebScalper {
    public static BufferedReader readWebsite(String url) throws IOException {
        BufferedReader website = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));
        System.out.println("finished reading "+url);
        return website;
    }



    //literally just for hero information because all their shit is on one line
    public static ArrayList<String> getItems(IntStream source) {
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

    private static ArrayList<ArrayList<String>> getTables(BufferedReader input) throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> table = new ArrayList<>();
        String line;
        boolean print = false;

        while ((line = input.readLine()) != null) {      //TODO: NOMERGE broke shit
            if (line.contains("<table style=\"display:inline-flex"))
                print = true;
            if (print) {
                ArrayList<String> datum = getItems(line.chars());
                for (int i=0; i<datum.size(); i++) datum.set(i, datum.get(i).trim());
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



    public static void main(String[] args) throws IOException {
        //source: https://stackoverflow.com/questions/6159118/using-java-to-pull-data-from-a-webpage
        // Make a URL to the web page
        URL url = new URL("https://feheroes.gamepedia.com/Weapon_Refinery");
        //SITE_LIST_OF_UPGRADABLE_WEAPONS

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();

        System.out.println("finished retrieving website");

        // Once you have the Input Stream, it's just plain old Java IO stuff.

        // For this case, since you are interested in getting plain-text web page
        // I'll use a reader and output the text content to System.out.

        // For binary content, it's better to directly read the bytes from stream and write
        // to the target file.


        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        /*
        String line;
        while ((line = br.readLine())!=null) {
            System.out.println(line);
        }
        */

        ArrayList<ArrayList<String>> tables;
        try {
            tables = getTables(br);
        } catch (IOException f) {
            System.out.println("no multitable found");
            return;
        }

        for (ArrayList<String> table:tables) {
            for (String datum:table) {
                System.out.println(datum);
            }
            //System.out.println();
        }

        /*
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            if (line.contains("<table class=\"wikitable sortable\"")) {
                break;
            }
        }

        if (line==null) {
            System.out.println("houston we got a problem");
            throw new Error();
        }

        String[] items = line.split("<tr");

        for (String x:items) {
            String[] info = x.split("<td>");
            for (String y:info) {
                System.out.println(y);
            }
            System.out.println();
        }
        */



        //testPassives();
        //testHeroLists();
        //testSummoningFocusArchive();
    }
}