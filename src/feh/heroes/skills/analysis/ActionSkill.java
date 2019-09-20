package feh.heroes.skills.analysis;

// action skills are only weapons and assists: they allow a unit to perform an action on another unit (enemy or not).
//todo: conv. to class and extend Skill so Weapon & Assist can extend this object?
public interface ActionSkill {
    int getRng();
}
