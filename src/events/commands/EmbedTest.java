package events.commands;

import events.MessageListener;
import net.dv8tion.jda.core.EmbedBuilder;
import utilities.feh.skills.Skill;
import utilities.feh.skills.SkillDatabase;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

public class EmbedTest extends MessageListener {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("embedTest");
    }

    public void onCommand() {
        EmbedBuilder eb = new EmbedBuilder();

        List<Skill> skillz = SkillDatabase.SKILLS;

        for (int i=0; i<5; i++) {
            Skill ex = skillz.get((int) (Math.random() * skillz.size()));



            switch (ex.getSlot()) {
                case 0:
                    eb.setColor(new Color(0xDE1336));
                    break;
                case 1:
                    eb.setColor(new Color(0x00EDB3));
                    break;
                case 2:
                    eb.setColor(new Color(0xF400E5));
                    break;
                case 3:
                    eb.setColor(new Color(0xFF2A2A));
                    break;
                case 4:
                    eb.setColor(new Color(0x003ED3));
                    break;
                case 5:
                    eb.setColor(new Color(0x09C639));
                    break;
                case 6:
                    eb.setColor(new Color(0xEDE500));
                    break;
            }

            eb.setAuthor(ex.getName());
            eb.setDescription(ex.getDescription());

            //eb.addField(ex.getName(), ex.getDescription(), true);

            //eb.addBlankField(false);

            //eb.setAuthor(ex.getName(),"https://feheroes.gamepedia.com/Attack_Defense_Solo","https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/e/ed/Atk_Def_Solo_3.png");

            //eb.setFooter("FauUIAerE EmbUL HeruO", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

            //eb.setImage("");

            HashMap<Integer, String> skillIcons = new HashMap<>();

            skillIcons.put(0, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/8/82/Icon_Skill_Weapon.png");
            skillIcons.put(1, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/9/9a/Icon_Skill_Assist.png");
            skillIcons.put(2, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/2/25/Icon_Skill_Special.png");
            skillIcons.put(3, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/68/Passive_Icon_A.png");
            skillIcons.put(4, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/6a/Passive_Icon_B.png");
            skillIcons.put(5, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/8/84/Passive_Icon_C.png");
            skillIcons.put(6, "https://d1u5p3l4wpay3k.cloudfront.net/feheroes_gamepedia_en/6/6f/Passive_Icon_S.png");

            eb.setThumbnail(skillIcons.get(ex.getSlot()));

            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    public char getPrefix() { return '&'; }


    /*
    private void exampleCode() {
        // source: https://gist.github.com/zekroTJA/c8ed671204dafbbdf89c36fc3a1827e1



        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

        // Set the title:
        // 1. Arg: title as string
        // 2. Arg: URL as string or could also be null

        eb.setTitle("Title", null);

        // Set the color

        eb.setColor(Color.red);
        eb.setColor(new Color(0xF40C0C));
        eb.setColor(new Color(255, 0, 54));

        // Set the text of the Embed:
        // Arg: text as string

        eb.setDescription("Text");

        // Add fields to embed:
        // 1. Arg: title as string
        // 2. Arg: text as string
        // 3. Arg: inline mode true / false

        eb.addField("Title of field", "test of field", false);

        // Add spacer like field
        // Arg: inline mode true / false

        eb.addBlankField(false);


        // Add embed author:
        // 1. Arg: name as string
        // 2. Arg: url as string (can be null)
        // 3. Arg: icon url as string (can be null)

        eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

        // Set footer:
        // 1. Arg: text as string
        // 2. icon url as string (can be null)

        eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

        // Set image:
        // Arg: image url as string

        eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");

        // Set thumbnail image:
        // Arg: image url as string

        eb.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
    }
    */
}
