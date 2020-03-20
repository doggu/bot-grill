package events.commands.campus;

import events.commands.Command;
import main.BotMain;

import java.util.TimeZone;

public class CanIEatRightNow extends Command {
    public boolean isCommand() {
        if (!e.getMessage().getMentionedUsers()
                .contains(e.getJDA().getSelfUser()))
            return false;
        else
            return e.getMessage().getContentRaw()
                    .contains("can i go eat"/* right now"*/);
    }

    public void onCommand() {
        int hours;
        if (args.length==7) {
            hours = 0;
        } else if (args.length==8) {
            try {
                hours = Integer.parseInt(args[6]);
            } catch (NumberFormatException nfe) {
                sendMessage("please use the format: \n\t" +
                        e.getJDA().getSelfUser().getAsMention() +
                        " can i go eat in [n] hours");
                return;
            }
        } else {
            System.out.println(args.length);
            System.out.println(e.getMessage().getContentRaw());
            return;
        }

        switch (e.getAuthor().getId()) {
            case "125857288807251968": //me
                if (Schedule.PIONEER_HALL.canGo(
                        TimeZone.getTimeZone("GMT-6"),
                        hours)) {
                    sendMessage("yes!");
                } else {
                    sendMessage("no.");
                }
                break;
            case "235275443941539840": //da-CoUGH i mean Satan
                if (Schedule.DLG_COMMONS.canGo(
                        TimeZone.getTimeZone("GMT-8"),
                        hours)) {
                    if (Schedule.CARRILLO_COMMONS.canGo(
                            TimeZone.getTimeZone("GMT-8"),
                            hours)) {
                        sendMessage("yes! both Carrillo and DLG " +
                                (hours==0?"are":(hours>0?"will be":"were")) +
                                " open.");
                    } else {
                        sendMessage("yes! DLG " +
                                (hours==0?"is":(hours>0?"will be":"was")) +
                                " open.");
                    }
                } else if (Schedule.CARRILLO_COMMONS.canGo(
                        TimeZone.getTimeZone("GMT-8"),
                        hours)) {
                    sendMessage("yes! Carrillo " +
                            (hours==0?"is":(hours>0?"will be":"was")) +
                            " open.");
                } else {
                    sendMessage("sorry, neither DLG nor Carrillo " +
                            (hours==0?"are":(hours>0?"will be":"were")) +
                                    " open"+(hours==0?"":" at that time") +
                            ".");
                }
                break;
            default:
                sendMessage("i don't know you, sorry.");
        }
    }

    @Override
    public String getName() {
        return "CanIEatRightNow";
    }

    @Override
    public String getDescription() {
        return "can you go to eat at your residential dining hall?";
    }

    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    protected char getPrefix() {
        return BotMain.bot_grill.getSelfUser().getAsMention().charAt(0);
    }
}
