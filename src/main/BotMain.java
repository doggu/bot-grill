package main;

import events.DevTools;
import events.Quips;
import events.Reactions;
import events.commands.*;
import events.commands.gamble.Chances;
import events.commands.gamble.Roll;
import events.commands.Help;
import events.commands.math.FracCalcListener;
import events.commands.math.GradientDescentListener;
import events.commands.math.Maffs;
import events.commands.math.UnitConversion;
import events.fehGame.Allies;
import events.fehGame.OrbBalance;
import events.fehGame.summoning.SummonSimulator;
import events.fehGame.summoning.SimulateDay;
import events.Vote;
import events.fehGame.retriever.HeroRetriever;
import events.fehGame.retriever.SkillRetriever;
import events.gameroom.CreateLobby;
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

    public static void addListener(ListenerAdapter listener) {
        bot_grill.addEventListener(listener);
        listeners.add(listener);
    }
    public static void removeListener(ListenerAdapter listener) {
        bot_grill.removeEventListener(listener);
        listeners.remove(listener);
    }

    public static List<ListenerAdapter> getListeners() { return listeners; }



    public static void main(String[] args) throws Exception {
        //construct listeners beforehand so bot is ready as soon as she goes live
        ListenerAdapter listenerUnitConversion = new UnitConversion();
        ListenerAdapter listenerQuips = new Quips();
        ListenerAdapter listenerHelp = new Help();
        ListenerAdapter listenerChances = new Chances();
        ListenerAdapter listenerRoll = new Roll();
        ListenerAdapter listenerSkillRetriever = new SkillRetriever();
        ListenerAdapter listenerHeroRetriever = new HeroRetriever();
        ListenerAdapter listenerEmotes = new Emotes();
        ListenerAdapter listenerGirl = new Girl();
        ListenerAdapter listenerDevTools = new DevTools();
        ListenerAdapter listenerSummonSimulator = new SummonSimulator();
        ListenerAdapter listenerSimulateDay = new SimulateDay();
        ListenerAdapter listenerReactions = new Reactions();
        ListenerAdapter listenerMaffs = new Maffs();
        ListenerAdapter listenerGD = new GradientDescentListener();
        ListenerAdapter listenerEmbedTest = new EmbedTest();
        ListenerAdapter listenerVote = new Vote();
        ListenerAdapter listenerDraw = new Draw();
        ListenerAdapter listenerOrbBalance = new OrbBalance();
        ListenerAdapter listenerCreateLobby = new CreateLobby();
        ListenerAdapter listenerAllies = new Allies();
        ListenerAdapter listenerFracCalcListener = new FracCalcListener();

        bot_grill = new JDABuilder(AccountType.BOT)
                .setToken(new Scanner(new File("./src/main/token.txt")).nextLine())
                .build();

        bot_grill.awaitReady();

        stones = bot_grill.getGuildsByName("summonicons", true).get(0).getEmotes();
        fehIcons = bot_grill.getGuildsByName("fehicons", true).get(0).getEmotes();

        addListener(listenerUnitConversion);
        addListener(listenerQuips);
        addListener(listenerHelp);
        addListener(listenerChances);
        addListener(listenerRoll);
        addListener(listenerSkillRetriever);
        addListener(listenerHeroRetriever);
        addListener(listenerEmotes);
        addListener(listenerGirl);
        addListener(listenerDevTools);
        addListener(listenerSummonSimulator);
        addListener(listenerSimulateDay);
        addListener(listenerReactions);
        addListener(listenerMaffs);
        addListener(listenerGD);
        addListener(listenerEmbedTest);
        addListener(listenerVote);
        addListener(listenerDraw);
        addListener(listenerOrbBalance);
        addListener(listenerCreateLobby);
        addListener(listenerAllies);
        addListener(listenerFracCalcListener);



        Scanner console = new Scanner(System.in);
        
        String command;
        while (!(command = console.nextLine()).equals("kill")) {
            switch (command) {
                case "getListeners":
                    for (Object x:bot_grill.getRegisteredListeners()) {
                        System.out.println(x);
                    }
                    break;
                default:
                    System.out.println("command not found.");
                    break;
            }
        }

        bot_grill.shutdown();
    }
}
