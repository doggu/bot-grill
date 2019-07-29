package events.commands.mcserver;

import events.commands.Command;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ServerInput extends Command {
    public static Thread server = null;
    public static MCServer app = null;



    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Server");
    }

    public void onCommand() {
        if (args.length<2) return;

        switch(args[1].toLowerCase()) {
            case "start":
                if (server!=null) {
                    sendMessage("the server is already up!");
                    return;
                } else {
                    sendMessage("initializing...");
                    server = new Thread(new MCServer());
                    server.start();
                }
                break;
            case "world":
                //get world name in server.properties
                break;
            case "stop":
                if (server==null) {
                    sendMessage("the server was never started!");
                    return;
                } else {
                    try {
                        app.sendCommand("stop");
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                    server = null;
                    app = null;
                }
                //get world name in server.properties
                break;
            default:
                //regular command
                if (server==null)
                    sendMessage("the server was never started!");
                else {
                    StringBuilder command = new StringBuilder();
                    for (int i=1; i<args.length; i++)
                        command.append(args[i]).append(' ');
                    app.sendCommand(command.substring(0,command.length()-1));
                }
                break;
        }
    }



    public String getName() {
        return "Server";
    }
    public String getDescription() {
        return "Send commands to the current Minecraft server (probably more later).";
    }
    public String getFullDescription() {
        return "";
    }
}
