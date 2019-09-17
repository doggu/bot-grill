package events.fehGame.retriever;

import discordUI.feh.FEHPrinter;
import events.commands.Command;
import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;
import feh.heroes.skills.SkillDatabase;
import feh.heroes.skills.analysis.StatModifier;
import feh.heroes.skills.skillTypes.PassiveA;
import feh.heroes.skills.skillTypes.PassiveS;
import feh.heroes.skills.skillTypes.Skill;
import feh.heroes.skills.skillTypes.Weapon;
import net.dv8tion.jda.core.MessageBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class HeroRetriever extends Command {
    private ArrayList<String> args;
    private int i=1;
    private boolean newestOnly, oldestOnly;
    private boolean lv1 = false;
    private int rarity = 5;
    private boolean getAll = true;

    //generates Unit
    private int boon = -1;
    private int bane = -1;
    private int merges = 0;
    private int dragonflowers = 0;
    private char support = 'd';

    //generates FieldedUnit
    private ArrayList<StatModifier> skills = new ArrayList<>();
    private boolean useBaseKit = false;
    //todo: legendary/mystic boosts


    private void getUnits() {
        if (super.args[0].matches("g(et)?[Ii][Vv]s?"))
            lv1 = true;

        ArrayList<Hero> candidates = new ArrayList<>();

        //scalper finding parameter data
        //TODO: convert these to individual methods
        // or at least separate the searching and printing some day please thanks
        for (; i<args.size(); i++) {
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
                while (!foundMatch&&i<args.size()) {
                    for (int j = 0; j < candidates.size(); j++) {
                        Hero c = candidates.get(j);
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
                    FEHPrinter.printCharacter(x, lv1, rarity, getAll, boon, bane,
                            merges, dragonflowers, support, skills))
                    .build());
        }



        StringBuilder report = new StringBuilder("found stats for:");
        for (int i=0; i<candidates.size(); i++) {
            if (i%2==0) report.append("\n\t");
            Hero f = candidates.get(i);
            report.append(f.getFullName()).append(", ");
        }
        report.delete(report.length()-2, report.length());

        log(report.toString());
    }


    public boolean isCommand() {
        String arg = super.args[0].toLowerCase();
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
        args = new ArrayList<>(Arrays.asList(super.args));
        i=1;
        newestOnly = false;
        oldestOnly = false;
        lv1 = false;
        rarity = 5;
        getAll = true;

        //generates Unit
        boon = -1;
        bane = -1;
        merges = 0;
        dragonflowers = 0;
        support = 'd';
        getUnits();
    }



    public String getName() { return "HeroRetriever"; }
    public String getDescription() { return "Get pertinent info on characters from Fire Emblem Heroes!"; }
    public String getFullDescription() {
        //TODO: write the full description of HeroRetriever
        return "woowee this is gonna take a long time to write";
    }
}
