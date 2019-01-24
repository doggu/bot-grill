package utilities.fehUnits.skills;

import utilities.WebScalper;

import java.io.BufferedReader;
import java.io.IOException;
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



        ArrayList<Weapon> weapons = new ArrayList<>();



        ArrayList<Assist> assists = new ArrayList<>();



        ArrayList<Special> specials = new ArrayList<>();



        ArrayList<Passive> passives = new ArrayList<>();

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



        return allSkills;
    }

    public static void main(String[] args) {
        BufferedReader testReader;
        try {
            testReader = readWebsite(WEAPONS);

            String line;
            boolean print = false;
            while ((line = testReader.readLine()) != null) {
                if (line.contains("<table class=\"cargoTable sortable\">")) {
                    ArrayList<String> items = getItems(line.chars());
                    for (String x:items) System.out.println(x);
                }
            }
        } catch (IOException g) { System.out.println("testReader had an issue"); throw new Error(); }
    }
}
