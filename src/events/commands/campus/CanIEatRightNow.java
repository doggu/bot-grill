package events.commands.campus;

import events.commands.Command;
import main.BotMain;

import java.util.TimeZone;

public class CanIEatRightNow extends Command {
    private static final Schedule HALL;
    static {
        if (Schedule.PIONEER_HALL == null) {
            System.out.println("wtf");
        }
        HALL = Schedule.DLG_COMMONS;
    }



    public boolean isCommand() {
        if (!e.getMessage().getMentionedUsers().contains(e.getJDA().getSelfUser())) return false;
        return e.getMessage().getContentRaw()
                .contains("can i go eat right now");
    }

    public void onCommand() {
        //todo: temporary

        boolean canGo;
        switch(e.getAuthor().getId()) {
            case "125857288807251968": //me
                canGo = Schedule.PIONEER_HALL.canGo(TimeZone.getTimeZone("GMT-6"));
                break;
            case "235275443941539840": //danny
                canGo = Schedule.DLG_COMMONS.canGo(TimeZone.getTimeZone("GMT-8"));
                break;
            default:
                sendMessage("i don't know you, sorry.");
                return;
        }

        if (canGo) {
            sendMessage("yes!");
            return;
        }

        sendMessage("i don't think so.");
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
