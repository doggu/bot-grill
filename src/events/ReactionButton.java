package events;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;

import static main.BotMain.bot_grill;

public class ReactionButton extends ReactionListener {
    private final Message message;
    private final Emote emote;
    private final MessageBuilder response;

    public ReactionButton(Message message, Emote emote, MessageBuilder response) {
        this.message = message;
        this.emote = emote;
        this.response = response;

        message.addReaction(emote).complete();

        bot_grill.addEventListener(this);
    }



    public boolean isCommand() {
        if (e.getUser().isBot()) return false;
        if (!e.getMessageId().equals(message.getId())) return false;
        if (!e.getReactionEmote().getEmote().getId().equals(emote.getId())) return false;

        return true;
    }

    public void onCommand() {
        sendMessage(response.build());
        e.getJDA().removeEventListener(this);
    }
}
