package feh.skills.analysis;

import java.util.ArrayList;
import java.util.Arrays;

public interface StatModifier {
    int[] getStatModifiers();

    //deprecated?
    //stat/cooldown modifiers are only evaluated upon the creation of a user's kit,
    //so simply checking for cooldown modifiers on ALL skills does not consume
    //a significant amount of resources. it would probably be easier to process
    //a null modifier result from a few hundred skills than to isolate a handful
    //which do modify stats/cooldown

    //TODO: i hella gotta rework this when i understand better
    static int[] parseStatModifiers(String description) {
        int[] statModifiers = new int[5];
        //"Grants Atk/Spd+5. Inflicts Def/Res-5"
        int grantsIndex = description.toLowerCase().indexOf("grants");
        int inflictsIndex = description.toLowerCase().indexOf("inflicts");

        if (grantsIndex>=0) {
            String grants = description.substring(grantsIndex);
            grants = grants.substring(0,grants.indexOf('.')+1);

            ArrayList<String> args = new ArrayList<>(Arrays.asList(grants.split(" ")));
            args.remove(0); //"grants"
            for (String x:args) {
                if (x.indexOf('+')<0) {
                    //the description is not describing a stat modifier
                    break;
                } else {
                    if (x.indexOf('.') >= 0 || x.indexOf(',') >= 0) {
                        int modifier = getModifier(x);
                        String[] statNames = {"HP", "Atk", "Spd", "Def", "Res"};
                                            //always 5
                        for (int i = 0; i < statNames.length; i++)
                            if (x.contains(statNames[i])) statModifiers[i] = modifier;
                    } else {
                        //the description is not describing a stat modifier
                        break;
                    }
                }
            }
        }
        if (inflictsIndex>=0) {
            String inflicts = description.substring(inflictsIndex);
            inflicts = inflicts.substring(0,inflicts.indexOf('.')+1);

            ArrayList<String> args = new ArrayList<>(Arrays.asList(inflicts.split(" ")));
            args.remove(0); //"inflicts"
            for (String x:args) {
                if (x.indexOf('-')<0) {
                    //the description is not describing a stat modifier
                    break;
                } else {
                    if (x.indexOf('.') >= 0 || x.indexOf(',') >= 0) {
                        int modifier = getModifier(x);
                        String[] statNames = {"HP", "Atk", "Spd", "Def", "Res"};
                                            //always 5
                        for (int i = 0; i < statNames.length; i++)
                            if (x.contains(statNames[i])) statModifiers[i] = modifier;
                    } else {
                        //the description is not describing a stat modifier
                        break;
                    }
                }
            }
        }

        return statModifiers;
    }

    private static int getModifier(String x) {
        int modifier;
        try {
            if (x.indexOf('+')>=0)
                modifier = Integer.parseInt(x.substring(x.indexOf('+'), x.length() - 1));
            else if (x.indexOf('-')>=0)
                modifier = Integer.parseInt(x.substring(x.indexOf('-'), x.length() - 1));
            else
                throw new Error();
        } catch (NumberFormatException g) {
            System.out.println("\""+x+"\" is not conforming");
            throw new Error();
        }
        return modifier;
    }
}
