package feh.heroes.skills.analysis;

import feh.heroes.character.MovementClass;
import feh.heroes.skills.SkillDatabase;
import feh.heroes.skills.skillTypes.Skill;

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


    private final ArrayList<String>
            startOfTurn,
            duringCombat,
            beforeCombat,
            afterCombat,
            unitInitiates,
            foeInitiates;



    private SkillAnalysis(Skill skill) {
        this.skill = skill;

        if (skill.getDescription().equals("")) {
            rawSentences = null;
            sentences = null;
            statModifiers = null;
            cdModifier = null;
            effectiveAgainst = null;
            startOfTurn = null;
            duringCombat = null;
            beforeCombat = null;
            afterCombat = null;
            unitInitiates = null;
            foeInitiates = null;
            return;
        }

        this.rawSentences =
                new ArrayList<>(Arrays.asList(skill
                        .getDescription()
                        .substring(0, skill.getDescription().length()-1)
                        .split("\\. ")));
        this.sentences = new ArrayList<>();
        for (String rawSentence:rawSentences)
            sentences.add(new ArrayList<>(Arrays.asList(rawSentence
                            .split(", "))));

        this.statModifiers = getStatModifiers();
        this.cdModifier = getCdModifier();
        this.effectiveAgainst = getEffective();
        startOfTurn = getStartOfTurn();
        duringCombat = getDuringCombat();
        beforeCombat = getBeforeCombat();
        afterCombat = getAfterCombat();
        unitInitiates = getUnitInitiates();
        foeInitiates = getFoeInitiates();
    }



    private int[] getStatModifiers() {
        try {
            int[] statModifiers = new int[5];

            for (ArrayList<String> sentence : sentences) {
                String[] args = sentence.get(0).split(" ");
                if (args[0].matches("(Grants)|(Inflicts)")) {
                    ArrayList<String> modifiers = new ArrayList<>(sentence);

                    //remove causational
                    if (modifiers.get(0).contains("Grants"))
                        modifiers.set(0, modifiers.get(0).substring(7));
                    else
                        modifiers.set(0, modifiers.get(0).substring(9));

                    for (String modifier : modifiers) {
                        int val;
                        try {
                            val = modifier.contains("+")?
                                    Integer.parseInt(modifier.substring(modifier.indexOf("+") + 1)) :
                                    Integer.parseInt(modifier.substring(modifier.indexOf("-")));
                        } catch (IndexOutOfBoundsException|NumberFormatException e) {
                            continue;
                        }

                        if (modifier.contains("Atk"))
                            statModifiers[1]+= val;
                        if (modifier.contains("Spd"))
                            statModifiers[1]+= val;
                        if (modifier.contains("Def"))
                            statModifiers[1]+= val;
                        if (modifier.contains("Res"))
                            statModifiers[1]+= val;
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
            if (raw.matches("Effective against (infantry)|(flying)|(armored)|(cavalry) (and (infantry)|(flying)|(armored)|(cavalry))?foes")) {
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
    private MovementClass getEffectiveAgainst(String input) {
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
    private ArrayList<String> findWith(String input) {
        ArrayList<String> incl = new ArrayList<>();
        for (int i=0; i<rawSentences.size(); i++) {
            if (rawSentences.get(i).toLowerCase().contains(input)) {
                incl.add(rawSentences.get(i));
                rawSentences.remove(i);
                i--;
            }
        }

        return incl;
    }
    private ArrayList<String> getStartOfTurn() {
        return findWith("at start of turn");
    }
    private ArrayList<String> getDuringCombat() {
        return findWith("during combat");
    }
    private ArrayList<String> getBeforeCombat() {
        return findWith("at start of combat");
    }
    private ArrayList<String> getAfterCombat() {
        return findWith("after combat");
    }
    private ArrayList<String> getUnitInitiates() {
        return findWith("if unit initiates combat");
    }
    private ArrayList<String> getFoeInitiates() {
        return findWith("if foe initiates combat");
    }



    public String toString() {
        return skill.getName()+": "+skill.getDescription();
    }

    public static void main(String[] na) {
        ArrayList<SkillAnalysis> analyses = new ArrayList<>();
        for (Skill x: SkillDatabase.SKILLS)
            analyses.add(new SkillAnalysis(x));

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
                System.out.println(x);
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
