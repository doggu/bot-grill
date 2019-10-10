package events.fehGame.retriever;

import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;
import feh.heroes.skills.SkillDatabase;
import feh.heroes.skills.skillTypes.Skill;
import feh.heroes.unit.Unit;
import utilities.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class HeroBuilder {
    public boolean producingHeroes() {
        return boon<0&&bane<0&&merges<0&&dragonflowers<0&&support<0;
    }
    public ArrayList<Hero> getHeroes() {
        return heroes;
    }
    public boolean producingUnits() {
        return !producingHeroes();
    }
    public ArrayList<Unit> getUnits() {
        ArrayList<Unit> units = new ArrayList<>();

        if (heroes==null) return new ArrayList<>();

        for (Hero x:heroes) {
            units.add(new Unit(x, rarity, boon, bane, (lv1?1:40),
                    support, merges, dragonflowers,
                    null, 0, 0,
                    skills, skills));
        }

        if (baseKit) {
            for (Unit unit:units) {
                unit.equip(unit.getBaseKit());
            }
        }

        return units;
    }



    //hero args
    public boolean isLv1() { return lv1; }
    public int getRarity() { return rarity; }
    public boolean getAll() { return getAll; }
    public ArrayList<Skill> getSkills() { return skills; }
    public boolean useBaseKit() { return baseKit; }
    /*
    public boolean producingFieldedUnits() {
        //let's do this later
    }
     */

    private ArrayList<Hero> heroes;

    private int i;
    private ArrayList<String> args;

    //produce Hero
    private boolean lv1 = false;
    private int rarity = 5;
    private boolean getAll = true;
    private ArrayList<Skill> skills = new ArrayList<>();
    private boolean baseKit = false;

    //produce Unit
    private int boon = -1,
            bane = -1,
            merges = -1,
            dragonflowers = -1,
            support = -1;



    public HeroBuilder(String args) {
        this(new ArrayList<>(Arrays.asList(args.split(" "))));
    }
    public HeroBuilder(ArrayList<String> args) {
        this.args = args;

        heroes = build();
    }



    private ArrayList<Hero> build() {
        ArrayList<Hero> heroes = new ArrayList<>();

        for (i=0; i<args.size(); i++) {
            for (Function<String, Boolean> scalper:scalpers) {
                if (scalper.apply(args.get(i))) { //scalper edits global fields during its process
                    //System.out.println(args.get(i));

                    args.remove(i);
                    i--;
                    break;
                }
            }
        }

        ArrayList<Hero> basics = UnitDatabase.DATABASE.findAll(StringUtil.join(args));

        ///*
        System.out.println("\n\n");

        System.out.println("args\t\t\t"+args);
        System.out.println("lv1\t\t\t\t"+lv1);
        System.out.println("rarity\t\t\t"+rarity);
        System.out.println("getAll\t\t\t"+getAll);
        System.out.println("boon\t\t\t"+boon);
        System.out.println("bane\t\t\t"+bane);
        System.out.println("merges\t\t\t"+merges);
        System.out.println("dragonflowers\t"+dragonflowers);
        System.out.println("support\t\t\t"+support);
        System.out.println("skills\t\t\t"+skills);
        System.out.println("baseKit\t\t"+ baseKit);
        // */

        return basics;
    }



    private final ArrayList<Function<String, Boolean>> scalpers = generateScalpers();

    private ArrayList<Function<String, Boolean>> generateScalpers() {
        ArrayList<Function<String, Boolean>> scalpers = new ArrayList<>();
        /*
        level ~
        IVs?
        merges/dragonflowers ~
        rarity ~
        support ~
        skills ~
         */

        //todo: create Scalper object to reduce duplicates
        //level
        scalpers.add(s -> {
            if (s.charAt(0)=='l') {
                if (s.charAt(1) == 'v') {
                    if (s.charAt(2)=='l')
                        lv1 = Integer.parseInt(s.substring(3))==1;
                    else
                        lv1 = Integer.parseInt(s.substring(2))==1;

                    scalpers.remove(this); //heh
                    return true;
                }
            }

            return false;
        });
        //IVs
        scalpers.add(s -> {
            int boonI = s.indexOf('+'),
                baneI = s.indexOf('-');

            if (boonI>=0&&baneI>=0) {
                int boon = getStat(s.charAt(boonI+1));
                int bane = getStat(s.charAt(baneI+1));

                if (boon>=0&&bane>=0) {
                    this.boon = boon;
                    this.bane = bane;

                    getAll = false;
                    return true;
                }
            }

            return false;
        });
        //rarity
        scalpers.add(s -> {
            if (s.charAt(1)=='*') {
                rarity = Integer.parseInt(String.valueOf(s.charAt(0)));

                scalpers.remove(this); //heh
                return true;
            }

            return false;
        });
        //merges
        scalpers.add(s -> {
            if (startsWith(s, '+')) {
                try {
                    merges = Integer.parseInt(s.substring(1));
                    getAll = false;

                    scalpers.remove(this);
                    return true;
                } catch (NumberFormatException nfe) {
                    //nothin
                }
            }

            return false;
        });
        //dragonflowers
        scalpers.add(s -> {
            if (startsWith(s, 'd','f','+')) {
                try {
                    dragonflowers = Integer.parseInt(s.substring(3));
                    getAll = false;

                    scalpers.remove(this);
                    return true;
                } catch (NumberFormatException nfe) {
                    //nothin
                }
            }

            //System.out.println("not df: "+s);

            return false;
        });
        //support
        scalpers.add(s -> {
            if (startsWith(s, 'w', '/')) {
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
            if (startsWith(s, 'w', '/', '"')) {
                StringBuilder skillName = new StringBuilder(s.substring(3));

                if (skillName.indexOf("\"")>0) {
                    skills.add(SkillDatabase.DATABASE.find(skillName.substring(0, skillName.length()-1)));
                    return true;
                }

                args.remove(i);

                while (i<args.size()) {
                    skillName.append(' ').append(args.get(i));
                    if (endsWith(args.get(i), '"')) {
                        skillName.delete(skillName.length()-1, skillName.length());
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

    private static boolean startsWith(String arg, char ... check) {
        try {
            for (int i = 0; i < check.length; i++) {
                if (arg.charAt(i)!=check[i])
                    return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
    private static boolean endsWith(String arg, char ... check) {
        try {
            for (int i=0; i<check.length; i++) {
                if (arg.charAt(arg.length()-(i+1))!=check[check.length-(i+1)]) {
                    return false;
                }
            }
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
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

    public static void main(String[] args) {
        SkillDatabase.DATABASE.getRandom();
        ArrayList<Hero> heroes = new HeroBuilder("+s-h lv1 4* +10 df+5 blue caeda w/sss w/\"Juicy Wave+\"")
                .build();

        if (heroes==null) return;

        for (Hero x:heroes) {
            if (x instanceof Unit) {
                System.out.println("unit");
            }
        }
    }
}
