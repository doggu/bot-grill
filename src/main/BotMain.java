package main;

import events.DevTools;
import events.Reactions;
import events.commands.*;
import events.fehGame.SummonSimulator;
import events.gameroom.Vote;
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
    public static JDA bot_grill;
    private static List<ListenerAdapter> listeners = new ArrayList<>();

    public static List<Emote> stones;
    public static List<Emote> fehIcons;

    private static void addListener(ListenerAdapter listener) {
        bot_grill.addEventListener(listener);
        listeners.add(listener);
    }
    private static void removeListener(ListenerAdapter listener) {
        bot_grill.removeEventListener(listener);
        listeners.remove(listener);
    }



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
        ListenerAdapter listenerSummonSimulator = new SummonSimulator();
        ListenerAdapter listenerReactions = new Reactions();
        ListenerAdapter listenerMaffs = new Maffs();
        ListenerAdapter listenerEmbedTest = new EmbedTest();


        addListener(listenerChances);
        addListener(listenerFEHRetriever);
        addListener(listenerEmotes);
        addListener(listenerGirl);
        addListener(listenerDevTools);
        addListener(listenerSummonSimulator);
        addListener(listenerReactions);
        addListener(listenerMaffs);
        addListener(listenerEmbedTest);

        addListener(new Vote());

        //ex:
        //removeListener(listenerChances);

        Scanner console = new Scanner(System.in);
        
        String command;
        while (!(command = console.nextLine()).equals("kill")) {
            switch (command) {
                case "getListeners":
                    for (Object x:bot_grill.getRegisteredListeners()) {
                        System.out.println(x);
                    }
                case "refresh": //TODO: make this based on already-present listeners (excluding CircleSimulator)
                    removeListener(listenerChances);
                    removeListener(listenerFEHRetriever);
                    removeListener(listenerEmotes);
                    removeListener(listenerGirl);
                    removeListener(listenerDevTools);
                    removeListener(listenerSummonSimulator);
                    removeListener(listenerReactions);
                    removeListener(listenerMaffs);
                    removeListener(listenerEmbedTest);
                    listenerChances = new Chances();
                    listenerFEHRetriever = new FEHRetriever();
                    listenerEmotes = new Emotes();
                    listenerGirl = new Girl();
                    listenerDevTools = new DevTools();
                    listenerSummonSimulator = new SummonSimulator();
                    listenerReactions = new Reactions();
                    listenerMaffs = new Maffs();
                    listenerEmbedTest = new EmbedTest();
                    addListener(listenerChances);
                    addListener(listenerFEHRetriever);
                    addListener(listenerEmotes);
                    addListener(listenerGirl);
                    addListener(listenerDevTools);
                    addListener(listenerSummonSimulator);
                    addListener(listenerReactions);
                    addListener(listenerMaffs);
                    addListener(listenerEmbedTest);
                    break;
                default:
                    System.out.println("command not found.");
                    break;
            }
        }

        bot_grill.shutdown();
    }
}
