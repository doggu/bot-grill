package events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public abstract class ReactionButton extends ReactionListener {
    private final Message message;
    private final Emote emote;

    protected ReactionButton(Message message, Emote emote) {
        this.message = message;
        this.emote = emote;

        message.addReaction(emote).complete();
    }



    public Message getMessage() { return message; }



    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        this.e = event;

        if (isCommand()) {
            onCommand();
            commitSuicide();
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {

    }

    public boolean isCommand() {
        if (e.getUser().isBot()) return false;
        if (!e.getMessageId().equals(message.getId())) return false;
        /*
        if (!e.getReactionEmote().getEmote().getId().equals(emote.getId())) return false;

        return true;

         *///just because i lov u jetbreins and want to see u check mark

        return e.getReactionEmote().getEmote().getId().equals(emote.getId());
    }

    public abstract void onCommand();
}
