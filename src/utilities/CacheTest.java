package utilities;

import java.io.*;
import java.util.ArrayList;

public class CacheTest {
    private static final String TEST_URL = "https://feheroes.gamepedia.com/Exclusive_skills";



    //stolen from SkillDatabase
    private static ArrayList<ArrayList<String>> getTables(BufferedReader input) throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> table = new ArrayList<>();
        String line;
        boolean print = false;

        while ((line = input.readLine()) != null) {
            System.out.println(line);
            if (!print)
                if (line.contains("<table class=\"cargoTable noMerge sortable"))
                    print = true;

            if (print) {
                ArrayList<String> datum = WebScalper.getItems(line.chars());
                for (int i=0; i<datum.size(); i++) datum.set(i, datum.get(i).trim());
                if (datum.size()>0) table.addAll(datum);

                if (line.contains("</tbody></table>")) {
                    print = false;
                    //apparently i DONT need to clone it
                    if (table.size()>0) data.add(/*(ArrayList<String>)*/ table/*.clone()*/);
                    table = new ArrayList<>();
                }
            }
        }

        return data;
    }



    public static void main(String[] args) throws IOException {
        BufferedReader website = WebScalper.readWebsite(TEST_URL);
        ArrayList<ArrayList<String>> items = getTables(website);
        System.out.println(items);

        File cache = new File("./src/utilities/feh/webCache/test.txt");
        FileWriter writer = new FileWriter(cache);
        System.out.println(cache.canWrite());

        for (ArrayList<String> list:items) {
            list.subList(0,3).clear();
            for (String x:list) {
                writer.write(x + "\n");
                //writer.append(x);
            }
        }

        writer.close();
    }
}
