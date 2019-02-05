package events.fehGame;

import events.ReactionListener;
import events.commands.FEHRetriever;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import utilities.feh.heroes.Unit;
import utilities.feh.summoning.Banner;
import utilities.feh.summoning.Orb;
import utilities.feh.players.Summoner;

import java.util.ArrayList;
import java.util.List;

public class CircleSimulator extends ReactionListener {
    private final Message circleMessage;
    private final Summoner summoner;
    private final Guild server;
    private final Banner banner;
    private final List<Orb> orbs;
    private final List<Emote> stones;
    private int pulls = 0;



    public CircleSimulator(Message message, Summoner summoner, Banner banner) {
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

        circleMessage.addReaction("❌").complete();

        this.stones = stones;
    }



    public Summoner getSummoner() { return summoner; }
    public User getUser() { return summoner.getUser(); }
    public Guild getServer() { return server; }



    private List<Orb> generateOrbs() {
        List<Orb> orbs = new ArrayList<>();

        for (int i=0; i<5; i++) {
            orbs.add(new Orb(banner));
        }

        return orbs;
    }

    boolean canClose() {
        for (Orb x:orbs)
            if (x.isPulled())
                return true;
        return false;
    }

    private void closeCircle() {
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
        for (Orb x : orbs) {
            if (!x.isPulled()) {
                circleComplete = false;
                break;
            }
        }


        if (circleComplete)
            closeCircle();
    }
}
