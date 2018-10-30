package events.commands;

import main.BotMain;
import net.dv8tion.jda.core.entities.Emote;
import utilities.fehUnits.heroes.Character;
import utilities.fehUnits.heroes.Unit;
import utilities.fehUnits.heroes.UnitDatabase;
import utilities.fehUnits.skills.Skill;
import utilities.fehUnits.skills.SkillDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FEHRetriever extends Command {
    private final List<Character> characters;
    private final List<Skill> skills;

    public FEHRetriever() {
        super();
        characters = UnitDatabase.characters;
        skills = SkillDatabase.getList();
    }

    public boolean isCommand() {
        String arg = args[0].toLowerCase();
        switch(arg) {
            case "getstats":
            case "getskill":
                return true;
            default:
                return false;
        }
    }

    public void onCommand() {
        switch(args[0].toLowerCase()) {
            case "getstats":
                getUnits();
                break;
            case "getskill":
                getSkills();
                break;
        }
    }

    private void getUnits() {
        boolean lv1 = false;
        int rarity = 5;
        boolean getAll = true;
        int boon = -1;
        int bane = -1;

        ArrayList<Character> candidates = new ArrayList<Character>();

        List<String> args = new ArrayList<>(Arrays.asList(this.args));

        //scalper finding parameter data
        //TODO: convert these to individual methods
        for (int i=1; i<args.size(); i++) {
            String x = args.get(i);
            //test for argument for lv1 vs lv40 stats
            if (x.indexOf("lv")==0) {
                switch (x) {
                    case "lv1":
                        lv1 = true;
                        break;
                    case "lv40":
                        lv1 = false;
                        break;
                    default:
                        sendMessage("sorry, i can only do lv1 or lv40 stats. " +
                                "i'm not that good yet!");
                        break;
                }
            }

            //test for boon/bane arguments
            if (x.equals("getAll")) {
                getAll = true;
                args.remove(i);
                i--;
            } else if (x.contains("+")||x.contains("-")){
                if (x.contains("+")) {
                    char boonP = x.charAt(x.indexOf('+')+1);
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
                            break;
                        default:
                            break;
                    }
                }

                if (x.contains("-")) {
                    char baneP = x.charAt(x.indexOf('-')+1);
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
                            break;
                        default:
                            break;
                    }
                }
                args.remove(i);
                i--;
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
        }

        //finding units with the correct name
        for (int i=1; i<args.size(); i++) {
            String x = args.get(i);
            //test for name/epithet arguments
            boolean epithetIncluded = false;

            if (x.contains(":")) {
                x = x.substring(0, x.indexOf(":"));
                epithetIncluded = true;
            }
            //find characters of the correct name
            for (Character c:characters) {
                if (c.getName().equalsIgnoreCase(x))
                    candidates.add(c);
            }

            if (epithetIncluded) {
                boolean foundMatch = characters.size()==1;
                //find characters (from list of valid names) of the correct epithet

                while (!foundMatch) {
                    i++;
                    for (int j = 0; j < candidates.size(); j++) {
                        Character c = candidates.get(j);
                        if (!c.getEpithet().toLowerCase().contains(args.get(i).toLowerCase())) {
                            candidates.remove(j);
                            j--;
                        }

                        if (candidates.size() == 0) {
                            sendMessage("invalid character! please try again.");
                            log("their epithet was prolly wrong");
                            return;
                        }
                    }

                    foundMatch = candidates.size()==1;
                }
            }
        }

        //TODO: should i restrict this to in-game possibilities only?
        //remove units based on valid rarity/IV data
        for (int i=0; i<candidates.size(); i++) {
            if (candidates.get(i).getRarity()>rarity) {
                candidates.remove(i);
                i--;
            }
        }

        //whittle down candidates list based on character properties
        if (candidates.size()>1) {
            for (String x:args) {
                x = x.toLowerCase();

                //find movement type hints
                //TODO: use big brein to make this less "coincidental"
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

                for (int j=0; j<candidates.size(); j++) {
                    Character c = candidates.get(j);
                    if (!move.equals("na")) {
                        if (!c.getMoveType().equals(move)) {
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
                        if (!c.getWeaponType().equals(weapon)) {
                            candidates.remove(j);
                            j--;
                        }
                    }
                }
            }

        }

        //log("printin stats");

        if (candidates.size()==0) {
            sendMessage("sorry, could not find your character.");
            log("couldn't find "+e.getAuthor().getName()+"'s character.");
        }

        for (Character g:candidates) {
            sendMessage(printCharacter(g, lv1, rarity, getAll, boon, bane));
        }



        StringBuilder report = new StringBuilder("found stats for: ");
        for (Character f:candidates)
            report.append(f.getName()).append(": ").append(f.getEpithet()).append(", ");
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
        for (Skill x:skills)
            if (x.getName().toLowerCase().contains(name.toString().toLowerCase()))
                candidates.add(x);

        StringBuilder skillList = new StringBuilder();
        for (Skill x:candidates) {
            skillList.append(x.toString()).append("\n");
            if (skillList.length()>2000) {
                skillList.delete(skillList.indexOf(x.toString()), skillList.length());
                sendMessage(skillList.toString());
                skillList = new StringBuilder();
            }
        }

        if (skillList.length()>0) sendMessage(skillList.toString());

        log("i did it");
    }

    private List<Emote> moveTypeEmotes;

    public static String printUnit(Unit x, boolean lv1) {
        return printCharacter(x, lv1, x.getRarity(), false, x.getBoon(), x.getBane());
    }

    public static String printCharacter(Character x, boolean lv1, int rarity, boolean getAll, int boon, int bane) {
        //import emotes from fehicons database
        //TODO: allow this to work as a static method (print emotes some other way)
        List<Emote> fehIconEmotes = BotMain.fehIcons;

        String info =
                x.getName()+": "+x.getEpithet()+"\n"+
                "Appears In: "+x.getOrigin()+"\n"+
                "Date Released: "
                    + (x.getReleaseDate().get(Calendar.MONTH)+1)+"-" //starts at 0 (january = 0)
                    + x.getReleaseDate().get(Calendar.DAY_OF_MONTH)+"-"
                    + x.getReleaseDate().get(Calendar.YEAR)+"\n";


        Emote moveType;
        Emote weaponType;

        //TODO: overhaul emote grabbing in general
        try { //get relevant data for calling the movement type emote
            String[] nameArr = x.getMoveType().split(" ");
            StringBuilder name = new StringBuilder("Icon_Move");
            for (String a:nameArr) name.append("_").append(a);
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
            String name = "Icon_Class_"+x.getColor()+"_"+x.getWeaponType();
            weaponType = fehIconEmotes.get(0);
            for (Emote e:fehIconEmotes) {
                weaponType = e;
                if (e.getName().equals(name))
                    break;
            }
            weaponType = fehIconEmotes.get(0);
        } catch (IndexOutOfBoundsException kjsgf) {
            //log("an emote is missing or unimplemented!");
            //TODO: make question mark icon or something
            throw new Error();
        }

        info+= "<:"+moveType.getName()+":"+moveType.getId()+">"+
                "<:"+weaponType.getName()+":"+weaponType.getId()+">\n";

        info+= "```\n";

        info+= rarity+"* lv"+(lv1?1:40)+" stats: \n"+
                "hp   atk  spd  def  res\n";

        String stats;

        if (getAll) {
            if (x.isSummonable()) {
                stats = printStats(x.getAllStats(lv1, rarity));
            } else {
                stats = printStats(x.getStats(lv1, rarity, boon, bane));
            }
        } else {
            stats = printStats(x.getStats(lv1, rarity, boon, bane));
        }

        info+= stats+"\n";

        if (!x.isSummonable()) info+= "this unit does not have access to IVs.\n";

        info+= "```";

        return info;
    }

    private static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((Math.log10(x)<1?"    ":"   "));
        statString.append("\n");

        return statString.toString();
    }

    private static String printStats(int[][] stats) {
        return  printStats(stats[0])+
                printStats(stats[1])+
                printStats(stats[2]);
    }
}
