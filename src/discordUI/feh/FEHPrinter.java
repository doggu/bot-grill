package discordUI.feh;

import feh.battle.FieldedUnit;
import feh.heroes.character.Hero;
import feh.heroes.character.HeroClass;
import feh.heroes.skills.skillTypes.ActionSkill;
import feh.heroes.skills.analysis.SkillAnalysis;
import feh.heroes.skills.analysis.StatModifier;
import feh.heroes.skills.skillTypes.*;
import feh.heroes.unit.Unit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static discordUI.EmoteHelper.getEmote;
import static discordUI.EmoteHelper.printEmote;
import static feh.heroes.UnitDatabase.HEROES;
import static main.BotMain.DEBUG;

public class FEHPrinter {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                      HEROES                                                    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
                        "Artist: "+x.getArtist()+"\n" +
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



        heroInfo.addField("Links", "[gamepedia]("+x.getGamepediaLink().toString()+")", true);

        return heroInfo;
    }
    public static EmbedBuilder printCharacter(Hero x, boolean lv1, int rarity,
                                              boolean getAll, int boon, int bane,
                                              int merges, int dragonflowers, char support) {
        return printCharacter(x, lv1, rarity, getAll, boon, bane, merges, dragonflowers, support, null);
    }
    public static EmbedBuilder printUnit(Unit x, boolean lv1) {
        return printCharacter(x, lv1, x.getRarity(), false,
                x.getBoon(), x.getBane(), x.getMerges(), x.getDragonflowers(),
                x.getSupportStatus());
    }
    public static EmbedBuilder printFieldedUnit(FieldedUnit x) {
        //todo: write FieldedUnit printer



        /*
        buffs, penalties
        status effects
        live cooldown and hp
         */
        return null;
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





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                      SKILLS                                                    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final HashMap<Integer, String> SKILL_ICONS;

    static {
        SKILL_ICONS = new HashMap<>();

        SKILL_ICONS.put(0, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/8/82/Icon_Skill_Weapon.png");
        SKILL_ICONS.put(1, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/9/9a/Icon_Skill_Assist.png");
        SKILL_ICONS.put(2, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/2/25/Icon_Skill_Special.png");
        SKILL_ICONS.put(3, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/1/10/Icon_Skill_Passive_A.png");
        SKILL_ICONS.put(4, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/c/ca/Icon_Skill_Passive_B.png");
        SKILL_ICONS.put(5, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/0/01/Icon_Skill_Passive_C.png");
        SKILL_ICONS.put(6, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/9/9c/Icon_Skill_Passive_S.png");
    }



    public static EmbedBuilder printSkill(Skill x) {
        EmbedBuilder skill = new EmbedBuilder();

        skill.setColor(x.getColor());

        skill.setAuthor(x.getName(), x.getLink().toString());
        skill.setDescription(x.getDescription());

        skill.setThumbnail(SKILL_ICONS.get(x.getSlot()));

        if (x instanceof ActionSkill) { //instanceof targeting skill
            if (x instanceof Weapon) {
                skill.addField("Might", String.valueOf(((Weapon) x).getMt()), true);
            }

            skill.addField("Range", String.valueOf(((ActionSkill) x).getRng()), true);
        } else if (x instanceof Special) {
            skill.addField("Cooldown", String.valueOf(((Special) x).getCooldown()), true);
        }



        if (x instanceof WeaponRefine) {
            skill.setThumbnail(((WeaponRefine) x).getIconURL().toString());
        }



        if (x instanceof Passive) {
            skill.setThumbnail(((Passive) x).getIcon().toString());
        }



        //something that a unit possesses
        if (!(x instanceof PassiveS)) {
            skill.addField("Exclusive?", (x.isExclusive()?"Yes":"No"), false);
            StringBuilder owners = new StringBuilder();
            int ownerCount = 0;
            for (Hero n:HEROES) {
                if (n.getBaseKit().contains(x)) {
                    owners.append(n).append(", ");
                    ownerCount++;
                }
            }
            if (owners.length() > 0) owners = new StringBuilder(owners.substring(0, owners.length()-2));
            skill.addField("Owner"+(ownerCount>1?"":"s"), owners.toString(), false);
        }



        //test displays
        if (DEBUG) {
            SkillAnalysis analysis = x.getAnalysis();

            if (x.getAnalysis().getStartOfTurn()!=null) {
                if (x.getAnalysis().getStatModifiers() != null)
                    skill.addField("stat modifiers",
                            "`" + printStats(analysis.getStatModifiers()) + "`",
                            false);

                int cdM = analysis.getCdModifier();
                ArrayList<HeroClass> effA = analysis.getEffective();
                HeroClass neutE = analysis.getNeutralizesEffectivity();
                boolean tA = analysis.getTriangleAdept();

                String attr = "";
                if (cdM!=0) attr+= "cooldown modifier: "+cdM+"\n";
                if (effA.size()>0) attr+= "effective against: "+effA+"\n";
                if (neutE!=null) attr+= "neutralizes: "+neutE+"\n";
                if (tA) attr+= "applies TA";
                if (x instanceof Special&&((Special) x).isAoE())
                    skill.addField("AoE pattern",
                            "```"+((Special) x).printDamagePattern()+"```",
                            false);

                if (!attr.equals(""))
                    skill.addField("attributes", attr, false);

                if (analysis.getStartOfTurn().size() != 0)
                    skill.addField("StartOfTurn", analysis.getStartOfTurn().toString(), false);
                if (analysis.getStartOfTurn1().size() != 0)
                    skill.addField("StartOfTurn1", analysis.getStartOfTurn1().toString(), false);
                if (analysis.getStartOfEveryNthTurn().size() != 0)
                    skill.addField("StartOfEveryNthTurn",
                            analysis.getStartOfEveryNthTurn().toString(), false);
                if (analysis.getStartOfEven().size() != 0)
                    skill.addField("StartOfEven", analysis.getStartOfEven().toString(), false);
                if (analysis.getStartOfOdd().size() != 0)
                    skill.addField("StartOfOdd", analysis.getStartOfOdd().toString(), false);
                //if (analysis.getDuringCombat().size() != 0)
                //    skill.addField("DuringCombat", analysis.getDuringCombat().toString(), false);
                if (analysis.getAtStartOfCombat().size() != 0)
                    skill.addField("AtStartOfCombat", analysis.getAtStartOfCombat().toString(), false);
                if (analysis.getBeforeCombatUnitInitiates().size() != 0)
                    skill.addField("BeforeCombatUnitInitiates",
                            analysis.getBeforeCombatUnitInitiates().toString(), false);
                if (analysis.getAfterCombat().size() != 0)
                    skill.addField("AfterCombat", analysis.getAfterCombat().toString(), false);
                if (analysis.getUnitInitiates().size() != 0)
                    skill.addField("UnitInitiates", analysis.getUnitInitiates().toString(), false);
                if (analysis.getFoeInitiates().size() != 0)
                    skill.addField("FoeInitiates", analysis.getFoeInitiates().toString(), false);
                if (analysis.getAfterMovementAssist().size() != 0)
                    skill.addField("AfterMovementAssist",
                            analysis.getAfterMovementAssist().toString(), false);
                if (analysis.getWhileUnitLives().size() != 0)
                    skill.addField("WhileUnitLives", analysis.getWhileUnitLives().toString(), false);
            }
        }



        return skill;
    }
}