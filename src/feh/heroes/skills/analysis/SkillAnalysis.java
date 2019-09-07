package feh.heroes.skills.analysis;

import feh.heroes.skills.SkillDatabase;
import feh.heroes.skills.skillTypes.Skill;

import java.util.ArrayList;
import java.util.Arrays;


public class SkillAnalysis {
    private final Skill skill;
    private final ArrayList<String> rawSentences;
    private final ArrayList<ArrayList<String>> sentences;
    private final int[] statModifiers;
    private final Integer cdModifier;



    private SkillAnalysis(Skill skill) {
        this.skill = skill;

        if (skill.getDescription().equals("")) {
            rawSentences = null;
            sentences = null;
            statModifiers = null;
            cdModifier = null;
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
    }



    private int[] getStatModifiers() {
        try {
            System.out.println("getting stat modifiers for " + skill.getName());
            int[] statModifiers = new int[5];

            for (ArrayList<String> sentence : sentences) {
                String[] args = sentence.get(0).split(" ");
                if (args[0].matches("(Grants)|(Inflicts)")) {
                    System.out.println("\tmight be a stat modifier!");
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
                case "Slows Special trigger (cooldown count-1)":
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



    public static void main(String[] na) {
        ArrayList<SkillAnalysis> analyses = new ArrayList<>();
        for (Skill x: SkillDatabase.SKILLS)
            analyses.add(new SkillAnalysis(x));




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
