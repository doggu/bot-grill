package feh.skills;

import utilities.WebScalper;
import feh.heroes.character.WeaponClass;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SkillDatabase extends WebScalper {
    public static ArrayList<Skill> SKILLS = getList();
    public static HashMap<String, ArrayList<Skill>> HERO_SKILLS = getHeroSkills();

    private static final String
            WEAPONS = "https://feheroes.gamepedia.com/Weapons",
            ASSISTS = "https://feheroes.gamepedia.com/Assists",
            SPECIALS = "https://feheroes.gamepedia.com/Specials",
            PASSIVES = "https://feheroes.gamepedia.com/Passives",
            //SACRED_SEALS_ALL = "https://feheroes.gamepedia.com/Sacred_Seals",

            EXCLUSIVE_SKILLS = "https://feheroes.gamepedia.com/Exclusive_skills",

            //SKILL_CHAINS_4_STARS = "https://feheroes.gamepedia.com/Skill_Chains_4_Stars_List",
            //SKILL_CHAINS_5_STARS = "https://feheroes.gamepedia.com/Skill_Chains_5_Stars_List",
            //LIST_OF_UPGRADABLE_WEAPONS = "https://feheroes.gamepedia.com/List_of_upgradable_weapons",

            HERO_BASE_SKILLS = "https://feheroes.gamepedia.com/Hero_skills_table";

    private static final String
            SWORDS_FILEPATH = "./src/utilities/feh/webCache/weapons/swords.txt",
            REDTOMES_FILEPATH = "./src/utilities/feh/webCache/weapons/redTomes.txt",
            LANCES_FILEPATH = "./src/utilities/feh/webCache/weapons/lances.txt",
            BLUETOMES_FILEPATH = "./src/utilities/feh/webCache/weapons/blueTomes.txt",
            AXES_FILEPATH = "./src/utilities/feh/webCache/weapons/axes.txt",
            GREENTOMES_FILEPATH = "./src/utilities/feh/webCache/weapons/greenTomes.txt",
            STAFFS_FILEPATH = "./src/utilities/feh/webCache/weapons/staffs.txt",
            BEASTS_FILEPATH = "./src/utilities/feh/webCache/weapons/beasts.txt",
            BREATHS_FILEPATH = "./src/utilities/feh/webCache/weapons/breaths.txt",
            BOWS_FILEPATH = "./src/utilities/feh/webCache/weapons/bows.txt",
            DAGGERS_FILEPATH = "./src/utilities/feh/webCache/weapons/daggers.txt",
            EXCLUSIVE_WEAPONS_FILEPATH = "./src/utilities/feh/webCache/exclusiveWeapons.txt";



    private static void updateCache() {
        ArrayList<ArrayList<String>> weaponTables;
        BufferedReader weaponData;
        try {
            weaponData = readWebsite(WEAPONS);
            weaponTables = getTables(weaponData);
        } catch (IOException g) { throw new Error("weapons had an issue"); }



        HashMap<Integer, String> weaponType = new HashMap<>();
        weaponType.put(0, SWORDS_FILEPATH);
        weaponType.put(1, REDTOMES_FILEPATH);
        weaponType.put(2, LANCES_FILEPATH);
        weaponType.put(3, BLUETOMES_FILEPATH);
        weaponType.put(4, AXES_FILEPATH);
        weaponType.put(5, GREENTOMES_FILEPATH);
        weaponType.put(6, STAFFS_FILEPATH);
        weaponType.put(7, BEASTS_FILEPATH);
        weaponType.put(8, BREATHS_FILEPATH);
        weaponType.put(9, BOWS_FILEPATH);
        weaponType.put(10, DAGGERS_FILEPATH);

        //remove initial junk
        for (ArrayList<String> x:weaponTables)
            x.subList(0,6).clear();

        for (int i=0; i<weaponTables.size(); i++) {
            Iterator<String> table = weaponTables.get(i).iterator();

            File file = new File(weaponType.get(i));

            try {
                if (file.createNewFile())
                    System.out.println("created new file at "+weaponType.get(i));
            } catch (IOException f) {
                File weaponsFolder = new File("./src/utilities/feh/webCache/weapons");
                if (weaponsFolder.mkdir())
                    System.out.println("created the weapons folder in the cache.");
                else throw new Error("could not create the weapons folder");

                try {
                    if (file.createNewFile())
                        System.out.println("created new file at "+weaponType.get(i));
                } catch (IOException g) {
                    throw new Error("was not able to create a file");
                }
            }

            FileWriter writer;

            try {
                writer = new FileWriter(file);
            } catch (IOException f) {
                throw new Error("table "+i+" didnt exist or something");
            }
            try {
                if (table.hasNext()) writer.write(table.next());
                while (table.hasNext()) {
                    writer.write('\n');
                    writer.write(table.next());
                }
                writer.close();
            } catch (IOException f) {
                throw new Error("couldnt write for table "+i+"!");
            }
        }



        ArrayList<ArrayList<String>> exclusiveTables;
        BufferedReader exclusiveData;
        try {
            exclusiveData = readWebsite(EXCLUSIVE_SKILLS);
            exclusiveTables = getTables(exclusiveData);
        } catch (IOException g) { throw new Error("exclusiveTable had an issue"); }

        for (ArrayList<String> x:weaponTables)
            x.subList(0,3).clear();

        File file = new File(EXCLUSIVE_WEAPONS_FILEPATH);

        for (int i=0; i<exclusiveTables.size(); i++) {
            Iterator<String> table = exclusiveTables.get(i).iterator();

            try {
                if (file.createNewFile()) {
                    System.out.println("created new file at "+weaponType.get(i));
                }
            } catch (IOException f) {
                System.out.println("was not able to create a file");
            }

            FileWriter writer;

            try {
                writer = new FileWriter(file);
            } catch (IOException f) {
                throw new Error("table "+i+" didnt exist or something");
            }
            try {
                while (table.hasNext()) {
                    writer.write(table.next()+'\n');
                }
                writer.close();
            } catch (IOException f) {
                throw new Error("couldnt write for table "+i+"!");
            }
        }
    }



    private static ArrayList<Skill> getList() {
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
        System.out.print("processing weapons... ");
        File weaponsFolder = new File("./src/utilities/feh/webCache/weapons");
        File[] weaponTables;
        try {
            weaponTables = weaponsFolder.listFiles();
        } catch (NullPointerException f) {
            throw new Error();
        }

        if (weaponTables==null||weaponTables.length==0) {
            updateCache();
            return processWeapons();
        }



        ArrayList<Weapon> weapons = new ArrayList<>();

        HashMap<String, String> weaponType = new HashMap<>();
        weaponType.put("axes.txt", "Sword");
        weaponType.put("beasts.txt", "Red Tome");
        weaponType.put("blueTomes.txt", "Lance");
        weaponType.put("bows.txt", "Blue Tome");
        weaponType.put("breaths.txt", "Axe");
        weaponType.put("daggers.txt", "Green Tome");
        weaponType.put("greenTomes.txt", "Staff");
        weaponType.put("lances.txt", "Beast");
        weaponType.put("redTomes.txt", "Breath");
        weaponType.put("staffs.txt", "Bow");
        weaponType.put("swords.txt", "Dagger");

        for (File file:weaponTables) {
            System.out.print(file.getName()+"... ");
            Scanner table;
            try {
                table = new Scanner(file);
            } catch (FileNotFoundException f) {
                //throw new Error("a file could not be read");
                updateCache();
                return processWeapons();
            }



            Weapon x;
            while (table.hasNextLine()) {
                String name = table.nextLine();
                int might = Integer.parseInt(table.nextLine());
                int range = Integer.parseInt(table.nextLine()); //technically the same for any given table but w/e
                StringBuilder desc = new StringBuilder();
                int cost = -1;
                while (cost<0) {
                    String line = table.nextLine();
                    try {
                        cost = Integer.parseInt(line);
                    } catch (NumberFormatException g) {
                        if (desc.length()>0) desc.append(" ");
                        desc.append(line);
                    }
                }
                String description = desc.toString();
                boolean exclusive = table.nextLine().equals("Yes"); //EXCLUSIVE_WEAPONS.contains(name);

                String typeStr = weaponType.get(file.getName());
                if (typeStr==null)
                    throw new Error("an unsolicited file was found in the weapons folder: "+file.getName());
                WeaponClass type = WeaponClass.getType(typeStr);

                x = new Weapon(name, description, cost, exclusive, might, range, type);
                weapons.add(x);
            }
        }

        System.out.println("done!");
        return weapons;
    }
    private static ArrayList<Assist> processAssists() {
        System.out.println("processing assists...");
        ArrayList<String> assistTable;
        BufferedReader assistData;
        try {
            assistData = readWebsite(ASSISTS);
            assistTable = getTables(assistData).get(0);
        } catch (IOException g) { System.out.println("assists had an issue"); throw new Error(); }
        
        
        
        ArrayList<Assist> assists = new ArrayList<>();

        assistTable.subList(0,4).clear(); //*grumble grumble* stupid indexing (this line removes 4 items, not 5)
        Iterator<String> data = assistTable.iterator();

        Assist x;
        while (data.hasNext()) {
            String name = data.next();
            StringBuilder desc = new StringBuilder();
            int cost = -1;
            while (cost<0) {
                String line = data.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            int range = Integer.parseInt(data.next());
            boolean exclusive = isExclusive(name);

            x = new Assist(name, description, cost, exclusive, range);
            assists.add(x);
        }

        return assists;
    }
    private static ArrayList<Special> processSpecials() {
        System.out.println("processing specials...");
        ArrayList<String> specialTable;
        BufferedReader specialData;
        try {
            specialData = readWebsite(SPECIALS);
            specialTable = getTables(specialData).get(0);
        } catch (IOException g) { System.out.println("specials had an issue"); throw new Error(); }
        
        
        
        ArrayList<Special> specials = new ArrayList<>();
        Iterator<String> data = specialTable.iterator();

        Special x;
        while (data.hasNext()) {
            String name = data.next();
            StringBuilder desc = new StringBuilder();
            int cost = -1;
            while (cost<0) {
                String line = data.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            int cooldown = Integer.parseInt(data.next());
            boolean exclusive = isExclusive(name);

            x = new Special(name, description, cost, exclusive, cooldown);
            specials.add(x);
        }

        return specials;
    }
    private static ArrayList<Passive> processPassives() {
        System.out.print("processing passives... ");
        ArrayList<ArrayList<String>> passiveTables;
        BufferedReader passiveData;
        try {
            passiveData = readWebsite(PASSIVES);
            passiveTables = getTables(passiveData);
        } catch (IOException g) {
            System.out.println("passives had an issue");
            throw new Error();
        }


        ArrayList<Passive> passives = new ArrayList<>();

        for (ArrayList<String> x : passiveTables)
            x.subList(0, 5).clear();

        if (passiveTables.size() < 4) {
            System.out.println("a table has gone missing from the passives website");
            throw new Error();
        } else if (passiveTables.size() > 4) {
            System.out.println("an unsolicited table has appeared in passives");
            throw new Error();
        } //else it's good dawg



        Passive x;

        for (int i=0; i<passiveTables.size(); i++) {
            System.out.print("table "+(i+1)+"... ");
            Iterator<String> iterator = passiveTables.get(i).iterator();
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

    private static final ArrayList<String> EXCLUSIVE = getExclusiveList();

    private static ArrayList<String> getExclusiveList() {
        //System.out.println("creating exclusive list...");
        File file;
        try {
            file = new File(EXCLUSIVE_WEAPONS_FILEPATH);
        } catch (NullPointerException f) { updateCache(); return getExclusiveList(); }
        Scanner lines;
        try {
            lines = new Scanner(file);
        } catch (IOException f) {
            updateCache(); return getExclusiveList();
        }

        ArrayList<String> list = new ArrayList<>();



        if (!lines.hasNextLine()) { updateCache(); return getExclusiveList(); }



        while (lines.hasNextLine()) {
            String line = lines.nextLine();
            if (!line.contains("+")) list.add(line);
            while (lines.hasNextLine()) {
                line = lines.nextLine();
                try {
                    Integer.parseInt(line);
                    break;
                } catch(NumberFormatException f) {
                    //continue
                }
            }
        }

        return list;
    }

    private static boolean isExclusive(String name) {
        for (String x:getExclusiveList()) if (x.equals(name)) return true;
        return false;
    }



    private static HashMap<String, ArrayList<Skill>> getHeroSkills() {ArrayList<String> baseSkillTable;
        BufferedReader skillData;
        try {
            skillData = readWebsite(HERO_BASE_SKILLS);
            baseSkillTable = getTable(skillData);
        } catch (IOException g) { System.out.println("hero base skills had an issue"); throw new Error(); }



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



    //TODO: stop ignoring tag data entirely (it contains links and other useful information)
    // ugh but it's really efficient
    private static ArrayList<ArrayList<String>> getTables(BufferedReader input) throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> table = new ArrayList<>();
        String line;
        boolean print = false;

        while ((line = input.readLine()) != null) {      //TODO: NOMERGE broke shit
            if (line.contains("<table class=\"cargoTable noMerge sortable\">"))
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
    private static ArrayList<String> getTable(BufferedReader input) throws IOException {
        ArrayList<String> table = new ArrayList<>();

        String line;
        while ((line = input.readLine()) != null) {
            if (line.contains("<table class=\"wikitable sortable"))
                table = getItems(line.chars());
        }

        return table;
    }
}
