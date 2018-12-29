package events.commands;

import java.util.ArrayList;

public class Maffs extends Command {
    public void onCommand() {
        if (args.length!=2) {
            sendMessage("incorrect format");
            return;
        }

        /////////////////////////
        //        parse        //
        /////////////////////////

        String problem = args[1];
        ArrayList<Double> nums = new ArrayList<>();
        ArrayList<Character> ops = new ArrayList<>();

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
                    System.out.println(i+" "+problem.length());
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

        /////////////////////////
        //        solve        //
        /////////////////////////

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



        StringBuilder message = new StringBuilder();
        for (int i=0; i<ops.size(); i++) {
            message.append(nums.get(i)).append(' ').append(ops.get(i)).append(' ');
        }
        message.append(nums.get(nums.size()-1));

        sendMessage(message.toString());
        log("did the thing");
    }

    public boolean isCommand() { return args[0].equalsIgnoreCase("math"); }
}
