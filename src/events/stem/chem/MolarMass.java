package events.stem.chem;

import discordUI.button.PersonalButton;
import events.commands.Command;
import events.stem.math.MathListener;
import stem.science.chem.particles.ChemicalElement;
import stem.science.chem.particles.ElementDatabase;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import static events.stem.chem.CharIdentity.LETTER_L;
import static events.stem.chem.CharIdentity.NUMBER;

//todo: separate logic from listener
public class MolarMass extends Command {
    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("m(olar)?m(ass)?");
    }

    @Override
    public void onCommand() {
        if (args.length!=2) return;

        ArrayList<ChemicalElement> elements = getElements(args[1]);

        double mm = 0;
        for (ChemicalElement e:elements) {
            try {
                mm += e.getAtomicWeight();
            } catch (NumberFormatException nfe) {
                sendMessage("sorry, that element is not supported yet. please try something else!");
            }
        }

        final double molarMass = mm;

        new PersonalButton(sendMessage(mm+" g/mol"),
                e.getJDA().getEmotesByName("sto", false).get(0),
                e.getAuthor()) {
            @Override
            public void onCommand() {
                if (MathListener.answers.get(e.getUser().getId())!=null) {
                    MathListener.answers.remove(e.getUser().getId());
                }

                MathListener.answers.put(e.getUser().getId(), molarMass);

                e.getChannel().getMessageById(e.getMessageId()).complete()
                        .addReaction(e.getJDA().getEmotesByName("Accepted", false).get(0))
                        .queue();

                new Thread(() -> {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    } finally {
                        e.getChannel().getMessageById(e.getMessageId()).complete()
                                .clearReactions().complete();
                    }
                });
            }
        };
    }

    private static int i;

    static ArrayList<ChemicalElement> getElements(String compound) {
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
                    int qty = parseNum(compound);

                    //System.out.println(qty);
                    if (qty==0)
                        elements.remove(elements.size()-1);
                    else
                        for (int times = 1; times<qty; times++)
                            elements.add(elements.get(elements.size()-1));
                    break;
                case PAREN_O:
                    closeParenthetical(compound);
                    //todo: separate from listener to allow recursion
                    //elements.addAll()
                    break;
                case PAREN_C:
                    throw new Error("unexpected closed parentheses");
            }
        }

        OffsetDateTime.now();

        return elements;
    }

    private static int parseNum(String compound) {
        int start = i;
        for (i = i+1; i<compound.length(); i++) {
            CharIdentity identity = CharIdentity.getIdentity(compound.charAt(i));
            if (identity != NUMBER)
                break;
        }

        int qty;
        try {
            qty = Integer.parseInt(compound.substring(start, i));
        } catch (NumberFormatException nfe) {
            qty = 0;
        }

        i--;
        return qty;
    }

    private static int closeParenthetical(String compound) {
        int start = i;
        for (i = i+1; i<compound.length(); i++) {
            CharIdentity identity = CharIdentity.getIdentity(compound.charAt(i));

            if (identity==null) return -1;

            switch (identity) {
                case PAREN_O:
                    closeParenthetical(compound);
                case PAREN_C:
                    break;
            }
        }

        int qty;
        try {
            qty = Integer.parseInt(compound.substring(start, i));
        } catch (NumberFormatException nfe) {
            qty = 0;
        }

        i--;
        return qty;
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



    public static void main(String[] args) {
        System.out.println(Integer.parseInt(""));
    }
}
