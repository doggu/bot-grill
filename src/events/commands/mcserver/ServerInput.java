package events.commands.mcserver;

import events.commands.Command;
import main.BotMain;
import utilities.permissions.Permissions;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class ServerInput extends Command {
    private static final int
            START_PERMISSION = 2,
            STOP_PERMISSION = 2,
            WORLD_ACCESS_PERMISSION = 0,
            WORLD_CHANGE_PERMISSION = 4,
            STATUS_PERMISSION = 0,
            GENERIC_COMMAND_PERMISSION = 3;

    private boolean canPerform(int commandPermission) {
        return Permissions.getPermissions(e.getAuthor().getIdLong())>=commandPermission; }

    private void insufficientPermissions() {
        sendMessage("you have insufficient permissions to perform this command!"); }

    private void scold() {
            sendMessage("you have insufficient permissions to perform this command! " +
                    "this is actually impossible without having been banished to a " +
                    "sub-zero permission level, so please reconsider your past actions."); }



    public static volatile Thread server = null;
    public static volatile MCServer app = null;



    public boolean isCommand() { return args[0].equalsIgnoreCase("Server"); }

    public void onCommand() {
        if (!BotMain.MCSERVER) {
            sendMessage("server commands are unavailable at this time. sorry!");
            return;
        }
        if (args.length<2) return;

        switch(args[1].toLowerCase()) {
            case "start":
                if (!canPerform(START_PERMISSION)) { insufficientPermissions(); return; }

                if (server!=null) {
                    sendMessage("the server is already up!");
                    return;
                } else {
                    sendMessage("initializing...");
                    app = new MCServer();
                    server = new Thread(app);
                    server.start();
                }
                break;
            case "stop":
                if (!canPerform(STOP_PERMISSION)) { insufficientPermissions(); return; }

                if (server==null) {
                    sendMessage("the server was never started!");
                    return;
                } else {
                    try {
                        app.sendCommand("stop");
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                    try {
                        server.join(10000);
                        if (server!=null) {
                            sendMessage("server stop unsuccessful.");
                            return;
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                        sendMessage("server stop unsuccessful.");
                        return;
                    }
                    server = null;
                    app = null;
                    sendMessage("the server was successfully stopped.");
                }
                break;
            case "world":
                //get world name in server.properties
                if (args.length<3) {
                    if (!canPerform(WORLD_ACCESS_PERMISSION)) { scold(); return; }

                    File[] folder = new File("./libs/server/worlds/").listFiles();
                    StringBuilder message = new StringBuilder("currently loaded worlds:\n```");
                    if (folder==null) {
                        sendMessage("there was an issue accessing the worlds folder!");
                        return;
                    }
                    for (File x:folder) {
                        message.append(x.getName()).append('\n');
                    }

                    sendMessage(message.append("```\n")
                            .append((canPerform(WORLD_CHANGE_PERMISSION)?
                                    "choose a world by sending the command \"?server world set [name]\"":"")));
                } else {
                    if (!canPerform(WORLD_ACCESS_PERMISSION)) { insufficientPermissions(); return; }

                    if (server!=null) {
                        sendMessage("please stop the server before modifying its properties.");
                        return;
                    }
                    if (args.length<4) {
                        sendMessage("please choose a world to load.");
                    } else {
                        File properties = new File("./libs/server/server.properties");

                        StringBuilder worldName = new StringBuilder(args[3]);
                        for (int i=4; i<args.length; i++) {
                            worldName.append(' ').append(args[i]);
                        }

                        Function<String,String> worldChanger = x -> {
                            if (x.indexOf("level-name")==0)
                                x = "level-name=worlds/"+worldName;
                            return x;
                        };

                        editFile(properties, worldChanger);
                        sendMessage("change complete!");
                    }
                }
                break;
            case "status":
                if (!canPerform(STATUS_PERMISSION)) { scold(); return; }
                break;
            default:
                if (!canPerform(GENERIC_COMMAND_PERMISSION)) { insufficientPermissions(); return; }
                //regular command
                if (server==null)
                    sendMessage("the server was never started!");
                else {
                    StringBuilder command = new StringBuilder();
                    for (int i=1; i<args.length; i++)
                        command.append(args[i]).append(' ');
                    if (app.sendCommand(command.substring(0,command.length()-1)))
                        sendMessage("command sent!");
                    else
                        sendMessage("command failed! please try again.");
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



    private static void editFile(File f, Function<String,String> editor) {
        FileReader fr;
        try {
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(editor.apply(line)+"\n");
            }
            FileWriter fw = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines) {
                out.write(s);
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
