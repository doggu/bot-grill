package events.commands.campus;

import events.commands.Command;
import main.BotMain;

public class CanIEatRightNow extends Command {
    private static final Schedule PIONEER_HALL;
    static {
        if (Schedule.PIONEER_HALL == null) {
            System.out.println("wtf");
        }
        PIONEER_HALL = Schedule.PIONEER_HALL;
    }



    public boolean isCommand() {
        if (!e.getMessage().getMentionedUsers().contains(e.getJDA().getSelfUser())) return false;
        return e.getMessage().getContentRaw()
                .contains("can i go eat right now");
    }

    public void onCommand() {
        if (PIONEER_HALL.canGo()) {
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
        return "can Everett go to eat at Pioneer Hall?";
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
