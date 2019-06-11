package feh.skills.analysis;

// action skills are only weapons and assists:
// with the exception of rehabilitate, they do not modify any values on the character
// (such as raw stats, Special cooldown, etc.)
public interface ActionSkill {
    int getRng();
}
