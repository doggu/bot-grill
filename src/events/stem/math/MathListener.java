package events.stem.math;

import discordUI.button.ReactionButton;
import events.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import stem.math.MathParse;

import java.util.HashMap;

public class MathListener extends Command {
    public static HashMap<String,Double> answers = new HashMap<>();
    private static final HashMap<String, HashMap<String, Double>>
            storedValues = new HashMap<>();
//    private HashMap<String, ArrayList<Function<Double, Double>>>
//            storedFunctions = new HashMap<>();


    public void onCommand() {
        String problem;
        double tv;

        if (args.length==1) {
            problem = args[0];
            tv = 1;
        } else if (args.length==2) {
            problem = args[1];
            try {
                tv = Double.parseDouble(args[1]);
            } catch (NumberFormatException g) {
                sendMessage("incorrect test value! please try again.");
                return;
            }
        } else if (args.length==3) {
            if (args[0].toLowerCase().matches("sto(re)?")) {
                String var = args[1];
                String value = args[2];

                if (value.equalsIgnoreCase("ans")) {
                    value = String.valueOf(answers.get(e.getAuthor().getId()));
                    //todo: how do i fix this better
                    value = value.replace("E", "*10^");
                }

                double val;
                try {
                    val = Double.parseDouble(value);
                } catch (NumberFormatException nfe) {
                    try {
                        val = new MathParse(value).getFunction().apply(0.0);
                    } catch (Exception e) {
                        sendMessage(
                                "could not decipher your value, " +
                                "please try again.");
                        return;
                    }
                }

                storedValues.computeIfAbsent(
                        e.getAuthor().getId(),
                        k -> new HashMap<>());
                storedValues
                        .get(e.getAuthor().getId())
                        .put(var, val);

                sendMessage("stored "+val+" in "+var+'.');
            }
            return;
        } else {
            return;
        }

        if (problem.contains("ans")) {
            double ans;
            try {
                ans = answers.get(e.getAuthor().getId());
            } catch (NullPointerException g) {
                sendMessage("no previous answer was found!");
                return;
            }
            String ansStr = String.valueOf(ans);
            problem = problem.replace(
                    "ans",
                    "("+ansStr+")");
        }

        HashMap<String, Double> userValues =
                storedValues.get(e.getAuthor().getId());

        if (userValues!=null) {
            for (String x:userValues.keySet()) {
                if (problem.contains(x)) {
                    problem = problem.replaceAll(
                            x, String.valueOf(userValues.get(x)));
                }
            }
        }

        problem = problem.replace("E", "*10^");


        double value = new MathParse(problem).getFunction().apply(tv);

        Message m;

        if (problem.contains("x"))
            m = sendMessage('`'+problem + "`\n`f("+tv+") = " + value+'`');
        else
            m = sendMessage('`'+problem+"`\n = `"+value+'`');

        answers.put(e.getAuthor().getId(), value);

        final String aleksPrint = String.valueOf(value)
                .replace("E", "*10^");

        new ReactionButton(m,
                e.getJDA().getEmotesByName(
                        "aleks",
                        true)
                .get(0)) {
            @Override
            public void onCommand() {
                getMessage().editMessage(
                        new EmbedBuilder().setTitle("`"+aleksPrint+"`")
                        .build()).complete();
            }
        };
        answers.put(e.getAuthor().getId(), value);
    }

    //todo: lots of dupicate cocodododleldckdfgddahgljkdfg
    public void onMessageReceived(MessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        if (e.getAuthor().isBot()) return;
        if (message.length()==0) return;
        this.e = e;
        this.args = message.split(" ");
        if (message.charAt(0)!=getPrefix()) {
            if (e.getChannel().getName().equalsIgnoreCase("math")) {
                e.getChannel().sendTyping().complete();
                onCommand();
            }
        } else {
            args[0] = args[0].substring(1);
            if (isCommand()) {
                String[] args2 = new String[args.length-1];
                System.arraycopy(args, 1, args2, 0, args2.length);
                args = args2;
                for (String x:args) System.out.println(x);
                e.getChannel().sendTyping().complete();
                onCommand();
            }
        }
    }

    public boolean isCommand() {
        if (args[0].toLowerCase().matches("m(ath)?"))
            return true;
        return e.getChannel().getName().equalsIgnoreCase("Math") &&
                args[0].toLowerCase().matches("(m(ath)?)?");
    }

    public String getName() { return "Math"; }
    public String getDescription() { return "perform basic math!"; }
    public String getFullDescription() {
        //TODO: write DESCRIPTon
        return getDescription()+"\n" +
                "it's complicated man";
    }
}
