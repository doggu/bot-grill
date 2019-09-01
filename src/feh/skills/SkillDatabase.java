package feh.skills;

import feh.FEHeroesCache;
import feh.skills.skillTypes.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import feh.heroes.character.WeaponClass;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class SkillDatabase {
    public static ArrayList<Skill> SKILLS = getList();
    public static HashMap<String, ArrayList<Skill>> HERO_SKILLS = getHeroSkills();

    private static final String
            SKILLS_SUBDIR = "/skills/";

    private static final String
            WEAPONS = "Weapons",
            ASSISTS = "Assists",
            SPECIALS = "Specials",
            PASSIVES = "Passives",
          //SACRED_SEALS_ALL = "Sacred_Seals",

    EXCLUSIVE_SKILLS = "Exclusive_skills",

          //SKILL_CHAINS_4_STARS = "https://feheroes.gamepedia.com/Skill_Chains_4_Stars_List",
          //SKILL_CHAINS_5_STARS = "https://feheroes.gamepedia.com/Skill_Chains_5_Stars_List",
          //LIST_OF_UPGRADABLE_WEAPONS = "https://feheroes.gamepedia.com/List_of_upgradable_weapons",
          //LIST_OF_EVOLVING_WEAPONS = "https://feheroes.gamepedia.com/List_of_evolving_weapons",

          //maybe a Skill thing
          //LIST_OF_DESCRIPTION_TAGS = "https://feheroes.gamepedia.com/List_of_description_tags",

    HERO_BASE_SKILLS = "Hero_skills_table",
            WEAPON_REFINES = "Weapon_Refinery";



    private static FEHeroesCache
            WEAPONS_FILE,
            ASSISTS_FILE,
            SPECIALS_FILE,
            PASSIVES_FILE,
            EXCLUSIVE_SKILLS_FILE,
            HERO_BASE_SKILLS_FILE,
            WEAPON_REFINES_FILE;

    private static FEHeroesCache[] SKILL_FILES = {
            WEAPONS_FILE,
            ASSISTS_FILE,
            SPECIALS_FILE,
            PASSIVES_FILE,
            EXCLUSIVE_SKILLS_FILE,
            HERO_BASE_SKILLS_FILE,
            WEAPON_REFINES_FILE,
    };



    //todo: create file manager
    // receives requests and handles them simultaneously
    // (e.g. SkillRetriever asks for all of its missing files)
    public static void updateCache() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i=0; i<SKILL_FILES.length; i++) {
            final int fileIndex = i;
            Thread loader = new Thread(() -> {
                try {
                    if (!SKILL_FILES[fileIndex].update()) throw new Error("unable to update " + SKILL_FILES[fileIndex].getName());
                } catch (NullPointerException npe) {
                    //SKILL_FILES[i] = new FEHeroesCache(SKILL_URLS[i]);
                }
            });

            threads.add(loader);
        }

        for (Thread loader:threads)
            loader.start();

        for (Thread loader:threads) {
            try {
                loader.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        SKILLS = getList();
        HERO_SKILLS = getHeroSkills();
    }



    private static ArrayList<Skill> getList() {
        System.out.print("processing skills... ");
        long start = System.nanoTime();



        //TODO: figure out how to get the initializers to handle this automagically
        WEAPONS_FILE = new FEHeroesCache(WEAPONS, SKILLS_SUBDIR);
        ASSISTS_FILE = new FEHeroesCache(ASSISTS, SKILLS_SUBDIR);
        SPECIALS_FILE = new FEHeroesCache(SPECIALS, SKILLS_SUBDIR);
        PASSIVES_FILE = new FEHeroesCache(PASSIVES, SKILLS_SUBDIR);
        EXCLUSIVE_SKILLS_FILE = new FEHeroesCache(EXCLUSIVE_SKILLS, SKILLS_SUBDIR);
        HERO_BASE_SKILLS_FILE = new FEHeroesCache(HERO_BASE_SKILLS, SKILLS_SUBDIR);
        WEAPON_REFINES_FILE = new FEHeroesCache(WEAPON_REFINES, SKILLS_SUBDIR);
        EXCLUSIVE = getExclusiveList();
        REFINES = getRefineableList();

        ArrayList<Skill> allSkills = new ArrayList<>();

        System.out.print("processing weapons... ");
        ArrayList<Weapon> weapons = processWeapons();
        System.out.print("processing assists... ");
        ArrayList<Assist> assists = processAssists();
        System.out.print("processing specials... ");
        ArrayList<Special> specials = processSpecials();
        System.out.print("processing passives... ");
        ArrayList<Passive> passives = processPassives();



        allSkills.addAll(weapons);
        allSkills.addAll(assists);
        allSkills.addAll(specials);
        allSkills.addAll(passives);



        System.out.println("done ("+
                new BigDecimal((System.nanoTime()-start)/1000000000.0)
                        .round(new MathContext(3))+
                " s)!");
        return allSkills;
    }

    private static ArrayList<Weapon> processWeapons() {
        ArrayList<Weapon> weapons = new ArrayList<>();



        Document weaponsFile;
        try {
            weaponsFile = Jsoup.parse(WEAPONS_FILE, "UTF-8");
                                                                //todo: make this a global variable
                                                                // i've probably already written this somewhere before
        } catch (IOException ioe) {
            System.out.println("weapons file not found!");
            return new ArrayList<>();
        }

        Elements tables = weaponsFile.select("table").select("tbody");

        String[] weaponType = {
                "Sword", "Red Tome",
                "Lance", "Blue Tome",
                "Axe", "Green Tome",
                "Staff",
                "Beast", "Breath", "Bow", "Dagger",
        };

        for (int i=0; i<tables.size(); i++) {
            Element table = tables.get(i);

            Elements rows = table.select("tr");
            for (Element row:rows) {
                Weapon x;
                Elements info = row.children();

                if (info.size()!=6) {
                    //not a weapon table
                    i--;
                    tables.remove(0);
                    break;
                }

                String name = info.get(0).text();
                int might = Integer.parseInt(info.get(1).text());
                int range = Integer.parseInt(info.get(2).text());
                String description = info.get(3).text();
                int sp = Integer.parseInt(info.get(4).text());
                boolean exclusive = (info.get(5).text().equals("Yes"));
                WeaponClass type = WeaponClass.getClass(weaponType[i]);
                WeaponRefine refine = getRefine(name);

                x = new Weapon(name, description, sp, exclusive, might, range, type, refine);

                weapons.add(x);
            }
        }



        return weapons;
    }
    private static ArrayList<Assist> processAssists() {
        ArrayList<Assist> assists = new ArrayList<>();



        Document assistsFile;
        try {
            assistsFile = Jsoup.parse(ASSISTS_FILE, "UTF-8");
        } catch (IOException ioe) {
            System.out.println("assists file not found!");
            return new ArrayList<>();
        }



        Elements tables = assistsFile.select("table").select("tbody");

        for (Element table:tables) {
            if (table.select("tr").size()>20) {
                Elements rows = table.select("tr");

                for (Element row:rows) {
                    Assist x;
                    Elements info = row.children();

                    String name = info.get(0).text();
                    String description = info.get(1).text();
                    int sp = Integer.parseInt(info.get(2).text());
                    int range = Integer.parseInt(info.get(3).text());
                    boolean exclusive = isExclusive(name);

                    x = new Assist(name, description, sp, exclusive, range);
                    assists.add(x);
                }
            }
        }



        return assists;
    }
    private static ArrayList<Special> processSpecials() {
        ArrayList<Special> specials = new ArrayList<>();



        Document specialsFile;
        try {
            specialsFile = Jsoup.parse(SPECIALS_FILE, "UTF-8");
        } catch (IOException ioe) {
            System.out.println("specials file not found!");
            return new ArrayList<>();
        }



        Elements tables = specialsFile.select("table");

        while (tables.get(0).select("tbody").select("tr").size()<25) tables.remove(0);

        Elements rows = tables.get(0).select("tbody").select("tr");



        for (Element row:rows) {
            Special x;
            Elements info = row.children();

            String name = info.get(0).text();
            String description = info.get(1).text();
            int sp = Integer.parseInt(info.get(2).text());
            int cooldown = Integer.parseInt(info.get(3).text());
            boolean exclusive = isExclusive(name);

            x = new Special(name, description, sp, exclusive, cooldown);
            specials.add(x);
        }



        return specials;
    }
    private static ArrayList<Passive> processPassives() {
        ArrayList<Passive> passives = new ArrayList<>();



        Document passivesFile;
        try {
            passivesFile = Jsoup.parse(PASSIVES_FILE, "UTF-8");
        } catch (IOException ioe) {
            System.out.println("passives file not found!");
            return new ArrayList<>();
        }



        Elements tables = passivesFile.select("table[class=\'cargoTable noMerge sortable\']");

        if (tables.size()!=4) {
            System.out.println("unknown table found (or one missing): "+tables.size());
            System.out.println(tables);
            return new ArrayList<>();
        }

        for (int i=0; i<tables.size(); i++) {
            Elements rows = tables.get(i).select("tbody").select("tr");

            for (Element row : rows) {
                try {
                    Passive x;
                    Elements info = row.children();
                    String[] urlSet = info.get(0).select("a")
                            .get(0).select("img")
                            .get(0).attr("srcset")
                            .split(" ");
                    String icon = urlSet[2];
                    String name = info.get(1).text();
                    String description = info.get(2).text();
                    int cost = Integer.parseInt(info.get(3).text());
                    boolean exclusive = (info.get(3).text().equals("Yes"));

                    if (i == 0)
                        x = new PassiveA(name, description, cost, exclusive, icon);
                    else if (i == 1)
                        x = new PassiveB(name, description, cost, exclusive, icon);
                    else if (i == 2)
                        x = new PassiveC(name, description, cost, exclusive, icon);
                    else if (i == 3)
                        x = new PassiveS(name, description, cost, exclusive, icon);
                    else {
                        System.out.println("this is not an expected table, how'd it even get this far");
                        break;
                    }

                    passives.add(x);
                } catch (Exception e) {
                    //let's not talk about that one
                }
            }
        }

        return passives;
    }



    private static ArrayList<String> EXCLUSIVE;

    private static ArrayList<String> getExclusiveList() {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<ArrayList<String>> tables = EXCLUSIVE_SKILLS_FILE.getTables();

        for (ArrayList<String> table:tables) list.addAll(table);



        return list;
    }

    private static boolean isExclusive(String name) {
        for (String x : EXCLUSIVE) if (x.equals(name)) return true;
        return false;
    }



    private static ArrayList<WeaponRefine> REFINES;

    private static ArrayList<WeaponRefine> getRefineableList() {
        Document refinesFile;
        try {
            refinesFile = Jsoup.parse(WEAPON_REFINES_FILE, "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new ArrayList<>();
        }



        Elements rawTables = refinesFile.select("table");
        Elements tables = refinesFile.select("table");
        for (Element table:rawTables) {
            if (!table.attributes().get("style").equals("")) {
                if (table.attributes().get("style")
                        .equals("display:inline-table;" +
                                "border:1px solid #a2a9b1;" +
                                "border-collapse:collapse;" +
                                "width:24em;" +
                                "margin:0.5em 0;" +
                                "background-color:#f8f9fa")) {
                    tables.add(table);
                }
            }
        }

        //the first table is stuck for some reason despite my VERY SPECIFIC qualifier
        tables.remove(0);

        ArrayList<WeaponRefine> refines = new ArrayList<>();

        for (Element table:tables) {
            Elements info = table.select("tr");

            if (info.size()!=6) {
                //it's not a unit's refine
                //new Error("unexpected table size found!").printStackTrace();
                //System.out.println(info);
                continue;
            }

            //0 is owner portrait(s)
            String icon = info.get(1)
                    .select("td").get(0)
                    .select("img").attr("srcset")
                    .split(" ")[2];
            String name = info.get(1).text();
            String stats = info.get(2).text();
            String description = info.get(3).text();
            String specialEff = info.get(4).text();
            //String cost = info.get(5).text(); //it's always 400SP, 200 Dew™

            String[] items = stats.split(" ");
            HashMap<String, Integer> modifiers = new HashMap<>();


            for (int i=0; i+1<items.length; i+=2) {
                if (items[i+1].contains(","))
                    items[i+1] = items[i+1].substring(0,items[i+1].length()-1);

                modifiers.put(items[i], Integer.parseInt(items[i + 1]));
            }

            Integer might = null, range = null;
            int[] values = new int[5];

            for (Map.Entry<String, Integer> e:modifiers.entrySet()) {
                switch (e.getKey()) {
                    case "Might:":
                        might = e.getValue();
                        break;
                    case "Range:":
                        range = e.getValue();
                        break;
                    case "HP":
                        values[0] = e.getValue();
                        break;
                    case "Atk":
                        values[1] = e.getValue();
                        break;
                    case "Spd":
                        values[2] = e.getValue();
                        break;
                    case "Def":
                        values[3] = e.getValue();
                        break;
                    case "Res":
                        values[4] = e.getValue();
                        break;
                    default:
                        new Error("unknown stat modifier: \""+e.getKey()+"\"").printStackTrace();
                }
            }

            if (might==null||range==null) {
                System.out.println("no might/range provided for "+name+"!");
                continue;
            }

            refines.add(new WeaponRefine(name, description, specialEff, icon, values, 400, might, range));
        }



        return refines;
    }

    /**
     * Used to associate refines with their base weapons.
     *
     * @param name name of the weapon in question
     * @return the Weapon object of [name]'s refine, null if no refine was found.
     */
    private static WeaponRefine getRefine(String name) {
        for (WeaponRefine x:REFINES) if (name.equals(x.getName())) return x;
        return null;
    }



    private static HashMap<String, ArrayList<Skill>> getHeroSkills() {
        HashMap<String, ArrayList<Skill>> heroSkills = new HashMap<>();


        Document baseSkillsFile;
        try {
            baseSkillsFile = Jsoup.parse(HERO_BASE_SKILLS_FILE, "UTF-8");
        } catch (IOException|NullPointerException e) {
            System.out.println("base skills file not found!");
            return new HashMap<>();
        }

        Element table = baseSkillsFile.select("table").get(0);

        Elements rows = table.select("tbody").select("tr");

        for (Element row:rows) {
            Elements info = row.children();

            //0 is an image
            String name = info.get(1).text();
            //2 is move type
            //3 is weapon type
            ArrayList<Skill> baseKit = new ArrayList<>();
            for (int i=4; i<info.size(); i++) {
                if (!info.get(i).text().equals("—")) {
                    Elements skills = info.get(i).select("a");
                    for (Element skill:skills) {
                        String skillName = skill.text();
                        baseKit.add(getSkill(skillName));
                    }
                }
            }

            heroSkills.put(name, baseKit);
        }



        return heroSkills;
    }



    public static Skill getSkill(String name) {
        for (Skill x:SKILLS) {
            if (x.getName().equalsIgnoreCase(name)) {
                return x;
            }
        }

        return null;
    }



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String chunk = input.nextLine().toLowerCase();

            HashMap<Skill, String> descPortion = new HashMap<>();

            for (Skill x:SKILLS) {
                String[] description = x.getDescription().toLowerCase().split("\\. ?");

                for (String s:description) System.out.println(s);
                System.out.println();
                System.out.println();

                for (String b:description) {
                    if (b.matches(chunk)) {
                        descPortion.put(x, b);
                    }
                }
            }

            System.out.println(descPortion);
        }
    }
}
