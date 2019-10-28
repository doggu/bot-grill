package events.stem.chem;

import events.commands.Command;
import stem.science.chem.particles.ChemicalElement;
import stem.science.chem.particles.ElementDatabase;

import java.util.ArrayList;

/*

+--+                                               +--+
|H |                                               |He|
+--+--+                             +--+--+--+--+--+--+
|Li|Be|                             |B |C |N |O |F |Ne|
+--+--+                             +--+--+--+--+--+--+
|Na|Mg|                             |Al|Si|P |S |Cl|Ar|
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|K |Ca|Sc|Tl|V |Cr|Mn|Fe|Co|Ni|Cu|Zn|Ga|Ge|As|Se|Br|Kr|
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|Rb|Sr|Y |Zr|Nb|Mo|Tc|Ru|Rh|Pd|Ag|Cd|In|Sn|Sb|Te|I |Xe|
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|Cs|Ba|La|Hf|Ta|W |Re|Os|Ir|Pt|Au|Hg|Tl|Pb|Bi|Po|At|Rn|
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|Fr|Ra|Ac|Rf|Ha|  |  |  |  |
+--+--+--+--+--+--+--+--+--+

+--+                                                                                         +--+
|H |                                                                                         |He|
+--+--+                                                                       +--+--+--+--+--+--+
|Li|Be|                                                                       |B |C |N |O |F |Ne|
+--+--+                                                                       +--+--+--+--+--+--+
|Na|Mg|                                                                       |Al|Si|P |S |Cl|Ar|
+--+--+--+                                         +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|K |Ca|Sc|                                         |Tl|V |Cr|Mn|Fe|Co|Ni|Cu|Zn|Ga|Ge|As|Se|Br|Kr|
+--+--+--+                                         +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|Rb|Sr|Y |                                         |Zr|Nb|Mo|Tc|Ru|Rh|Pd|Ag|Cd|In|Sn|Sb|Te|I |Xe|
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|Cs|Ba|La|Ce|Pr|Nd|Pm|Sm|Eu|Gd|Tb|Dy|Ho|Er|Tm|Yb|Lu|Hf|Ta|W |Re|Os|Ir|Pt|Au|Hg|Tl|Pb|Bi|Po|At|Rn|
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|Fr|Ra|Ac|Th|Pa|U |Np|Pu|Am|Cm|Bk|Cf|Es|Fm|Md|No|Lr|Rf|Ha|  |  |  |  |
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

 */

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
                return;
            }
        }

        sendMessage("sorry, i could not find your requested element.");
    }

    private String printAtom(ChemicalElement a) {
        return "**"+a.getName()+"**\n"+
                "Symbol: "+a.getSymbol()+"\n"+
                a.chemicalProperties()+"\n"+
                "```"+
                "atomicNumber: "+a.getAtomicNumber()+"\n"+
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
