package discordUI.button;

import events.ReactionListener;
import main.BotMain;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ReactionButton extends ReactionListener {
    private final Message message;
    private final Emote emote;
    private final String emoticon;



    @NotNull
    protected ReactionButton(Message message, Emote emote) {
        this.message = message;
        this.emote = emote;
        this.emoticon = null;

        message.addReaction(emote).complete();
        BotMain.addListener(this);
    }
    protected ReactionButton(Message message, String emoticon) {
        this.message = message;
        this.emote = null;
        this.emoticon = emoticon;

        message.addReaction(emoticon).complete();
        BotMain.addListener(this);
    }



    public Message getMessage() { return message; }



    //if a user never uses the button, it may be destroyed by something outside the class
    public void removeButton() {
        if (emote!=null) {
            for (MessageReaction reaction : e.getJDA().getTextChannelById(
                    message.getChannel().getId()).getMessageById(message.getId()).complete().getReactions()) {
                if (!reaction.getReactionEmote().isEmote()) continue;
                if (reaction.getReactionEmote().getEmote().equals(emote)) {
                    reaction.removeReaction().queue();
                }
            }
        } else {
            for (MessageReaction reaction : e.getJDA().getTextChannelById(
                    message.getChannel().getId()).getMessageById(message.getId()).complete().getReactions()) {
                if (reaction.getReactionEmote().isEmote()) continue;
                if (reaction.getReactionEmote().toString().equals("RE:"+emoticon+"(null)")) {
                    reaction.removeReaction().queue();
                }
            }
        }

        commitSuicide();
    }



    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        this.e = event;

        if (isCommand()) {
            onCommand();
            commitSuicide();
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        this.e = event;
    }

    public boolean isCommand() {
        if (e.getUser().isBot()) return false;
        if (!e.getMessageId().equals(message.getId())) return false;

        if (emote!=null)
            return e.getReactionEmote().getEmote().getId().equals(emote.getId());
        else
            return e.getReactionEmote().toString().equals("RE:"+emoticon+"(null)");
    }

    public abstract void onCommand();
}
