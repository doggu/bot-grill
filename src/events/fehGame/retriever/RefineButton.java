package events.fehGame.retriever;

import discordUI.feh.FEHPrinter;
import feh.characters.skills.skillTypes.Weapon;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RefineButton extends ListenerAdapter {
    private static final long REFINE_BUTTON_ID = 588311818217324544L;



    private GenericMessageReactionEvent e;
    private final Message message;
    private final Weapon weapon;



    RefineButton(Message message, Weapon weapon) {
        message.addReaction(message.getJDA().getEmoteById(REFINE_BUTTON_ID))
                .complete();
        this.message = message;
        this.weapon = weapon;
    }



    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        this.e = e;

        if (isCommand()) {
            setRefine();
        }
    }

    private void setRefine() {
        e.getChannel().retrieveMessageById(e.getMessageId()).complete()
                .editMessage(FEHPrinter.printSkill(weapon.getRefine()).build())
                .complete();
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        this.e = e;

        if (isCommand()) {
            setWeapon();
        }
    }

    private void setWeapon() {
        e.getChannel().retrieveMessageById(e.getMessageId()).complete()
                .editMessage(FEHPrinter.printSkill(weapon).build()).complete();
    }

    public boolean isCommand() {
        if (e.getUser().isBot()) return false;
        if (!e.getMessageId().equals(message.getId())) return false;
        if (e.getReactionEmote().getEmote()==null) return false;
        return e.getReactionEmote().getEmote()
                .equals(e.getJDA().getEmoteById(REFINE_BUTTON_ID));
    }
}
