package feh.heroes.skills.analysis;

import feh.heroes.character.MovementClass;
import feh.heroes.skills.SkillDatabase;
import feh.heroes.skills.skillTypes.Skill;
import utilities.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static feh.heroes.character.MovementClass.*;


public class SkillAnalysis {
    private final Skill skill;
    private final ArrayList<String> rawSentences;
    private final ArrayList<ArrayList<String>> sentences;
    private final int[] statModifiers;
    private final int cdModifier;
    private final ArrayList<MovementClass> effectiveAgainst;
    private final MovementClass neutralizes;
    private final boolean triangleAdept;


    private final ArrayList<String>
            startOfTurn,
            startOfTurn1,
            startOfEveryNthTurn,
            evenTurns,
            oddTurns,
            //duringCombat,
            atStartOfCombat,
            beforeCombatUnitInitiates,
            afterCombat,
            unitInitiates,
            foeInitiates,
            afterMovementAssist,
            whileUnitLives;



    public SkillAnalysis(Skill skill) {
        this.skill = skill;

        if (skill.getDescription().equals("")) {
            this.rawSentences = null;
            this.sentences = null;
            this.statModifiers = null;
            this.cdModifier = 0;
            this.effectiveAgainst = null;
            this.neutralizes = null;
            this.triangleAdept = false;

            this.startOfTurn = null;
            this.startOfTurn1 = null;
            this.startOfEveryNthTurn = null;
            this.evenTurns = null;
            this.oddTurns = null;
            //this.duringCombat = null;
            this.atStartOfCombat = null;
            this.beforeCombatUnitInitiates = null;
            this.afterCombat = null;
            this.unitInitiates = null;
            this.foeInitiates = null;
            this.afterMovementAssist = null;
            this.whileUnitLives = null;
            return;
        }

        this.rawSentences = getRawSentences();
        this.sentences = getSentences();

        this.statModifiers = generateStatModifiers();
        this.cdModifier = generateCdModifier();
        this.effectiveAgainst = generateEffective();
        this.neutralizes = generateNeutralizesEffectivity();
        this.triangleAdept = generateTriangleAdept();

        this.startOfTurn = generateStartOfTurn();
        this.startOfTurn1 = generateStartOfTurn1();
        this.startOfEveryNthTurn = generateStartOfEveryNthTurn();
        this.evenTurns = generateStartOfEven();
        this.oddTurns = generateStartOfOdd();
        //this.duringCombat = generateDuringCombat();
        this.atStartOfCombat = generateAtStartOfCombat();
        this.beforeCombatUnitInitiates = generateBeforeCombatUnitInitiates();
        this.afterCombat = generateAfterCombat();
        this.unitInitiates = generateUnitInitiates();
        this.foeInitiates = generateFoeInitiates();
        this.afterMovementAssist = generateAfterMovementAssist();
        this.whileUnitLives = generateWhileUnitLives();
    }
    private ArrayList<String> getRawSentences() {
        String desc = skill.getDescription().replaceAll("\\. \\(", " (");
        for (int i=0; i<desc.length(); i++) {
            if (desc.charAt(i)=='(') {
                for (i++; i < desc.length(); i++) {
                    char c = desc.charAt(i);
                    if (c == '.')
                        desc = desc.substring(0, i) + "[p]" + desc.substring(i + 1);
                    else if (c == ')')
                        break;
                }
            }
        }

        ArrayList<String> rawSentences = new ArrayList<>(Arrays.asList(desc.split("\\. ")));

        for (int i=0; i<rawSentences.size(); i++) {
            String raw = rawSentences.get(i);
            if (raw.contains("[p]"))
                rawSentences.set(i, raw.replaceAll("\\[p]", "."));
        }

        if (rawSentences.get(rawSentences.size()-1).charAt(rawSentences.get(rawSentences.size()-1).length()-1)=='.')
            rawSentences.set(
                    rawSentences.size()-1,
                    rawSentences.get(rawSentences.size()-1)
                            .substring(0, rawSentences.get(rawSentences.size()-1).length()-1));

        return rawSentences;
    }
    private ArrayList<ArrayList<String>> getSentences() {
        ArrayList<ArrayList<String>> sentences = new ArrayList<>();
        for (String rawSentence:rawSentences) {
            int p = rawSentence.indexOf('(');
            if (p>=0) {
                for (p++; p<rawSentence.length(); p++) {
                    char c = rawSentence.charAt(p);
                    if (c==',')
                        rawSentence = rawSentence.substring(0, p)+"[c]"+rawSentence.substring(p+1);
                    else if (c==')')
                        break;
                }
            }

            ArrayList<String> sentence = new ArrayList<>(Arrays.asList(rawSentence
                    .split(", ")));
            //it is TECHNICALLY a clause
            for (int i=0; i<sentence.size(); i++)
                sentence.set(i, sentence.get(i).replaceAll("\\[c]", ","));

            sentences.add(sentence);
        }

        return sentences;
    }



    private int[] generateStatModifiers() {
        try {
            int[] statModifiers = new int[5];

            for (int i=0; i<sentences.size(); i++) {
                ArrayList<String> sentence = sentences.get(i);
                String[] args = sentence.get(0).split(" ");
                if (args[0].matches("(Grants)|(Inflicts)")) {
                    ArrayList<String> modifiers = new ArrayList<>(sentence);

                    //remove causational
                    /*
                    if (modifiers.get(0).contains("Grants"))
                        modifiers.set(0, modifiers.get(0).substring(7));
                    else
                        modifiers.set(0, modifiers.get(0).substring(9));
                     */
                    boolean isModifier = true;

                    for (String modifier : modifiers) {
                        int val;
                        try {
                            val = modifier.contains("+")?
                                    Integer.parseInt(modifier.substring(modifier.indexOf("+") + 1)) :
                                    Integer.parseInt(modifier.substring(modifier.indexOf("-")));
                        } catch (IndexOutOfBoundsException|NumberFormatException e) {
                            isModifier = false;
                            break;
                        }

                        if (modifier.contains("HP"))
                            statModifiers[0]+= val;
                        if (modifier.contains("Atk"))
                            statModifiers[1]+= val;
                        if (modifier.contains("Spd"))
                            statModifiers[2]+= val;
                        if (modifier.contains("Def"))
                            statModifiers[3]+= val;
                        if (modifier.contains("Res"))
                            statModifiers[4]+= val;
                    }

                    if (isModifier) {
                        sentences.remove(i);
                        rawSentences.remove(i);
                        i--;
                    }
                }
            }

            return statModifiers;
        } catch (Exception e) {
            //System.out.println(skill.getName());
            e.printStackTrace();
        }

        return null;
    }
    private Integer generateCdModifier() {
        Integer cdModifier = 0;
        for (int i=0; i<rawSentences.size(); i++) {
            String rawSentence = rawSentences.get(i);
            switch (rawSentence) {
                case "Accelerates Special trigger (cooldown count-1)":
                    cdModifier = 1;
                    break;
                case "Slows Special trigger (cooldown count+1)":
                    cdModifier = -1;
                    break;
            }

            //only checks for one cdModifier, but i think that's good enough
            if (cdModifier!=0) {
                rawSentences.remove(i);
                sentences.remove(i);
                break;
            }
        }


        return cdModifier;
    }
    private ArrayList<MovementClass> generateEffective() {
        ArrayList<MovementClass> effectivity = new ArrayList<>();
        for (int i=0; i<rawSentences.size(); i++) {
            String raw = rawSentences.get(i);
            if (raw.matches("Effective against (infantry)|(flying)|(armored)|(cavalry) " +
                    "(and (infantry)|(flying)|(armored)|(cavalry))?foes")) {
                MovementClass eff1, eff2;
                effectivity.add(generateEffectiveAgainst(sentences.get(i).get(2)));
                try {
                    effectivity.add(generateEffectiveAgainst(sentences.get(i).get(4)));
                } catch (IndexOutOfBoundsException ioobe) {
                    continue;
                }

                rawSentences.remove(i);
                sentences.remove(i);
                i--;
            }
        }

        return effectivity;
    }
    private MovementClass generateEffectiveAgainst(String input) { //todo: magic and dragon foes
        if (input==null) return null;
        switch (input) {
            case "infantry":
                return INFANTRY;
            case "armored":
                return ARMORED;
            case "flying":
                return FLYING;
            case "cavalry":
                return CAVALRY;
            default:
                return null;
        }
    }
    private MovementClass generateNeutralizesEffectivity() { //todo: magic and dragon foes
        MovementClass effectivity = null;
        for (int i=0; i<rawSentences.size(); i++) {
            String raw = rawSentences.get(i);
            if (raw.matches("Neutralizes \"effective against (infantry)|(flying)|(armored)|(cavalry)\" " +
                    "bonuses")) {
                effectivity = generateEffectiveAgainst(sentences.get(i).get(3));

                rawSentences.remove(i);
                sentences.remove(i);
                break;
            }
        }

        return effectivity;
    }
    private boolean generateTriangleAdept() {
        for (int i=0; i<rawSentences.size(); i++) {
            if (rawSentences.get(i).equals("If unit has weapon-triangle advantage, boosts Atk by 20%"))
                try {
                    if (rawSentences.get(i+1).equals("If unit has weapon-triangle disadvantage, reduces Atk by 20%")) {
                        rawSentences.remove(i);
                        rawSentences.remove(i+1);
                        return true;
                    }
                } catch (IndexOutOfBoundsException ioobe) {
                    //nothin
                }
        }

        return false;
    }
    private ArrayList<String> findWith(String input) {
        ArrayList<String> incl = new ArrayList<>();
        for (int i=0; i<sentences.size(); i++) {
            for (int j=0; j<sentences.get(i).size(); j++) {
                if (sentences.get(i).get(j).toLowerCase().contains(input.toLowerCase())) {
                    incl.add(rawSentences.get(i));
                    rawSentences.remove(i);
                    sentences.remove(i);
                    i--;
                    break;
                }
            }
        }

        return incl;
    }
    private ArrayList<String> generateStartOfTurn() { return findWith("at start of turn"); }
    private ArrayList<String> generateStartOfTurn1() { return findWith("at the start of turn 1"); }
    private ArrayList<String> generateStartOfEveryNthTurn() { return findWith("at the start of every"); }
    private ArrayList<String> generateStartOfEven() { return findWith("at start of even-numbered turns"); }
    private ArrayList<String> generateStartOfOdd() { return findWith("at start of odd-numbered turns"); }

    //private ArrayList<String> generateDuringCombat() { return findWith("during combat"); }
    private ArrayList<String> generateAtStartOfCombat() { return findWith("at start of combat"); }
    private ArrayList<String> generateBeforeCombatUnitInitiates() {
        return findWith("before combat this unit initiates"); }
    private ArrayList<String> generateAfterCombat() { return findWith("after combat"); }
    private ArrayList<String> generateUnitInitiates() { return findWith("unit initiates combat"); }
    private ArrayList<String> generateFoeInitiates() { return findWith("foe initiates combat"); }
    //really only for valor/exp skills
    private ArrayList<String> generateWhileUnitLives() { return findWith("while unit lives"); }
    private ArrayList<String> generateAfterMovementAssist() { return
            findWith("If a movement Assist skill (like Reposition, Shove, Pivot, etc.) " +
                    "is used by unit or targets unit"); }



    public int[] getStatModifiers() { return statModifiers; }
    public int getCdModifier() { return cdModifier; }
    public ArrayList<MovementClass> getEffective() { return effectiveAgainst; }
    public MovementClass getNeutralizesEffectivity() { return neutralizes; }
    public boolean getTriangleAdept() { return triangleAdept; }
    //int getTANeutralizingLevel
    public ArrayList<String> getStartOfTurn() { return startOfTurn; }
    public ArrayList<String> getStartOfTurn1() { return startOfTurn1; }
    public ArrayList<String> getStartOfEveryNthTurn() { return startOfEveryNthTurn; }
    public ArrayList<String> getStartOfEven() { return evenTurns; }
    public ArrayList<String> getStartOfOdd() { return oddTurns; }
    //public ArrayList<String> getDuringCombat() { return duringCombat; }
    public ArrayList<String> getAtStartOfCombat() { return atStartOfCombat; }
    public ArrayList<String> getBeforeCombatUnitInitiates() { return beforeCombatUnitInitiates; }
    public ArrayList<String> getAfterCombat() { return afterCombat; }
    public ArrayList<String> getUnitInitiates() { return unitInitiates; }
    public ArrayList<String> getFoeInitiates() { return foeInitiates; }
    public ArrayList<String> getAfterMovementAssist() { return afterMovementAssist; }
    public ArrayList<String> getWhileUnitLives() { return whileUnitLives; }



    public String toString() {
        return skill.getName()+": "+skill.getDescription();
    }

    public static void main(String[] na) {
        Stopwatch s = new Stopwatch();
        s.start();
        ArrayList<SkillAnalysis> analyses = new ArrayList<>();
        for (Skill x: SkillDatabase.SKILLS)
            analyses.add(new SkillAnalysis(x));

        System.out.println("done ("+s.timeInSeconds()+"!)");
        s.stop();



        //completion test
        /*
        for (SkillAnalysis x:analyses) {
            if (x.rawSentences!=null) {
                if (x.rawSentences.size() > 0) {
                    System.out.println(x.skill.getName() + "\'s unprocessed effects:");
                    for (String str : x.rawSentences) {
                        System.out.println(str);
                    }
                    System.out.println();
                }
            }
        }
         */



        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String chunk = input.nextLine();//.toLowerCase();

            Skill f = SkillDatabase.DATABASE.find(chunk);
            if (f!=null) {
                System.out.println(f+": "+f.getDescription());
                continue;
            }

            HashMap<SkillAnalysis, String> descPortion = new HashMap<>();

            for (SkillAnalysis x:analyses) {
                if (x.rawSentences==null) continue;
                for (String b:x.rawSentences) {
                    if (b.contains(chunk)) {
                        descPortion.put(x, b);
                    }
                }
            }

            for (SkillAnalysis x:descPortion.keySet()) {
                System.out.println(x.skill.getName());
                if (x.rawSentences.size()>0) {
                    for (ArrayList<String> strArr:x.sentences) {
                        for (String str:strArr) {
                            System.out.println("\t"+str);
                        }
                    }
                }
                /*
                if (x.getStatModifiers()!=null) {
                    for (int n:x.getStatModifiers())
                        System.out.println("\t\t"+n);
                }
                */
            }
        }




        //count instances of certain items



        /*
        //beep boop random test:
        //this proves that the new ArrayList has new instances of each of its Strings, meaning modifications won't
        //carry back over into the old ArrayList (idk if this is obvious with some simple rule but i needed to know)
        ArrayList<String> args = new ArrayList<>();
        args.add("ah");
        args.add("AH");
        args.add("AHHHH");

        ArrayList<String> newArgs = new ArrayList<>(args.subList(1, 3));

        newArgs.set(0, newArgs.get(0).replace('H', 'n'));

        System.out.println(args);
        System.out.println(newArgs);
         */
    }
}
