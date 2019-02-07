package utilities.feh.summoning;

import utilities.feh.heroes.Unit;
import utilities.feh.heroes.UnitDatabase;
import utilities.feh.heroes.character.Hero;
import utilities.feh.players.Summoner;

import java.util.ArrayList;

public class Barracks {
    private final ArrayList<Unit> units = new ArrayList<>();

    public void addUnit(Unit x) { units.add(x); }



    public String toString() {
        return units.toString();
    }



    public static void main(String[] args) {
        Barracks g = new Barracks();
        Hero sheeda = null;
        for (Hero x:UnitDatabase.HEROES) {
            if (x.getFullName().toString().equals("Caeda: Talys's Bride")) {
                sheeda = x;
            }
        }

        if (sheeda==null) return;

        sheeda = new Unit(sheeda, 5, Unit.SPD, Unit.HP);
        g.addUnit((Unit) sheeda);

        System.out.println(g);
    }
}
