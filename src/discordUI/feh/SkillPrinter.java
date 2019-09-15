package discordUI.feh;

import feh.heroes.character.Hero;
import feh.heroes.skills.analysis.ActionSkill;
import feh.heroes.skills.analysis.StatModifier;
import feh.heroes.skills.skillTypes.*;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.HashMap;

import static feh.heroes.UnitDatabase.HEROES;

public class SkillPrinter {
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



        return skill;
    }



    private static String printStats(int[] stats) {
        StringBuilder statString = new StringBuilder();
        for (int x:stats)
            statString.append(x).append((Math.log10(x)<1?"    ":"   "));
        statString.append("\n");

        return statString.toString();
    }
}
