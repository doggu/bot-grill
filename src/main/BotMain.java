package main;

import events.Quips;
import events.Reactions;
import events.Vote;
import events.devTools.Draw;
import events.commands.Emotes;
import events.commands.Girl;
import events.commands.mcserver.ServerInput;
import events.commands.chem.ElementRetriever;
import events.commands.gamble.Chances;
import events.commands.gamble.Roll;
import events.commands.help.Help;
import events.commands.math.FracCalcListener;
import events.commands.math.GradientDescentListener;
import events.commands.math.Maffs;
import events.commands.math.UnitConversionListener;
import events.devTools.DevTools;
import events.devTools.EmbedTest;
import events.devTools.PortraitTest;
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

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BotMain {
    private static final boolean FEHEROES_UTILS = true;
    public static final boolean MCSERVER = false;

    public static final boolean DEBUG = true;



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


        if (FEHEROES_UTILS) {
            System.out.println("computerizing FEH data...");
            long startTime = System.nanoTime();
            SkillDatabase.SKILLS.size();
            UnitDatabase.HEROES.size();
            BannerDatabase.BANNERS.size();

            long endTime = System.nanoTime();
            double totalTimeInSeconds = new BigDecimal((endTime-startTime)/1000000000.0)
                    .round(new MathContext(3)).doubleValue();
            System.out.println("finished ("+totalTimeInSeconds+" s)!");
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
        addListener(new DevTools());
        addListener(new Reactions());
        addListener(new Maffs());
        addListener(new GradientDescentListener());
        addListener(new EmbedTest());
        addListener(new Vote());
        addListener(new Draw());
        addListener(new CreateLobby());
        addListener(new FracCalcListener());

        if (FEHEROES_UTILS) {
            stones = bot_grill.getGuildsByName("summonicons", true).get(0).getEmotes();
            fehIcons = bot_grill.getGuildsByName("fehicons", true).get(0).getEmotes();

            addListener(new SkillRetriever());
            addListener(new HeroRetriever());
            addListener(new SummonSimulator());
            addListener(new SimulateDay());
            addListener(new Allies());
            addListener(new OrbBalance());

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
