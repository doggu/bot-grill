package events.stem.chem;

import events.commands.Command;
import stem.science.chem.particles.ChemicalElement;
import stem.science.chem.particles.ElementDatabase;

import java.util.ArrayList;
import java.util.Comparator;

//todo: create Chemmand
public class Electronegativity extends Command {
            //todo: generalize to any periodic table trend
    private static final char APPROXIMATELY = '≈';

    @Override
    public void onCommand() {
        ArrayList<String> unknown = new ArrayList<>();
        ArrayList<ChemicalElement> elements = new ArrayList<>();
        for (int i = 1; i<args.length; i++) {
            ChemicalElement e = ElementDatabase.DATABASE.find(args[i]);

            if (e==null) unknown.add(args[i]);
            else elements.add(e);
        }



        //aleks sort
        ArrayList<ChemicalElement> aleksSort = new ArrayList<>(elements);
        Comparator<ChemicalElement> aComparator = (o1, o2) -> {
            int pC = Integer.compare(o1.getPeriod(), o2.getPeriod());
            int gC = Integer.compare(o2.getGroup(), o1.getGroup());
            int compare;
            if ((pC>0 && gC >= 0) || (pC >= 0 && gC>0)) compare = 1;
            else if ((pC<0 && gC<=0) || (pC<=0 && gC<0)) compare = -1;
            else compare = 0;

            return compare;
        };
        aleksSort.sort(aComparator);

        //wiki values sort
        ArrayList<ChemicalElement> wikiSort = new ArrayList<>(elements);
        //idk what the fuck this is but intelliJ said it works so i trust it
        wikiSort.sort(Comparator.comparingDouble(ChemicalElement::getElectronegativity));



        StringBuilder message = new StringBuilder();
        if (unknown.size()>0)
            message.append("unrecognized elements excluded: ").append(unknown).append('\n');

        message.append("trend-based sort: \n`");
        for (int i = 0; i<aleksSort.size()-1; i++) {
            message.append(aleksSort.get(i)).append(' ')
                    .append(aComparator.compare(
                            aleksSort.get(i),
                            aleksSort.get(i+1))==0 ? APPROXIMATELY:'>')
                    .append(' ');
        }
        message.append(aleksSort.get(aleksSort.size()-1)).append("`\n");

        message.append("wiki-based sort: \n`");
        for (int i = 0; i<wikiSort.size()-1; i++) {
            message.append(wikiSort.get(i)).append(' ')
                    .append(Double.compare(
                            wikiSort.get(i).getElectronegativity(),
                            wikiSort.get(i+1).getElectronegativity())==0 ? APPROXIMATELY:'>')
                    .append(' ');
        }
        message.append(aleksSort.get(aleksSort.size()-1)).append("`\n");



        sendMessage(message.toString());
        log(message.toString());
    }

    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("e(lectro)?n(egativity)?");
    }

    @Override
    public String getName() {
        return "Electronegativity";
    }

    @Override
    public String getDescription() {
        return "compare the electronegativity of different elements on the periodic table.";
    }

    @Override
    public String getFullDescription() {
        return "compare the electronegativity of different elements on the periodic table.\n"+
                "limited to comparisons only; no numerical values.";
    }
}
