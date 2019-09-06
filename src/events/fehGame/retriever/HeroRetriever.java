package events.fehGame.retriever;

import events.commands.Command;
import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;
import feh.heroes.unit.Unit;
import feh.heroes.skills.SkillDatabase;
import feh.heroes.skills.analysis.StatModifier;
import feh.heroes.skills.skillTypes.PassiveA;
import feh.heroes.skills.skillTypes.PassiveS;
import feh.heroes.skills.skillTypes.Skill;
import feh.heroes.skills.skillTypes.Weapon;
import main.BotMain;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HeroRetriever extends Command {
    private void getUnits() {
        boolean newestOnly = false, oldestOnly = false;
        boolean lv1 = false;
        int rarity = 5;
        boolean getAll = true;

        //generates Unit
        int boon = -1;
        int bane = -1;
        int merges = 0;
        int dragonflowers = 0;
        char support = 'd';

        //generates FieldedUnit
        ArrayList<StatModifier> skills = new ArrayList<>();
        boolean useBaseKit = false;
        //todo: legendary/mystic boosts



        if (args[0].equalsIgnoreCase("getIVs")||
                args[0].equalsIgnoreCase("giv"))
            lv1 = true;

        ArrayList<Hero> candidates = new ArrayList<>();

        List<String> args = new ArrayList<>(Arrays.asList(this.args));

        //scalper finding parameter data
        //TODO: convert these to individual methods
        // or at least separate the searching and printing some day please thanks
        for (int i=1; i<args.size(); i++) {
            String x = args.get(i);
            //test for argument for lv1 vs lv40 stats
            if (x.indexOf("lv")==0) {
                switch (x) {
                    case "lv1":
                    case "lvl1":
                        lv1 = true;
                        break;
                    case "lv40":
                    case "lvl40":
                        lv1 = false;
                        break;
                    default:
                        sendMessage("sorry, i can only do lv1 or lv40 stats. "  +
                                "i'm not that good yet!");
                        break;
                }
                args.remove(i);
                i--;
                continue;
            }

            //test for boon/bane arguments and merges
            if (x.equals("getAll")) {
                getAll = true;
                args.remove(i);
                i--;
            } else if (x.contains("+")||x.contains("-")) {
                if (x.contains("+")) {
                    if (x.contains("df")) {
                        try {
                            dragonflowers = Integer.parseInt(x.substring(3));
                            getAll = false;
                        } catch (NumberFormatException g) {
                            log("issue getting dragonflower count from string \""+x+"\"");
                        }
                    } else {
                        //try for merge number; if not, it's probably a boon modifier
                        //or legendary marth's fucking name god damn it
                        try {
                            merges = Integer.parseInt(x.substring(1));
                            getAll = false;
                        } catch (NumberFormatException g) {
                            char boonP = x.charAt(x.indexOf('+') + 1);
                            switch (boonP) {
                                //res is the last stat in the array, so add to index for every stat before it
                                case 'r':
                                    boon++;
                                case 'd':
                                    boon++;
                                case 's':
                                    boon++;
                                case 'a':
                                    boon++;
                                case 'h':
                                    boon++;
                                    getAll = false;
                                    args.remove(x);
                                    i--;
                                default:
                                    break;
                            }
                        }
                    }
                }

                if (x.contains("-")) {
                    char baneP = x.charAt(x.indexOf('-') + 1);
                    switch (baneP) {
                        case 'r':
                            bane++;
                        case 'd':
                            bane++;
                        case 's':
                            bane++;
                        case 'a':
                            bane++;
                        case 'h':
                            bane++;
                            getAll = false;
                            args.remove(x);
                            i--;
                        default:
                            break;
                    }

                }
            }

            //test for rarity
            if (x.contains("*")) {
                try {
                    rarity = x.charAt(0)-'0';
                    args.remove(i);
                    i--;
                } catch (NumberFormatException g) {
                    //probably add more analysis here later on
                }
            }

            //test for support status (and skills cuz i'm disorganized)
            if (x.indexOf("w/")==0) {
                switch (x.substring(2).toLowerCase()) {
                    //this is the only case
                    case "ssc": support = 'c'; break;
                    case "ssb": support = 'b'; break;
                    case "ssa": support = 'a'; break;
                    case "sss": support = 's'; break;
                    case "basekit":
                        useBaseKit = true;
                        getAll = false;
                        break;
                    default:
                        if (x.charAt(2)=='\"') {
                            StringBuilder skillName = new StringBuilder(x.substring(3));

                            int j;
                            for (j=i+1; j<args.size(); j++) {
                                skillName.append(' ').append(args.get(j));
                                if (args.get(j).contains("\""))
                                    break;
                            }

                            if (skillName.indexOf("\"")>=0)
                                skillName.deleteCharAt(skillName.length()-1);

                            args.subList(i, j).clear();

                            Skill skill = SkillDatabase.DATABASE.find(skillName.toString());
                            if (skill instanceof StatModifier)
                                skills.add((StatModifier) skill);

                            if (skills.size()!=0)
                                getAll = false;
                        }
                }
            }

            //test for "new" keyword
            if (x.equalsIgnoreCase("new")) {
                newestOnly = true;
                args.remove(i);
                i--;
            } else if (x.equalsIgnoreCase("old")) {
                oldestOnly = true;
                args.remove(i);
                i--;
            }
        }

        //finding units with the correct name
        for (int i=1; i<args.size(); i++) {
            String x = args.get(i);
            //test for name/epithet arguments
            boolean epithetIncluded = false;

            if (x.contains(":")) {
                x = x.substring(0, x.indexOf(":"));
                //System.out.println(x);
                epithetIncluded = true;
            }
            //find HEROES of the correct name
            for (Hero c: UnitDatabase.HEROES) {
                if (c.getFullName().getName().equalsIgnoreCase(x))
                    candidates.add(c);
                if (c.getFullName().getName().toLowerCase().indexOf(x.toLowerCase())==0) {
                    try {
                        if (c.getFullName().getName()
                                .equalsIgnoreCase(x+" "+args.get(i+1))) {
                            candidates.add(c);
                        }
                    } catch (IndexOutOfBoundsException ioobe) {
                        //break;
                    }
                }
            }

            if (epithetIncluded) {
                boolean foundMatch = UnitDatabase.HEROES.size()==1;
                //find HEROES (from list of valid names) of the correct epithet

                i++;
                //System.out.println(args);
                while (!foundMatch&&i<args.size()) {
                    //System.out.println(args.get(i));
                    for (int j = 0; j < candidates.size(); j++) {
                        Hero c = candidates.get(j);
                        //System.out.println(c.getFullName().getEpithet().toLowerCase()+" "+args.get(i).toLowerCase());
                        if (!c.getFullName().getEpithet().toLowerCase().contains(args.get(i).toLowerCase())) {
                            candidates.remove(j);
                            j--;
                        }

                        if (candidates.size() == 0) {
                            sendMessage("invalid character! please try again.");
                            log("their epithet was prolly wrong");
                            return;
                        }

                    }

                    i++;
                    foundMatch = UnitDatabase.HEROES.size()==1;
                }
            }
        }

        //remove units based on valid rarity/IV data
        for (int i=0; i<candidates.size(); i++) {
            if (candidates.get(i).getSummonableRarity()>rarity) {
                candidates.remove(i);
                i--;
            }
        }

        //whittle down candidates list based on character properties
        if (candidates.size()>1) {
            if (newestOnly) {
                Hero newestHero = candidates.get(0);
                for (Hero x:candidates) {
                    if (x.getReleaseDate().getTimeInMillis()>newestHero.getReleaseDate().getTimeInMillis()) {
                        newestHero = x;
                    }
                }
                candidates.clear();
                candidates.add(newestHero);
            } else if (oldestOnly) {
                Hero oldestHero = candidates.get(0);
                for (Hero x:candidates) {
                    if (x.getReleaseDate().getTimeInMillis()<oldestHero.getReleaseDate().getTimeInMillis()) {
                        oldestHero = x;
                    }
                }
                candidates.clear();
                candidates.add(oldestHero);
            } else {
                for (String x : args) {
                    x = x.toLowerCase();

                    //find movement type hints
                    //TODO: use big brein to make this less "coincidental"
                    //these heroes have some of these keywords in their names:
                    //Clair: Highborn Flier, Florina: Lovely Flier, Shanna: Sprightly Flier
                    String move;
                    switch (x) {
                        case "infantry":
                        case "inf":
                            move = "Infantry";
                            break;
                        case "armor":
                        case "armored":
                            move = "Armor";
                            break;
                        case "horse":
                        case "cavalry":
                        case "cav":
                            move = "Cavalry";
                            break;
                        case "flying":
                        case "flier":
                        case "flyer": //debatable
                            move = "Flier";
                            break;
                        default:
                            move = "na";
                            break;
                    }

                    char gender;
                    switch (x) {
                        case "m":
                        case "man":
                        case "male":
                        case "boy":
                        case "boi":
                        case "gentleman":
                        case "dude":
                            gender = 'm';
                            break;
                        case "f":
                        case "female":
                        case "feman":
                        case "woman":
                        case "wamen":
                        case "lady":
                        case "girl":
                        case "dudette":
                            gender = 'f';
                            break;
                        default:
                            gender = 'n';
                    }

                    char color;
                    //find color hints
                    switch (x) {
                        case "r":
                        case "red":
                            color = 'r';
                            break;
                        case "b":
                        case "blue":
                            color = 'b';
                            break;
                        case "g":
                        case "green":
                            color = 'g';
                            break;
                        case "c":
                        case "gray":
                        case "grey":
                        case "colorless":
                            color = 'c';
                            break;
                        default:
                            color = 'n';
                            break;
                    }

                    //find weapon type hints
                    String weapon;
                    switch (x) {
                        case "sword":
                            weapon = "Sword";
                            break;
                        case "lance":
                            weapon = "Lance";
                            break;
                        case "axe":
                            weapon = "Axe";
                            break;
                        case "tome":
                        case "magic":
                            weapon = "Tome";
                            break;
                        case "staff":
                        case "stave":
                            weapon = "Staff";
                            break;
                        case "bow":
                        case "archer":
                            weapon = "Bow";
                            break;
                        case "dagger":
                            weapon = "Dagger";
                            break;
                        case "breath":
                        case "dragon":
                            weapon = "Breath";
                            break;
                        default:
                            weapon = "na";
                            break;
                    }

                    for (int j = 0; j < candidates.size(); j++) {
                        Hero c = candidates.get(j);
                        if (!move.equals("na")) {
                            if (!c.getMoveType().toString().equals(move)) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                        if (color!='n') {
                            if (c.getColor()!=color) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                        if (!weapon.equals("na")) {
                            if (!c.getWeaponType().toString().equals(weapon)) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                        if (gender!='n') {
                            if (c.getGender()!=gender) {
                                candidates.remove(j);
                                j--;
                            }
                        }
                    }
                }
            }
        }

        if (candidates.size()==0) {
            sendMessage("sorry, could not find your character.");
            log("couldn't find " + e.getAuthor().getName() + "'s character.");
            return;
        }



        for (Hero x:candidates) {
            //fill in the gaps with base kit, if requested
            if (useBaseKit) {
                for (Skill skill : x.getBaseKit()) {
                    if (skill instanceof StatModifier)
                        skills.add((StatModifier) skill);
                }
            }

            //construct a feasable base kit (StatModifiers only)
            Weapon weapon = null;
            PassiveA a = null;
            PassiveS s = null; //TODO: how to distinguish reasonably between inheritable and seal passives?

            for (StatModifier skill:skills) {
                if (weapon==null) {
                    if (skill instanceof Weapon) {
                        weapon = (Weapon) skill;
                        continue;
                    }
                }
                if (a==null) {
                    if (skill instanceof PassiveA) {
                        a = (PassiveA) skill;
                        continue;
                    }
                }
                if (s==null) {
                    if (skill instanceof PassiveS) {
                        s = (PassiveS) skill;
                        //continue;
                    }
                }
            }

            skills.clear();
            if (weapon!=null)
                skills.add(weapon);
            if (a!=null)
                skills.add(a);
            if (s!=null)
                skills.add(s);



            sendMessage(new MessageBuilder(
                    printCharacter(x, lv1, rarity, getAll, boon, bane,
                            merges, dragonflowers, support, skills))
                    .build());
        }

        /*
        StringBuilder message = new StringBuilder();
        for (Hero x:candidates) {
            String charString = printCharacter(x, lv1, rarity, getAll, boon, bane, merges, dragonflowers, support);
            if (message.length()+charString.length()>2000) {
                sendMessage(message.toString());
                message = new StringBuilder();
            }
            message.append(charString).append("\n");
        }
        if (message.length()>0) sendMessage(message.toString());
        */

        StringBuilder report = new StringBuilder("found stats for:");
        for (int i=0; i<candidates.size(); i++) {
            if (i%2==0) report.append("\n\t");
            Hero f = candidates.get(i);
            report.append(f.getFullName()).append(", ");
        }
        report.delete(report.length()-2, report.length());

        log(report.toString());
    }



    //todo: literally move ALL this shit into some kind of Discord UI section
    private static EmbedBuilder printCharacter(Hero x, boolean lv1, int rarity,
                                          boolean getAll, int boon, int bane,
                                          int merges, int dragonflowers, char support) {
        return printCharacter(x, lv1, rarity, getAll, boon, bane, merges, dragonflowers, support, null);
    }
    private static EmbedBuilder printCharacter(Hero x, boolean lv1, int rarity,
                                         boolean getAll, int boon, int bane,
                                         int merges, int dragonflowers, char support,
                                         ArrayList<StatModifier> skills) {
        EmbedBuilder heroInfo = new EmbedBuilder();



        StringBuilder description = new StringBuilder();

        Emote moveType = getEmote("Icon_Move_"+x.getMoveType());
        String unitColor;
        switch (x.getColor()) {
            case 'r':
                unitColor = "Red";
                break;
            case 'g':
                unitColor = "Green";
                break;
            case 'b':
                unitColor = "Blue";
                break;
            case 'c':
                unitColor = "Colorless";
                break;
            default:
                unitColor = "orange";
        }
        Emote weaponType = getEmote("Icon_Class_"+unitColor+"_"+x.getWeaponType());

        description
                .append(printEmote(moveType))
                .append(printEmote(weaponType))
                .append(x.getFullName().toString());

        /*
        heroInfo.setAuthor(description.toString());
        heroInfo.setDescription('*'+x.getOrigin().toString()+"*\n" +
                "Debuted "  +
                (x.getReleaseDate().get(Calendar.MONTH) + 1) + "-" +//starts at 0 (january = 0)
                x.getReleaseDate().get(Calendar.DAY_OF_MONTH) + "-" +
                x.getReleaseDate().get(Calendar.YEAR));
         */

        heroInfo.addField(description.toString(),
                '*'+x.getOrigin().toString()+"*\n" +
                        "Debuted "  +
                        (x.getReleaseDate().get(Calendar.MONTH) + 1) + "-" +//starts at 0 (january = 0)
                        x.getReleaseDate().get(Calendar.DAY_OF_MONTH) + "-" +
                        x.getReleaseDate().get(Calendar.YEAR),
                false);

        Color rColor;
        switch (rarity) {
            case 1:
                rColor = Color.DARK_GRAY;
                break;
            case 2:
                rColor = Color.GRAY;
                break;
            case 3:
                rColor = new Color(185, 95,0);
                break;
            case 4:
                rColor = Color.LIGHT_GRAY;
                break;
            case 5:
                rColor = Color.YELLOW;
                break;
            default:
                rColor = new Color(120, 0, 180); //purple-ish
                break;
        }
        heroInfo.setColor(rColor);

        heroInfo.setThumbnail(x.getPortraitLink().toString());


        String info = "```\n";

        info+= rarity + "* lv" + (lv1?1:40) + " stats: \n" +
                "hp   atk  spd  def  res\n";

        String stats;
        String bst;

        //todo: use instanceof to make this cleaner
        if (getAll) {
            if (x.isSummonable()) {
                stats = printStats(x.getAllStats(lv1, rarity));
                bst = printBST(x.getAllStats(lv1, rarity));
            } else {
                stats = printStats(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support, skills));
                bst = printBST(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support));
            }
        } else {
            stats = printStats(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support, skills));
            bst = printBST(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support));
        }

        info+= stats+"\n\n"+bst+"\n";

        if (!x.isSummonable()) info+= "this unit does not have access to IVs.\n";
        else if (merges>0&&getAll) info+= "predictions might not be 100% accurate.\n";
        info+= "```";

        heroInfo.addField("stats",info, false);

        StringBuilder baseKit = new StringBuilder();
        for (int i=0; i<x.getBaseKit().size(); i++) {
            baseKit.append(x.getBaseKit().get(i));
            if (i+1!=x.getBaseKit().size()) baseKit.append(", ");
        }

        heroInfo.addField("Base Skills", baseKit.toString(), false);

        return heroInfo;
    }
    public static EmbedBuilder printUnit(Unit x, boolean lv1) {
        return printCharacter(x, lv1, x.getRarity(), false,
                x.getBoon(), x.getBane(), 0, 0,
                x.getSupportStatus());
    }

    public static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((Math.log10(x)<1?"    ":"   "));
        return statString.toString();
    }
    public static String printStats(int[][] stats) {
        return  printStats(stats[0])+'\n'+
                printStats(stats[1])+(stats.length==3?'\n'+
                printStats(stats[2]):"");
    }

    public static String printBST(int[] stats) {
        int bst = 0;
        for (int i:stats) bst+= i;
        return "BST: " + bst;
    }
    public static String printBST(int[][] stats) {
        int bst = 0;
        for (int i=0; i<5; i++) bst+= stats[1][i];
        int maxBST = bst, minBST = bst;

        bst = 0;
        if (stats.length==2) {
            for (int i=0; i<5; i++) {
                for (int j=0; j<5; j++)
                    bst+= stats[(i==j?1:0)][j];
                if (bst > maxBST) maxBST = bst;
                if (bst < minBST) minBST = bst;
                bst = 0;
            }
        } else if (stats.length==3) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (j == i) continue;
                    for (int k = 0; k < 5; k++) {
                        if (k == i) bst += stats[0][k];
                        else if (k == j) bst += stats[2][k];
                        else bst += stats[1][k];
                    }
                    if (bst > maxBST) maxBST = bst;
                    if (bst < minBST) minBST = bst;
                    bst = 0;
                }
            }
        } else {
            return "idk lol";
        }
        if (maxBST==minBST) return "BST: "+maxBST;
        else return "BST: "+minBST+"-"+maxBST;
    }



    private static Emote getEmote(String name) {
        try {
            return BotMain.bot_grill.getEmotesByName(name, true).get(0);
        } catch (IndexOutOfBoundsException ioobe) {
            return BotMain.bot_grill.getEmotes()
                    .get((int)(Math.random()*BotMain.bot_grill.getEmotes().size()));
        }
    }
    //todo: put this in some lower level class

    private static String printEmote(Emote e) {
        return "<:"+e.getName()+":"+e.getId()+">";
    }



    public boolean isCommand() {
        String arg = args[0].toLowerCase();
        switch(arg) {
            case "getstats":
            case "gst":
            case "getivs":
            case "giv":
                return true;
            default:
                return false;
        }
    }

    public void onCommand() {
        getUnits();
    }



    public String getName() { return "HeroRetriever"; }
    public String getDescription() { return "Get pertinent info on characters from Fire Emblem Heroes!"; }
    public String getFullDescription() {
        //TODO: write the full description of HeroRetriever
        return "woowee this is gonna take a long time to write";
    }
}
