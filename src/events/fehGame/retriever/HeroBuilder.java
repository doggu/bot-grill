package events.fehGame.retriever;

import feh.characters.HeroDatabase;
import feh.characters.hero.Hero;
import feh.characters.skills.SkillDatabase;
import feh.characters.skills.skillTypes.Skill;
import feh.characters.unit.Unit;
import main.BotMain;
import utilities.StringUtil;

import java.util.ArrayList;

public class HeroBuilder {
    //shut yo ass up IDE
    @SuppressWarnings("PointlessBooleanExpression")
    private static final boolean DEBUG = BotMain.DEBUG && true;


    boolean producingHeroes() {
        return boon<0 && bane<0 && merges==0 && dragonflowers==0 && support<0;
    }
    public ArrayList<Hero> getHeroes() {
        return heroes;
    }
    boolean producingUnits() {
        //remember to update this when the FieldedUnits hit
        return !producingHeroes();
    }
    public ArrayList<Unit> getUnits()
            throws MissingBaneException, InvalidIVException {
        if (needsMerges && merges==0)
            throw new MissingBaneException();

        ArrayList<Unit> units = new ArrayList<>();
        if (heroes==null)
            return new ArrayList<>();

        for (Hero x:heroes) {
            if (!x.isSummonable() && boon>=0)
                continue;

            units.add(new Unit(x, rarity, boon, bane, (lv1 ? 1:40),
                    support, merges, dragonflowers,
                    null, 0, 0,
                    null, skills));
        }
        //guaranteed to have units unless an unsummonable unit was weeded out
        if (units.size()==0) {
            throw new InvalidIVException();
        }

        if (baseKit) {
            for (Unit unit:units) {
                unit.equip(unit.getBaseKit());
            }
        }

        return units;
    }


    //hero args
    boolean isLv1() { return lv1; }
    public int getRarity() { return rarity; }
    public boolean getAll() { return getAll; }
    public ArrayList<Skill> getSkills() { return skills; }
    boolean useBaseKit() { return baseKit; }
    /*
    public boolean producingFieldedUnits() {
        //let's do this later
    }
     */


    private final ArrayList<Hero> heroes;

    private int i;
    private final ArrayList<String> args;

    //produce Hero
    private boolean lv1 = false;
    private int rarity = 5;
    private boolean getAll = true;
    private final ArrayList<Skill> skills = new ArrayList<>();
    private boolean baseKit = false;

    //produce Unit
    private int boon = -1,
                bane = -1,
                merges = 0,
                dragonflowers = 0,
                support = -1;
    //user can supply just "+atk" if they have merges on their unit
    //functionally identical to (boon>=0 && bane<0)
    private boolean needsMerges = false;


    public HeroBuilder(ArrayList<String> args) {
        this.args = args;
        heroes = build();
    }


    private ArrayList<Hero> build() {
        //gather parameters from scalpers, on each argument
        //with each scalper (that doesn't destroy itself)
        for (i=0; i<args.size(); i++) {
            if (scalpers.apply(args.get(i))) {
                args.remove(i);
                i--;
            }
        }

        ArrayList<Hero> basics =
                HeroDatabase.DATABASE.findAll(StringUtil.join(args));

        ///*
        if (DEBUG) {
            System.out.println("args\t\t\t"+args);
            System.out.println("lv1\t\t\t\t"+lv1);
            System.out.println("rarity\t\t\t"+rarity);
            System.out.println("getAll\t\t\t"+getAll);
            System.out.println("boon\t\t\t"+boon);
            System.out.println("bane\t\t\t"+bane);
            System.out.println("merges\t\t\t"+merges);
            System.out.println("dragonflowers\t"+dragonflowers);
            System.out.println("support\t\t\t"+support);
            System.out.println("needsMerges\t\t"+needsMerges);
            System.out.println("skills\t\t\t"+skills);
            System.out.println("baseKit\t\t"+ baseKit);
        }
        /**/

        return basics;
    }


    private final Scalpers
            scalpers = generateScalpers();

    @SuppressWarnings("SuspiciousMethodCalls")
    private Scalpers generateScalpers() {
        Scalpers scalpers = new Scalpers();

        //level
        scalpers.add(s -> {
            if (s.matches("lvl?\\d\\d?")) {
                lv1 = Integer.parseInt(s.replaceAll("lvl?", ""))
                        ==1;
                scalpers.remove(this); //heh
                return true;
            }
            return false;
        });
        //rarity
        scalpers.add(s -> {
            if (s.matches("\\d\\*")) {
                rarity = Integer.parseInt(String.valueOf(s.charAt(0)));
                scalpers.remove(this); //heh
                return true;
            }

            return false;
        });
        //IVs
        scalpers.add(s -> {
            //string pertains to merges
            if (s.matches("\\+\\d\\d?"))
                return false;

            int boon = s.indexOf('+'),
                bane = s.indexOf('-');

            if (boon>=0) {
                this.boon = getStat(s.charAt(boon+1));
                if (bane>=0) {
                    this.bane = getStat(s.charAt(bane+1));
                } else {
                    needsMerges = true;
                }

                getAll = false;
                scalpers.remove(this); //heh
                return true;
            }
            return false;
        });
        //IVs/merges
        scalpers.add(s -> {
            if (s.startsWith("+")) {
                try {
                    merges = Integer.parseInt(s.substring(1));
                    getAll = false;

                    scalpers.remove(this);
                    return true;
                } catch (NumberFormatException nfe) {
                    System.out.println(s);
                    //nothing
                }
            }

            return false;
        });
        //dragonflowers
        scalpers.add(s -> {
            if (s.startsWith("df+")) {
                try {
                    dragonflowers = Integer.parseInt(s.substring(3));
                    getAll = false;

                    scalpers.remove(this);
                    return true;
                } catch (NumberFormatException nfe) {
                    //nothing
                }
            }

            //System.out.println("not df: "+s);

            return false;
        });
        //support
        scalpers.add(s -> {
            if (s.startsWith("w/ss")) {
                switch (s) {
                    case "w/sss":
                        support+=48;
                    case "w/ssa":
                        support+=24;
                    case "w/ssb":
                        support+=8;
                    case "w/ssc":
                        support+=1;
                        scalpers.remove(this);
                        return true;
                }
            }

            return false;
        });
        //skills
        scalpers.add(s -> {
            if (s.startsWith("w/\"")) {
                StringBuilder skillName = new StringBuilder(s.substring(3));

                if (skillName.indexOf("\"")>0) {
                    skills.add(SkillDatabase.DATABASE.find(
                            skillName.substring(0, skillName.length()-1)));
                    return true;
                }

                args.remove(i);

                while (i<args.size()) {
                    skillName.append(' ').append(args.get(i));
                    if (args.get(i).endsWith("\"")) {
                        skillName.delete(skillName.length()-1,
                                         skillName.length());
                        break;
                    }
                    args.remove(i);
                }

                System.out.println(skillName.toString());

                skills.add(SkillDatabase.DATABASE.find(skillName.toString()));
                //do not remove this scalper
                return true;
            }

            return false;
        });
        //baseKit fill-in
        scalpers.add(s -> {
            if (s.equalsIgnoreCase("w/baseKit")) {
                baseKit = true;
                scalpers.remove(this);
                return true;
            }

            return false;
        });

        return scalpers;
    }

    private static int getStat(char val) {
        int stat = -1;
        switch (val) {
            case 'r': stat++;
            case 'd': stat++;
            case 's': stat++;
            case 'a': stat++;
            case 'h': stat++;
        }
        return stat;
    }
}
