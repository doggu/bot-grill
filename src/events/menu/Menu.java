package events.menu;

import events.ReactionListener;
import main.BotMain;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

/*
this class allows for (only) the viewing of specific items in a list.

the displaying of an infinite amount of items is done by generating
navigating arrows (reactions) and proceeding buttons, labelled 1-5,
corresponding to embed text indicating each one's purpose.

clicking the arrows can either advance or return to different
parts of the menu, with the reaction buttons remaining the same--
corresponding to a specific row in the generated table.

todo: make this a lower-level class with extensions such as "VisualMenu" and specialized ones like "HelpMenu"?
 */
public class Menu extends ReactionListener {
    private final User user;
    private Message message;
    private final ArrayList<MenuEntry> entries;
    private final ArrayList<Message> windows;
    private int currentWindow = 0;
    private ArrayList<MessageReaction> userInput = new ArrayList<>();



    public Menu(User user, MessageChannel channel, Message header, ArrayList<MenuEntry> entries) {
        this.user = user;
        this.entries = entries;
        this.windows = generateWindows(header);

        this.message = channel.sendMessage(windows.get(0)).complete();
        initializeMenu();
        register();
    }

    private void register() {
        BotMain.addListener(this);
    }

    private ArrayList<Message> generateWindows(Message header) {
        ArrayList<Message> windows = new ArrayList<>();

        MessageBuilder embed = new MessageBuilder(header).append("\n\n");
        for (int i=0; i<entries.size(); i++) {
            embed.append(i%5+1).append(". ").append(entries.get(i).getTitle());

            if ((i+1)%5==0) {
                windows.add(embed.build());
                embed = new MessageBuilder(header).append("\n\n");
            } else
                embed.append('\n');
        }

        return windows;
    }

    private void initializeMenu() {
        createReactions();
    }

    private static final String
            L_ARROW = "◀",
            R_ARROW = "▶",
            ONE = "1⃣",
            TWO = "2⃣",
            THREE = "3⃣",
            FOUR = "4⃣",
            FIVE = "5⃣";
    private void createReactions() {
        message.addReaction(L_ARROW).queue();
        message.addReaction(ONE).queue();
        message.addReaction(TWO).queue();
        message.addReaction(THREE).queue();
        message.addReaction(FOUR).queue();
        message.addReaction(FIVE).queue();
        message.addReaction(R_ARROW).queue();
    }

    private void clearUserInput() {
        for (MessageReaction r:userInput) {
            r.removeReaction(user).queue();
        }

        userInput = new ArrayList<>();
    }



    public boolean isCommand() {
        if (e.getUser().isBot()) return false;
        return e.getUser().getId().equals(user.getId());
    }

    public void onCommand() {
        userInput.add(e.getReaction());
        switch (e.getReaction().getReactionEmote().toString()) {
            case "RE:◀(null)":
                clearUserInput();
                currentWindow--;
                try {
                    message = message.editMessage(windows.get(currentWindow))
                            .complete();
                } catch (IndexOutOfBoundsException ioobe) {
                    sendMessage("you are already on the first page!");
                    currentWindow++;
                }
                break;
            case "RE:▶(null)":
                clearUserInput();
                currentWindow++;
                try {
                    message = message.editMessage(windows.get(currentWindow))
                            .complete();
                } catch (IndexOutOfBoundsException ioobe) {
                    sendMessage("you are already on the last page!");
                    currentWindow--;
                }
                break;
            case "RE:1⃣(null)":
                message.editMessage(new MessageBuilder(message)
                        .setEmbed(entries.get(currentWindow*5).build())
                        .build()).queue();
                break;
            case "RE:2⃣(null)":
                message.editMessage(new MessageBuilder(message)
                        .setEmbed(entries.get(currentWindow*5+1).build())
                        .build()).queue();
                break;
            case "RE:3⃣(null)":
                message.editMessage(new MessageBuilder(message)
                        .setEmbed(entries.get(currentWindow*5+2).build())
                        .build()).queue();
                break;
            case "RE:4⃣(null)":
                message.editMessage(new MessageBuilder(message)
                        .setEmbed(entries.get(currentWindow*5+3).build())
                        .build()).queue();
                break;
            case "RE:5⃣(null)":
                message.editMessage(new MessageBuilder(message)
                        .setEmbed(entries.get(currentWindow*5+4).build())
                        .build()).queue();
                break;
            default:
                System.out.println(e.getReaction().getReactionEmote().toString());
                break;
        }
    }
}
