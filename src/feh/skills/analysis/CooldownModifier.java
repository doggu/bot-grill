package feh.skills.analysis;

public interface CooldownModifier {

    //deprecated?
    //stat/cooldown modifiers are only evaluated upon the creation of a user's kit,
    //so simply checking for cooldown modifiers on ALL skills does not consume
    //a significant amount of resources. it would probably be easier to process
    //a null modifier result from a few hundred skills than to isolate a handful
    //which do modify stats/cooldown

    int getCooldownModifier();
}
