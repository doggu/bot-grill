package events.fehGame.retriever;

import discordUI.feh.SkillPrinter;
import events.commands.Command;
import feh.heroes.skills.skillTypes.Skill;
import feh.heroes.skills.skillTypes.Weapon;
import main.BotMain;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static feh.heroes.skills.SkillDatabase.SKILLS;

public class SkillRetriever extends Command {
    public void onCommand() {
        List<String> nameArr = new ArrayList<>(Arrays.asList(args));
        nameArr.remove(0);

        List<Skill> candidates = new ArrayList<>(SKILLS);



        //filter out based on detected parameters
        HashMap<String,String> params = new HashMap<>();

        for (int i=0; i<nameArr.size(); i++) {
            String x = nameArr.get(i);
            int colon = x.indexOf(':');
            if (colon>0&&colon<x.length()-1) { //exists and isn't start of string
                params.put(x.substring(0,colon).toLowerCase(),
                        x.substring(colon+1).toLowerCase());
                nameArr.remove(i);
                i--;
            }
        }

        for (String x:params.keySet()) {
            switch (x) {
                case "exclusive":
                    boolean exclusive;
                    try {
                        exclusive = parseBoolean(params.get(x));
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                        continue;
                    }

                    for (int i=0; i<candidates.size(); i++) {
                        if (candidates.get(i).isExclusive()!=exclusive) {
                            candidates.remove(i);
                            i--;
                        }
                    }
                    break;
                case "slot":
                case "type":
                    char slot;
                    switch (params.get(x)) {
                        case "w":
                        case "weapon":
                        case "wep":
                            slot = 0;
                            break;
                        case "assist":
                        case "ass":
                        case "ast":
                        case "asst":
                            slot = 1;
                            break;
                        case "special":
                        case "sp":
                            slot = 2;
                            break;
                        case "a":
                            slot = 3;
                            break;
                        case "b":
                            slot = 4;
                            break;
                        case "c":
                            slot = 5;
                            break;
                        case "s":
                            slot = 6;
                            break;
                        case "passive":
                        case "pass":
                            //uhhhHhhhHhh
                        default:
                            continue;
                    }

                    for (int i=0; i<candidates.size(); i++) {
                        if (candidates.get(i).getSlot()!=slot) {
                            candidates.remove(i);
                            i--;
                        }
                    }
                    break;
            }
        }



        StringBuilder name = new StringBuilder();
        for (String x:nameArr)
            name.append(x).append(" ");
        name.delete(name.length()-1, name.length());
        for (Skill x:SKILLS) {
            if (!x.getName().toLowerCase().contains(name.toString().toLowerCase())) {
                candidates.remove(x);
            }
        }




        //remove lower tiers of skills (the number thingy)
        for (int i=0; i<candidates.size(); i++) {
            for (int j=0; j<candidates.size(); j++) {
                if (i==j) continue;

                Skill x = candidates.get(i),
                        y = candidates.get(j);
                if (x!=y) {
                    String xN = x.getName(), yN = y.getName();
                    if (xN.substring(0,xN.length()-2).equals(yN.substring(0,yN.length()-2)) //same skill
                            &&xN.charAt(xN.length()-1)!=yN.charAt(yN.length()-1)) { //different numbers
                        //example of something that would cause an error:
                        //comparing "bark" and "barf"
                        int xLevel = Integer.parseInt(String.valueOf(xN.charAt(xN.length()-1)));
                        int yLevel = Integer.parseInt(String.valueOf(yN.charAt(yN.length()-1)));

                        if (xLevel==4||yLevel==4) continue; //one's tier 4 which is prolly different

                        if (xLevel>yLevel) {
                            candidates.remove(y);
                            j--;
                        } else {
                            candidates.remove(x);
                            i--;
                            break;
                        }
                    }
                }

                if (x.getName().contains(y.getName())) {
                    if (x.isExclusive()||y.isExclusive()) break;
                    candidates.remove(y);
                }
            }
        }

        if (candidates.size()==0) {
            sendMessage("could not find your skill.");
            return;
        }




        for (Skill x:candidates) {
            //todo: menu for multiple results? (kind of cumbersome for stuff like this)
            Message f = sendMessage(SkillPrinter.printSkill(x).build());

            if (x instanceof Weapon) {
                if (((Weapon) x).hasRefine()) {
                    BotMain.addListener(new RefineButton(f, (Weapon) x));
                }
            }
        }



        StringBuilder report =
                new StringBuilder("found skill" + (candidates.size()>1?"s":"") +
                        " for "+e.getAuthor().getName()+":");

        for (int i=0; i<candidates.size(); i++) {
            Skill x = candidates.get(i);
            if (i%2==0) {
                report.append("\n\t");
            }
            report.append(x.getName()).append(", ");
        }
        report.delete(report.length()-2, report.length());

        log(report.toString());
    }








    private static boolean parseBoolean(String x) {
        switch (x) {
            case "true":
            case "absolutely":
            case "yes":
            case "y":
            case "ye":
            case "yee":
            case "yeee":
            case "yeeee":
            case "yea":
            case "totes":
            case "obviously":
            case "yuh":
                return true;
            default:
                return false;
        }
    }



    public boolean isCommand() {
        return args[0].toLowerCase().matches("g(et)?s(k(ill)?)?");
    }



    public String getName() { return "SkillRetriever"; }
    public String getDescription() { return "Get pertinent info on skills in Fire Emblem Heroes!"; }
    public String getFullDescription() {
        //TODO: this also needs to be written at some point
        return "wooooooooooooooooooweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    }
}
