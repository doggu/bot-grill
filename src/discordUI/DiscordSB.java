package discordUI;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class DiscordSB extends MessageBuilder {
    public DiscordSB() {}
    public DiscordSB(CharSequence content) {
        super(content);
    }
    public DiscordSB(Message message) {
        super(message);
    }
    public DiscordSB(MessageBuilder builder) {
        super(builder);
    }
    public DiscordSB(EmbedBuilder builder) {
        super(builder);
    }
    public DiscordSB(MessageEmbed embed) {
        super(embed);
    }


    //idk if this is sacreligious or something
    public DiscordSB unshift(String s) {
        builder.insert(0, s);
        return this;
    }



////////////////////////////////////////////////////////////////////////////////
//                                  MARKDOWN                                  //
////////////////////////////////////////////////////////////////////////////////
    //general formatting
    /**
     * uses generally normal boxing shit to italicize
     * a certain range in the StringBuilder.
     * @param start the first character to be italicizec
     * @param end the character after the last character to be italicized
     * @return this object.
     */
    public DiscordSB italicize(int start, int end) {
        return wrap(start, end, '*');
    }
    public DiscordSB italicize() {
        return italicize(0, builder.length());
    }

    public DiscordSB bold(int start, int end) {
        return wrap(start, end, "**");
    }
    public DiscordSB bold() {
        return bold(0, builder.length());
    }

    public DiscordSB boltalicize(int start, int end) {
        return wrap(start, end, "***");
    }
    public DiscordSB boltalicize() {
        return boltalicize(0, builder.length());
    }

    public DiscordSB underline(int start, int end) {
        return wrap(start, end, "__");
    }
    public DiscordSB underline() {
        return underline(0, builder.length());
    }

    public DiscordSB strikethrough(int start, int end) {
        return wrap(start, end, "~~");
    }
    public DiscordSB strikethrough() {
        return strikethrough(0, builder.length());
    }


    //code lines/blocks
    public DiscordSB wrapCode() {
        return wrap (0, builder.length(), '`');
    }
    public DiscordSB wrapCode(int start, int end) {
        return wrap(start, end, '`');
    }

    @Nullable
    public DiscordSB blockCode(String language) {
        builder.insert(0, "```" + language).append("```");
        return this;
    }

    public DiscordSB appendCodeLine(String code) {
        builder.append('`').append(code).append('`');
        return this;
    }
    @Nullable
    public DiscordSB appendCodeBlock(@Nullable String language, String code) {
        builder.append("```");
        if (language!=null) builder.append(language);
        builder.append('\n').append(code).append("```");
        return this;
    }



////////////////////////////////////////////////////////////////////////////////
//                                     UI                                     //
////////////////////////////////////////////////////////////////////////////////
    public DiscordSB mention(@NotNull User user) {
        super.append(user.getAsMention());
        return this;
    }
    //implementing an insertion version is probably unnecessary


    //spoilers
    public DiscordSB wrapSpoiler(int start, int end) {
        return wrap(start, end, "||");
    }

    //todo: maybe use id enum to abstractify append
    public DiscordSB appendSpoiler(String spoiler) {
        builder.append("||").append(spoiler).append("||");
        return this;
    }


    //private utilities that seemed useful enough to be public

    public DiscordSB wrap(int start, int end, char c) {
        builder.insert(start, c).insert(end, c);
        return this;
    }

    public DiscordSB wrap(int start, int end, String s) {
        builder.insert(start, s).insert(end, s);
        return this;
    }

    public DiscordSB appendWrapped(String string, char c) {
        builder.append(c).append(string).append(c);
        return this;
    }
    public DiscordSB appendWrapped(String string, String s) {
        builder.append(s).append(string).append(s);
        return this;
    }



    ////////////
    //                          tools or something idk this organization isn't super helpful anyway
    ////////////

    public DiscordSB appendBrkt(String string) {
        builder.append('[').append(string).append(']');
        return this;
    }
    public DiscordSB appendPrns(String string) {
        builder.append('(').append(string).append(')');
        return this;
    }
    public DiscordSB appendBrcd(String string) {
        builder.append('{').append(string).append('}');
        return this;
    }
    //somewhat useless
    public DiscordSB appendQuote(String string) {
        return appendWrapped(string, '"');
    }
}
