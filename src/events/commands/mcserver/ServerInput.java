package events.commands.mcserver;

import events.commands.Command;

import java.io.*;

public class ServerInput extends Command {
    private static MCServer server = null;



    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Server");
    }

    public void onCommand() {
        if (args.length<2) return;

        switch(args[1].toLowerCase()) {
            case "start":
                server = new MCServer();
                server.run();
            case "stop":
                if (server==null)
                    sendMessage("the server was never started!");
                else {
                    byte[] stop = "stop\r".getBytes();
                    try {
                        server.getOutputStream().write(stop);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    server = null;
                }
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
}
