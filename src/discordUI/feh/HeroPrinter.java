package discordUI.feh;

import feh.heroes.character.Hero;
import feh.heroes.skills.analysis.StatModifier;
import feh.heroes.unit.Unit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

import static discordUI.EmoteHelper.getEmote;
import static discordUI.EmoteHelper.printEmote;

public class HeroPrinter {
    //todo: literally move ALL this shit into some kind of Discord UI section
    public static EmbedBuilder printCharacter(Hero x, boolean lv1, int rarity,
                                               boolean getAll, int boon, int bane,
                                               int merges, int dragonflowers, char support) {
        return printCharacter(x, lv1, rarity, getAll, boon, bane, merges, dragonflowers, support, null);
    }
    public static EmbedBuilder printCharacter(Hero x, boolean lv1, int rarity,
                                               boolean getAll, int boon, int bane,
                                               int merges, int dragonflowers, char support,
                                               ArrayList<StatModifier> skills) {
        EmbedBuilder heroInfo = new EmbedBuilder();



        StringBuilder description = new StringBuilder();

        Emote moveType = getEmote("Icon_Move_"+x.getMoveType());
        String unitColor;
        switch (x.getColor()) {
            case 'r':
                unitColor = "Red";
                break;
            case 'g':
                unitColor = "Green";
                break;
            case 'b':
                unitColor = "Blue";
                break;
            case 'c':
                unitColor = "Colorless";
                break;
            default:
                unitColor = "orange";
        }
        Emote weaponType = getEmote("Icon_Class_"+unitColor+"_"+x.getWeaponType());

        description
                .append(printEmote(moveType))
                .append(printEmote(weaponType))
                .append(x.getFullName().toString());

        /*
        heroInfo.setAuthor(description.toString());
        heroInfo.setDescription('*'+x.getOrigin().toString()+"*\n" +
                "Debuted "  +
                (x.getReleaseDate().get(Calendar.MONTH) + 1) + "-" +//starts at 0 (january = 0)
                x.getReleaseDate().get(Calendar.DAY_OF_MONTH) + "-" +
                x.getReleaseDate().get(Calendar.YEAR));
         */

        heroInfo.addField(description.toString(),
                '*'+x.getOrigin().toString()+"*\n" +
                        "Debuted "  +
                        (x.getReleaseDate().get(Calendar.MONTH) + 1) + "-" +//starts at 0 (january = 0)
                        x.getReleaseDate().get(Calendar.DAY_OF_MONTH) + "-" +
                        x.getReleaseDate().get(Calendar.YEAR),
                false);

        Color rColor;
        switch (rarity) {
            case 1:
                rColor = Color.DARK_GRAY;
                break;
            case 2:
                rColor = Color.GRAY;
                break;
            case 3:
                rColor = new Color(185, 95,0);
                break;
            case 4:
                rColor = Color.LIGHT_GRAY;
                break;
            case 5:
                rColor = Color.YELLOW;
                break;
            default:
                rColor = new Color(120, 0, 180); //purple-ish
                break;
        }
        heroInfo.setColor(rColor);

        heroInfo.setThumbnail(x.getPortraitLink().toString());


        String info = "```\n";

        info+= rarity + "* lv" + (lv1?1:40) + " stats: \n" +
                "hp   atk  spd  def  res\n";

        String stats;
        String bst;

        //todo: use instanceof to make this cleaner
        if (getAll) {
            if (x.isSummonable()) {
                stats = printStats(x.getAllStats(lv1, rarity));
                bst = printBST(x.getAllStats(lv1, rarity));
            } else {
                stats = printStats(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support, skills));
                bst = printBST(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support));
            }
        } else {
            stats = printStats(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support, skills));
            bst = printBST(x.getStats(lv1, rarity, boon, bane, merges, dragonflowers, support));
        }

        info+= stats+"\n\n"+bst+"\n";

        if (!x.isSummonable()) info+= "this unit does not have access to IVs.\n";
        else if (merges>0&&getAll) info+= "predictions might not be 100% accurate.\n";
        info+= "```";

        heroInfo.addField("stats",info, false);

        StringBuilder baseKit = new StringBuilder();
        for (int i=0; i<x.getBaseKit().size(); i++) {
            baseKit.append(x.getBaseKit().get(i));
            if (i+1!=x.getBaseKit().size()) baseKit.append(", ");
        }

        heroInfo.addField("Base Skills", baseKit.toString(), false);

        return heroInfo;
    }
    public static EmbedBuilder printUnit(Unit x, boolean lv1) {
        return printCharacter(x, lv1, x.getRarity(), false,
                x.getBoon(), x.getBane(), 0, 0,
                x.getSupportStatus());
    }

    public static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((Math.log10(x)<1?"    ":"   "));
        return statString.toString();
    }
    public static String printStats(int[][] stats) {
        return  printStats(stats[0])+'\n'+
                printStats(stats[1])+(stats.length==3?'\n'+
                printStats(stats[2]):"");
    }

    public static String printBST(int[] stats) {
        int bst = 0;
        for (int i:stats) bst+= i;
        return "BST: " + bst;
    }
    public static String printBST(int[][] stats) {
        int bst = 0;
        for (int i=0; i<5; i++) bst+= stats[1][i];
        int maxBST = bst, minBST = bst;

        bst = 0;
        if (stats.length==2) {
            for (int i=0; i<5; i++) {
                for (int j=0; j<5; j++)
                    bst+= stats[(i==j?1:0)][j];
                if (bst > maxBST) maxBST = bst;
                if (bst < minBST) minBST = bst;
                bst = 0;
            }
        } else if (stats.length==3) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (j == i) continue;
                    for (int k = 0; k < 5; k++) {
                        if (k == i) bst += stats[0][k];
                        else if (k == j) bst += stats[2][k];
                        else bst += stats[1][k];
                    }
                    if (bst > maxBST) maxBST = bst;
                    if (bst < minBST) minBST = bst;
                    bst = 0;
                }
            }
        } else {
            return "idk lol";
        }
        if (maxBST==minBST) return "BST: "+maxBST;
        else return "BST: "+minBST+"-"+maxBST;
    }
}
