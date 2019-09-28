package feh.heroes.skills.skillTypes;

import java.awt.*;
import java.net.URL;

public class Special extends Skill {
    private final int cd;

    private final boolean[][] damagePattern;



    public Special(String name, String description, URL link, int cost, boolean exclusive,
                   int cd, boolean[][] damagePattern) {
        super(name, description, link, new Color(0xF400E5), 'S', cost, exclusive);

        this.cd = cd;
        this.damagePattern = damagePattern;
    }



    public int getCooldown() { return cd; }
    public boolean isAoE() { return damagePattern!=null; }
    public boolean[][] getDamagePattern() { return damagePattern; }
    public String printDamagePattern() {
        if (!isAoE()) return null;
        StringBuilder table = new StringBuilder(divider());

        for (boolean[] row:damagePattern) {
            table.append("| ");
            for (boolean d:row) {
                if (d) table.append('x');
                else table.append(' ');
                table.append(" | ");
            }
            table.append('\n').append(divider());
        }

        return table.substring(0, table.length()-1);
    }
    private String divider() {
        return "+" + " - +".repeat(damagePattern[0].length) + "\n"; //the IDE totally owned me in efficiency
    }
}
