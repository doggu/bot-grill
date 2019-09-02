package main;

import events.Quips;
import events.Reactions;
import events.Vote;
import events.commands.Emotes;
import events.commands.Girl;
import events.commands.chem.ElementRetriever;
import events.commands.gamble.Chances;
import events.commands.gamble.Roll;
import events.commands.help.Help;
import events.commands.math.FracCalcListener;
import events.commands.math.GradientDescentListener;
import events.commands.math.Maffs;
import events.commands.math.UnitConversionListener;
import events.commands.mcserver.ServerInput;
import events.devTools.*;
import events.fehGame.Allies;
import events.fehGame.OrbBalance;
import events.fehGame.retriever.HeroRetriever;
import events.fehGame.retriever.SkillRetriever;
import events.fehGame.summoning.SimulateDay;
import events.fehGame.summoning.SummonSimulator;
import events.gameroom.CreateLobby;
import feh.heroes.UnitDatabase;
import feh.skills.SkillDatabase;
import feh.summoning.BannerDatabase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utilities.Stopwatch;
import utilities.science.chem.ElementDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BotMain {
    private static final boolean FEHEROES_UTILS = true;
    public static final boolean MCSERVER = false;

    public static final boolean DEBUG = true; //it's always debug time



    public static JDA bot_grill;
    private static List<ListenerAdapter> listeners = new ArrayList<>();

    public static List<Emote> stones;



    public static void addListener(ListenerAdapter listener) {
        bot_grill.addEventListener(listener);
        listeners.add(listener);
    }
    public static void removeListener(ListenerAdapter listener) {
        bot_grill.removeEventListener(listener);
        listeners.remove(listener);
    }

    public static List<ListenerAdapter> getListeners() { return listeners; }



    private static void preloadFEHUtils() {
        //initialize lists so that they are loaded before the bot goes live
        System.out.println("computerizing FEH data...");
        Stopwatch fehTime = new Stopwatch();
        fehTime.start();

        int i = SkillDatabase.SKILLS.size(),
            j = UnitDatabase.HEROES.size(),
            k = BannerDatabase.BANNERS.size();

        if (DEBUG) {
            System.out.println("SkillDatabase: " + i);
            System.out.println("UnitDatabase: " + j);
            System.out.println("BannerDatabase: " + k);
        }

        fehTime.stop();
        System.out.println("finished ("+fehTime.timeInSeconds()+")!");
    }

    private static void loadDevTools() {
        addListener(new DevTools());
        addListener(new Draw());
        addListener(new EmbedTest());
        addListener(new PermissionsListener());
    }

    public static void main(String[] rgs) throws Exception {
        if (FEHEROES_UTILS)
            preloadFEHUtils();

        bot_grill = new JDABuilder(AccountType.BOT)
                .setToken(new Scanner(new File("./src/main/token.txt")).nextLine())
                .build();

        bot_grill.awaitReady();

        //science
        addListener(new ElementRetriever());
        addListener(new UnitConversionListener());
        addListener(new FracCalcListener());

        //gamble
        addListener(new Chances());
        addListener(new Roll());

        addListener(new Maffs());
        addListener(new GradientDescentListener());

        //gameroom
        addListener(new CreateLobby());

        //emoticon stuff
        addListener(new Quips());
        addListener(new Reactions());
        addListener(new Emotes());
        addListener(new Girl());
        addListener(new Vote());

        //devTools
        loadDevTools();

        //FEH
        if (FEHEROES_UTILS) {
            stones = bot_grill.getGuildsByName("summonicons", true).get(0).getEmotes();

            addListener(new SkillRetriever());
            addListener(new HeroRetriever());
            addListener(new SummonSimulator());
            addListener(new SimulateDay());
            addListener(new Allies());
            addListener(new OrbBalance());
        }

        //minecraft
        addListener(new ServerInput());

        addListener(new Help());



        //todo: rewrite to allow reception of commands from other sources throughout the code
        Scanner console = new Scanner(System.in);
        
        String command;
        while (!(command = console.nextLine()).equals("kill")) {
            String[] args = command.toLowerCase().split(" ");
            if (args.length==0) continue;
            switch (args[0]) {
                case "getlisteners":
                    for (Object x:bot_grill.getRegisteredListeners()) {
                        System.out.println(x);
                    }
                    break;
                case "update":
                    if (args.length>1) {
                        switch (args[1]) {
                            case "e":
                            case "elements":
                                ElementDatabase.DATABASE.updateCache();
                                break;
                            case "h":
                            case "heroes":
                                UnitDatabase.DATABASE.updateCache();
                                break;
                            case "s":
                            case "skills":
                                SkillDatabase.DATABASE.updateCache();
                                break;
                            case "b":
                            case "banners":
                                BannerDatabase.DATABASE.updateCache();
                                break;
                            default:
                                SkillDatabase.DATABASE.updateCache();
                                UnitDatabase.DATABASE.updateCache();
                                BannerDatabase.DATABASE.updateCache();
                                break;

                        }
                    }
                    break;
                case "mem":
                case "memory":
                    System.out.println("Total:\t"+Runtime.getRuntime().totalMemory());
                    System.out.println("Free:\t"+Runtime.getRuntime().freeMemory());
                    System.out.println("Max:\t"+Runtime.getRuntime().maxMemory());
                    break;
            }
        }

        console.close();

        bot_grill.shutdownNow();
    }
}
