package events.stem.chem;

import discordUI.button.ReactionButton;
import events.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import stem.math.matrix.Fraction;
import stem.math.matrix.Matrix;
import stem.science.chem.particles.ChemicalElement;
import utilities.Misc;

import java.util.ArrayList;
import java.util.Arrays;

public class Balance extends Command {
    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("b(alance)?");
    }

    @Override
    public String getName() {
        return "Balance";
    }

    @Override
    public String getDescription() {
        return "balance chemical equations!";
    }

    @Override
    public String getFullDescription() {
        return "seriously, it balances chemical equations";
    }

    @Override
    public void onCommand() {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(this.args));

        args.remove(0);
        System.out.println(args);

        if (args.size()<4) {
            sendMessage("please include at least three reactants and products " +
                    "(otherwise you wouldn't need me, really)");
            return;
        }

        if (!args.contains(">")) {
            sendMessage("i could not find your arrow (use symbol \">\")");
            return;
        }

        ArrayList<String> reactantsStr =
                new ArrayList<>(args.subList(0, args.indexOf(">")));
        System.out.println(reactantsStr);

        ArrayList<String> productsStr =
                new ArrayList<>(args.subList(args.indexOf(">")+1, args.size()));
        System.out.println(productsStr);

        ArrayList<ArrayList<ChemicalElement>> reactants = new ArrayList<>();
        ArrayList<ArrayList<ChemicalElement>> products = new ArrayList<>();

        for (String x: reactantsStr) reactants.add(MolarMass.getElements(x));
        for (String x: productsStr) products.add(MolarMass.getElements(x));

        ArrayList<ChemicalElement> involvedElements = new ArrayList<>();

        for (ArrayList<ChemicalElement> list:reactants) {
            for (ChemicalElement e:list) {
                if (!involvedElements.contains(e)) involvedElements.add(e);
            }
        }

        int[][] m = new int[involvedElements.size()]
                           [reactants.size()+products.size()];
        int i;
        for (i=0; i<involvedElements.size(); i++) {
            int j;
            for (j = 0; j<reactants.size(); j++) {
                m[i][j] = getCount(involvedElements.get(i), reactants.get(j));
            }
            for (ArrayList<ChemicalElement> product : products) {
                m[i][j] = getCount(involvedElements.get(i), product);
                j++;
            }
        }

        Matrix matrix = new Matrix(m);
//        System.out.println(matrix);
        matrix.reducedRowEchelonForm().reduce();
//        System.out.println(matrix);
        Fraction[] lCol = matrix.getColumn(
                reactants.size()+products.size()-1);
//        for (Fraction fraction : lCol)
//            System.out.println(fraction);

        equalizeDenominator(lCol);

        int[] coefficients = new int[reactants.size()+products.size()];
        for (i=0; i<lCol.length; i++) {
            if (lCol[i].getNumerator()==0) break;
            coefficients[i] = Math.abs(lCol[i].getNumerator());
        }
        coefficients[i] = lCol[0].getDenominator();

        StringBuilder message = new StringBuilder();

        args.remove(">");

        for (int j = 0; j<args.size(); j++) {
            if (j==reactants.size())
                message.append("→ ");

            if (Math.abs(coefficients[j])>1)
                message.append(Math.abs(coefficients[j]));
            message.append(args.get(j)).append(' ');
        }

        message.delete(message.length()-1, message.length());

        Message me = sendMessage('`'+message.toString()+'`');



        //todo: this doesn't actually create an aleks-pastable string
        //  need to use \chem[shit] notation

        message.replace(
                message.indexOf(String.valueOf('→'))-1,
                message.indexOf(String.valueOf('→'))+2,
                String.valueOf('='));

        //that i up there is so obnoxious
        for (int f=1; f<message.length(); f++) {
            if (message.charAt(f) >= '0' && message.charAt(f)<='9' &&
                    !(message.charAt(f-1)==' ' ||
                            (message.charAt(f-1) >= '0' &&
                                    message.charAt(f-1)<='9'))) {
                message.insert(f, '_');
                f++;
            }
        }

        // but didn't you write this yourself

        final String aleksPrint = message.toString()
                .replaceAll(" ", "+");

        // shut FUCK up

        new ReactionButton(
                me,
                e.getJDA().getEmotesByName("aleks", true)
                        .get(0)) {
            @Override
            public void onCommand() {
                getMessage().editMessage(
                        new EmbedBuilder().setTitle('`'+aleksPrint+'`')
                                .build())
                        .complete();
            }
        };
    }

    private void equalizeDenominator(Fraction[] col) {
        int[] denominators = new int[col.length];
        for (int i=0; i<col.length; i++)
            denominators[i] = col[i].getDenominator();

        int maxDen = Misc.lcm(denominators);

        for (Fraction fraction : col) {
            if (fraction.getDenominator()==maxDen) continue;

            int multiplicand = maxDen/fraction.getDenominator();

            fraction.multiplyBy(multiplicand)
                    .divideBy(multiplicand);
        }
    }



    private static int getCount(ChemicalElement element,
                                ArrayList<ChemicalElement> compound) {
        int count = 0;
        for (ChemicalElement e:compound) {
            if (element==e) count++;
        }

        return count;
    }
}
