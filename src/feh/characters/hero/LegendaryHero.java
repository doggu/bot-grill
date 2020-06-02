package feh.characters.hero;

import feh.characters.skills.skillTypes.Skill;

import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class LegendaryHero extends Hero {
    public LegendaryHero(HeroName fullName, Origin origin, String description,
                         URL gamepediaLink, URL portraitLink,
                         String artist, char gender,
                         char color,
                         WeaponClass weaponType, MovementClass moveType,
                         int summonableRarity, Availability availability,
                         GregorianCalendar dateReleased,
                         HeroStats stats, ArrayList<Skill> baseKit) {
        super(fullName, origin, description,
                gamepediaLink, portraitLink,
                artist, gender,
                color,
                weaponType, moveType,
                summonableRarity, availability,
                dateReleased,
                stats, baseKit);
    }
}