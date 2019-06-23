package events.commands.chem;

import events.commands.Command;
import utilities.chem.Element;
import utilities.chem.ElementDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElementRetriever extends Command {
    private final ArrayList<Element> elementList;



    public ElementRetriever() {
        elementList = ElementDatabase.ELEMENTS;
    }



    public boolean isCommand() {
        return args[0].equalsIgnoreCase("GetElement");
    }

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

        for (Element a: elementList) {
            if (a.getName().equalsIgnoreCase(argument)
                    ||a.getSymbol().equalsIgnoreCase(argument)) {
                sendMessage(printAtom(a));
            }
        }
    }

    private String printAtom(Element a) {
        return "**"+a.getName()+"**"+
                "\nSymbol: "+a.getSymbol()+
                "\nAtomic Number: "+a.getNumber();
    }



    public String getName() { return "ElementRetriever"; }
    public String getDescription() { return "finds elements based on symbol or name! (WIP)"; }
    public String getFullDescription() {
        return "ok just do the normal command thing with the second argument as whatever you wanna find";
    }
}
