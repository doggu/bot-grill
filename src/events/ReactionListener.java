package events;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

public abstract class ReactionListener extends Listener {
    protected GenericMessageReactionEvent e;



    protected abstract boolean isCommand();
    protected abstract void onCommand();



    //TODO: create ability for multiple commands
    // so i don't have to make 4885974985 listeners
    // (unless creating individual listeners is correct/efficient)
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        this.e = event;
        if (isCommand()) {
            onCommand();
        }
    }

    protected Message sendMessage(String message) {
        MessageAction g = e.getChannel().sendMessage(message);
        return g.complete();
    }
    protected Message sendMessage(Message message) {
        MessageAction g = e.getChannel().sendMessage(message);
        return g.complete();
    }
    protected void addReaction(Emote emote) {
        e.getReaction()
                .getChannel()
                .retrieveMessageById(e.getMessageId())
                .complete()
                .addReaction(emote)
                .queue();
    }
}
