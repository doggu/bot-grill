package events.commands;

import main.BotMain;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import utilities.feh.heroes.character.Hero;
import utilities.feh.heroes.unit.Unit;
import utilities.feh.heroes.UnitDatabase;
import utilities.feh.skills.*;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

//TODO: split fehretriever into multiple classes/listeners

public class FEHRetriever extends Command {
    private final List<Hero> heroes;
    private final List<Skill> skills;



    public FEHRetriever() {
        heroes = UnitDatabase.HEROES;
        skills = SkillDatabase.SKILLS;

        Method[] methods = FEHRetriever.class.getMethods();

        for (Method method:methods) {
            //System.out.println(method.getName());
        }
    }



    private void getUnits() {
        boolean lv1 = false;
        int rarity = 5;
        boolean getAll = true;
        int boon = -1;
        int bane = -1;
        int merges = 0;
        int dragonflowers = 0;
        char support = 'd';
        boolean newestOnly = false;

        if (args[0].equalsIgnoreCase("getIVs")) lv1 = true;

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
                        } catch (NumberFormatException g) {
                            log("issue getting dragonflower count from string \""+x+"\"");
                        }
                    } else {
                        //try for merge number; if not, it's probably a boon modifier
                        //or legendary marth's fucking name god damn it
                        try {
                            merges = Integer.parseInt(x.substring(1));
                        } catch (NumberFormatException g) {
                            char boonP = x.charAt(x.indexOf('+') + 1);
                            switch (boonP) {
                                case 'r':   //res is the last stat in the array, so add to index for every stat before it
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
                    rarity = Integer.parseInt(x.substring(0,1));
                    args.remove(i);
                    i--;
                } catch (NumberFormatException g) {
                    //probably add more analysis here later on
                }
            }

            //test for support status
            if (x.indexOf("w/")==0) {
                switch (x.substring(2).toLowerCase()) {
                    //this is the only case
                    case "ssc": support = 'c'; break;
                    case "ssb": support = 'b'; break;
                    case "ssa": support = 'a'; break;
                    case "sss": support = 's'; break;
                }
            }

            //test for "new" keyword
            if (x.equalsIgnoreCase("new")) {
                newestOnly = true;
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
            for (Hero c: heroes) {
                if (c.getFullName().getName().equalsIgnoreCase(x))
                    candidates.add(c);
            }

            if (epithetIncluded) {
                boolean foundMatch = heroes.size()==1;
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
                    foundMatch = heroes.size()==1;
                }
            }
        }

        //remove units based on valid rarity/IV data
        for (int i=0; i<candidates.size(); i++) {
            if (candidates.get(i).getRarity()>rarity) {
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
                candidates = new ArrayList<>();
                candidates.add(newestHero);
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
                            move = "Armored";
                            break;
                        case "horse":
                        case "cavalry":
                        case "cav":
                            move = "Cavalry";
                            break;
                        case "flying":
                        case "flier":
                        case "flyer": //debatable
                            move = "Flying";
                            break;
                        default:
                            move = "na";
                            break;
                    }

                    String color;
                    //find color hints
                    switch (x) {
                        case "r":
                        case "red":
                            color = "Red";
                            break;
                        case "b":
                        case "blue":
                            color = "Blue";
                            break;
                        case "g":
                        case "green":
                            color = "Green";
                            break;
                        case "c":
                        case "gray":
                        case "grey":
                        case "colorless":
                            color = "Colorless";
                            break;
                        default:
                            color = "na";
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
                        if (!color.equals("na")) {
                            if (!c.getColor().equals(color)) {
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
                    }
                }
            }
        }



        //log("printin stats");

        if (candidates.size()==0) {
            sendMessage("sorry, could not find your character.");
            log("couldn't find " + e.getAuthor().getName() + "'s character.");
            return;
        }

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

        StringBuilder report = new StringBuilder("found stats for:");
        for (int i=0; i<candidates.size(); i++) {
            if (i%2==0) report.append("\n\t\t\t\t\t\t");
            Hero f = candidates.get(i);
            report.append(f.getFullName()).append(", ");
        }
        report.delete(report.length()-2, report.length());

        log(report.toString());
    }

    private void getSkills() {
        List<String> nameArr = new ArrayList<>(Arrays.asList(args));
        nameArr.remove(0);

        StringBuilder name = new StringBuilder();
        for (String x:nameArr)
            name.append(x).append(" ");
        name.delete(name.length()-1, name.length());

        List<Skill> candidates = new ArrayList<>();
        for (Skill x:skills) {
            if (x.getName().toLowerCase().contains(name.toString().toLowerCase())) {
                candidates.add(x);
            }
        }

        //for (Skill x:candidates) System.out.println(x.getName());

        List<Skill> remove = new ArrayList<>();
        for (Skill x:candidates) {
            for (Skill y:candidates) {
                //this code migh obsolete my whole life
                //same objects
                if (x!=y) {
                    if (x.getName().contains(y.getName())) {
                        boolean legendary = false;
                        if (x.isExclusive()||y.isExclusive()) legendary = true;
                        /*
                        String[] legendaryWeapons = {
                                "Armads",
                                "Aura",
                                "Durandal",
                                "Excalibur",
                                "Mulagir",
                                "Tyrfing",
                        };

                        for (String z:legendaryWeapons) {
                            if (x.getName().equals(z)) {
                                legendary = true;
                                break;
                            }
                        }
                        */
                        if (!legendary) remove.add(y);
                    }
                }
            }
        }
        for (Skill x:remove)
            candidates.remove(x);



        HashMap<Integer, String> skillIcons = new HashMap<>();

        skillIcons.put(0, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/8/82/Icon_Skill_Weapon.png");
        skillIcons.put(1, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/9/9a/Icon_Skill_Assist.png");
        skillIcons.put(2, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/2/25/Icon_Skill_Special.png");
        skillIcons.put(3, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/68/Passive_Icon_A.png");
        skillIcons.put(4, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/6a/Passive_Icon_B.png");
        skillIcons.put(5, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/8/84/Passive_Icon_C.png");
        skillIcons.put(6, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/6f/Passive_Icon_S.png");

        if (candidates.size()==0) {
            sendMessage("could not find your skill.");
            return;
        }

        for (Skill x:candidates) {
            EmbedBuilder skill = new EmbedBuilder();

            switch (x.getSlot()) {
                case 0: //weapon
                    skill.setColor(new Color(0xDE1336));
                    break;
                case 1: //assist
                    skill.setColor(new Color(0x00EDB3));
                    break;
                case 2: //special
                    skill.setColor(new Color(0xF400E5));
                    break;
                case 3: //a-passive
                    skill.setColor(new Color(0xFF2A2A));
                    break;
                case 4: //b-passive
                    skill.setColor(new Color(0x003ED3));
                    break;
                case 5: //c-passive
                    skill.setColor(new Color(0x09C639));
                    break;
                case 6: //sacred seal
                    skill.setColor(new Color(0xEDE500));
                    break;
            }

            skill.setAuthor(x.getName());
            skill.setDescription(x.getDescription());

            skill.setThumbnail(skillIcons.get(x.getSlot()));

            if (x instanceof  Weapon) {
                skill.addField("Might", ((Weapon) x).getMt()+"", false);
            }

            if (x instanceof ActionSkill) { //instanceof targeting skill
                skill.addField("Range", ""+((ActionSkill) x).getRng(), false);
            }

            if (x instanceof Special) {
                skill.addField("Cooldown", ""+((Special) x).getCooldown(), false);
            }



            //test displays
            if (x instanceof StatModifier) {
                int[] statModifiers = ((StatModifier) x).getStatModifiers();
                String printedStatModifiers = "```\n"+printStats(statModifiers)+"\n```";
                skill.addBlankField(false);
                skill.addField("stat modifiers", printedStatModifiers, false);
            }

            e.getChannel().sendMessage(skill.build()).queue();
        }



        StringBuilder report = new StringBuilder("found skill" + (candidates.size()>1?"s":"") + " for doggu:");

        for (int i=0; i<candidates.size(); i++) {
            Skill x = candidates.get(i);
            if (i%2==0) {
                report.append("\n\t\t\t\t\t\t");
            }
            report.append(x.getName());
            if (i!=candidates.size()-1) {
                report.append(", ");
            }
        }

        log(report.toString());
    }



    public boolean isCommand() {
        String arg = args[0].toLowerCase();
        switch(arg) {
            case "getstats":
            case "getivs":
            case "getskill":
            case "getskills":
                return true;
            default:
                return false;
        }
    }

    public void onCommand() {
        switch(args[0].toLowerCase()) {
            case "getivs":
            case "getstats":
                getUnits();
                break;
            case "getskill":
            case "getskills":
                getSkills();
                break;
        }
    }



    private static String printCharacter(Hero x, boolean lv1, int rarity,
                                         boolean getAll, int boon, int bane,
                                         int merges, int dragonflowers, char support) {
        //import emotes from fehicons database
        List<Emote> fehIconEmotes = BotMain.fehIcons;

        //TODO: this is not correct
        boolean fiveStarSummoned = rarity==5&&boon>0&&bane>0;

        String info =
                (fiveStarSummoned?"**":"") + x.getFullName() + (fiveStarSummoned?"**":"") + "\n" +
                "Appears In: *" + x.getOrigin() + "*\n" + 
                "Date Released: "
                    + (x.getReleaseDate().get(Calendar.MONTH) + 1) + "-" //starts at 0 (january = 0)
                    + x.getReleaseDate().get(Calendar.DAY_OF_MONTH) + "-"
                    + x.getReleaseDate().get(Calendar.YEAR) + "\n";


        Emote moveType;
        Emote weaponType;

        //TODO: overhaul emote grabbing in general
        try { //get relevant data for calling the movement type emote
            String name = "Icon_Move_" + x.getMoveType();
            moveType = fehIconEmotes.get(0);
            for (Emote e:fehIconEmotes) {
                moveType = e;
                if (e.getName().equals(name))
                    break;
            }
        } catch (IndexOutOfBoundsException kjsgf) {
            //log("an emote is missing or unimplemented!");
            //TODO: make question mark icon or something
            throw new Error();
        }

        try { //get relevant data for calling the weapon color/type emote
            String name = "Icon_Class_" + x.getColor() + "_" + x.getWeaponType();
            weaponType = fehIconEmotes.get(0);
            for (Emote e:fehIconEmotes) {
                weaponType = e;
                if (e.getName().equals(name))
                    break;
            }
        } catch (IndexOutOfBoundsException kjsgf) {
            //log("an emote is missing or unimplemented!");
            //TODO: make question mark icon or something
            throw new Error();
        }

        info+= printEmote(moveType) +
                printEmote(weaponType) + "\n";

        info+= "```\n";

        info+= rarity + "* lv" + (lv1?1:40) + " stats: \n" + 
                "hp   atk  spd  def  res\n";

        String stats;

        if (getAll) {
            if (x.isSummonable()) {
                stats = printStats(x.getAllStats(lv1, rarity, merges));
                info+= stats+"\n";
                info+= printBST(x.getAllStats(lv1, rarity, merges));
            } else {
                stats = printStats(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers));
                info+= stats+"\n";
                info+= printBST(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers));
            }
        } else {
            stats = printStats(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers));
            info+= stats+"\n";
            info+= printBST(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers));
        }
        info+= "\n";

        if (!x.isSummonable()) info+= "this unit does not have access to IVs.\n";
        else if (merges>0&&getAll) info+= "predictions might not be 100% accurate.\n";
        info+= "```";
        info+= "\nSkills: ";
        for (Skill skill:x.getBaseKit())
            info+= skill.toString();
        info+= "\n";
        return info;
    }
    public static String printUnit(Unit x, boolean lv1) {
        return printUnit(x, lv1, 'd');
    }
    public static String printUnit(Unit x, boolean lv1, char supportStatus) {
        return printCharacter(x, lv1, x.getRarity(), false,
                x.getBoon(), x.getBane(), 0, 0,
                supportStatus);
    }

    private static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((Math.log10(x)<1?"    ":"   "));
        statString.append("\n");

        return statString.toString();
    }
    private static String printStats(int[][] stats) {
        return  printStats(stats[0]) + 
                printStats(stats[1]) + 
                printStats(stats[2]);
    }

    private static String printBST(int[] stats) {
        int bst = 0;
        for (int i:stats) bst+= i;
        return "BST: " + bst;
    }
    private static String printBST(int[][] stats) {
        //TODO: technically not correct, as a unit could have a superboon with only other superbanes
        // the real solution would be to test every scenario (21) for stat totals
        int medium = 0; //convert to int so i can use printBST(int[] arg) here
        for (int i:stats[1]) medium+= i;
        boolean sboon = false, sbane = false;
        for (int i=0; i<5; i++) {
            if (Math.abs(stats[1][i]-stats[2][i])==4) {
                sboon = true;
            }
            if (Math.abs(stats[1][i]-stats[0][i])==4) {
                sbane = true;
            }
        }

        //"" for readability
        //who am i kidding this is stupid
        return "BST: "+(!sboon&&!sbane?medium:"")
                + (sboon&&!sbane?medium+"-"+(medium+1):"")
                + (!sboon&&sbane?(medium-1)+"-"+medium:"")
                + (sboon&&sbane?(medium-1)+"-"+(medium+1):"");
    }



    private static String printEmote(Emote e) {
        return "<:"+e.getName()+":"+e.getId()+">";
    }
}
