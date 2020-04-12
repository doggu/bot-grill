package feh.characters.skills.skillTypes;



import java.awt.*;
import java.net.URL;

public class PassiveB extends Passive {
    public PassiveB(String name, String description,
                    URL icon, URL link,
                    int cost, boolean exclusive) {
        super (name, description,
                icon, link,
                new Color(0x003ED3), 'b',
                cost, exclusive);
    }
}
