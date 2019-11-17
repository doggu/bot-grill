package events.stem.math;

import discordUI.button.ReactionButton;
import events.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import stem.math.MathParse;

import java.util.HashMap;

public class MathListener extends Command {
    public static HashMap<String,Double> answers = new HashMap<>();
    private static HashMap<String, HashMap<String, Double>> storedValues = new HashMap<>();
    //private HashMap<String, ArrayList<Function<Double, Double>>> storedFunctions = new HashMap<>();


    public void onCommand() {
        String problem;
        double tv;

        if (args.length==1&&e.getChannel().getName().equalsIgnoreCase("math")) {
            problem = args[0];
            tv = 1;
        } else if (args.length==2) {
            problem = args[1];
            tv = 1.0;
        } else if (args.length==3) {
            problem = args[2];
            try {
                tv = Double.parseDouble(args[1]);
            } catch (NumberFormatException g) {
                sendMessage("incorrect test value! please try again.");
                return;
            }
        } else if (args.length==4) {
            if (args[1].toLowerCase().matches("sto(re)?")) {
                String var = args[2];
                String value = args[3];

                if (value.equalsIgnoreCase("ans")) {
                    value = String.valueOf(answers.get(e.getAuthor().getId()));
                    value = value.replace("E", "*10^"); //todo: how do i fix this better
                }

                double val;
                try {
                    val = Double.parseDouble(value);
                } catch (NumberFormatException nfe) {
                    try {
                        val = new MathParse(value).getFunction().apply(0.0);
                    } catch (Exception e) {
                        sendMessage("could not decipher your value, please try again.");
                        return;
                    }
                }

                storedValues.computeIfAbsent(e.getAuthor().getId(), k -> new HashMap<>());
                storedValues.get(e.getAuthor().getId()).put(var, val);
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
            problem = problem.replace("ans", "("+ansStr+")");
        }

        HashMap<String, Double> userValues = storedValues.get(e.getAuthor().getId());

        if (userValues!=null) {
            for (String x:userValues.keySet()) {
                if (problem.contains(x)) {
                    problem = problem.replaceAll(x, String.valueOf(userValues.get(x)));
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

        final String aleksPrint = String.valueOf(value).replace("E", "*10^");

        new ReactionButton(m,
                e.getJDA().getEmotesByName("aleks", true).get(0)) {
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
    //todo: add special
    public void onMessageReceived(MessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        if (e.getAuthor().isBot()) return;
        if (message.length()==0) return;
        this.e = e;
        if (message.charAt(0)!=getPrefix()) {
            this.args = message.split(" ");
            if (e.getChannel().getName().equalsIgnoreCase("math")) {
                e.getChannel().sendTyping().complete();
                onCommand();
            }
        } else {
            this.args = message.substring(1).split(" ");
            if (isCommand()) {
                e.getChannel().sendTyping().complete();
                onCommand();
            }
        }
    }

    public boolean isCommand() {
        return false;
        /*
        if (args[0].toLowerCase().matches("m(ath)?")) return true;
        return e.getChannel().getName().equalsIgnoreCase("Math") &&
                args[0].toLowerCase().matches("(m(ath)?)?");
         */
    }

    public String getName() { return "Math"; }
    public String getDescription() { return "perform basic math!"; }
    public String getFullDescription() {
        //TODO: write DESCRIPTon
        return getDescription()+"\n" +
                "it's complicated man";
    }
}
