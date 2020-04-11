package discordUI.menu;

import events.ReactionListener;
import main.BotMain;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

/*
this class allows for (only) the viewing of specific items in a list.

the displaying of an infinite amount of items is done by generating
navigating arrows (reactions) and proceeding buttons, labelled 1-5,
corresponding to embed text indicating each one's purpose.

clicking the arrows can either advance or return to different
parts of the menu, with the reaction buttons remaining the same--
corresponding to a specific row in the generated table.

todo: make this a lower-level class with extensions?
 (such as "VisualMenu" and specialized ones like "HelpMenu")
 */
public class Menu extends ReactionListener {
    private final User user;
    private Message message;
    private final ArrayList<MenuEntry> entries;
    private final ArrayList<Message> windows;
    private int currentWindow = 0;



    public Menu(User user, MessageChannel channel,
                Message header, ArrayList<MenuEntry> entries) {
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

        if (!embed.toString()
                .equals(new MessageBuilder(header).append("\n\n").toString()))
            windows.add(embed.build());

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
        message.addReaction(R_ARROW).queue();
        message.addReaction(ONE).queue();
        message.addReaction(TWO).queue();
        message.addReaction(THREE).queue();
        message.addReaction(FOUR).queue();
        message.addReaction(FIVE).queue();
    }



    private void displayPrevPage() {
        currentWindow--;
        displayNewPage();
    }
    private void displayNextPage() {
        currentWindow++;
        displayNewPage();
    }
    private void displayNewPage() throws IndexOutOfBoundsException {
        message = message.editMessage(windows.get(currentWindow))
                .complete();
    }
    private void displayEntry(int item) {
        message = message.editMessage(
                new MessageBuilder(
                        message)
                        .setEmbed(entries.get(currentWindow*5+item-1).build())
                        .build())
                .complete();
    }



    public boolean isCommand() {
        if (e.getUser().isBot()) return false;
        if (!e.getUser().getId().equals(user.getId())) return false;

        return e.getChannel().retrieveMessageById(e.getMessageId()).complete()
                .equals(message);
    }

    public void onCommand() {
        switch (e.getReaction().getReactionEmote().toString()) {
            case "RE:◀(null)":
                try {
                    displayPrevPage();
                } catch (IndexOutOfBoundsException ioobe) {
                    sendMessage("you are already on the first page!");
                    currentWindow++;
                }
                break;
            case "RE:▶(null)":
                try {
                    displayNextPage();
                } catch (IndexOutOfBoundsException ioobe) {
                    sendMessage("you are already on the last page!");
                    currentWindow--;
                }
                break;
            default:
                try {
                    switch (e.getReaction().getReactionEmote().toString()) {
                        case "RE:1⃣(null)":
                            displayEntry(1);
                            break;
                        case "RE:2⃣(null)":
                            displayEntry(2);
                            break;
                        case "RE:3⃣(null)":
                            displayEntry(3);
                            break;
                        case "RE:4⃣(null)":
                            displayEntry(4);
                            break;
                        case "RE:5⃣(null)":
                            displayEntry(5);
                            break;
                        default:
                            System.out.println(
                                    e.getReaction().getReactionEmote()
                                            .toString());
                            break;
                    }
                } catch (IndexOutOfBoundsException ioobe) {
                    sendMessage("that entry does not exist!");
                }
        }

        e.getReaction().removeReaction(user).queue();
    }
}
