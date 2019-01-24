package utilities.fehUnits.skills;

import utilities.WebScalper;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class SkillDatabase extends WebScalper {
    ArrayList<Skill> SKILLS = getList();

    private static final String
            WEAPONS = "https://feheroes.gamepedia.com/Weapons",
            ASSISTS = "https://feheroes.gamepedia.com/Assists",
            SPECIALS = "https://feheroes.gamepedia.com/Specials",
            PASSIVES = "https://feheroes.gamepedia.com/Passives",
            SACRED_SEALS_ALL = "https://feheroes.gamepedia.com/Sacred_Seals",
            SKILL_CHAINS_4_STARS = "https://feheroes.gamepedia.com/Skill_Chains_4_Stars_List",
            SKILL_CHAINS_5_STARS = "https://feheroes.gamepedia.com/Skill_Chains_5_Stars_List";

    private static ArrayList<Skill> getList() {
        ArrayList<Skill> allSkills = new ArrayList<>();


        ArrayList<Weapon> weapons = processWeapons();
        //ArrayList<Assist> assists = processAssists();
        //ArrayList<Special> specials = processSpecials();
        //ArrayList<Passive> passives = processPassives();

        /*
        maybe? (could also be done in special function)

        ArrayList<PassiveA> Apassives = new ArrayList<>();
        ArrayList<PassiveB> Bpassives = new ArrayList<>();
        ArrayList<PassiveC> Cpassives = new ArrayList<>();
        ArrayList<PassiveS> Spassives = new ArrayList<>();
         */



        allSkills.addAll(weapons);
        //allSkills.addAll(assists);
        //allSkills.addAll(specials);
        //allSkills.addAll(passives);



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
                x.subList(0,5).remove(0);

        for (ArrayList<String> x:weaponTables) System.out.println(x);



        Weapon g = new Weapon("name","b",3,true,1,1);

        return weapons;
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
        ArrayList<Skill> skills = getList();

        for (Skill x:skills) {
            System.out.println(x);
        }
    }
}
