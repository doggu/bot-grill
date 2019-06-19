package events.commands.chem;

import events.commands.Command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElementRetriever extends Command {
    public final ArrayList<Atom> atomList;



    public ElementRetriever() {
        atomList = getList();
    }



    public boolean isCommand() {
        return args[0].equalsIgnoreCase("GetAtom");
    }

    public void onCommand() {
        if (args.length<2) {
            sendMessage("incorrect format! please try again.");
            return;
        }

        String argument = args[1];

        try {
            int number = Integer.parseInt(argument);
            sendMessage(printAtom(atomList.get(number-1)));
        } catch (NumberFormatException nfe) {
            //do nothing
        }

        for (Atom a:atomList) {
            if (a.getName().equalsIgnoreCase(argument)
                    ||a.getSymbol().equalsIgnoreCase(argument)) {
                sendMessage(printAtom(a));
            }
        }
    }

    private String printAtom(Atom a) {
        return "**"+a.getName()+"**"+
                "\nSymbol: "+a.getSymbol()+
                "\nAtomic Number: "+a.getNumber();
    }



    private static ArrayList<Atom> getList() {
        ArrayList<Atom> atoms = new ArrayList<>();

        Scanner file;
        try {
            file = new Scanner(new File("./src/utilities/chem/periodicTable_basic.txt"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        while (file.hasNextLine()) {
            String[] atom = file.nextLine().split("\t");

            try {
                atoms.add(new Atom(atom[2], atom[1], Integer.parseInt(atom[0])));
            } catch (IndexOutOfBoundsException ioobe) {
                throw new Error("some data was missing from an atom");
            } catch (NumberFormatException nfe) {
                throw new Error("incorrect atomic number");
            }
        }

        return atoms;
    }



    public String getName() { return "ElementRetriever"; }
    public String getDescription() { return "finds elements based on symbol or name! (WIP)"; }
    public String getFullDescription() {
        return "ok just do the normal command thing with the second argument as whatever you wanna find";
    }
}
