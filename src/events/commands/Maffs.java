package events.commands;

import utilities.ScannerUtil;

import java.io.File;
import java.util.ArrayList;

public class Maffs extends Command {
    private static ArrayList<Double> nums = new ArrayList<>();
    private static ArrayList<Character> ops = new ArrayList<>();



    private void parse() {
        String problem = args[1];
        for (int i=0; i<problem.length(); i++) {
            switch (problem.charAt(i)) {
                case '+':
                case '-':
                case '*':
                case '/':
                    try {
                        nums.add((double) Integer.parseInt(problem.substring(0, i)));
                    } catch (NumberFormatException g) {
                        System.out.println("ah fuck");
                        return;
                    }
                    ops.add(problem.charAt(i));
                    problem = problem.substring(i+1);
                    i = -1; //account for i++
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                    //System.out.println(i+" "+problem.length());
                    if (i>=problem.length()-1) {
                        try {
                            nums.add((double) Integer.parseInt(problem));
                        } catch (NumberFormatException g) {
                            System.out.println("ah fuck (end)");
                            return;
                        }
                    }
                    break;
                default:
                    System.out.println("ah fuUck");
                    return;
            }
        }
    }

    private void solve() {
        //peMDas (ok, more like MDas)
        for (int i=0; i<ops.size(); i++) {
            char op = ops.get(i);
            switch (op) {
                case '*':
                    nums.set(i, nums.get(i)*nums.get(i+1));
                    nums.remove(i+1);
                    ops.remove(i);
                    i--;
                    break;
                case '/':
                    nums.set(i, nums.get(i)/nums.get(i+1));
                    nums.remove(i+1);
                    ops.remove(i);
                    i--;
                    break;
                default:
                    //hold
                    break;
            }
        }
        //mdAS
        for (int i=0; i<ops.size(); i++) {
            char op = ops.get(i);
            switch (op) {
                case '+':
                    nums.set(i, nums.get(i)+nums.get(i+1));
                    nums.remove(i+1);
                    ops.remove(i);
                    i--;
                    break;
                case '-':
                    nums.set(i, nums.get(i)-nums.get(i+1));
                    nums.remove(i+1);
                    ops.remove(i);
                    i--;
                    break;
                default:
                    //technically nothing should go here
                    break;
            }
        }
    }



    public void onCommand() {
        if (args.length!=2) {
            sendMessage("incorrect format");
            return;
        }

        for (int i=0; i<args[1].length(); i++) {
            switch (args[1].charAt(i)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '+':
                case '-':
                case '*':
                case '/':
                    continue;
                default:
                    String[] dunnoPath = { ".", "src", "events", "commands", "dunno.png" };
                    e.getChannel().sendFile(ScannerUtil.createFile(dunnoPath)).queue();
                    return;
            }
        }

        parse();
        solve();

        StringBuilder message = new StringBuilder();
        for (int i=1; i<ops.size(); i++) {
            message.append(nums.get(i)).append(' ').append(ops.get(i)).append(' ');
        }
        message.append(nums.get(nums.size()-1));

        sendMessage(message.toString());
        log("mathed "+args[1]);
    }

    public boolean isCommand() { return args[0].equalsIgnoreCase("math"); }
}
