package utilities.feh.players;

import utilities.feh.heroes.unit.Unit;
import utilities.feh.heroes.UnitDatabase;
import utilities.feh.heroes.character.Hero;

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



    private HashMap<String, Unit> nicknames = new HashMap<>();



    public Barracks() {
        super();
        Unit alfonse = new Unit(new Hero("Alfonse: Prince of Askr"),4,-1,-1);
        Unit sharena = new Unit(new Hero("Sharena: Princess of Askr"),4,-1,-1);
        Unit anna =  new Unit(new Hero("Anna: Commander"),4,-1,-1);
        Unit takumi = new Unit(new Hero("Takumi: Wild Card"),4,-1,-1);
        Unit virion = new Unit(new Hero("Virion: Elite Archer"),4,-1,-1);
        Unit raigh = new Unit(new Hero("Raigh: Dark Child"),4,-1,-1);
        super.add(alfonse);
        super.add(sharena);
        super.add(anna);
        super.add(takumi);
        super.add(virion);
        super.add(raigh);
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
