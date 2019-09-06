package events.menu;

import net.dv8tion.jda.core.EmbedBuilder;

//idk how to create a linked pair without doing sketchy shit with components of entrysets so yeah here we are
public class MenuEntry extends EmbedBuilder {
    private final String title;
    public MenuEntry(String title) {
        this.title = title;
    }
    public MenuEntry(String title, EmbedBuilder content) {
        super(content);
        this.title = title;
    }

    String getTitle() { return title; }
}
