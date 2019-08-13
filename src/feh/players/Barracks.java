package feh.players;

import feh.heroes.unit.Unit;
import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;

import java.util.ArrayList;
import java.util.HashMap;

public class Barracks extends ArrayList<Unit> {
    public static final int
            NAME = 0,
            HP = 1,
            ATK = 2,
            SPD = 3,
            DEF = 4,
            RES = 5;

    public static final Unit
            ALFONSE = new Unit(new Hero("Alfonse: Prince of Askr"),4,-1,-1),
            SHARENA = new Unit(new Hero("Sharena: Princess of Askr"),4,-1,-1),
            ANNA =  new Unit(new Hero("Anna: Commander"),4,-1,-1),
            TAKUMI = new Unit(new Hero("Takumi: Wild Card"),4,-1,-1),
            VIRION = new Unit(new Hero("Virion: Elite Archer"),4,-1,-1),
            RAIGH = new Unit(new Hero("Raigh: Dark Child"),4,-1,-1);



    private HashMap<String, Unit> nicknames = new HashMap<>();



    public Barracks() {
        super();
        super.add(ALFONSE);
        super.add(SHARENA);
        super.add(ANNA);
        super.add(TAKUMI);
        super.add(VIRION);
        super.add(RAIGH);
    }



    public void sort(ArrayList<Integer> stat) {
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
    public boolean registerNickname(String nickname, Unit unit) {
        if (nicknames.get(nickname.toLowerCase())!=null)
            return false;
        nicknames.put(nickname.toLowerCase(), unit);
        return true;
    }
    public Unit retrieveUnit(String nickname) {
        return nicknames.get(nickname.toLowerCase());
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
