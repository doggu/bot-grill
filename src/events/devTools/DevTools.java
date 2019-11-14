package events.devTools;

import events.MessageListener;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class DevTools extends MessageListener {
    private static AudioManager am = null;

    public void onCommand() {
        System.out.println(e.getMessage().getContentRaw());
        switch(args[0].toLowerCase()) {
            case "kill":
                main.BotMain.bot_grill.shutdownNow();
                System.exit(1);
                break;
            case "sendmessage":
                StringBuilder message = new StringBuilder();
                for (int i = 1; i<args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                sendMessage(message.toString());
                break;
            case "invade":
                VoiceChannel channel = e.getGuild()
                        .getVoiceChannelsByName("musak", true).get(0);
                am = e.getGuild().getAudioManager();

                am.openAudioConnection(channel);
                break;
            case "retreat":
                if (am==null) {
                    sendMessage("i haven't invaded yet.");
                } else {
                    am.closeAudioConnection();
                    am = null;
                }
        }
    }

    public boolean isCommand() {
        return e.getAuthor().getId().equals("125857288807251968"); //doggu, me
    }

    public char getPrefix() {
        return '&';
    }
}
