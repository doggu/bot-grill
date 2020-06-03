package feh.characters.skills;

import com.google.gson.stream.JsonReader;
import feh.FEHeroesCache;
import feh.characters.hero.WeaponClass;
import feh.characters.skills.skillTypes.Skill;
import feh.characters.skills.skillTypes.constructionSite.IncompleteDataException;
import feh.characters.skills.skillTypes.constructionSite.SkillConstructor;
import main.BotMain;
import utilities.Stopwatch;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SkillDatabase extends Database<Skill> {
    public static SkillDatabase DATABASE;
    public static ArrayList<Skill> SKILLS;
    public static HashMap<String, ArrayList<Skill>> HERO_SKILLS;

    private static final String
            SKILLS_SUBDIR = "/skills/";


    //the only one i need to keep
    // (though i could just add it manually really)
    // nah this is more fun
//    private static final String
//            AOE_PATTERNS_PATH =
//                    "https://feheroes.gamepedia.com/Area-of-effect_Specials";
    private static final String
            /*
            sites this new query renders obsolete:
                slot-specific tables
                exclusive skills table
                inheritable skills table
                upgradable weapons table
                evolving weapons table
             */
            SKILLS_PATH = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=Skills" +
                    "&&fields=" +
                            "_pageName%3DPage%2C" +
                            "GroupName%3DGroupName%2C" +
                            "Name%3DName%2C" +
                            "WikiName%3DWikiName%2C" +
                            "TagID%3DTagID%2C" +
                            "Scategory%3DScategory%2C" +
                            "UseRange%3DUseRange%2C" +
                            "Icon%3DIcon%2C" +
                            "RefinePath%3DRefinePath%2C" +
                            "Description%3DDescription%2C" +
                            "Required__full%3DRequired%2C" +
                            "Next%3DNext%2C" +
                            "PromotionRarity%3DPromotionRarity%2C" +
                            "PromotionTier%3DPromotionTier%2C" +
                            "Exclusive%3DExclusive%2C" +
                            "SP%3DSP%2C" +
                            "CanUseMove__full%3DCanUseMove%2C" +
                            "CanUseWeapon__full%3DCanUseWeapon%2C" +
                            "Might%3DMight%2C" +
                            "StatModifiers%3DStatModifiers%2C" +
                            "Cooldown%3DCooldown%2C" +
                            "WeaponEffectiveness%3DWeaponEffectiveness%2C" +
                            "SkillBuildCost%3DSkillBuildCost%2C" +
                            "Properties__full%3DProperties" +
                    "&&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60GroupName%60%2C" +
                            "%60Name%60%2C" +
                            "%60WikiName%60%2C" +
                            "%60TagID%60" +
                    "&limit=5000" +
                    "&format=json",
            /*
            sites this new query renders obsolete:
                hero skills table
                i'm gonna stop writing this shit and actually start implementing
             */
            //todo: i need to query this twice, limit 4096, and offset 4096 for
            // the second time
            HERO_SKILLS_PATH_ONE = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=UnitSkills" +
                    "&&fields=" +
                            "_pageName%3DPage%2C" +
                            "WikiName%3DWikiName%2C" +
                            "skill%3Dskill%2C" +
                            "skillPos%3DskillPos%2C" +
                            "defaultRarity%3DdefaultRarity%2C" +
                            "unlockRarity%3DunlockRarity" +
                    "&&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60WikiName%60%2C" +
                            "%60skill%60%2C" +
                            "%60skillPos%60%2C" +
                            "%60defaultRarity%60" +
                    "&limit=4096" +
                    "&format=json",

            HERO_SKILLS_PATH_TWO = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=UnitSkills" +
                    "&&fields=" +
                            "_pageName%3DPage%2C" +
                            "WikiName%3DWikiName%2C" +
                            "skill%3Dskill%2C" +
                            "skillPos%3DskillPos%2C" +
                            "defaultRarity%3DdefaultRarity%2C" +
                            "unlockRarity%3DunlockRarity" +
                    "&&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60WikiName%60%2C" +
                            "%60skill%60%2C" +
                            "%60skillPos%60%2C" +
                            "%60defaultRarity%60" +
                    "&limit=4096" +
                    "&offset=4096" +
                    "&format=json";


    private static final FEHeroesCache
            SKILLS_FILE,
            HERO_SKILLS_FILE_ONE,
            HERO_SKILLS_FILE_TWO;

    private static final FEHeroesCache[] SKILL_FILES;

    static {
        SKILLS_FILE =
                new FEHeroesCache(SKILLS_PATH, SKILLS_SUBDIR);
        HERO_SKILLS_FILE_ONE =
                new FEHeroesCache(HERO_SKILLS_PATH_ONE, SKILLS_SUBDIR);
        HERO_SKILLS_FILE_TWO =
                new FEHeroesCache(HERO_SKILLS_PATH_TWO, SKILLS_SUBDIR);
//        AOE_PATTERNS_FILE =
//                new FEHeroesCache(AOE_PATTERNS_PATH, SKILLS_SUBDIR);

        SKILL_FILES = new FEHeroesCache[]{
                SKILLS_FILE,
                HERO_SKILLS_FILE_ONE,
                HERO_SKILLS_FILE_TWO,
        };

        DATABASE = new SkillDatabase();
        SKILLS = DATABASE.getList();
        try {
            HERO_SKILLS = DATABASE.getHeroSkills();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected WebCache[] getOnlineResources() {
        return SKILL_FILES;
    }


    protected ArrayList<Skill> getList() {
        System.out.println("processing skills... ");

        Stopwatch pTime = new Stopwatch();
        pTime.start();

        ArrayList<Skill> allSkills;

        try {
             allSkills = getSkills();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        if (BotMain.DEBUG) System.out.println(pTime.presentResult());

        return allSkills;
    }

    private ArrayList<Skill> getSkills() throws IOException {
        ArrayList<Skill> skills = new ArrayList<>();

        JsonReader reader = new JsonReader(new FileReader(SKILLS_FILE));
        reader.beginArray();
        while (reader.hasNext()) {
//            System.out.println("ReADInG sOmE stATs");

            //open new skill
            reader.beginObject();

            //page
            reader.nextName();
            String page = reader.nextString();

            //groupName
            reader.nextName();
            String groupName = reader.nextString();

            //name
            reader.nextName();
            String name = reader.nextString();

            //wikiname
            reader.nextName();
            String wikiname = reader.nextString();

            //TagID
            reader.nextName();
            String tagID = reader.nextString();

            //skill category
            reader.nextName();
            String category = reader.nextString();

            //todo: i think i need to branch out from here
            // nevermind it just doesn't fill in anything
            //useRange
            reader.nextName();
            String useRange = reader.nextString();

            //icon
            reader.nextName();
            String icon = reader.nextString();

            //refinePath
            reader.nextName();
            String refinePath = reader.nextString();

            //description
            reader.nextName();
            String description = reader.nextString();

            //required
            reader.nextName();
            reader.beginArray();
            ArrayList<String> prereqs = new ArrayList<>();
            while (reader.hasNext()) {
                String prereq = reader.nextString();
                prereqs.add(prereq);
            }
            reader.endArray();

            //next
            //todo: this probably becomes an array
            reader.nextName();
            String next = reader.nextString();

            //promotion rarity
            reader.nextName();
            String promotionRarity = reader.nextString();

            //promotion tier
            reader.nextName();
            String promotionTier = reader.nextString();

            //exclusive
            reader.nextName();
            boolean exclusive = reader.nextInt()!=0;

            //SP
            reader.nextName();
            int sp = reader.nextInt();

            //can use movement classes
            reader.nextName();
            reader.beginArray();
            ArrayList<String> canUseMove = new ArrayList<>();
            while (reader.hasNext()) {
                String canUse = reader.nextString();
                canUseMove.add(canUse.trim());
            }
            reader.endArray();

            //can use weapon classes
            reader.nextName();
            reader.beginArray();
            ArrayList<String> canUseWeapon = new ArrayList<>();
            while (reader.hasNext()) {
                String canUse = reader.nextString();
                canUseWeapon.add(canUse.trim());
            }
            reader.endArray();

            //might
            reader.nextName();
            String mt = reader.nextString();

            //stat modifiers
            reader.nextName();
            String statModifiers = reader.nextString();

            //cooldown
            reader.nextName();
            String cd = reader.nextString();

            //weapon effectiveness
            //note: comma-separated (e.g. "armored,cavalry")
            reader.nextName();
            String eff = reader.nextString();

            //"SkillBuildCost"
            reader.nextName();
            String skillBuildCost = reader.nextString();

            //"Properties"
            reader.nextName();
            reader.beginArray();
            while (reader.hasNext()) {
                reader.nextString();
            }
            reader.endArray();




            reader.endObject();

            Skill skill;

            URL link = new URL("https://feheroes.gamepedia.com/"+groupName.replace(' ','_'));
            URL image_link = new URL("https://feheroes.gamepedia.com/"+groupName.replace(' ','_'));

            SkillConstructor s = new SkillConstructor();

            s.setName(wikiname);
            s.setDescription(description);
            //todo: i got no idea what to do about this
            s.setIcon(image_link);
            s.setLink(link);
            s.setCost(sp);
            s.setExclusive(exclusive);

            try {
                switch (category) {
                    case "weapon":
                        WeaponClass weaponType =
                                WeaponClass.getClass(canUseWeapon.get(0));
                        s.setType(weaponType);
                        s.setMight(Integer.parseInt(mt));
                        s.setRange(Integer.parseInt(useRange));
                        skill = s.generateWeapon();
                        break;
                    case "assist":
                        s.setRange(Integer.parseInt(useRange));
                        skill = s.generateAssist();
                        break;
                    case "special":
                        s.setCooldown(Integer.parseInt(cd));
                        skill = s.generateSpecial();
                        break;
                    case "passivea":
                        skill = s.generatePassiveA();
                        break;
                    case "passiveb":
                        skill = s.generatePassiveB();
                        break;
                    case "passivec":
                        skill = s.generatePassiveC();
                        break;
                    case "sacredseal":
                        skill = s.generatePassiveS();
                        break;
                    default:
                        System.out.println("unknown skill type: "+category);
                        return null;
                }
            } catch (IncompleteDataException ide) {
                ide.printStackTrace();
                return null;
            }

            skills.add(skill);
        }

        return skills;
    }

    private HashMap<String, ArrayList<Skill>> getHeroSkills()
            throws IOException {
        HashMap<String, ArrayList<Skill>>
                list1 = getHeroSkills(HERO_SKILLS_FILE_ONE),
                list2 = getHeroSkills(HERO_SKILLS_FILE_TWO);

        for (String hero:list2.keySet()) {
            ArrayList<Skill> skills =
                    list1.computeIfAbsent(hero, k -> new ArrayList<>());
            skills.addAll(list2.get(hero));
        }

        return list1;
    }

    private HashMap<String, ArrayList<Skill>> getHeroSkills(FEHeroesCache file)
            throws IOException {
        HashMap<String, ArrayList<Skill>> heroSkills = new HashMap<>();

        JsonReader reader =
                new JsonReader(new FileReader(file));
        reader.beginArray();

        while (reader.hasNext()) {
            //open new entry
            reader.beginObject();

            //page
            reader.nextName();
            String page = reader.nextString();

            //wikiName
            reader.nextName();
            String wikiName = reader.nextString();

            //skill
            reader.nextName();
            String skill = reader.nextString();

            //skillPos
            reader.nextName();
            String skillPos = reader.nextString();

            //defaultRarity
            reader.nextName();
            String defaultRarity = reader.nextString();

            //unlockRarity
            reader.nextName();
            String unlockRarity = reader.nextString();

            reader.endObject();

            Skill skillObj = DATABASE.find(skill);
            ArrayList<Skill> skills = heroSkills
                    .computeIfAbsent(page, k -> new ArrayList<>());
            skills.add(skillObj);
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
        System.out.println(SKILLS);
        while (input.hasNextLine()) {
            Skill s = DATABASE.find(input.nextLine());

            for (String hero:HERO_SKILLS.keySet()) {
                if (HERO_SKILLS.get(hero).contains(s)) {
                    System.out.println(hero+"\n\t"+HERO_SKILLS.get(hero));
                }
            }
        }

        input.close();
    }
}
