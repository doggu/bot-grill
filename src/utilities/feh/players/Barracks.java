package utilities.feh.players;

import utilities.feh.heroes.Unit;
import utilities.feh.heroes.UnitDatabase;
import utilities.feh.heroes.character.Hero;

import java.util.ArrayList;

public class Barracks extends ArrayList<Unit> {
    public static final int
    NAME = 0,
    HP = 1,
    ATK = 2,
    SPD = 3,
    DEF = 4,
    RES = 5;



    public Barracks() {
        super();
    }



    public void sort(int type) {
        for (int i=0; i<super.size(); i++) {

        }
    }

    public void sortByAtk() {
        while (!sortedByAtk()) {
            for (int i = 0; i < super.size()-1; i++) {
                if (super.get(i).getAtk()<super.get(i+1).getAtk()) {
                    Unit temp = super.get(i);
                    super.set(i, super.get(i+1));
                    super.set(i+1, temp);
                }
            }
        }
    }
    private boolean sortedByAtk() {
        if (super.size()==1) return true;
        for (int i=0; i<super.size()-1; i++) {
            if (super.get(i).getAtk()<super.get(i+1).getAtk())
                return false;
        }
        return true;
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
        g.add((Unit) sheeda);

        System.out.println(g);
    }
}
