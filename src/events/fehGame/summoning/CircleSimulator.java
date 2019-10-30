package events.fehGame.summoning;

import discordUI.button.PersonalButton;
import discordUI.feh.FEHPrinter;
import events.ReactionListener;
import feh.characters.hero.Hero;
import feh.characters.unit.Unit;
import feh.players.Summoner;
import feh.summoning.Banner;
import feh.summoning.Stone;
import main.BotMain;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CircleSimulator extends ReactionListener {
    private final MessageChannel messageChannel;
    private final Message circleMessage;
    private final Summoner summoner;
    private final Banner banner;
    private final List<Stone> stones;
    private final List<Emote> stoneEmotes;
    private PersonalButton newButton, stopButton;
    private int pulls = 0;



    CircleSimulator(MessageChannel messageChannel, Summoner summoner, Banner banner) {
        this.messageChannel = messageChannel;
        this.summoner = summoner;
        this.banner = banner;
        this.circleMessage = messageChannel.sendMessage(generateMessage()).complete();
        this.stones = generateStones();
        this.stoneEmotes = generateEmotes();
    }



    public Summoner getSummoner() { return summoner; }
    public User getUser() { return summoner.getUser(); }
    Message getSessionMessage() { return circleMessage; }



    private Message generateMessage() {
        MessageBuilder message = new MessageBuilder("your summons for: \n")
                .append(banner.getName())
                .append('\n')
                .append("featured units: ");
        for (Hero x:banner.getRarityFPool()) { //Character is not a good name for a class (changed to Hero)
            message.append(x.getFullName().getName());
            if (x.getFullName().isAmbiguousName())
                message
                        .append(" (")
                        .append(x.getWeaponType())
                        .append(' ')
                        .append(x.getMoveType())
                        .append(')');
            //there are three axe armor hectors btw
            message.append(", ");
        }
        message.replaceLast(", ", "");

        GregorianCalendar startDate = banner.getStartDate();
        //MONTH IS ZERO-BASED (again)
        message.append("\n").append(startDate.get(Calendar.MONTH)+1)
                .append("/").append(startDate.get(Calendar.DAY_OF_MONTH))
                .append("/").append(startDate.get(Calendar.YEAR));

        GregorianCalendar endDate = banner.getEndDate();
        //MONTH IS ZERO-BASED (again)
        message.append(" - ").append(endDate.get(Calendar.MONTH)+1)
                .append("/").append(endDate.get(Calendar.DAY_OF_MONTH))
                .append("/").append(endDate.get(Calendar.YEAR));

        return message.build();
    }

    private List<Stone> generateStones() {
        List<Stone> stones = new ArrayList<>();

        for (int i=0; i<5; i++) {
            stones.add(new Stone(banner));
        }

        return stones;
    }

    private List<Emote> generateEmotes() {
        ArrayList<Emote> stoneEmotes = new ArrayList<>();

        int rStones = 1, bStones = 1, gStones = 1, cStones = 1;
        for (Stone x:stones) {
            char color = x.getColor();
            Emote stone;
            switch (color) {
                case 'r':
                    stone = getStoneEmote(color, rStones);
                    rStones++;
                    break;
                case 'g':
                    stone = getStoneEmote(color, gStones);
                    gStones++;
                    break;
                case 'b':
                    stone = getStoneEmote(color, bStones);
                    bStones++;
                    break;
                case 'c':
                    stone = getStoneEmote(color, cStones);
                    cStones++;
                    break;
                default:
                    throw new Error("invalid color argument passed!");
            }
            circleMessage.addReaction(stone).queue();
            stoneEmotes.add(stone);
        }

        return stoneEmotes;
    }

    private Emote getStoneEmote(char color, int level) {
        return circleMessage.getJDA()
                .getEmotesByName(color+"_stone_"+level, true)
                .get(0);
    }

    void register() {
        summoner.startSummoning(this);
        BotMain.addListener(this);
    }

    private void addFinishButtons() {
        newButton = new PersonalButton(circleMessage, "\uD83D\uDD04", summoner.getUser()) {
            @Override
            public void onCommand() {
                //idk if there's some 4D hacking someone can do to break this but this error checking is unnecessary
                if (canClose()) {
                    closeCircle();
                    new CircleSimulator(messageChannel, summoner, banner).register();
                } else {
                    sendMessage("please choose at least one orb before starting a new session.");
                    return;
                }

                e.getReaction().removeReaction().queue();
                e.getReaction().removeReaction(summoner.getUser()).queue();

                stopButton.removeButton();
            }
        };
        stopButton = new PersonalButton(circleMessage, "❌", summoner.getUser()) {
            @Override
            public void onCommand() {
                if (canClose()) {
                    closeCircle();
                } else {
                    sendMessage("please choose at least one orb before starting a new session.");
                    return;
                }

                e.getReaction().removeReaction().queue();
                e.getReaction().removeReaction(summoner.getUser()).queue();

                newButton.removeButton();
            }
        };
    }

    boolean canClose() {
        for (Stone x:stones)
            if (x.isPulled())
                return true;
        return false;
    }

    //for later reference, commitSuicide being called here will never cause a null pointer exception
    //because they must have summoned a stone before they may close the circle.
    void closeCircle() {
        summoner.stopSummoning();
        //circleMessage.clearReactions().complete();
        commitSuicide();
    }



    @Override
    public void commitSuicide() {
        newButton.removeButton();
        stopButton.removeButton();
        super.commitSuicide();
    }

    @Override
    protected void onCommand() {
        //the summoner has selected a stone, and a unit must be presented



        //is not a custom emote or stop emote (i hope)
        if (e.getReaction().getReactionEmote().isEmote()) {
            String stoneId = e.getReaction().getReactionEmote().getId();
            for (int i=0; i<stoneEmotes.size(); i++) {
                Emote stone = stoneEmotes.get(i);
                if (stoneId.equals(stone.getId())) {
                    Unit hero;
                    try {
                        hero = stones.get(i).pull();
                    } catch (Exception g) {
                        sendMessage("cannot pull an orb that's already been pulled!");
                        log("user attempted to pull an already-pulled stone");
                        return;
                    }

                    //summoner.openPrivateChannel().complete()
                    // .sendMessage(FEHRetriever.printUnit(hero, true)).complete();
                    circleMessage.editMessage(
                            FEHPrinter.printUnit(hero)
                                    .build()).complete();
                    summoner.getBarracks().add(hero);

                    int cost = 0;
                    switch (pulls) {
                        case 0: //first pull
                            //the circle is now closeable
                            addFinishButtons();
                            cost++;
                        case 1:
                        case 2:
                        case 3:
                            cost++;
                        case 4: //fifth pull
                            cost += 3;
                            summoner.spendOrbs(cost);
                    }
                    pulls++;
                }
            }
        }

        //there's a more efficient way to do this but this is absolute

        boolean circleComplete = true;
        for (Stone x : stones) {
            if (!x.isPulled()) {
                circleComplete = false;
                break;
            }
        }


        if (circleComplete) {
            commitSuicide();
            stopButton.removeButton();
        }
    }

    @Override
    protected boolean isCommand() {
        if (e.getReaction().isSelf()) return false;
        if (!e.getMessageId().equals(circleMessage.getId())) return false;
        return summoner.getUser().equals(e.getUser());
    }
}
