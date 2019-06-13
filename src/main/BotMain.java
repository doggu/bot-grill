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
import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;
import feh.skills.Skill;
import feh.skills.SkillDatabase;
import feh.summoning.Banner;
import feh.summoning.BannerDatabase;
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



    public static void main(String[] rgs) throws Exception {
        //initialize lists so that they are loaded before the bot goes live
        UnitDatabase.HEROES.size();
        SkillDatabase.SKILLS.size();
        BannerDatabase.BANNERS.size();

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
            String[] args = command.toLowerCase().split(" ");
            if (args.length==0) continue;
            switch (args[0]) {
                case "getlisteners":
                    for (Object x:bot_grill.getRegisteredListeners()) {
                        System.out.println(x);
                    }
                    break;
                case "update":
                    if (args.length<2) {
                        System.out.println("please be more descriptive");
                        continue;
                    }
                    switch (args[1]) {
                        case "units":
                            try {
                                UnitDatabase.updateCache();
                            } catch (Error f) {
                                f.printStackTrace();
                                System.out.println("nice code dumbass");
                            }
                            break;
                        case "skills":
                            try {
                                SkillDatabase.updateCache();
                            } catch (Error f) {
                                f.printStackTrace();
                                System.out.println("nice code dumbass");
                            }
                            break;
                        case "banners":
                            try {
                                BannerDatabase.updateCache();
                            } catch (Error f) {
                                f.printStackTrace();
                                System.out.println("nice code dumbass");
                            }
                            break;
                        case "all":
                            try {
                                SkillDatabase.updateCache();
                                UnitDatabase.updateCache();
                                BannerDatabase.updateCache();
                            } catch (Error f) {
                                f.printStackTrace();
                                System.out.println("nice code dumbass");
                            }
                            break;
                        default:
                            System.out.println("idk what this is, sorry");
                            break;
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
