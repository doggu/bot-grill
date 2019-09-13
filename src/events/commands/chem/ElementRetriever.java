package events.commands.chem;

import events.commands.Command;
import stem.science.chem.ChemicalElement;
import stem.science.chem.ElementDatabase;

import java.util.ArrayList;

public class ElementRetriever extends Command {
    private final ArrayList<ChemicalElement> elementList;



    public ElementRetriever() {
        elementList = ElementDatabase.ELEMENTS;
    }



    public boolean isCommand() { return args[0].toLowerCase().matches("g(et)?e(lement)?"); }

    public void onCommand() {
        if (args.length<2) {
            sendMessage("incorrect format! please try again.");
            return;
        }

        String argument = args[1];

        try {
            int number = Integer.parseInt(argument);
            sendMessage(printAtom(elementList.get(number-1)));
        } catch (NumberFormatException nfe) {
            //do nothing
        }

        for (ChemicalElement a: elementList) {
            if (a.getName().equalsIgnoreCase(argument)
                    ||a.getSymbol().equalsIgnoreCase(argument)) {
                sendMessage(printAtom(a));
            }
        }
    }

    private String printAtom(ChemicalElement a) {
        return "**"+a.getName()+"**"+
                "\nSymbol: "+a.getSymbol()+
                "\nAtomic Number: "+a.getAtomicNumber()+
                "```"+
                "origin: "+a.getOrigin()+"\n" +
                "group: "+a.getGroup()+"\n" +
                "period: "+a.getPeriod()+"\n" +
                "atomicWeight: "+a.getAtomicWeight()+"\n" +
                "density: "+a.getDensity()+"\n" +
                "meltingPoint: "+a.getMeltingPoint()+"\n" +
                "boilingPoint: "+a.getBoilingPoint()+"\n" +
                "C: "+a.getC()+"\n" +
                "electronegativity: "+a.getElectronegativity()+"\n" +
                "earthAbundance: "+a.getEarthAbundance()+
                "```";

    }



    public String getName() { return "ElementRetriever"; }
    public String getDescription() { return "finds elements based on symbol or name! (WIP)"; }
    public String getFullDescription() {
        return "ok just do the normal command thing with the second argument as whatever you wanna find";
    }
}
