package events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.jetbrains.annotations.NotNull;

public abstract class PersonalButton extends ReactionButton {
    private final User user;

    @NotNull
    protected PersonalButton(Message message, Emote emote, User user) {
        super(message, emote);
        this.user = user;
    }
    protected PersonalButton(Message message, String emoticon, User user) {
        super(message, emoticon);
        this.user = user;
    }

    @Override
    public boolean isCommand() {
        return super.isCommand()&&e.getUser().equals(user);
    }
}
