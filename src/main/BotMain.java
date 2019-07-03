package main;

import events.DevTools;
import events.Quips;
import events.Reactions;
import events.commands.*;
import events.commands.chem.ElementRetriever;
import events.commands.gamble.Chances;
import events.commands.gamble.Roll;
import events.commands.help.Help;
import events.commands.math.FracCalcListener;
import events.commands.math.GradientDescentListener;
import events.commands.math.Maffs;
import events.commands.math.UnitConversionListener;
import events.fehGame.Allies;
import events.fehGame.OrbBalance;
import events.fehGame.summoning.SummonSimulator;
import events.fehGame.summoning.SimulateDay;
import events.Vote;
import events.fehGame.retriever.HeroRetriever;
import events.fehGame.retriever.SkillRetriever;
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


        bot_grill = new JDABuilder(AccountType.BOT)
                .setToken(new Scanner(new File("./src/main/token.txt")).nextLine())
                .build();

        bot_grill.awaitReady();

        stones = bot_grill.getGuildsByName("summonicons", true).get(0).getEmotes();
        fehIcons = bot_grill.getGuildsByName("fehicons", true).get(0).getEmotes();

        addListener(new ElementRetriever());

        addListener(new UnitConversionListener());
        addListener(new Quips());
        addListener(new Help());
        addListener(new Chances());
        addListener(new Roll());
        addListener(new SkillRetriever());
        addListener(new HeroRetriever());
        addListener(new Emotes());
        addListener(new Girl());
        addListener(new DevTools());
        addListener(new SummonSimulator());
        addListener(new SimulateDay());
        addListener(new Reactions());
        addListener(new Maffs());
        addListener(new GradientDescentListener());
        addListener(new EmbedTest());
        addListener(new Vote());
        addListener(new Draw());
        addListener(new OrbBalance());
        addListener(new CreateLobby());
        addListener(new Allies());
        addListener(new FracCalcListener());



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
            }
        }

        bot_grill.shutdownNow();
        console.close();
    }
}
