package events.fehGame.retriever;

import events.ReactionButton;
import events.commands.Command;
import feh.heroes.character.Hero;
import feh.skills.*;
import feh.skills.analysis.ActionSkill;
import feh.skills.analysis.StatModifier;
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

        StringBuilder name = new StringBuilder();
        for (String x:nameArr)
            name.append(x).append(" ");
        name.delete(name.length()-1, name.length());

        List<Skill> candidates = new ArrayList<>();
        for (Skill x:SKILLS) {
            if (x.getName().toLowerCase().contains(name.toString().toLowerCase())) {
                candidates.add(x);
            }
        }

        //for (Skill x:candidates) System.out.println(x.getName());

        List<Skill> remove = new ArrayList<>();

        //remove lower tiers of skills (the number thingy)
        for (Skill x:candidates) {
            for (Skill y:candidates) {
                if (x!=y) {
                    String xN = x.getName(), yN = y.getName();
                    if (xN.substring(0,xN.length()-2)
                            .equals(yN.substring(0,yN.length()-2))) {
                        int xLevel = Integer.parseInt(String.valueOf(xN.charAt(xN.length()-1)));
                        int yLevel = Integer.parseInt(String.valueOf(yN.charAt(yN.length()-1)));

                        if (xLevel==4||yLevel==4); //one's tier 4 which is prolly different
                        else if (xLevel>yLevel)
                            remove.add(y);
                        else
                            remove.add(x);
                    }
                }
            }
        }

        //remove lower tiers of weapons
        for (Skill x:candidates) {
            for (Skill y:candidates) {
                //same objects
                if (x!=y) {
                    if (x.getName().contains(y.getName())) {
                        if (x.isExclusive()||y.isExclusive()) break;
                        remove.add(y);
                    }
                }
            }
        }
        for (Skill x:remove)
            candidates.remove(x);

        if (candidates.size()==0) {
            sendMessage("could not find your skill.");
            return;
        }



        HashMap<Integer, String> skillIcons = new HashMap<>();

        skillIcons.put(0, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/8/82/Icon_Skill_Weapon.png");
        skillIcons.put(1, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/9/9a/Icon_Skill_Assist.png");
        skillIcons.put(2, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/2/25/Icon_Skill_Special.png");
        skillIcons.put(3, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/1/10/Icon_Skill_Passive_A.png");
        skillIcons.put(4, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/c/ca/Icon_Skill_Passive_B.png");
        skillIcons.put(5, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/0/01/Icon_Skill_Passive_C.png");
        skillIcons.put(6, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/9/9c/Icon_Skill_Passive_S.png");

        for (Skill x:candidates) {
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

            skill.setThumbnail(skillIcons.get(x.getSlot()));

            if (x instanceof ActionSkill) { //instanceof targeting skill
                if (x instanceof Weapon) {
                    skill.addField("Might", String.valueOf(((Weapon) x).getMt()), true);
                }

                skill.addField("Range", String.valueOf(((ActionSkill) x).getRng()), true);
            } else if (x instanceof Special) {
                skill.addField("Cooldown", String.valueOf(((Special) x).getCooldown()), true);
            }

            //something that a unit can possess
            if (!(x instanceof PassiveS)) {
                skill.addField("Exclusive?", (x.isExclusive()?"Yes":"No"), false);
                StringBuilder owners = new StringBuilder();
                for (Hero n:HEROES) {
                    if (n.getBaseKit().contains(x)) {
                        owners.append(n).append(", ");
                    }
                }
                if (owners.length() > 0) owners = new StringBuilder(owners.substring(0, owners.length()-2));
                skill.addField("Owner"+(x.isExclusive()?"":"s"), owners.toString(), false);
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

            Message f = sendMessage(skill.build());

            if (x instanceof Weapon) {
                if (((Weapon) x).hasRefine()) {
                    WeaponRefine refine = ((Weapon) x).getRefine();
                    ReactionButton refineButton = new ReactionButton(
                            f,
                            e.getJDA()
                                    .getEmotesByName("Divine_Dew", false)
                                    .get(0),
                            new MessageBuilder(new EmbedBuilder()
                                    .setTitle(refine.getName())
                                    .setDescription(refine.getDescription())
                                    .setColor(new Color(0xDE1336))
                                    .build())
                    );
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
