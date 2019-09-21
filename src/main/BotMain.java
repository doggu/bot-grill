package main;

import events.commands.Emotes;
import events.commands.Girl;
import events.commands.campus.CanIEatRightNow;
import events.commands.chem.ElementRetriever;
import events.commands.chem.MolarMass;
import events.commands.gamble.Chances;
import events.commands.gamble.Roll;
import events.commands.help.Help;
import events.commands.math.FracCalcListener;
import events.commands.math.GradientDescentListener;
import events.commands.math.Maffs;
import events.commands.math.UnitConversionListener;
import events.commands.mcserver.ServerInput;
import events.devTools.DevTools;
import events.devTools.Draw;
import events.devTools.PermissionsListener;
import events.fehGame.Allies;
import events.fehGame.OrbBalance;
import events.fehGame.retriever.HeroRetriever;
import events.fehGame.retriever.SkillRetriever;
import events.fehGame.summoning.SimulateDay;
import events.fehGame.summoning.SummonSimulator;
import events.gameroom.CreateLobby;
import events.reactionary.Quips;
import events.reactionary.Reactions;
import events.reactionary.Vote;
import feh.heroes.UnitDatabase;
import feh.heroes.skills.SkillDatabase;
import feh.summoning.BannerDatabase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import stem.science.chem.ElementDatabase;
import utilities.Stopwatch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BotMain {
    private static final boolean FEHEROES_UTILS = true;
    public static final boolean MCSERVER = false;

    public static final boolean DEBUG = false; //it's always debug time



    public static JDA bot_grill;
    private static List<ListenerAdapter> listeners = new ArrayList<>();



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
        addListener(new PermissionsListener());
    }

    private static void addShutdownThreads() {
        //assassinate the bot
        Runtime.getRuntime().addShutdownHook(new Thread(() -> bot_grill.shutdown()));

        //TODO: add caching hooks which commit user persistence to storage
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
        addListener(new MolarMass());

        //gamble
        addListener(new Chances());
        addListener(new Roll());

        //math
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

        //campus help
        addListener(new CanIEatRightNow());

        //devTools
        loadDevTools();

        //FEH
        if (FEHEROES_UTILS) {
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

        addShutdownThreads();
        
        //new Thread(() -> {
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
        //}).start();



        System.exit(0);
    }
}
