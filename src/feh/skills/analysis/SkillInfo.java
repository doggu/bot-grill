package feh.skills.analysis;

import java.util.ArrayList;

public class SkillInfo {
    private final String name;
    private final ArrayList<SkillProperty> properties;


    public SkillInfo(String name, ArrayList<SkillProperty> properties) {
        this.name = name;
        this.properties = properties;
    }


    public String getName() {
        return name;
    }

    public ArrayList<SkillProperty> getProperties() {
        return properties;
    }


    public String toString() {
        StringBuilder info = new StringBuilder(name);
        for (SkillProperty x : properties) {
            info.append(x.toString()).append('\n');
        }
        info.append('\n');
        return info.toString();
    }
}