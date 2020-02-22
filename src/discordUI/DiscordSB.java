package discordUI;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
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

    /* shit i wrote when i was going to completely remake StringBuilder

    // missing functions that i don't really need:
    //      AbstractStringBuilder append(AbstractStringBuilder asb)
    //          ^ i literally cannot



    //from StringBuilder.java (and only StringBuilder.java)



    //from AbstractStringBuilder.java
    @Override
    public int compareTo(DiscordSB dsb) { return sb.compareTo(dsb.sb); }
    @Override
    public int length() { return sb.length(); }
    public int capacity() { return sb.capacity(); }
    public void ensureCapacity(int minimumCapacity) { sb.ensureCapacity(minimumCapacity); }
    public void trimToSize() { sb.trimToSize(); }
    public void setLength(int newLength) { sb.setLength(newLength); }

    @Override
    public char charAt(int index) { return sb.charAt(index); }

    public int codePointAt(int index) { return sb.codePointAt(index); }
    public int codePointBefore(int index) { return sb.codePointBefore(index); }
    public int codePointCount(int beginIndex, int endIndex) { return sb.codePointCount(beginIndex, endIndex); }
    public int offsetByCodePoints(int index, int codePointOffset) {
        return sb.offsetByCodePoints(index, codePointOffset); }
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        sb.getChars(srcBegin, srcEnd, dst, dstBegin); }
    public void setCharAt(int index, char ch) { sb.setCharAt(index, ch); }

    //the good ones
    public DiscordSB append(Object obj) { sb.append(obj); return this; }
    public DiscordSB append(String str) { sb.append(str); return this; }
    public DiscordSB append(StringBuffer sb) { this.sb.append(sb); return this; }
    public DiscordSB append(CharSequence s) { sb.append(s); return this; }
    public DiscordSB append(CharSequence s, int start, int end) { sb.append(s, start, end); return this; }
    public DiscordSB append(char[] str) { sb.append(str); return this; }
                       //note: changed from "C-style" to "logical style"
    public DiscordSB append(char[] str, int offset, int len) { sb.append(str, offset, len); return this; }
    public DiscordSB append(boolean b) { sb.append(b); return this; }
    public DiscordSB append(char c) { sb.append(c); return this; }
    public DiscordSB append(int i) { sb.append(i); return this; }
    public DiscordSB append(long l) { sb.append(l); return this; }
    public DiscordSB append(float f) { sb.append(f); return this; }
                       //StringBuilder.java had no comments on this
    public DiscordSB append(double d) { sb.append(d); return this; }

    //eh
    public DiscordSB delete(int start, int end) { sb.delete(start, end); return this; }
    public DiscordSB appendCodePoint(int codePoint) { sb.appendCodePoint(codePoint); return this; }

    public DiscordSB deleteCharAt(int index) {
        sb.deleteCharAt(index);
        return this;
    }
    public DiscordSB replace(int start, int end, String str) {
        sb.replace(start, end, str);
        return this;
    }
                          //idk if i should go through sb or my own substring(start, end)
    public String substring(int start) {
        return sb.substring(start);
    }
    @Override
    public CharSequence subSequence(int start, int end) { return sb.subSequence(start, end); }
    public String substring(int start, int end) {
        return sb.substring(start, end);
    }
    public DiscordSB insert(int index, char[] str, int offset,
                            int len)
    {
        sb.insert(index, str, offset, len);
        return this;
    }
    public DiscordSB insert(int offset, String str) {
        sb.insert(offset, str);
        return this;
    }
    public DiscordSB insert(int offset, char[] str) {
        sb.insert(offset, str);
        return this;
    }
    public DiscordSB insert(int dstOffset, CharSequence s) {
        sb.insert(dstOffset, s);
        return this;
    }
    public DiscordSB insert(int dstOffset, CharSequence s, int start, int end) {
        sb.insert(dstOffset, s, start, end);
        return this;
    }
    public DiscordSB insert(int offset, boolean b) {
        sb.insert(offset, b);
        return this;
    }


    public DiscordSB insert(int offset, char c) {
        sb.insert(offset, c);
        return this;
    }

    public DiscordSB insert(int offset, int i) {
        sb.insert(offset, i);
        return this;
    }
    public DiscordSB insert(int offset, long l) {
        return insert(offset, String.valueOf(l));
    }
    public DiscordSB insert(int offset, float f) {
        return insert(offset, String.valueOf(f));
    }
    public DiscordSB insert(int offset, double d) {
        return insert(offset, String.valueOf(d));
    }
    public int indexOf(String str) {
        return indexOf(str, 0);
    }
    public int indexOf(String str, int fromIndex) {
        return sb.indexOf(str, fromIndex);
    }
    public int lastIndexOf(String str) {
        return sb.lastIndexOf(str);
    }
    public int lastIndexOf(String str, int fromIndex) {
        return sb.lastIndexOf(str, fromIndex);
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    @Override
    public IntStream chars() {
        return sb.chars();
    }

    public IntStream codePoints() {
        return sb.codePoints();
    }
     */
}
