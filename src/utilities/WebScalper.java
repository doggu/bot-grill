package utilities;

import utilities.fehUnits.skills.Passive;
import utilities.fehUnits.skills.Skill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.IntStream;


public abstract class WebScalper {
    public abstract ArrayList<> getList();

    public static BufferedReader readWebsite(String url) throws IOException {
        return new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));
    }

    private static String stripHTML(String line) {
        char[] charArr = line.toCharArray();
        List<Character> chars = new ArrayList<>();
        for (char x : charArr)
            chars.add(x);

        boolean tag = false;
        for (int i = 0; i < chars.size(); i++) {
            if (chars.get(i) == '<') tag = true;
            if (chars.get(i) == '>') {
                tag = false;
                chars.remove(i);
                i--;
                continue;
            }
            if (tag) {
                chars.remove(i);
                i--;
            }

        }

        StringBuilder text = new StringBuilder();
        for (char x: chars) text.append(x);
        return text.toString();
    }

    private static String[][] getTables(BufferedReader br) throws IOException {

        return new String[3][3];
    }

    //literally just for hero information because all their shit is on one line
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



    private static void testPassives() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://feheroes.gamepedia.com/Passives").openConnection().getInputStream()));


        /*
        // read each line and write to System.out
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        */

        //read important lines
        String line;

        ArrayList<Passive> aPassives = new ArrayList<>();
        ArrayList<Passive> bPassives = new ArrayList<>();
        ArrayList<Passive> cPassives = new ArrayList<>();
        ArrayList<Passive> sPassives = new ArrayList<>();

        HashMap<Character, ArrayList<Passive>> lists = new HashMap<>();

        lists.put('a', new ArrayList<>());
        lists.put('b', new ArrayList<>());
        lists.put('c', new ArrayList<>());
        lists.put('s', new ArrayList<>());

        int listNumber = 0;
        int skillNumber = 0;

        while ((line = br.readLine()) != null) {
            if (line.contains("<div style=")) {
                char listChar;
                switch (listNumber) {
                    case 0:
                        listChar = 'a';
                        break;
                    case 1:
                        listChar = 'b';
                        break;
                    case 2:
                        listChar = 'c';
                        break;
                    case 3:
                        listChar = 's';
                        break;
                    default:
                        throw new Error();
                }
                ArrayList<Passive> list = lists.get(listChar);
                //System.out.println("skill list "+listNumber);
                while (!(line = br.readLine()).contains("</div>")) {
                    if (line.equals("<tr>")) {
                        //System.out.println("skill "+skillNumber);

                        /*
                        while (!(line = br.readLine()).contains("</tr>")) {
                            //System.out.println(stripHTML(line));
                        }
                        */

                        ///*
                        String img = br.readLine();
                        String name = stripHTML(br.readLine());
                        String description = stripHTML(br.readLine());
                        String costStr = stripHTML(br.readLine());
                        int cost = Integer.parseInt(costStr);
                        boolean exclusive = stripHTML(br.readLine()).contains("Yes");

                        try {
                            Passive k = new Passive(name, description, listChar, cost, exclusive);
                            list.add(k);
                        } catch (Error fuckinLazyProgrammingWithThisErrorMessage) {
                            System.out.println("somthin went wrang");
                            System.out.println(name);
                        }
                        //*/
                        skillNumber++;
                    } else {
                        //System.out.println(line);
                    }
                }

                System.out.println("number of skills: " + skillNumber);

                if (listNumber == 3) break;
                skillNumber = 1;

                listNumber++;
            }
        }

        //System.out.println("number of lists: "+listNumber);

        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String[] paramsStr = input.nextLine().split(" ");
            char list;
            int skill;
            try {
                list = paramsStr[0].charAt(0);
                skill = Integer.parseInt(paramsStr[1]);
            } catch (IndexOutOfBoundsException | NumberFormatException g) {
                System.out.println("incorrect syntax");
                continue;
            }

            try {
                System.out.println(lists.get(list).get(skill).toString());
            } catch (IndexOutOfBoundsException f) {
                System.out.println("out of range");
            }
        }
    }

    private static void testHeroLists() throws IOException {
        BufferedReader lv1Stats = new BufferedReader(new InputStreamReader(new URL("https://feheroes.gamepedia.com/Level_1_stats_table").openConnection().getInputStream()));
        BufferedReader growthRates = new BufferedReader(new InputStreamReader(new URL("https://feheroes.gamepedia.com/Growth_rate_table").openConnection().getInputStream()));
        BufferedReader heroList = new BufferedReader(new InputStreamReader(new URL("https://feheroes.gamepedia.com/Hero_list").openConnection().getInputStream()));

        IntStream lv1StatsTable = null;
        IntStream growthRatesTable = null;
        IntStream heroListTable = null;

        //actual data
        //lv1Stats - pic, name, weapon/color, movement, hp, atk, spd, def, res, total (lv1)
        //growthRates - pic, name, weapon/color, movement, lv1 total stats, total growth, "[lv1 total], [total growth]", hp, atk, spd, def, res (growth), release date
        //heroList - pic, name, origin, weapon/color, movement, rarity/status, release date

        //html-stripped data
        //lv1Stats -

        
        
        String line;
        while ((line = lv1Stats.readLine())!=null) {
            //the entire fucking table is on one line...
            if (line.contains("<table class=\"wikitable sortable\"")) lv1StatsTable = line.chars();
        }
        if (lv1StatsTable==null) {
            System.out.println("lv1StatsTable got some issues");
            throw new Error();
        }

        while ((line = growthRates.readLine())!=null) {
            //same for all of em
            if (line.contains("<table class=\"wikitable default sortable\"")) growthRatesTable = line.chars();
        }
        if (growthRatesTable==null) {
            System.out.println("growthRatesTable got some issues");
            throw new Error();
        }

        while ((line = heroList.readLine())!=null) {
            //ugh
            if (line.contains("<table class=\"wikitable default sortable\"")) heroListTable = line.chars();
        }
        if (heroListTable==null) {
            System.out.println("heroListTable got some issues");
            throw new Error();
        }

        
        ArrayList<String> lv1StatsData = getItems(lv1StatsTable);
        ArrayList<String> growthRatesData = getItems(growthRatesTable);
        ArrayList<String> heroListData = getItems(heroListTable);

        System.out.println("printing");
        ArrayList<ArrayList<String>> tablesOfTables = new ArrayList<>();
        tablesOfTables.add(lv1StatsData);
        tablesOfTables.add(growthRatesData);
        tablesOfTables.add(heroListData);

        for (ArrayList<String> x:tablesOfTables) {
            System.out.println("a table\n");
            for (int i=0; i<x.size()&&i<40; i++) {
                System.out.println(x.get(i));
            }
            System.out.println('\n');
        }
    }



    public static void main(String[] args) throws IOException {
        //source: https://stackoverflow.com/questions/6159118/using-java-to-pull-data-from-a-webpage
        // Make a URL to the web page
        URL url = new URL("https://feheroes.gamepedia.com/Passives");

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();

        // Once you have the Input Stream, it's just plain old Java IO stuff.

        // For this case, since you are interested in getting plain-text web page
        // I'll use a reader and output the text content to System.out.

        // For binary content, it's better to directly read the bytes from stream and write
        // to the target file.


        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        //testPassives();
        testHeroLists();
    }
}