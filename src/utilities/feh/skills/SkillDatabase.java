package utilities.feh.skills;

import utilities.WebScalper;
import utilities.feh.heroes.character.MovementClass;
import utilities.feh.heroes.character.WeaponClass;

import java.io.BufferedReader;
import java.io.IOException;
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
            SACRED_SEALS_ALL = "https://feheroes.gamepedia.com/Sacred_Seals",

            SKILL_CHAINS_4_STARS = "https://feheroes.gamepedia.com/Skill_Chains_4_Stars_List",
            SKILL_CHAINS_5_STARS = "https://feheroes.gamepedia.com/Skill_Chains_5_Stars_List",
            LIST_OF_UPGRADABLE_WEAPONS = "https://feheroes.gamepedia.com/List_of_upgradable_weapons",

            HERO_BASE_SKILLS = "https://feheroes.gamepedia.com/Hero_skills_table";



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
        ArrayList<ArrayList<String>> weaponTables;
        BufferedReader weaponData;
        try {
            weaponData = readWebsite(WEAPONS);
            weaponTables = getTables(weaponData);
        } catch (IOException g) { System.out.println("weapons had an issue"); throw new Error(); }



        ArrayList<Weapon> weapons = new ArrayList<>();

        //remove initial junk
        for (ArrayList<String> x:weaponTables)
            x.subList(0,6).clear(); //*grumble grumble* stupid indexing (this line removes 6 items, not 7)

        HashMap<Integer, String> weaponType = new HashMap<>();
        weaponType.put(0, "Sword");
        weaponType.put(1, "Red Tome");
        weaponType.put(2, "Lance");
        weaponType.put(3, "Blue Tome");
        weaponType.put(4, "Axe");
        weaponType.put(5, "Green Tome");
        weaponType.put(6, "Staff");
        weaponType.put(7, "Beast");
        weaponType.put(8, "Breath");
        weaponType.put(9, "Bow");
        weaponType.put(10, "Dagger");

        for (ArrayList<String> table:weaponTables) {
            Iterator<String> list = table.iterator();
            Weapon x;
            while (list.hasNext()) {
                String name = list.next();
                int might = Integer.parseInt(list.next());
                int range = Integer.parseInt(list.next()); //technically the same for any given table but w/e
                StringBuilder desc = new StringBuilder();
                int cost = -1;
                while (cost<0) {
                    String line = list.next();
                    try {
                        cost = Integer.parseInt(line);
                    } catch (NumberFormatException g) {
                        if (desc.length()>0) desc.append(" ");
                        desc.append(line);
                    }
                }
                String description = desc.toString();
                boolean exclusive = "Yes".equals(list.next());
                String typeStr = weaponType.get(weaponTables.indexOf(table));
                WeaponClass type = WeaponClass.getType(typeStr);

                x = new Weapon(name, description, cost, exclusive, might, range, type);
                weapons.add(x);
            }
        }

        return weapons;
    }
    private static ArrayList<Assist> processAssists() {
        ArrayList<String> assistTable;
        BufferedReader assistData;
        try {
            assistData = readWebsite(ASSISTS);
            assistTable = getTables(assistData).get(0);
        } catch (IOException g) { System.out.println("assists had an issue"); throw new Error(); }
        
        
        
        ArrayList<Assist> assists = new ArrayList<>();

        assistTable.subList(0,4).clear(); //*grumble grumble* stupid indexing (this line removes 6 items, not 7)
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
            boolean exclusive = cost>400;   //TODO: utilize https://feheroes.gamepedia.com/Exclusive_skills efficiently

            x = new Assist(name, description, cost, exclusive, range);
            assists.add(x);
        }

        return assists;
    }
    private static ArrayList<Special> processSpecials() {
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
            boolean exclusive = cost>400; //TODO: utilize https://feheroes.gamepedia.com/Exclusive_skills efficiently

            x = new Special(name, description, cost, exclusive, cooldown);
            specials.add(x);
        }

        return specials;
    }
    private static ArrayList<Passive> processPassives() {
        ArrayList<ArrayList<String>> passiveTables;
        BufferedReader passiveData;
        try {
            passiveData = readWebsite(PASSIVES);
            passiveTables = getTables(passiveData);
        } catch (IOException g) { System.out.println("passives had an issue"); throw new Error(); }



        ArrayList<Passive> passives = new ArrayList<>();

        for (ArrayList<String> x:passiveTables)
            x.subList(0,5).clear();

        if (passiveTables.size()<4) {
            System.out.println("a table has gone missing from the passives website");
            throw new Error();
        } else if (passiveTables.size()>4) {
            System.out.println("an unsolicited table has appeared in passives");
            throw new Error();
        } //else it's good dawg

        Iterator<String>
                passiveA = passiveTables.get(0).iterator(),
                passiveB = passiveTables.get(1).iterator(),
                passiveC = passiveTables.get(2).iterator(),
                passiveS = passiveTables.get(3).iterator();

        Passive x;
        while (passiveA.hasNext()) {
            String name = passiveA.next();
            int cost = -1;
            StringBuilder desc = new StringBuilder();
            //while cost is not defined, the description is being presented
            while (cost<0) {
                String line = passiveA.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            boolean exclusive = "Yes".equals(passiveA.next());

            x = new PassiveA(name, description, cost, exclusive);
            passives.add(x);
        }
        while (passiveB.hasNext()) {
            String name = passiveB.next();
            StringBuilder desc = new StringBuilder();
            int cost = -1;
            while (cost<0) {
                String line = passiveB.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            boolean exclusive = "Yes".equals(passiveB.next());

            x = new PassiveB(name, description, cost, exclusive);
            passives.add(x);
        }
        while (passiveC.hasNext()) {
            String name = passiveC.next();
            StringBuilder desc = new StringBuilder();
            int cost = -1;
            while (cost<0) {
                String line = passiveC.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            boolean exclusive = "Yes".equals(passiveC.next());

            x = new PassiveC(name, description, cost, exclusive);
            passives.add(x);
        }
        while (passiveS.hasNext()) {
            String name = passiveS.next();
            StringBuilder desc = new StringBuilder();
            int cost = -1;
            while (cost<0) {
                String line = passiveS.next();
                try {
                    cost = Integer.parseInt(line);
                } catch (NumberFormatException g) {
                    if (desc.length()>0) desc.append(" ");
                    desc.append(line);
                }
            }
            String description = desc.toString();
            boolean exclusive = "Yes".equals(passiveS.next());

            x = new PassiveS(name, description, cost, exclusive);
            passives.add(x);
        }



        return passives;
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
            // (or at least make the individual lists crated in getList() accessible/stored)
            for (String skillName:skillNames)
                for (Skill x:SKILLS)
                    if (x.getName().equals(skillName))
                        skills.add(x);

            //Skill[] arr = new Skill[skills.size()];
            //heroSkills.put(name, skills.toArray(arr));
            heroSkills.put(name, skills);

            //the name was hit in the while loop, and must be the name for the next hero (this is pretty stupid)
            name = skill;
        }

        System.out.println("finished processing base skills.");
        return heroSkills;
    }



    private static ArrayList<ArrayList<String>> getTables(BufferedReader input) throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> table = new ArrayList<>();
        String line;
        boolean print = false;

        while ((line = input.readLine()) != null) {
            if (line.contains("<table class=\"cargoTable sortable\">"))
                print = true;

            if (print) {
                ArrayList<String> datum = getItems(line.chars());
                for (int i=0; i<datum.size(); i++) datum.set(i, datum.get(i).trim());
                if (datum.size()>0) table.addAll(datum);
            }

            if (line.contains("</tbody></table>")) {
                print = false;
                                               //apparently i DONT need to clone it
                if (table.size()>0) data.add(/*(ArrayList<String>)*/ table/*.clone()*/);
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


    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        String input;
        while ((input = console.nextLine())!=null) {
            System.out.println(HERO_SKILLS.get(input));
        }

        console.close();
    }
}
