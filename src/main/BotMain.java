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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BotMain {
    private static final boolean FEHEROES_UTILS = true;
    public static final boolean MCSERVER = true;

    public static final boolean DEBUG = true; //it's always debug time



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



    private static void preloadFEHUtils() {
        System.out.println("computerizing FEH data...");
        Stopwatch fehTime = new Stopwatch();
        fehTime.start();

        int i = SkillDatabase.SKILLS.size(),
            j = UnitDatabase.HEROES.size(),
            k = BannerDatabase.BANNERS.size();

        fehTime.stop();
        System.out.println("finished ("+fehTime.timeInSeconds()+")!");
    }

    public static void main(String[] rgs) throws Exception {
        //initialize lists so that they are loaded before the bot goes live


        if (FEHEROES_UTILS) {
            preloadFEHUtils();
        }



        bot_grill = new JDABuilder(AccountType.BOT)
                .setToken(new Scanner(new File("./src/main/token.txt")).nextLine())
                .build();

        bot_grill.awaitReady();

        addListener(new ServerInput());
        addListener(new ElementRetriever());
        addListener(new UnitConversionListener());
        addListener(new Quips());
        addListener(new Help());
        addListener(new Chances());
        addListener(new Roll());
        addListener(new Emotes());
        addListener(new Girl());
        addListener(new Reactions());
        addListener(new Maffs());
        addListener(new GradientDescentListener());
        addListener(new Vote());
        addListener(new CreateLobby());
        addListener(new FracCalcListener());

        //devTools
        addListener(new DevTools());
        addListener(new Draw());
        addListener(new EmbedTest());
        addListener(new PermissionsListener());

        if (FEHEROES_UTILS) {
            stones = bot_grill.getGuildsByName("summonicons", true).get(0).getEmotes();
            fehIcons = bot_grill.getGuildsByName("fehicons", true).get(0).getEmotes();

            addListener(new SkillRetriever());
            addListener(new HeroRetriever());
            addListener(new SummonSimulator());
            addListener(new SimulateDay());
            addListener(new Allies());
            addListener(new OrbBalance());

            //devTools
            addListener(new PortraitTest());
        }



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
                            case "heroes":
                                UnitDatabase.updateCache();
                                break;
                            case "skills":
                                SkillDatabase.updateCache();
                                break;
                            case "banners":
                                BannerDatabase.updateCache();
                                break;
                            case "all":
                                SkillDatabase.updateCache();
                                UnitDatabase.updateCache();
                                BannerDatabase.updateCache();
                                break;

                        }
                    }
            }
        }

        bot_grill.shutdownNow();
        console.close();
    }
}
