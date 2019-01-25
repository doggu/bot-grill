package events.gameroom;

import events.ReactionListener;
import events.commands.FEHRetriever;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import utilities.fehUnits.heroes.Unit;
import utilities.fehUnits.summoning.Banner;
import utilities.fehUnits.summoning.Orb;

import java.util.ArrayList;
import java.util.List;

public class CircleSimulator extends ReactionListener {
    private final Message circleMessage;
    private final User summoner;
    private final Guild server;
    private final Banner banner;
    private final List<Orb> orbs;
    private final List<Emote> stones;
    private int pulls = 0;

    public CircleSimulator(Message message, User summoner, Banner banner) {
        super();
        this.circleMessage = message;
        this.server = circleMessage.getGuild();
        this.summoner = summoner;
        this.banner = banner;
        this.orbs = generateOrbs();

        List<Emote> stones = new ArrayList<>();

        int rOrbs = 1, bOrbs = 1, gOrbs = 1, cOrbs = 1;
        for (Orb x:orbs) {
            Emote stone;
            switch (x.getColor()) {
                case "Red":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("r_orb_"+rOrbs, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stones.add(stone);
                    rOrbs++;
                    break;
                case "Blue":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("b_orb_"+bOrbs, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stones.add(stone);
                    bOrbs++;
                    break;
                case "Green":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("g_orb_"+gOrbs, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stones.add(stone);
                    gOrbs++;
                    break;
                case "Colorless":
                    stone = circleMessage.getJDA()
                            .getEmotesByName("c_orb_"+cOrbs, true).get(0);
                    circleMessage.addReaction(stone).queue();
                    stones.add(stone);
                    cOrbs++;
                    break;
                default:
                    System.out.println("AaaAaaaaaaaaaaaAAAAAAAAAaAAAaaaaaaaaaa");
                    throw new Error();
            }
        }

        this.stones = stones;
    }

    private List<Orb> generateOrbs() {
        List<Orb> orbs = new ArrayList<>();

        for (int i=0; i<5; i++) {
            orbs.add(new Orb(banner));
        }

        return orbs;
    }



    public User getSummoner() { return summoner; }
    public Guild getServer() { return server; }

    public boolean canClose() {
        for (Orb x:orbs)
            if (x.isPulled())
                return true;
        return false;
    }



    @Override
    protected boolean isCommand() {
        if (!summoner.equals(e.getUser())) return false;
        if (!e.getMessageId().equals(circleMessage.getId())) return false;
        if (!e.getMessageId().equals(circleMessage.getId())) return false;
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

        //is not a custom emote (i hope)
        if (!e.getReaction().getReactionEmote().isEmote()) {
            System.out.println("what the fuck is this shit");
            return;
        }
        String stoneId = e.getReaction().getReactionEmote().getId();
        for (int i = 0; i < stones.size(); i++) {
            Emote stone = stones.get(i);
            if (stoneId.equals(stone.getId())) {
                Unit hero;
                try {
                    hero = orbs.get(i).pull();
                } catch (Exception g) {
                    sendMessage("cannot pull an orb that's already been pulled!");
                    log("user attempted to pull an already-pulled stone");
                    return;
                }

                //summoner.openPrivateChannel().complete().sendMessage(FEHRetriever.printUnit(hero, true)).complete();
                sendMessage(FEHRetriever.printUnit(hero, true));
            }
        }

        //remove my own reaction so they can't double up (even though there's protection already)
        //TODO: may want to keep self-reactions for historical purposes (user could remove their reactions)
        //e.getReaction().removeReaction(e.getJDA().getSelfUser()).queue();

        //there's a more efficient way to do this but this is absolute

        boolean circleComplete = true;
        for (Orb x : orbs) {
            if (!x.isPulled()) {
                circleComplete = false;
                break;
            }
        }

        if (circleComplete) {
            //interesting bit of code
            //e.getJDA().addEventListener();
            e.getJDA().removeEventListener(this);
        }
    }
}
