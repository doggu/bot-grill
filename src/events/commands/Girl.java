package events.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;

import java.util.ArrayList;
import java.util.List;

public class Girl extends Command {
    private static String[] girlIDs = {
            "455957293566263298",
            "455957289447194625",
            "455957293633372173",
            "436792188832186369",
            "436792539480326154",
            "455957293771653131",
            "436792146004279297",
            "455957289598189588",
            "436792289839415317",
            "436792550297305089",
            "436792157198614528",
            "454900010816110593",
            "436792572950740992",
            "455957293641760790",
            "436792763951087627",
            "455957293494697995",
            "455957293469663234",
            "455967748984537090",
            "454458123508252692",
            "387416607984713730",
            "454410627780182029",
            "449124595174342656",
            "368939836113158154",
            "478485191233372161",
            "368939844900093953",
            "412536856773656576",
            "367898634894966784",
            "450154813410508820",
            "470826600720629760",
            "399886280340537346",
            "470828589777158144",
            "452318605959364608",
            "387413363455688704",
            "438871170490695691",
            "452323104996196363",
            "474728931359064075",
            "401907489307885568",
            "461701236308312066",
            "392250099104874496",
            "428444386297118730",
            "411384701140336640",
            "455980816191979534",
            "458123987713785856",
            "408825417626288135",
            "447305265612587009",
    };
    private List<Emote> girls = new ArrayList<>();

    public void onCommand() {
        compileList(); //create list of girls based on ID array

        int times = 1;
        if (args.length>1) {
            try {
                times = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                //continue to reaction
            }
        }

        if (times==1) {
            int r = (int)Math.floor(Math.random()*girls.size());
            addReaction(girls.get(r));
            log("sent a girl");
        } else {
            String msg = "";
            int pkgs = 0;
            for (int i=1; i<=times; i++) {
                int r = (int)Math.floor(Math.random()*girls.size());
                String emote = "<:"+girls.get(r).getName()+":"+girls.get(r).getId()+">";
                msg+= emote;
                if (i%40==0) {
                    sendMessage(msg);
                    msg = "";
                    pkgs++;
                }
            }

            if (msg.length()>0) {
                sendMessage(msg);
                pkgs++;
            }
            log("sent "+times+" girls ("+pkgs+" packages)");
        }
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("girl");
    }

    private void compileList() {
        JDA jda = e.getJDA();

        for (int i=0; i<girlIDs.length; i++) {
            Emote e = jda.getEmoteById(girlIDs[i]);
            girls.add(e);
        }
    }
}
