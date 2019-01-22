package events.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import utilities.fehUnits.heroes.UnitDatabase_old;
import utilities.fehUnits.skills.Skill;
import utilities.fehUnits.skills.SkillDatabase;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

public class EmbedTest extends Command {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("embedTest");
    }


    public void onCommand() {
        EmbedBuilder eb = new EmbedBuilder();

        List<Skill> skillz = SkillDatabase.getList();

        Skill ex = skillz.get((int)(Math.random()*skillz.size()));

        eb.setTitle("Description", null);

        eb.setColor(Color.red);
        eb.setColor(new Color(0xF40C0C));
        eb.setColor(new Color(255, 0, 54));

        eb.setDescription(ex.getDescription());

        eb.addField("Title of field", "test of field", false);

        eb.addBlankField(false);

        eb.setAuthor(ex.getName());
        //eb.setAuthor(ex.getName(), [url], [iconURL]);

        //eb.setFooter("FauUIAerE EmbUL HeruO", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

        //eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");

        HashMap<Integer, String> skillIcons = new HashMap<>();

        skillIcons.put(0, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/8/82/Icon_Skill_Weapon.png");
        skillIcons.put(1, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/9/9a/Icon_Skill_Assist.png");
        skillIcons.put(2, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/2/25/Icon_Skill_Special.png");
        skillIcons.put(3, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/68/Passive_Icon_A.png");
        skillIcons.put(4, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/8/84/Passive_Icon_C.png");
        skillIcons.put(5, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/6a/Passive_Icon_B.png");
        skillIcons.put(6, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/6f/Passive_Icon_S.png");

        eb.setThumbnail(skillIcons.get(ex.getSlot()));

        e.getChannel().sendMessage(eb.build()).queue();
    }

    private void exampleCode() {
        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

        /*
            Set the title:
            1. Arg: title as string
            2. Arg: URL as string or could also be null
         */
        eb.setTitle("Title", null);

        /*
            Set the color
         */
        eb.setColor(Color.red);
        eb.setColor(new Color(0xF40C0C));
        eb.setColor(new Color(255, 0, 54));

        /*
            Set the text of the Embed:
            Arg: text as string
         */
        eb.setDescription("Text");

        /*
            Add fields to embed:
            1. Arg: title as string
            2. Arg: text as string
            3. Arg: inline mode true / false
         */
        eb.addField("Title of field", "test of field", false);

        /*
            Add spacer like field
            Arg: inline mode true / false
         */
        eb.addBlankField(false);

        /*
            Add embed author:
            1. Arg: name as string
            2. Arg: url as string (can be null)
            3. Arg: icon url as string (can be null)
         */
        eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

        /*
            Set footer:
            1. Arg: text as string
            2. icon url as string (can be null)
         */
        eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

        /*
            Set image:
            Arg: image url as string
         */
        eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");

        /*
            Set thumbnail image:
            Arg: image url as string
         */
        eb.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
    }
}
