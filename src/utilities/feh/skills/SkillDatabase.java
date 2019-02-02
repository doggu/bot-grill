package utilities.feh.skills;

import utilities.WebScalper;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SkillDatabase extends WebScalper {
    public static ArrayList<Skill> SKILLS = getList();

    private static final String
            WEAPONS = "https://feheroes.gamepedia.com/Weapons",
            ASSISTS = "https://feheroes.gamepedia.com/Assists",
            SPECIALS = "https://feheroes.gamepedia.com/Specials",
            PASSIVES = "https://feheroes.gamepedia.com/Passives",
            SACRED_SEALS_ALL = "https://feheroes.gamepedia.com/Sacred_Seals",
            SKILL_CHAINS_4_STARS = "https://feheroes.gamepedia.com/Skill_Chains_4_Stars_List",
            SKILL_CHAINS_5_STARS = "https://feheroes.gamepedia.com/Skill_Chains_5_Stars_List",
            LIST_OF_UPGRADABLE_WEAPONS = "https://feheroes.gamepedia.com/List_of_upgradable_weapons";



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



        System.out.println("finished processing skills");
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
        weaponType.put(0, "Red Sword");
        weaponType.put(1, "Red Tome");
        weaponType.put(2, "Blue Lance");
        weaponType.put(3, "Blue Tome");
        weaponType.put(4, "Green Axe");
        weaponType.put(5, "Green Tome");
        weaponType.put(6, "Staff");
        weaponType.put(7, "Beast");
        weaponType.put(8, "Breath");
        weaponType.put(9, "Bow");
        weaponType.put(10, "Dagger");

        //TODO: this code currently cannot tell what kind of weapon it's reading
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
                String type = weaponType.get(weaponTables.indexOf(table));

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
            boolean exclusive = cost>400;   //TODO: find an actual source for exclusivity
                                            //Skill chain lists are a possibility
                                            //NEW NEWS: https://feheroes.gamepedia.com/Exclusive_skills

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
            boolean exclusive = cost>400; //TODO: another exclusivity issue

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

        if (passiveTables.size()!=4) {
            System.out.println("an unsolicited table has appeared in passives");
            throw new Error();
        }

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
                if (table.size()>0) data.add((ArrayList<String>) table.clone());
                table = new ArrayList<>();
            }
        }

        return data;
    }



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String line;
        while (!(line = input.nextLine()).equals("quit"))
            for (Skill x:SKILLS)
                if (x.getName().equalsIgnoreCase(line))
                    System.out.println(x);
    }
}
