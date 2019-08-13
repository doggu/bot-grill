package events.fehGame.retriever;

import events.ReactionButton;
import events.commands.Command;
import feh.heroes.character.Hero;
import feh.skills.analysis.ActionSkill;
import feh.skills.analysis.StatModifier;
import feh.skills.skillTypes.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static feh.heroes.UnitDatabase.HEROES;
import static feh.skills.SkillDatabase.SKILLS;

public class SkillRetriever extends Command {
    public void onCommand() {
        List<String> nameArr = new ArrayList<>(Arrays.asList(args));
        nameArr.remove(0);

        List<Skill> candidates = new ArrayList<>(SKILLS);



        //filter out based on detected parameters
        HashMap<String,String> params = new HashMap<>();

        for (int i=0; i<nameArr.size(); i++) {
            String x = nameArr.get(i);
            int colon = x.indexOf(':');
            if (colon>0&&colon<x.length()-1) { //exists and isn't start of string
                params.put(x.substring(0,colon).toLowerCase(),
                        x.substring(colon+1).toLowerCase());
                nameArr.remove(i);
                i--;
            }
        }

        for (String x:params.keySet()) {
            switch (x) {
                case "exclusive":
                    boolean exclusive;
                    try {
                        exclusive = parseBoolean(params.get(x));
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                        continue;
                    }

                    for (int i=0; i<candidates.size(); i++) {
                        if (candidates.get(i).isExclusive()!=exclusive) {
                            candidates.remove(i);
                            i--;
                        }
                    }
                    break;
                case "slot":
                case "type":
                    char slot;
                    switch (params.get(x)) {
                        case "w":
                        case "weapon":
                        case "wep":
                            slot = 0;
                            break;
                        case "assist":
                        case "ass":
                        case "ast":
                        case "asst":
                            slot = 1;
                            break;
                        case "special":
                        case "sp":
                            slot = 2;
                            break;
                        case "a":
                            slot = 3;
                            break;
                        case "b":
                            slot = 4;
                            break;
                        case "c":
                            slot = 5;
                            break;
                        case "s":
                            slot = 6;
                            break;
                        case "passive":
                        case "pass":
                            //uhhhHhhhHhh
                        default:
                            continue;
                    }

                    for (int i=0; i<candidates.size(); i++) {
                        if (candidates.get(i).getSlot()!=slot) {
                            candidates.remove(i);
                            i--;
                        }
                    }
                    break;
            }
        }



        StringBuilder name = new StringBuilder();
        for (String x:nameArr)
            name.append(x).append(" ");
        name.delete(name.length()-1, name.length());
        for (Skill x:SKILLS) {
            if (!x.getName().toLowerCase().contains(name.toString().toLowerCase())) {
                candidates.remove(x);
            }
        }




        //remove lower tiers of skills (the number thingy)
        for (int i=0; i<candidates.size(); i++) {
            for (int j=0; j<candidates.size(); j++) {
                if (i==j) continue;

                Skill x = candidates.get(i),
                        y = candidates.get(j);
                if (x!=y) {
                    String xN = x.getName(), yN = y.getName();
                    if (xN.substring(0,xN.length()-2).equals(yN.substring(0,yN.length()-2)) //same skill
                            &&xN.charAt(xN.length()-1)!=yN.charAt(yN.length()-1)) { //different numbers
                        //example of something that would cause an error:
                        //comparing "bark" and "barf"
                        int xLevel = Integer.parseInt(String.valueOf(xN.charAt(xN.length()-1)));
                        int yLevel = Integer.parseInt(String.valueOf(yN.charAt(yN.length()-1)));

                        if (xLevel==4||yLevel==4) continue; //one's tier 4 which is prolly different

                        if (xLevel>yLevel) {
                            candidates.remove(y);
                            j--;
                        } else {
                            candidates.remove(x);
                            i--;
                            break;
                        }
                    }
                }

                if (x.getName().contains(y.getName())) {
                    if (x.isExclusive()||y.isExclusive()) break;
                    candidates.remove(y);
                }
            }
        }

        if (candidates.size()==0) {
            sendMessage("could not find your skill.");
            return;
        }




        for (Skill x:candidates) {
            Message f = sendMessage(printSkill(x).build());

            if (x instanceof Weapon) {
                if (((Weapon) x).hasRefine()) {
                    WeaponRefine refine = ((Weapon) x).getRefine();
                    e.getJDA().addEventListener(new ReactionButton(
                            f,
                            e.getJDA()
                                    .getEmotesByName("Divine_Dew", false)
                                    .get(0)
                    ) {
                        @Override
                        public void onCommand() {
                            getMessage().editMessage(new EmbedBuilder(getMessage().getEmbeds().get(0))
                                    .setAuthor(refine.getName()+" (+Eff)")
                                    .setDescription(refine.getDescription())
                                    //.setColor(new Color(0xDE1336))
                                    .build()).complete();
                        }
                    });
                }
            }
        }



        StringBuilder report = new StringBuilder("found skill" + (candidates.size()>1?"s":"") + " for doggu:");

        for (int i=0; i<candidates.size(); i++) {
            Skill x = candidates.get(i);
            if (i%2==0) {
                report.append("\n\t");
            }
            report.append(x.getName()).append(", ");
        }
        report.delete(report.length()-2, report.length());

        log(report.toString());
    }



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



    private MessageBuilder printSkill(Skill x) {
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder skill = new EmbedBuilder();

        switch (x.getSlot()) {
            case 0: //weapon
                skill.setColor(new Color(0xDE1336));
                break;
            case 1: //assist
                skill.setColor(new Color(0x00EDB3));
                break;
            case 2: //special
                skill.setColor(new Color(0xF400E5));
                break;
            case 3: //a-passive
                skill.setColor(new Color(0xFF2A2A));
                break;
            case 4: //b-passive
                skill.setColor(new Color(0x003ED3));
                break;
            case 5: //c-passive
                skill.setColor(new Color(0x09C639));
                break;
            case 6: //sacred seal
                skill.setColor(new Color(0xEDE500));
                break;
        }

        skill.setAuthor(x.getName());
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



        if (x instanceof  Passive) {
            skill.setThumbnail(((Passive) x).getIcon());
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
        if (x instanceof StatModifier) {
            int[] statModifiers = ((StatModifier) x).getStatModifiers();
            boolean modifiesStats = false;
            for (int stat:statModifiers) if (stat!=0) {
                modifiesStats = true;
                break;
            }
            if (modifiesStats) {
                String printedStatModifiers = "```\n" + printStats(statModifiers) + "\n```";
                skill.addBlankField(false);
                skill.addField("stat modifiers", printedStatModifiers, false);
            }
        }



        message.setEmbed(skill.build());



        return message;
    }

    private static boolean parseBoolean(String x) {
        switch (x) {
            case "true":
            case "absolutely":
            case "yes":
            case "y":
            case "ye":
            case "yee":
            case "yeee":
            case "yeeee":
            case "yea":
            case "totes":
            case "obviously":
            case "yuh":
                return true;
            default:
                return false;
        }
    }



    private static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((Math.log10(x)<1?"    ":"   "));
        statString.append("\n");

        return statString.toString();
    }





    public boolean isCommand() {
        String arg = args[0].toLowerCase();
        switch(arg) {
            case "getskill":
            case "getskills":
            case "gs":
                return true;
            default:
                return false;
        }
    }



    public String getName() { return "SkillRetriever"; }
    public String getDescription() { return "Get pertinent info on skills in Fire Emblem Heroes!"; }
    public String getFullDescription() {
        //TODO: this also needs to be written at some point
        return "wooooooooooooooooooweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    }
}
