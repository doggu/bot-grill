package events.reactionary;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Quips extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().toLowerCase()
                .matches("no?ice|naisu|\\uD83D\\uDC4C"))
            event.getMessage().addReaction("\uD83D\uDC4C").complete();

        if (event.getAuthor().getId().equals("425348396015157258"))
            if (event.getMessage().getContentRaw().toLowerCase().contains("okay"))
                event.getMessage().addReaction(event.getJDA().getEmoteById("581366103872372738"))
                        .complete();
    }
}
