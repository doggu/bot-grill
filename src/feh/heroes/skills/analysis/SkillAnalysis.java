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
    private final Integer cdModifier;
    private final ArrayList<MovementClass> effectiveAgainst;
    private final MovementClass neutralizes;
    private final boolean triangleAdept;


    private final ArrayList<String>
            startOfTurn,
            startOfTurn1,
            startOfEveryNthTurn,
            evenTurns,
            oddTurns,
            duringCombat,
            beforeCombat,
            afterCombat,
            unitInitiates,
            foeInitiates,
            whileUnitLives;



    private SkillAnalysis(Skill skill) {
        this.skill = skill;

        if (skill.getDescription().equals("")) {
            rawSentences = null;
            sentences = null;
            statModifiers = null;
            cdModifier = null;
            effectiveAgainst = null;
            neutralizes = null;
            this.triangleAdept = false;

            startOfTurn = null;
            startOfTurn1 = null;
            startOfEveryNthTurn = null;
            evenTurns = null;
            oddTurns = null;
            duringCombat = null;
            beforeCombat = null;
            afterCombat = null;
            unitInitiates = null;
            foeInitiates = null;
            whileUnitLives = null;
            return;
        }

        this.rawSentences = getRawSentences();
        this.sentences = getSentences();

        this.statModifiers = getStatModifiers();
        this.cdModifier = getCdModifier();
        this.effectiveAgainst = getEffective();
        this.neutralizes = getNeutralizesEffectivity();
        this.triangleAdept = triangleAdept();

        startOfTurn = getStartOfTurn();
        startOfTurn1 = getStartOfTurn1();
        startOfEveryNthTurn = getStartOfEveryNthTurn();
        evenTurns = getStartOfEven();
        oddTurns = getStartOfOdd();
        duringCombat = getDuringCombat();
        beforeCombat = getBeforeCombat();
        afterCombat = getAfterCombat();
        unitInitiates = getUnitInitiates();
        foeInitiates = getFoeInitiates();
        whileUnitLives = getWhileUnitLives();
    }
    private ArrayList<String> getRawSentences() {
        String desc = skill.getDescription().replaceAll("\\. \\(", " (");
        int p = desc.indexOf('(');
        if (p>=0) {
            for (p++; p<desc.length(); p++) {
                char c = desc.charAt(p);
                if (c=='.')
                    desc = desc.substring(0, p)+"[period]"+desc.substring(p+1);
                else if (c==')')
                    break;
            }
        }
        ArrayList<String> rawSentences = new ArrayList<>(Arrays.asList(desc.split("\\. ")));

        for (int i=0; i<rawSentences.size(); i++) {
            String raw = rawSentences.get(i);
            if (raw.contains("[period]"))
                rawSentences.set(i, raw.replaceAll("\\[period]", "."));
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
        for (String rawSentence:rawSentences)
            sentences.add(new ArrayList<>(Arrays.asList(rawSentence
                    .split(", "))));

        return sentences;
    }



    private int[] getStatModifiers() {
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
            System.out.println(skill.getName());
            e.printStackTrace();
        }

        return null;
    }
    private Integer getCdModifier() {
        Integer cdModifier = null;
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

            if (cdModifier!=null) {
                rawSentences.remove(i);
                sentences.remove(i);
                break;
            }
        }


        return cdModifier;
    }
    private ArrayList<MovementClass> getEffective() {
        ArrayList<MovementClass> effectivity = new ArrayList<>();
        for (int i=0; i<rawSentences.size(); i++) {
            String raw = rawSentences.get(i);
            if (raw.matches("Effective against (infantry)|(flying)|(armored)|(cavalry) " +
                    "(and (infantry)|(flying)|(armored)|(cavalry))?foes")) {
                MovementClass eff1, eff2;
                effectivity.add(getEffectiveAgainst(sentences.get(i).get(2)));
                try {
                    effectivity.add(getEffectiveAgainst(sentences.get(i).get(4)));
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
    private MovementClass getEffectiveAgainst(String input) { //todo: magic and dragon foes
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
    private MovementClass getNeutralizesEffectivity() { //todo: magic and dragon foes
        MovementClass effectivity = null;
        for (int i=0; i<rawSentences.size(); i++) {
            String raw = rawSentences.get(i);
            if (raw.matches("Neutralizes \"effective against (infantry)|(flying)|(armored)|(cavalry)\" " +
                    "bonuses")) {
                effectivity = getEffectiveAgainst(sentences.get(i).get(3));

                rawSentences.remove(i);
                sentences.remove(i);
                break;
            }
        }

        return effectivity;
    }
    private boolean triangleAdept() {
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
                if (sentences.get(i).get(j).toLowerCase().contains(input)) {
                    incl.add(sentences.get(i).get(j));
                    rawSentences.remove(i);
                    sentences.remove(i);
                    i--;
                    break;
                }
            }
        }

        return incl;
    }
    private ArrayList<String> getStartOfTurn1() {
        return findWith("at the start of turn 1");
    }
    private ArrayList<String> getStartOfEveryNthTurn() {
        return findWith("at the start of every");
    }
    private ArrayList<String> getStartOfTurn() {
        return findWith("at start of turn");
    }
    private ArrayList<String> getStartOfEven() {
        return findWith("at start of even-numbered turns");
    }
    private ArrayList<String> getStartOfOdd() {
        return findWith("at start of odd-numbered turns");
    }

    private ArrayList<String> getDuringCombat() {
        return findWith("during combat");
    }
    private ArrayList<String> getBeforeCombat() {
        return findWith("at start of combat");
    }
    private ArrayList<String> getBeforeCombatUnitInitiates() {
        return findWith("before combat this unit initiates"); }
    private ArrayList<String> getAfterCombat() {
        return findWith("after combat");
    }
    private ArrayList<String> getUnitInitiates() {
        return findWith("unit initiates combat");
    }
    private ArrayList<String> getFoeInitiates() {
        return findWith("foe initiates combat");
    }
    //really only for valor/exp skills
    private ArrayList<String> getWhileUnitLives() {
        return findWith("while unit lives");
    }



    public String toString() {
        return skill.getName()+": "+skill.getDescription();
    }

    public static void main(String[] na) {
        Stopwatch f = new Stopwatch();
        f.start();
        ArrayList<SkillAnalysis> analyses = new ArrayList<>();
        for (Skill x: SkillDatabase.SKILLS)
            analyses.add(new SkillAnalysis(x));

        System.out.println("done ("+f.timeInSeconds()+"!)");
        f.stop();

        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String chunk = input.nextLine();//.toLowerCase();

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
                    for (String s:x.rawSentences) {
                        System.out.println("\t"+s);
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
