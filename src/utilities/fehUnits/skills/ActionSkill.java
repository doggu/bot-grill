package utilities.fehUnits.skills;

// action skills are only specials and assists:
// with the exception of rehabilitate, they do not modify any values on the character
// (such as raw stats, Special cooldown, etc.)
public class ActionSkill extends Skill {
    public ActionSkill() {
        super("Camilla\'s Megajugs", "Grants atk/spd/def/res+69", 'a', 10000, true);
    }
}
