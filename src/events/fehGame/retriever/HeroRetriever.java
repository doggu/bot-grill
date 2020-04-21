package events.fehGame.retriever;

import discordUI.feh.FEHPrinter;
import events.commands.Command;
import feh.characters.hero.Hero;
import feh.characters.skills.skillTypes.Skill;
import feh.characters.unit.Unit;
import utilities.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class HeroRetriever extends Command {
    /*
    private ArrayList<String> args;
    private int i;
    private boolean lv1 = false;
    private int rarity = 5;
    private boolean getAll = true;

    //generates Unit
    private int boon,
                bane,
                merges,
                dragonflowers,
                support;
    private ArrayList<Skill> skills = new ArrayList<>();
    private boolean useBaseKit = false;

    //generates FieldedUnit
    //put buffs n shit here
    //todo: legendary/mystic boosts
    // it may be commented out twice over but it's still relevant
    private void getUnits() {
        if (super.args[0].toLowerCase().matches("g(et)?ivs?"))
            lv1 = true;

        //scalper finding parameter data
        //TODO: convert these to individual methods
        // or at least separate the searching and printing some day please thanks
        for (i=0; i<args.size(); i++) {
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
                            int stat = getStat(boonP);
                            if (stat>=0) {
                                bane = stat;
                                getAll = false;
                                args.remove(x);
                                i--;
                            }
                        }
                    }
                }

                if (x.contains("-")) {
                    char baneP = x.charAt(x.indexOf('-') + 1);
                    int stat = getStat(baneP);
                    if (stat>=0) {
                        bane = stat;
                        getAll = false;
                        args.remove(x);
                        i--;
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
                    case "ssc": support = 0;  break;
                    case "ssb": support = 8;  break;
                    case "ssa": support = 32; break;
                    case "sss": support = 80; break;
                    case "basekit":
                        useBaseKit = true;
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
                            skills.add(skill);
                        }
                }
            }
        }



        ArrayList<Hero> candidates = UnitDatabase.DATABASE.findAll(
                StringUtil.join(args.subList(0, args.size())));

        //remove units based on valid rarity/IV data
        for (int i=0; i<candidates.size(); i++) {
            if (candidates.get(i).getSummonableRarity()>rarity) {
                candidates.remove(i);
                i--;
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
                skills.addAll(x.getBaseKit());
            }

            //construct a feasable base kit (StatModifiers only)
            Weapon weapon = null;
            PassiveA a = null;
            PassiveS s = null; //TODO: how to distinguish reasonably between inheritable and seal passives?

            for (Skill skill:skills) {
                if (weapon==null) {
                    if (skill instanceof Weapon) {
                        weapon = (Weapon) skill;
                    }
                } else if (a==null) {
                    if (skill instanceof PassiveA) {
                        a = (PassiveA) skill;
                    }
                } else if (s==null) {
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

            if (getAll) {
                sendMessage(new MessageBuilder(
                        FEHPrinter.printCharacter(x, lv1, rarity, skills))
                        .build());
            } else {
                sendMessage(new MessageBuilder(
                        FEHPrinter.printUnit(
                                new Unit(x, rarity, boon, bane, lv1 ? 1 : 40,
                                        support, merges, dragonflowers,
                                        null, 0, 0,
                                        skills, skills))
                        ).build());
            }
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
    private static int getStat(char val) {
        int stat = -1;
        switch (val) {
            case 'r':
                stat++;
            case 'd':
                stat++;
            case 's':
                stat++;
            case 'a':
                stat++;
            case 'h':
                stat++;
        }

        return stat;
    }
     */


    public boolean isCommand() {
        return super.args[0].toLowerCase()
                .matches("g(et)?st(ats?)?"); //gs reserved for getSkills
    }

    public void onCommand() {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(super.args));
        args.remove(0);
        /*
        //i=0;
        lv1 = false;
        rarity = 5;
        getAll = true;

        //generates Unit
        boon = -1;
        bane = -1;
        merges = 0;
        dragonflowers = 0;
        support = -1;
        skills = new ArrayList<>();
        useBaseKit = false;
         */

        HeroBuilder f = new HeroBuilder(args);
        StringBuilder log =
                new StringBuilder("found units for ")
                .append(e.getAuthor()).append(":");

        if (f.producingHeroes()) {
            ArrayList<Hero> heroes = f.getHeroes();

            if (heroes.size()!=0) {
                for (Hero hero : heroes) {
                    ArrayList<Skill> skills = f.getSkills();
                    if (f.useBaseKit()) skills.addAll(hero.getBaseKit());

                    sendMessage(FEHPrinter.printCharacter(
                            hero,
                            f.isLv1(),
                            f.getRarity(),
                            skills).build());
                    log.append("\n\t\t").append(hero.getFullName());
                }
            } else {
                sendMessage("could not find your character.");
                log("could not find "+e.getAuthor()+"'s character: " +
                        StringUtil.join(args));
//                return;
            }
        } else if (f.producingUnits()) {
            ArrayList<Unit> units;
            try {
                units = f.getUnits();
            } catch (MissingBaneException | InvalidIVException e) {
                sendMessage(e.getMessage());
                return;
            }

            if (units.size()!=0) {
                for (Unit unit : units) {
                    sendMessage(FEHPrinter.printUnit(unit).build());
                    log.append("\n\t\t").append(unit.getFullName());
                }
            } else {
                sendMessage("could not create your unit. " +
                        "perhaps some parameters were conflicting? " +
                        "(e.g. GHB units with IVs)");
            }
        }
    }


    public String getName() { return "HeroRetriever"; }
    public String getDescription() {
        return "Get pertinent info on characters from Fire Emblem Heroes!"; }
    public String getFullDescription() {
        //TODO: write the full description of HeroRetriever
        return "woowee this is gonna take a long time to write"; }
}
