package events.fehGame;

import events.ReactionListener;
import events.commands.FEHRetriever;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import utilities.feh.heroes.Unit;
import utilities.feh.summoning.Banner;
import utilities.feh.summoning.Stone;
import utilities.feh.players.Summoner;

import java.util.ArrayList;
import java.util.List;

public class CircleSimulator extends ReactionListener {
    private final Message circleMessage;
    private final Summoner summoner;
    private final Guild server;
    private final Banner banner;
    private final List<Stone> stones;
    private final List<Emote> stoneEmotes;
    private int pulls = 0;



    public CircleSimulator(Message message, Summoner summoner, Banner banner) {
        super();
        this.circleMessage = message;
        this.server = circleMessage.getGuild();
        this.summoner = summoner;
        this.banner = banner;
        this.stones = generateStones();

        List<Emote> stoneEmotes = new ArrayList<>();

        int rStones = 1, bStones = 1, gStones = 1, cStones = 1;
        for (Stone x:stones) {
            Emote stone;
            switch (x.getColor()) {
                case "Red":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("r_stone_"+rStones, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stoneEmotes.add(stone);
                    rStones++;
                    break;
                case "Blue":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("b_stone_"+bStones, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stoneEmotes.add(stone);
                    bStones++;
                    break;
                case "Green":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("g_stone_"+gStones, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stoneEmotes.add(stone);
                    gStones++;
                    break;
                case "Colorless":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("c_stone_"+cStones, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stoneEmotes.add(stone);
                    cStones++;
                    break;
                default:
                    System.out.println("AaaAaaaaaaaaaaaAAAAAAAAAaAAAaaaaaaaaaa");
                    throw new Error();
            }
        }

        circleMessage.addReaction("❌").queue();

        this.stoneEmotes = stoneEmotes;
    }



    public Summoner getSummoner() { return summoner; }
    public User getUser() { return summoner.getUser(); }
    public Guild getServer() { return server; }



    private List<Stone> generateStones() {
        List<Stone> stones = new ArrayList<>();

        for (int i=0; i<5; i++) {
            stones.add(new Stone(banner));
        }

        return stones;
    }

    boolean canClose() {
        for (Stone x:stones)
            if (x.isPulled())
                return true;
        return false;
    }

    void closeCircle() {
        summoner.stopSummoning();
        e.getJDA().removeEventListener(this);
    }



    @Override
    protected boolean isCommand() {
        if (!summoner.getUser().equals(e.getUser())) return false;
        if (!e.getMessageId().equals(circleMessage.getId())) return false;
        if (!e.getMessageId().equals(circleMessage.getId())) return false; //what the fuck
        if (e.getReaction().isSelf()) return false; //it's for readability i swear

        //these are apparently equivalent
        //System.out.println(e.getReaction().getReactionEmote().getId());
        //System.out.println(e.getReaction().getReactionEmote().getEmote().getId());

        //ReactionPaginationAction g = event.getReaction().getUsers();

        return true;
    }

    @Override
    protected void onCommand() {
        //the summoner has selected a stone, and a unit must be presented



        //is not a custom emote or stop emote (i hope)
        if (!e.getReaction().getReactionEmote().isEmote()) {
            if (e.getReactionEmote().toString().equals("RE:❌(null)")) {
                if (canClose()) {
                    closeCircle();
                    return;
                } else {
                    e.getReaction().removeReaction(summoner.getUser()).queue();
                    sendMessage("please choose at least one orb before closing this session.");
                    return;
                }
            } else {
                System.out.println("what the fuck is this shit");
                return;
            }
        } else {
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

                    //summoner.openPrivateChannel().complete().sendMessage(FEHRetriever.printUnit(hero, true)).complete();
                    sendMessage(FEHRetriever.printUnit(hero, true));
                    summoner.getBarracks().add(hero);

                    int cost = 0;
                    switch (pulls) {
                        case 0: //first pull
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

        //remove my own reaction so they can't double up (even though there's protection already)
        //TODO: may want to keep self-reactions for historical purposes (user could remove their reactions)
        //e.getReaction().removeReaction(e.getJDA().getSelfUser()).queue();

        //there's a more efficient way to do this but this is absolute

        boolean circleComplete = true;
        for (Stone x : stones) {
            if (!x.isPulled()) {
                circleComplete = false;
                break;
            }
        }


        if (circleComplete)
            closeCircle();
    }
}
