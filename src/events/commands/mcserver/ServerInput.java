package events.commands.mcserver;

import events.commands.Command;
import main.BotMain;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

public class ServerInput extends Command {
    public static Thread server = null;
    public static MCServer app = null;



    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Server");
    }

    public void onCommand() {
        if (!BotMain.MCSERVER) {
            sendMessage("server commands are unavailable at this time. sorry!");
        }
        if (args.length<2) return;

        switch(args[1].toLowerCase()) {
            case "start":
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
            case "world":
                if (server!=null) {
                    sendMessage("please stop the server before modifying its properties.");
                    return;
                }
                //get world name in server.properties
                if (args.length<3) {
                    File[] folder = new File("./libs/server/worlds/").listFiles();
                    StringBuilder message = new StringBuilder("currently loaded worlds:\n");
                    for (File x:folder) {
                        message.append(x.getName()).append('\n');
                    }
                    sendMessage(message.append("choose a world by sending the command \"?server world set [name]\""));
                } else {
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
                    try {
                        server.join(10000);
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
            default:
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



    public static void main(String[] args) {
        File properties = new File("./libs/server/server.properties");

        Scanner lines;
        try {
            lines = new Scanner(properties);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return;
        }

        while (lines.hasNextLine()) {
            System.out.println(lines.nextLine());
        }
    }
}
