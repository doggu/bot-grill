package feh.skills;

import feh.FEHeroesCache;
import utilities.WebScalper;
import feh.heroes.character.WeaponClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SkillDatabase extends WebScalper {
    public static ArrayList<Skill> SKILLS = getList();
    public static HashMap<String, ArrayList<Skill>> HERO_SKILLS = getHeroSkills();

    private static final String
            SKILLS_SUBDIR = "/skills/";

    private static final String
            WEAPONS = "Weapons",
            ASSISTS = "Assists",
            SPECIALS = "Specials",
            PASSIVES = "Passives",
            //SACRED_SEALS_ALL = "https://feheroes.gamepedia.com/Sacred_Seals",

            EXCLUSIVE_SKILLS = "Exclusive_skills",

            //SKILL_CHAINS_4_STARS = "https://feheroes.gamepedia.com/Skill_Chains_4_Stars_List",
            //SKILL_CHAINS_5_STARS = "https://feheroes.gamepedia.com/Skill_Chains_5_Stars_List",
            //LIST_OF_UPGRADABLE_WEAPONS = "https://feheroes.gamepedia.com/List_of_upgradable_weapons",

            HERO_BASE_SKILLS = "Hero_skills_table";



    private static FEHeroesCache
            WEAPONS_FILE,
            ASSISTS_FILE,
            SPECIALS_FILE,
            PASSIVES_FILE,
            EXCLUSIVE_SKILLS_FILE,
            HERO_BASE_SKILLS_FILE;

    private static FEHeroesCache[] SKILL_FILES = {
            WEAPONS_FILE,
            ASSISTS_FILE,
            SPECIALS_FILE,
            PASSIVES_FILE,
            EXCLUSIVE_SKILLS_FILE,
            HERO_BASE_SKILLS_FILE,
    };



    public static void updateCache() {
        for (FEHeroesCache x:SKILL_FILES) {
            if (!x.update()) throw new Error("unable to update "+x.getName());
        }

        SKILLS = getList();
        HERO_SKILLS = getHeroSkills();
    }



    private static ArrayList<Skill> getList() {
        //TODO: figure out how to get the initializers to handle this automagically
        WEAPONS_FILE = new FEHeroesCache(WEAPONS, SKILLS_SUBDIR);
        ASSISTS_FILE = new FEHeroesCache(ASSISTS, SKILLS_SUBDIR);
        SPECIALS_FILE = new FEHeroesCache(SPECIALS, SKILLS_SUBDIR);
        PASSIVES_FILE = new FEHeroesCache(PASSIVES, SKILLS_SUBDIR);
        EXCLUSIVE_SKILLS_FILE = new FEHeroesCache(EXCLUSIVE_SKILLS, SKILLS_SUBDIR);
        HERO_BASE_SKILLS_FILE = new FEHeroesCache(HERO_BASE_SKILLS, SKILLS_SUBDIR);
        EXCLUSIVE = getExclusiveList();

        ArrayList<Skill> allSkills = new ArrayList<>();

        ArrayList<Weapon> weapons = processWeapons();
        ArrayList<Assist> assists = processAssists();
        ArrayList<Special> specials = processSpecials();
        ArrayList<Passive> passives = processPassives();

        /*
        maybe? (could also be done in special function)

        ArrayList<PassiveA> Apassives = new ArrayList<>();
        ArrayList<PassiveB> Bpassives = new ArrayList<>();
        ArrayList<PassiveC> Cpassives = new ArrayList<>();
        ArrayList<PassiveS> Spassives = new ArrayList<>();
         */



        allSkills.addAll(weapons);
        allSkills.addAll(assists);
        allSkills.addAll(specials);
        allSkills.addAll(passives);



        System.out.println("finished processing skills.");
        return allSkills;
    }

    private static ArrayList<Weapon> processWeapons() {
        ArrayList<ArrayList<String>> weaponTables = WEAPONS_FILE.getTables();

        System.out.print("processing weapons... ");

        ArrayList<Weapon> weapons = new ArrayList<>();

        String[] weaponType = {
                "Sword", "Red Tome",
                "Lance", "Blue Tome",
                "Axe", "Green Tome",
                "Staff",
                "Beast", "Breath", "Bow", "Dagger",
        };

        for (ArrayList<String> table:weaponTables)
            table.subList(0,6).clear();

        for (int i=0; i<weaponTables.size(); i++) {
            System.out.print(weaponType[i]+"s... ");
            Iterator<String> iterator = weaponTables.get(i).iterator();



            Weapon x;
            while (iterator.hasNext()) {
                String name = iterator.next();
                int might = Integer.parseInt(iterator.next());
                int range = Integer.parseInt(iterator.next()); //technically the same for any given table but w/e
                StringBuilder desc = new StringBuilder();
                int cost = -1;
                while (cost<0) {
                    String line = iterator.next();
                    try {
                        cost = Integer.parseInt(line);
                    } catch (NumberFormatException g) {
                        if (desc.length()>0) desc.append(" ");
                        desc.append(line);
                    }
                }
                String description = desc.toString();
                boolean exclusive = iterator.next().equals("Yes"); //EXCLUSIVE_WEAPONS.contains(name);
                WeaponClass type = WeaponClass.getType(weaponType[i]);

                x = new Weapon(name, description, cost, exclusive, might, range, type);
                weapons.add(x);
            }
        }

        System.out.println("done!");
        return weapons;
    }
    private static ArrayList<Assist> processAssists() {
        ArrayList<String> data = ASSISTS_FILE.getTables().get(0);

        System.out.print("processing assists... ");



        Iterator<String> iterator = data.iterator();

        for (int i=0; i<4; i++) iterator.next();
        
        ArrayList<Assist> assists = new ArrayList<>();
        Assist x;
        while (iterator.hasNext()) {
            String name = iterator.next();
            StringBuilder desc = new StringBuilder();
            int cost = -1;
            while (cost<0) {
                String line = iterator.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            int range = Integer.parseInt(iterator.next());
            boolean exclusive = isExclusive(name);

            x = new Assist(name, description, cost, exclusive, range);
            assists.add(x);
        }

        System.out.println("done!");
        return assists;
    }
    private static ArrayList<Special> processSpecials() {
        ArrayList<String> data = SPECIALS_FILE.getTables().get(0);

        System.out.print("processing specials...");



        data.subList(0,4).clear();

        Iterator<String> iterator = data.iterator();

        ArrayList<Special> specials = new ArrayList<>();
        Special x;
        while (iterator.hasNext()) {
            String name = iterator.next();
            StringBuilder desc = new StringBuilder();
            int cost = -1;
            while (cost<0) {
                String line = iterator.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            int cooldown = Integer.parseInt(iterator.next());
            boolean exclusive = isExclusive(name);

            x = new Special(name, description, cost, exclusive, cooldown);
            specials.add(x);
        }

        System.out.println("done!");
        return specials;
    }
    private static ArrayList<Passive> processPassives() {
        ArrayList<ArrayList<String>> tables = PASSIVES_FILE.getTables();



        System.out.print("processing passives... ");

        ArrayList<Passive> passives = new ArrayList<>();

        for (ArrayList<String> x : tables)
            x.subList(0, 5).clear();

        if (tables.size() < 4) {
            System.out.println("a table has gone missing from the passives website");
            throw new Error();
        } else if (tables.size() > 4) {
            System.out.println("an unsolicited table has appeared in passives");
            throw new Error();
        } //else it's good dawg



        Passive x;

        for (int i=0; i<tables.size(); i++) {
            System.out.print("table "+(i+1)+"... ");
            Iterator<String> iterator = tables.get(i).iterator();
            while (iterator.hasNext()) {
                String name = iterator.next();
                StringBuilder desc = new StringBuilder();
                int cost = -1;
                //while cost is not defined, the description is being presented
                //it's 20XX, IS has introduced skills which give heroes SP
                while (cost < 0) {
                    String line = iterator.next();
                    try {
                        cost = Integer.parseInt(line);
                    } catch (NumberFormatException g) {
                        desc.append(" ");
                        desc.append(line);
                    }
                }
                String description = desc.substring(1);
                boolean exclusive = iterator.next().equals("Yes");

                switch (i) {
                    case 0:
                        x = new PassiveA(name, description, cost, exclusive);
                        break;
                    case 1:
                        x = new PassiveB(name, description, cost, exclusive);
                        break;
                    case 2:
                        x = new PassiveC(name, description, cost, exclusive);
                        break;
                    case 3:
                        x = new PassiveS(name, description, cost, exclusive);
                        break;
                    default:
                        System.out.println("what the fuck");
                        throw new Error();
                }
                passives.add(x);
            }
        }


        System.out.println("done!");
        return passives;
    }



    private static ArrayList<String> EXCLUSIVE;

    private static ArrayList<String> getExclusiveList() {
        //System.out.println("creating exclusive list...");
        ArrayList<String> list = new ArrayList<>();
        ArrayList<ArrayList<String>> tables = EXCLUSIVE_SKILLS_FILE.getTables();

        for (ArrayList<String> table:tables) list.addAll(table);

        return list;
    }

    private static boolean isExclusive(String name) {
        for (String x : EXCLUSIVE) if (x.equals(name)) return true;
        return false;
    }



    private static HashMap<String, ArrayList<Skill>> getHeroSkills() {
        if (HERO_BASE_SKILLS_FILE==null) HERO_BASE_SKILLS_FILE = new FEHeroesCache(HERO_BASE_SKILLS, SKILLS_SUBDIR);
        ArrayList<String> baseSkillTable = HERO_BASE_SKILLS_FILE.getTable("<table class=\"wikitable sortable");



        baseSkillTable.subList(0,7).clear();

        Iterator<String> table = baseSkillTable.iterator();



        HashMap<String, ArrayList<Skill>> heroSkills = new HashMap<>();

        String name = table.next();
        while (table.hasNext()) {
            ArrayList<String> skillNames = new ArrayList<>();

            String skill;
            while (!(skill = table.next()).contains(": ")&&table.hasNext()) {
                skillNames.add(skill);
            }


            ArrayList<Skill> skills = new ArrayList<>();

            //TODO: separate skill list into their respective types
            // (or at least make the individual lists created in getList() accessible/stored)
            for (String skillName:skillNames)
                for (Skill x:SKILLS)
                    if (x.getName().equals(skillName))
                        skills.add(x);

            //TODO: Falchion is added to the normal falchion users' base kits multiple times


            //Skill[] arr = new Skill[skills.size()];
            //heroSkills.put(name, skills.toArray(arr));
            heroSkills.put(name, skills);

            //the name was hit in the while loop, and must be the name for the next hero (this is pretty stupid)
            name = skill;
        }

        System.out.println("finished processing base skills.");
        return heroSkills;
    }



    public static void main(String[] args) {
        updateCache();
        FEHeroesCache f = new FEHeroesCache(EXCLUSIVE_SKILLS, SKILLS_SUBDIR);

        System.out.println(f.getTables());
    }
}
