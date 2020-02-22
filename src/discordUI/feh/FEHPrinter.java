package discordUI.feh;

import feh.battle.FieldedUnit;
import feh.characters.hero.Hero;
import feh.characters.hero.HeroClass;
import feh.characters.hero.MovementClass;
import feh.characters.hero.WeaponClass;
import feh.characters.skills.analysis.SkillAnalysis;
import feh.characters.skills.skillTypes.*;
import feh.characters.unit.Unit;
import main.BotMain;
import net.dv8tion.jda.core.EmbedBuilder;
import utilities.Range;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static discordUI.EmoteHelper.printEmote;
import static feh.characters.HeroDatabase.HEROES;
import static main.BotMain.DEBUG;

public class FEHPrinter {
////////////////////////////////////////////////////////////////////////////////
//                                    HEROES                                  //
////////////////////////////////////////////////////////////////////////////////
    public static EmbedBuilder printCharacter(Hero x, boolean lv1, int rarity,
                                               ArrayList<Skill> skills) {
        EmbedBuilder heroInfo = new EmbedBuilder();
        headingInformation(heroInfo, x);

        Color color;
        switch (rarity) { //?
            case 1: color = HERO_1; break;
            case 2: color = HERO_2; break;
            case 3: color = HERO_3; break;
            case 4: color = HERO_4; break;
            case 5: color = HERO_5; break;
            default:
                color = new Color(120, 0, 180); //purple-ish
                break;
        }

        heroInfo.setColor(color); //i don't want it to be summonable rarity
                                 //but idk how to fix it now


        String info = "```\n";
        info+= rarity + "* lv" + (lv1?1:40) + " stats: \n" +
                "hp   atk  spd  def  res\n";
        String stats, bst;
        if (x.isSummonable()) {
            stats = printStats(x.getAllStats(lv1, rarity, skills));
            bst = printBST(x.getAllStats(lv1, rarity));
        } else {
            stats = printStats(x.getAllStats(lv1, rarity, skills)[1]);
            bst = printBST(x.getAllStats(lv1, rarity)[1]);
        }
        info+= stats+"\n\n"+bst+"\n";
        if (!x.isSummonable())
            info+= "this unit does not have access to IVs.\n";
        info+= "```";


        heroInfo.addField("stats",info, false);

        StringBuilder baseKit = new StringBuilder();
        for (int i=0; i<x.getBaseKit().size(); i++) {
            baseKit.append(x.getBaseKit().get(i));
            if (i+1!=x.getBaseKit().size()) baseKit.append(", ");
        }

        heroInfo.addField("Base Skills",
                baseKit.toString(), false);

        footerInformation(heroInfo, x);

        return heroInfo;
    }
    public static EmbedBuilder printUnit(Unit x) {
        EmbedBuilder unitInfo = new EmbedBuilder();
        headingInformation(unitInfo, x);

        String info = "```\n";
        info+= x.getRarity() + "* lv" + x.getLevel() + " stats: \n" +
                "hp   atk  spd  def  res\n";
        String stats = printStats(x.getStatsArr()),
               bst = printBST(x.getStatsArr());
        info+= stats+"\n\n"+bst+"\n";
        info+= "```";

        unitInfo.addField("stats",info, false);
        footerInformation(unitInfo, x);

        return unitInfo;
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


    private static void headingInformation(EmbedBuilder builder, Hero x) {
        /*
        heroInfo.setAuthor(description.toString());
        heroInfo.setDescription('*'+x.getOrigin().toString()+"*\n" +
                "Debuted "  +
                                                //starts at 0 (january = 0)
                (x.getReleaseDate().get(Calendar.MONTH) + 1) + "-" +
                x.getReleaseDate().get(Calendar.DAY_OF_MONTH) + "-" +
                x.getReleaseDate().get(Calendar.YEAR));
         */

        String description = printMovementClassEmote(x.getMoveType()) +
                printWeaponClassEmote(x.getColor(), x.getWeaponType()) +
                x.getFullName().toString();
        builder.addField(description,
                '*'+x.getOrigin().toString()+"*\n" +
                        "Artist: "+x.getArtist()+"\n" +
                        "Debuted "  +               //starts at 0 (january = 0)
                        (x.getReleaseDate().get(Calendar.MONTH) + 1) + "-" +
                        x.getReleaseDate().get(Calendar.DAY_OF_MONTH) + "-" +
                        x.getReleaseDate().get(Calendar.YEAR),
                false);

        Color rColor = palatte(x);
        builder.setColor(rColor);

        builder.setThumbnail(x.getPortraitLink().toString());
    }

    private static void footerInformation(EmbedBuilder builder, Hero x) {
        builder.addField(
                "Links",
                "[gamepedia]("+x.getGamepediaLink().toString()+")",
                true);
    }



    private static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((x/10>=1?"   ":"    "));
        return statString.toString();
    }
    private static String printStats(int[][] stats) {
        return  printStats(stats[0])+'\n'+
                printStats(stats[1])+
                (stats.length==3?'\n'+printStats(stats[2]):"");
    }

    private static String printBST(int[] stats) {
        int bst = 0;
        for (int i:stats) bst+= i;
        return "BST: " + bst;
    }
    private static String printBST(int[][] stats) {
        int bst = 0;
        for (int i=0; i<5; i++) bst+= stats[1][i];

        Range<Integer> bstRange = new Range<>(bst, bst);

        bst = 0;
            //does not check neutral case, but it is not necessary
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j == i) continue;
                for (int k = 0; k < 5; k++) {
                    if (k == i) bst += stats[0][k];
                    else if (k == j) bst += stats[2][k];
                    else bst += stats[1][k];
                }
                bstRange.updateRange(bst);
                bst = 0;
            }
        }

        return "BST: " + bstRange.getMin()
                + (bstRange.getMin().equals(bstRange.getMax()) ?
                        "-" + bstRange.getMax() : "");
    }



////////////////////////////////////////////////////////////////////////////////
//                                    SKILLS                                  //
////////////////////////////////////////////////////////////////////////////////
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
                skill.addField(
                        "Might",
                        String.valueOf(((Weapon) x).getMt()),
                        true);
            }

            skill.addField(
                    "Range",
                    String.valueOf(((ActionSkill) x).getRng()),
                    true);
        } else if (x instanceof Special) {
            skill.addField(
                    "Cooldown",
                    String.valueOf(((Special) x).getCooldown()),
                    true);
        }

        if (x instanceof WeaponRefine) {
            skill.setThumbnail(((WeaponRefine) x).getIconURL().toString());
        }

        if (x instanceof Passive) {
            skill.setThumbnail(((Passive) x).getIcon().toString());
        }


        //something that a unit possesses
        if (!(x instanceof PassiveS)) {
            skill.addField(
                    "Exclusive?",
                    (x.isExclusive()?"Yes":"No"),
                    false);
            StringBuilder owners = new StringBuilder();
            int ownerCount = 0;
            for (Hero n:HEROES) {
                if (n.getBaseKit().contains(x)) {
                    owners.append(n).append(", ");
                    ownerCount++;
                }
            }
            if (owners.length() > 0)
                owners = new StringBuilder(
                        owners.substring(0, owners.length()-2));
            skill.addField(
                    "Owner"+(ownerCount>1?"":"s"),
                    owners.toString(),
                    false);
        }


        //TESTDISPLAYS
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
        //END

        return skill;
    }



////////////////////////////////////////////////////////////////////////////////
//                                  UTILITIES                                 //
////////////////////////////////////////////////////////////////////////////////
    private static String printMovementClassEmote(MovementClass type) {
        return printEmote(
                BotMain.bot_grill.getEmotesByName(
                        "Icon_Move_"+type.toString(),
                        true).get(0));
    }

    private static String printWeaponClassEmote(char color, WeaponClass type) {
        return printEmote(
                BotMain.bot_grill.getEmotesByName("Icon_Class_"+
                        (color=='r'?"Red":
                                (color=='g'?"Green":
                                        (color=='b'?"Blue":"Colorless")))
                        +"_"+type.toString(),
                true).get(0));
    }


    private static Color
            DEFAULT = Color.MAGENTA,
            HERO_1 = Color.DARK_GRAY,
            HERO_2 = Color.GRAY,
            HERO_3 = new Color(185, 95, 0),
            HERO_4 = Color.LIGHT_GRAY,
            HERO_5 = Color.YELLOW,
            HERO_5_10 = new Color(255, 255, 128);


    private static Color palatte(Object o) {
        Color color;

        if (o instanceof FieldedUnit) {
            color = HERO_5;
        } else if (o instanceof Unit) {
            int rarity = ((Unit) o).getRarity();
            switch (rarity) {
                case 1:
                    color = HERO_1;
                    break;
                case 2:
                    color = HERO_2;
                    break;
                case 3:
                    color = HERO_3;
                    break;
                case 4:
                    color = HERO_4;
                    break;
                case 5:
                    if (((Unit) o).getMerges()==10)
                        color = HERO_5_10;
                    else
                        color = HERO_5;
                    break;
                default:
                    color = DEFAULT;
                    break;
            }
        } else if (o instanceof Hero) {
            switch (((Hero) o).getSummonableRarity()) { //?
                case 1:
                    color = HERO_1;
                    break;
                case 2:
                    color = HERO_2;
                    break;
                case 3:
                    color = HERO_3;
                    break;
                case 4:
                    color = HERO_4;
                    break;
                case 5:
                    color = HERO_5;
                    break;
                default:
                    color = DEFAULT;
                    break;
            }
        } else if (o instanceof Skill) {
            color = ((Skill) o).getColor();
        } else {
            color = Color.white;
        }

        return color;
    }
}