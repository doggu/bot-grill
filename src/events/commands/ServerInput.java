package events.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerInput extends Command {


    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Server");
    }

    public void onCommand() {
        if (args.length<2) return;

        switch(args[1].toLowerCase()) {
            case "start":
                String command = "java -Xms2048M -Xmx4096M -jar libs/server/server.jar";


                try {
                    Process p = Runtime.getRuntime().exec(command);

                    try {
                        printLines(command + " stdout:", p.getInputStream());
                        printLines(command + " stderr:", p.getErrorStream());
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                } catch (IOException ioe) {
                    System.out.println("honestly don't expect this to work anyway");
                }
            case "stop":
            default:
        }
    }



    public String getName() {
        return "";
    }
    public String getDescription() {
        return "";
    }
    public String getFullDescription() {
        return "";
    }



    //taken from https://www.journaldev.com/937/compile-run-java-program-another-java-program

    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }

    public static void main(String[] args) throws Exception {
        String command = "java -Xms2048M -Xmx4096M -jar libs/server/server.jar -Xbootclasspath/a:";

        try {
            Process p = Runtime.getRuntime().exec(command);

            printLines(command + " stdout:", p.getInputStream());
            printLines(command + " stderr:", p.getErrorStream());
        } catch (IOException ioe) {
            System.out.println("honestly don't expect this to work anyway");
        }
    }
}
