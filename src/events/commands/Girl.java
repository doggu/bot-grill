package events.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;

import java.util.ArrayList;
import java.util.List;

public class Girl extends Command {
    private static String[] girlNames = {
            //main
            "highlvlthinking",
            "we",
            "merbel",
            "blaze",
            "safetyfirst",
            "flying",
            "respecc",
            "smorte",
            "hydrated",
            "harmony",
            "triggered",
            "kay",
            "theOrbs",
            "baedaS",
            "mOrth",
            "naisu",
            "moZu",
            "oOf",
            "immaculate",
            "she",
            "severa",
            "nowi",
            "fjOrm",
            "dabYuu",
            "dabChii",
            "lewd",
            //emojirepo
            "abigail",
            "baeda",
            "bonds",
            "eff",
            "faste",
            "felutino",
            "goodbye",
            "hat",
            "kanae",
            "kondouodnok",
            "legg",
            "painful",
            "porn",
            "strategizingF",
            "superiority",
            "thnyankINg",
            "tropicalAttack",
            "trouble",
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
            int r = (int) Math.floor(Math.random()*girls.size());
            addReaction(girls.get(r));
            log("sent a girl");
        } else {
            StringBuilder msg = new StringBuilder();
            int pkgs = 0;
            for (int i=1; i<=times; i++) {
                int r = (int) Math.floor(Math.random()*girls.size());
                String emote = "<:"+girls.get(r).getName()+":"+girls.get(r).getId()+">";
                msg.append(emote);
                if (i%40==0) {
                    sendMessage(msg);
                    msg = new StringBuilder();
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

        for (String name:girlNames) {
            List<Emote> e = jda.getEmotesByName(name, false);
            if (e.size()==0) {
                System.out.println("broke");
                System.out.println(name);
            } else if (e.size()>1) {
                System.out.println("wtf, there are two emotes with the exact same name");
            } else {
                girls.add(e.get(0));
            }
        }
    }



    public String getName() { return "Girl"; }
    public String getDescription() { return "Print a bunch of emotes of girls!"; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "Syntax: \"?Girl\" OR \"?Girl [number of girls]\"\n" +
                "The first syntax will react to the command message with a random girl, while " +
                "the second syntax will print as many girls as indicated. Too large a number " +
                "will produce a serious amount of messages—please use wisely!";

    }
}
