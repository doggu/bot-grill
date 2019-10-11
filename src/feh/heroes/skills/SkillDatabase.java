package feh.heroes.skills;

import feh.FEHeroesCache;
import feh.heroes.character.WeaponClass;
import feh.heroes.skills.skillTypes.*;
import feh.heroes.skills.skillTypes.constructionSite.IncompleteDataException;
import feh.heroes.skills.skillTypes.constructionSite.SkillConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.Database;
import utilities.WebCache;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SkillDatabase extends Database<Skill> {
    public static SkillDatabase DATABASE;
    public static ArrayList<Skill> SKILLS;
    /*
    public static ArrayList<Weapon> WEAPONS;
    public static ArrayList<Assist> ASSISTS;
    public static ArrayList<Special> SPECIALS;
    public static ArrayList<Passive> PASSIVES;
    public static ArrayList<PassiveA> PASSIVES_A;
    public static ArrayList<PassiveB> PASSIVES_B;
    public static ArrayList<PassiveC> PASSIVES_C;
    public static ArrayList<PassiveS> PASSIVES_S;
     */
    public static HashMap<String, ArrayList<Skill>> HERO_SKILLS;

    private static final String
            SKILLS_SUBDIR = "/skills/";
    private static final String
            WEAPONS_PATH = "Weapons",
            ASSISTS_PATH = "Assists",
            SPECIALS_PATH = "Specials",
            PASSIVES_PATH = "Passives",
          //SACRED_SEALS_ALL = "Sacred_Seals",
            EXCLUSIVE_SKILLS_PATH = "Exclusive_skills",
          //SKILL_CHAINS_4_STARS = "https://feheroes.gamepedia.com/Skill_Chains_4_Stars_List",
          //SKILL_CHAINS_5_STARS = "https://feheroes.gamepedia.com/Skill_Chains_5_Stars_List",
          //LIST_OF_UPGRADABLE_WEAPONS = "https://feheroes.gamepedia.com/List_of_upgradable_weapons",
          //LIST_OF_EVOLVING_WEAPONS = "https://feheroes.gamepedia.com/List_of_evolving_weapons",
          //maybe a Skill thing
          //LIST_OF_DESCRIPTION_TAGS = "https://feheroes.gamepedia.com/List_of_description_tags",
            HERO_BASE_SKILLS_PATH = "Hero_skills_table",
            WEAPON_REFINES_PATH = "Weapon_Refinery",
            AOE_PATTERNS_PATH = "Area-of-effect_Specials";

    private static final String
            FEHEROES = "https://feheroes.gamepedia.com";



    private static FEHeroesCache
            WEAPONS_FILE,
            ASSISTS_FILE,
            SPECIALS_FILE,
            PASSIVES_FILE,
            EXCLUSIVE_SKILLS_FILE,
            HERO_BASE_SKILLS_FILE,
            WEAPON_REFINES_FILE,
            AOE_PATTERNS_FILE;

    private static FEHeroesCache[] SKILL_FILES;

    static {
        WEAPONS_FILE = new FEHeroesCache(WEAPONS_PATH, SKILLS_SUBDIR);
        ASSISTS_FILE = new FEHeroesCache(ASSISTS_PATH, SKILLS_SUBDIR);
        SPECIALS_FILE = new FEHeroesCache(SPECIALS_PATH, SKILLS_SUBDIR);
        PASSIVES_FILE = new FEHeroesCache(PASSIVES_PATH, SKILLS_SUBDIR);
        EXCLUSIVE_SKILLS_FILE = new FEHeroesCache(EXCLUSIVE_SKILLS_PATH, SKILLS_SUBDIR);
        HERO_BASE_SKILLS_FILE = new FEHeroesCache(HERO_BASE_SKILLS_PATH, SKILLS_SUBDIR);
        WEAPON_REFINES_FILE = new FEHeroesCache(WEAPON_REFINES_PATH, SKILLS_SUBDIR);
        AOE_PATTERNS_FILE = new FEHeroesCache(AOE_PATTERNS_PATH, SKILLS_SUBDIR);

        SKILL_FILES = new FEHeroesCache[]{
                WEAPONS_FILE,
                ASSISTS_FILE,
                SPECIALS_FILE,
                PASSIVES_FILE,
                EXCLUSIVE_SKILLS_FILE,
                HERO_BASE_SKILLS_FILE,
                WEAPON_REFINES_FILE,
                AOE_PATTERNS_FILE,
        };

        EXCLUSIVE = getExclusiveList();
        REFINES = getRefineableList();
        AOE_PATTERNS = getAOEPatternList();

        DATABASE = new SkillDatabase();
        /*
        WEAPONS = processWeapons();
        ASSISTS = processAssists();
        SPECIALS = processSpecials();
        PASSIVES = processPassives();
        ArrayList<PassiveA> aPassives = new ArrayList<>();
        ArrayList<PassiveB> bPassives = new ArrayList<>();
        ArrayList<PassiveC> cPassives = new ArrayList<>();
        ArrayList<PassiveS> sPassives = new ArrayList<>();

        for (Passive x:PASSIVES) {
            if (x instanceof PassiveA) {
                aPassives.add((PassiveA) x);
            } else if (x instanceof PassiveB) {
                bPassives.add((PassiveB) x);
            } else if (x instanceof PassiveC) {
                cPassives.add((PassiveC) x);
            } else if (x instanceof PassiveS) {
                sPassives.add((PassiveS) x);
            }
        }

        PASSIVES_A = aPassives;
        PASSIVES_B = bPassives;
        PASSIVES_C = cPassives;
        PASSIVES_S = sPassives;
        SKILLS = new ArrayList<>(); //DATABASE.getList();
        SKILLS.addAll(WEAPONS);
        SKILLS.addAll(ASSISTS);
        SKILLS.addAll(SPECIALS);
        SKILLS.addAll(PASSIVES);
         */
        SKILLS = DATABASE.getList();
        HERO_SKILLS = DATABASE.getHeroSkills();
    }



    @Override
    protected WebCache[] getOnlineResources() {
        return SKILL_FILES;
    }



    protected ArrayList<Skill> getList() {
        System.out.print("processing skills... ");
        long start = System.nanoTime();

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

        Elements tables = weaponsFile.select("table");

        String[] weaponType = {
                "Sword", "Red Tome",
                "Lance", "Blue Tome",
                "Axe", "Green Tome",
                "Staff",
                "Beast", "Breath", "Bow", "Dagger",
        };

        for (int i=0; i<tables.size(); i++) {
            Element table = tables.get(i);

            Element header;
            try {
                header = table.selectFirst("thead");
            } catch (NullPointerException npe) {
                //npe.printStackTrace();
                continue;
            }

            if (header==null) {
                tables.remove(i);
                i--;
                continue;
            }

            Elements rows = table.selectFirst("tbody").select("tr");
            for (Element row:rows) {
                SkillConstructor x = new SkillConstructor();
                Elements info = row.children();

                gatherBasicInformation(x, header, info);

                if (info.size()!=6) {
                    //not a weapon table
                    i--;
                    tables.remove(0);
                    break;
                }

                x.setMight(Integer.parseInt(info.get(1).text()));
                x.setRange(Integer.parseInt(info.get(2).text()));
                x.setExclusive(info.get(5).text().equals("Yes"));
                x.setType(WeaponClass.getClass(weaponType[i]));
                x.setRefine(getRefine(x.getName()));

                try {
                    weapons.add(x.generateWeapon());
                } catch (IncompleteDataException ide) {
                    ide.printStackTrace();
                    //continue
                }
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

        Elements tables = assistsFile.select("table");

        for (Element table:tables) {
            if (table.select("tr").size()>20) {
                Element header = table.selectFirst("thead");
                Elements rows = table.selectFirst("tbody").select("tr");

                for (Element row:rows) {
                    SkillConstructor x = new SkillConstructor();
                    Elements info = row.children();

                    gatherBasicInformation(x, header, info);

                    x.setRange(Integer.parseInt(info.get(3).text()));
                    x.setExclusive(isExclusive(x.getName()));

                    try {
                        assists.add(x.generateAssist());
                    } catch (IncompleteDataException ide) {
                        ide.printStackTrace();
                    }
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

        Element table = specialsFile.select("table").get(1),
                header = table.selectFirst("thead");
        Elements rows = table.select("tbody").select("tr");

        for (Element row:rows) {
            SkillConstructor x = new SkillConstructor();
            Elements info = row.children();

            gatherBasicInformation(x, header, info);

            x.setCooldown(Integer.parseInt(info.get(3).text()));
            x.setExclusive(isExclusive(x.getName()));

            x.setDamagePattern(AOE_PATTERNS.get(x.getName()));

            try {
                specials.add(x.generateSpecial());
            } catch (IncompleteDataException ide) {
                ide.printStackTrace();
            }
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
            Element header = tables.get(i).selectFirst("thead");

            for (Element row : rows) {
                try {
                    SkillConstructor x = new SkillConstructor();
                    Elements info = row.children();

                    gatherBasicInformation(x, header, info);

                    String[] urlSet = info.get(0).select("a")
                            .get(0).select("img")
                            .get(0).attr("srcset")
                            .split(" ");
                    try {
                        x.setIcon(new URL(urlSet[2]));
                    } catch (MalformedURLException murle) {
                        System.out.println("got a murle for icon "+x.getName());
                        x.setIcon(null);
                    }

                    x.setExclusive(info.get(4).text().equals("Yes"));

                    switch (i) {
                        case 0:
                            passives.add(x.generatePassiveA());
                            break;
                        case 1:
                            passives.add(x.generatePassiveB());
                            break;
                        case 2:
                            passives.add(x.generatePassiveC());
                            break;
                        case 3:
                            passives.add(x.generatePassiveS());
                            break;
                        default:
                            System.out.println("this is not an expected table, how'd it even get this far");
                            //continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //let's not talk about that one
                }
            }
        }

        return passives;
    }
    private static void gatherBasicInformation(SkillConstructor x, Element header, Elements info) {
        ArrayList<String> titles = new ArrayList<>(header.child(0).children().eachText());

        for (int i=0; i<titles.size(); i++) {
            switch (titles.get(i)) {
                case "Name":
                case "Weapon":
                    x.setName(info.get(i).text());
                    try {
                        x.setLink(new URL(FEHEROES+info.get(i).children().get(0).attr("href")));
                    } catch (MalformedURLException murle) {
                        System.out.println("got a murle for "+x.getName());
                        x.setLink(null);
                    }
                    break;
                case "Description":
                    x.setDescription(info.get(i).text());
                    break;
                case "SP":
                    x.setCost(Integer.parseInt(info.get(i).text()));
            }
        }
    }

    private static final ArrayList<String> EXCLUSIVE;
    private static ArrayList<String> getExclusiveList() {
        ArrayList<String> list = new ArrayList<>();
        Document exclusivesFile;
        try {
            exclusivesFile = Jsoup.parse(EXCLUSIVE_SKILLS_FILE, "UTF-8");
        } catch (IOException ioe) {
            System.out.println("exclusive list not found!");
            return new ArrayList<>();
        }

        Elements tables = exclusivesFile.select("table");

        Elements headers = tables.select("thead").select("tr");
        Elements bodies = exclusivesFile.select("table").select("tbody");

        for (int i=0; i<headers.size(); i++) {
            Elements labels = headers.get(i).select("th");
            Elements rows = bodies.get(i).children();
            int nameRow;
            for (nameRow=0; nameRow<labels.size(); nameRow++)
                if (labels.get(nameRow).text().matches("(Weapon)|(Assist)|(Special)|(Name)"))
                    break;

            for (Element row:rows)
                list.add(row.children().get(nameRow).text());
        }



        return list;
    }
    private static boolean isExclusive(String name) {
        for (String x : EXCLUSIVE) if (x.equals(name)) return true;
        return false;
    }

    private static final ArrayList<WeaponRefine> REFINES;
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
            String name = info.get(1).text();
            String stats = info.get(2).text();
            String description = info.get(3).text();
            String specialEff = info.get(4).text();
            URL icon;
            try {
                icon = new URL(info.get(1)
                        .select("td").get(0)
                        .select("img").attr("srcset")
                        .split(" ")[2]);
            } catch (Exception murle) {
                System.out.println("got a murle for "+name);
                icon = null;
            }
            URL link;
            try {
                link = new URL(FEHEROES+info.get(0).select("a").get(0).attr("href"));
            } catch (MalformedURLException murle) {
                System.out.println("got a murle for "+name);
                link = null;
            }
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

            refines.add(new WeaponRefine(name, description, specialEff, link, icon, values, 400, might, range));
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
    //todo: evolutions

    private static final HashMap<String, boolean[][]> AOE_PATTERNS;
    private static HashMap<String, boolean[][]> getAOEPatternList() {
        Document patternsFile;
        try {
            patternsFile = Jsoup.parse(AOE_PATTERNS_FILE, "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new HashMap<>();
        }

        Element table = patternsFile.selectFirst("tbody");
        Elements elements = table.children();
        elements.remove(0); //header that's not actually a thead

        if (elements.size()!=4) {
            System.out.println("there aren't four elements of AoE!\n"+elements);
        }

        HashMap<String, boolean[][]> patterns = new HashMap<>();

        for (Element element:elements) {
            Elements items = element.children();

            String type = items.get(0).text();

            Element blazing = items.get(1).selectFirst("table"),
                    growing = items.get(2).selectFirst("table");

            boolean[][] blazingP = getPattern(blazing), growingP = getPattern(growing);
            patterns.put("Rising "+type, blazingP);
            patterns.put("Blazing "+type, blazingP);
            patterns.put("Growing "+type, growingP);
        }

        return patterns;
    }
    private static boolean[][] getPattern(Element table) {
        Elements rows = table.select("tr");

        ArrayList<Elements> patternTable = new ArrayList<>();

        for (Element row:rows) {
            patternTable.add(row.children());
        }

        boolean[][] pattern = new boolean[patternTable.size()][patternTable.get(0).size()];

        for (int i=0; i<pattern.length; i++) {
            Elements row = patternTable.get(i);
            for (int j=0; j<pattern[i].length; j++) {
                pattern[i][j] = row.get(j).children().size()>0;
            }
        }

        return pattern;
    }

    private HashMap<String, ArrayList<Skill>> getHeroSkills() {
        HashMap<String, ArrayList<Skill>> heroSkills = new HashMap<>();

        Document baseSkillsFile;
        try {
            baseSkillsFile = Jsoup.parse(HERO_BASE_SKILLS_FILE, "UTF-8");
        } catch (IOException|NullPointerException e) {
            System.out.println("doin it again because i don't understand priorities...");
            HERO_BASE_SKILLS_FILE = new FEHeroesCache(HERO_BASE_SKILLS_PATH);
            if (HERO_BASE_SKILLS_FILE.update()) return getHeroSkills();
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
                        baseKit.add(DATABASE.find(skillName));
                    }
                }
            }

            heroSkills.put(name, baseKit);
        }



        return heroSkills;
    }



    public ArrayList<Skill> findAll(String name) {
        ArrayList<Skill> all = new ArrayList<>();

        for (Skill x:SKILLS) {
            if (x.getName().equalsIgnoreCase(name)) {
                all.add(x);
            }
        }

        return all;
    }

    @Override
    public Skill getRandom() {
        return SKILLS.get((int)(Math.random()*SKILLS.size()));
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String chunk = input.nextLine();//.toLowerCase();

            HashMap<Skill, String> descPortion = new HashMap<>();

            for (Skill x:SKILLS) {
                String[] description;
                try {
                description = x.getDescription()
                            .substring(0, x.getDescription().length() - 1)
                            //.toLowerCase()
                            .split("\\. ");
                } catch (IndexOutOfBoundsException ioobe) {
                    //stpid silver sowrd
                    continue;
                }

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
