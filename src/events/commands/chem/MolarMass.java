package events.commands.chem;

import events.commands.Command;
import stem.science.chem.ChemicalElement;
import stem.science.chem.ElementDatabase;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import static events.commands.chem.CharIdentity.LETTER_L;
import static events.commands.chem.CharIdentity.NUMBER;



public class MolarMass extends Command {
    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("m(olar)?m(ass)?");
    }

    @Override
    public void onCommand() {
        if (args.length!=2) return;

        compound = args[1];

        ArrayList<ChemicalElement> elements = getElements();

        double mm = 0;
        for (ChemicalElement e:elements) {
            try {
                mm += e.getAtomicWeight();
            } catch (NumberFormatException nfe) {
                sendMessage("sorry, that element is not supported yet. please try something else!");
            }
        }

        sendMessage(mm+" g/mol");
    }

    private String compound;
    private int i;

    private ArrayList<ChemicalElement> getElements() {
        ArrayList<ChemicalElement> elements = new ArrayList<>();



        for (i=0; i<compound.length(); i++) {
            CharIdentity identity = CharIdentity.getIdentity(compound.charAt(i));
            if (identity==null) throw new NullPointerException("a character could not be identified");
            switch (identity) {
                case LETTER_U:
                    String atom = String.valueOf(compound.charAt(i));
                    for (i = i+1; i<compound.length(); i++) {
                        identity = CharIdentity.getIdentity(compound.charAt(i));
                        if (identity==LETTER_L)
                            atom+= compound.charAt(i);
                        else {
                            i--;
                            break;
                        }
                    }

                    elements.add(ElementDatabase.DATABASE.find(atom));
                    break;
                case LETTER_L:
                    throw new Error("orphaned letter");
                case NUMBER:
                    int qty = 0;
                    int start = i;
                    for (i = i+1; i<compound.length(); i++) {
                        try {
                            identity = CharIdentity.getIdentity(compound.charAt(i));
                            if (identity != NUMBER) {
                                qty = Integer.parseInt(compound.substring(start, i)); //only i since it's already past
                                i--;
                                break;
                            }
                        } catch (IndexOutOfBoundsException ioobe) {
                            qty = Integer.parseInt(compound.substring(start, i)); //only i since it's already past
                            i--;
                            break;
                        }
                    }
                    if (qty==0)
                        elements.remove(elements.size()-1);
                    else
                        for (int times = 1; times<qty; times++)
                            elements.add(elements.get(elements.size()-1));
                    break;
                case PAREN_O:
                    /*
                    int pB = 1;
                    while (pB!=0) {
                        for (i = i+1; i<compound.length(); i++) {

                        }
                    }
                     */
                case PAREN_C:
                    throw new Error("unexpected closed parentheses");
            }
        }

        OffsetDateTime.now();

        return elements;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
