package events.stem.chem;

import events.commands.Command;

public class Soluble extends Command {
    private static final String[] CATIONS = {
            //G1A metals
            //if (group==1&&period!=1) it's a G1A metal (alkali metal)
            //ammonium
            "NH4",
            //special insoluble metals
            //chloride
            "Ag",
            "Pb(2+)",
            "Hg2(2+)",
            //bromides & iodides
            "Ag",
            "Pb(2+)",
            "Hg2(2+)",
            "Hg(2+)",
            //sulfates
            "Ag",
            "Pb(2+)",
            "Hg2(2+)",
            "Ca(2+)",
            "Ba(2+)",
            //Hg(2+), Hg2(2+)
            "Hg"

    };

    @Override
    public void onCommand() {
        if (args.length!=6) {
            sendMessage("please provide up to four ions you wish to compare.");
        }


    }

    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("s(oluble)?");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getFullDescription() {
        return null;
    }
}
