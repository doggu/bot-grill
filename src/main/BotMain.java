package main;

import events.DevTools;
import events.Reactions;
import events.commands.*;
import events.gameroom.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BotMain {
    private static JDA bot_grill;
    private static List<ListenerAdapter> listeners = new ArrayList<>();

    public static List<Emote> stones;
    public static List<Emote> fehIcons;

    public static void main(String[] args) throws Exception {
        bot_grill = new JDABuilder(AccountType.BOT)
                .setToken(new Scanner(new File("./src/main/token.txt")).nextLine())
                .build();

        bot_grill.awaitReady();

        stones = bot_grill.getGuildsByName("summonicons", true).get(0).getEmotes();
        fehIcons = bot_grill.getGuildsByName("fehicons", true).get(0).getEmotes();

        //in case i want to make them removable one day
        ListenerAdapter listenerChances = new Chances();
        ListenerAdapter listenerFEHRetriever = new FEHRetriever();
        ListenerAdapter listenerEmotes = new Emotes();
        ListenerAdapter listenerGirl = new Girl();
        ListenerAdapter listenerDevTools = new DevTools();
        ListenerAdapter summonSimulator = new SummonSimulator();


        addListener(listenerChances);
        addListener(listenerFEHRetriever);
        addListener(listenerEmotes);
        addListener(listenerGirl);
        addListener(listenerDevTools);
        addListener(summonSimulator);
        addListener(new Reactions());
        addListener(new Maffs());
        addListener(new EmbedTest());

        //ex:
        //removeListener(listenerChances);

        Scanner console = new Scanner(System.in);

        while (console.hasNextLine()) {
            boolean kill = false;
            String command = console.nextLine();
            switch (command) {
                case "getListeners":
                    for (Object x:bot_grill.getRegisteredListeners()) {
                        System.out.println(x);
                    }
                case "refresh":
                    removeListener(listenerChances);
                    removeListener(listenerFEHRetriever);
                    removeListener(listenerEmotes);
                    removeListener(listenerGirl);
                    removeListener(listenerDevTools);
                    removeListener(summonSimulator);
                    listenerChances = new Chances();
                    listenerFEHRetriever = new FEHRetriever();
                    listenerEmotes = new Emotes();
                    listenerGirl = new Girl();
                    listenerDevTools = new DevTools();
                    summonSimulator = new SummonSimulator();
                    addListener(listenerChances);
                    addListener(listenerFEHRetriever);
                    addListener(listenerEmotes);
                    addListener(listenerGirl);
                    addListener(listenerDevTools);
                    addListener(summonSimulator);
                    break;
                case "kill":
                    bot_grill.shutdown();
                    kill = true;
                    break;
                default:
                    System.out.println("command not found.");
                    break;
            }

            if (kill) break;
        }
    }

    private static void addListener(ListenerAdapter listener) {
        bot_grill.addEventListener(listener);
        listeners.add(listener);
    }

    private static void removeListener(ListenerAdapter listener) {
        bot_grill.removeEventListener(listener);
        listeners.remove(listener);
    }
}
