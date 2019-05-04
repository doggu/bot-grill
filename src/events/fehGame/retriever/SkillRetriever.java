package events.fehGame.retriever;

import events.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import utilities.feh.skills.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SkillRetriever extends Command {
    private final List<Skill> skills;



    public SkillRetriever() {
        skills = SkillDatabase.SKILLS;
    }



    private void getSkills() {
        List<String> nameArr = new ArrayList<>(Arrays.asList(args));
        nameArr.remove(0);

        StringBuilder name = new StringBuilder();
        for (String x:nameArr)
            name.append(x).append(" ");
        name.delete(name.length()-1, name.length());

        List<Skill> candidates = new ArrayList<>();
        for (Skill x:skills) {
            if (x.getName().toLowerCase().contains(name.toString().toLowerCase())) {
                candidates.add(x);
            }
        }

        //for (Skill x:candidates) System.out.println(x.getName());

        List<Skill> remove = new ArrayList<>();
        for (Skill x:candidates) {
            for (Skill y:candidates) {
                //same objects
                if (x!=y) {
                    if (x.getName().contains(y.getName())) {
                        boolean legendary = false;
                        if (x.isExclusive()||y.isExclusive()) legendary = true;
                        if (!legendary) remove.add(y);
                    }
                }
            }
        }
        for (Skill x:remove)
            candidates.remove(x);



        HashMap<Integer, String> skillIcons = new HashMap<>();

        skillIcons.put(0, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/8/82/Icon_Skill_Weapon.png");
        skillIcons.put(1, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/9/9a/Icon_Skill_Assist.png");
        skillIcons.put(2, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/2/25/Icon_Skill_Special.png");
        skillIcons.put(3, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/1/10/Icon_Skill_Passive_A.png");
        skillIcons.put(4, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/c/ca/Icon_Skill_Passive_B.png");
        skillIcons.put(5, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/0/01/Icon_Skill_Passive_C.png");
        skillIcons.put(6, "https://gamepedia.cursecdn.com/feheroes_gamepedia_en/9/9c/Icon_Skill_Passive_S.png");

        if (candidates.size()==0) {
            sendMessage("could not find your skill.");
            return;
        }

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

            if (x instanceof Weapon) {
                skill.addField("Might", ((Weapon) x).getMt()+"", false);
            }

            if (x instanceof ActionSkill) { //instanceof targeting skill
                skill.addField("Range", ""+((ActionSkill) x).getRng(), false);
            }

            if (x instanceof Special) {
                skill.addField("Cooldown", ""+((Special) x).getCooldown(), false);
            }

            Set<String> heroes = SkillDatabase.HERO_SKILLS.keySet();
            String owners = "";
            for (String n:heroes) {
                if (SkillDatabase.HERO_SKILLS.get(n).contains(x)) {
                    owners+= n+", ";
                }
            }
            if (owners.length()>0) owners = owners.substring(0,owners.length()-2);
            skill.addField("Owners", owners, false);



            //test displays
            if (x instanceof StatModifier) {
                int[] statModifiers = ((StatModifier) x).getStatModifiers();
                String printedStatModifiers = "```\n"+printStats(statModifiers)+"\n```";
                skill.addBlankField(false);
                skill.addField("stat modifiers", printedStatModifiers, false);
            }

            e.getChannel().sendMessage(skill.build()).queue();
        }



        StringBuilder report = new StringBuilder("found skill" + (candidates.size()>1?"s":"") + " for doggu:");

        for (int i=0; i<candidates.size(); i++) {
            Skill x = candidates.get(i);
            if (i%2==0) {
                report.append("\n\t");
            }
            report.append(x.getName());
            if (i!=candidates.size()-1) {
                report.append(", ");
            }
        }

        log(report.toString());
    }



    private static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((Math.log10(x)<1?"    ":"   "));
        statString.append("\n");

        return statString.toString();
    }

    private static String printEmote(Emote e) {
        return "<:"+e.getName()+":"+e.getId()+">";
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

    public void onCommand() {
        getSkills();
    }
}
