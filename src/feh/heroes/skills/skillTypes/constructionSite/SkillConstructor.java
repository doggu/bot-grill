package feh.heroes.skills.skillTypes.constructionSite;

import feh.heroes.character.WeaponClass;
import feh.heroes.skills.skillTypes.*;

import java.awt.*;
import java.net.URL;

public class SkillConstructor {
    private String name;
    private String description;
    private URL link;
    private Color color; //determined by skill type anyway?
    private Integer cost;
    private Boolean exclusive;
    
    //ActionSkill (weapon and assist)
    private Integer range;
    
    //weapon
    private Integer might;
    private WeaponClass type;
    private WeaponRefine refine;
    
    //Special
    private Integer cooldown;

    //Passive
    private URL icon;


    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLink(URL link) {
        this.link = link;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }
    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    public void setRange(Integer range) {
        this.range = range;
    }
    public void setMight(Integer might) {
        this.might = might;
    }
    public void setType(WeaponClass type) {
        this.type = type;
    }
    public void setRefine(WeaponRefine refine) {
        this.refine = refine;
    }
    public void setCooldown(Integer cooldown) {
        this.cooldown = cooldown;
    }
    public void setIcon(URL icon) {
        this.icon = icon;
    }



    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public URL getLink() {
        return link;
    }
    public Color getColor() {
        return color;
    }
    public Integer getCost() {
        return cost;
    }
    public Boolean getExclusive() {
        return exclusive;
    }

    public Integer getRange() {
        return range;
    }
    public Integer getMight() {
        return might;
    }
    public WeaponClass getType() {
        return type;
    }
    public WeaponRefine getRefine() {
        return refine;
    }
    public Integer getCooldown() {
        return cooldown;
    }
    public URL getIcon() {
        return icon;
    }



    private void exists(Object o, String fieldName) throws IncompleteDataException {
        if (o==null) throw new IncompleteDataException("missing field "+fieldName);
    }
    private void vitalInfoReady() throws IncompleteDataException {
        exists(name, "name");
        exists(description, "description");
        exists(link, "link");
        exists(color, "color");
        exists(cost, "cost");
        exists(exclusive, "exclusive");
    }
    private void actionSkillReady() throws IncompleteDataException {
        exists(range, "range");
    }

    public Weapon generateWeapon() throws IncompleteDataException {
        vitalInfoReady();
        actionSkillReady();
        exists(might, "might");
        exists(type, "weapon type");
        exists(refine, "weapon refine");

        return new Weapon(name, description, link, cost, exclusive, might, range, type, refine);
    }
    public Assist generateAssist() throws IncompleteDataException {
        vitalInfoReady();
        actionSkillReady();

        return new Assist(name, description, link, cost, exclusive, range);
    }
    public Special generateSpecial() throws IncompleteDataException {
        vitalInfoReady();
        exists(cooldown, "cooldown");

        return new Special(name, description, link, cost, exclusive, cooldown);
    }

    private void passiveReady() throws IncompleteDataException {
        exists(icon, "icon");
    }
    public PassiveA generatePassiveA() throws IncompleteDataException {
        vitalInfoReady();
        passiveReady();

        return new PassiveA(name, description, icon, link, cost, exclusive);
    }
    public PassiveB generatePassiveB() throws IncompleteDataException {
        vitalInfoReady();
        passiveReady();

        return new PassiveB(name, description, icon, link, cost, exclusive);
    }
    public PassiveC generatePassiveC() throws IncompleteDataException {
        vitalInfoReady();
        passiveReady();

        return new PassiveC(name, description, icon, link, cost, exclusive);
    }
    public PassiveS generatePassiveS() throws IncompleteDataException {
        vitalInfoReady();
        passiveReady();

        return new PassiveS(name, description, icon, link, cost, exclusive);
    }



    public static void main(String[] args) {

    }
}
