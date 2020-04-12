package feh.players;

import feh.characters.unit.Unit;
import feh.characters.HeroDatabase;
import feh.characters.hero.Hero;

import java.util.ArrayList;
import java.util.HashMap;

public class Barracks extends ArrayList<Unit> {
    public static final Unit
            ALFONSE = new Unit(
                    new Hero("Alfonse: Prince of Askr"),
                    4,-1,-1, 40),
            SHARENA = new Unit(
                    new Hero("Sharena: Princess of Askr"),
                    4,-1,-1, 40),
            ANNA =  new Unit(
                    new Hero("Anna: Commander"),
                    4,-1,-1, 40),
            TAKUMI = new Unit(
                    new Hero("Takumi: Wild Card"),
                    4,-1,-1, 40),
            VIRION = new Unit(
                    new Hero("Virion: Elite Archer"),
                    4,-1,-1, 40),
            RAIGH = new Unit(
                    new Hero("Raigh: Dark Child"),
                    4,-1,-1, 40);



    private HashMap<String, Unit> nicknames = new HashMap<>();



    public Barracks() {
        super();
        add(ALFONSE);
        add(SHARENA);
        add(ANNA);
        add(TAKUMI);
        add(VIRION);
        add(RAIGH);
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
        for (Hero x: HeroDatabase.HEROES) {
            if (x.getFullName().toString().equals("Caeda: Talys's Bride")) {
                sheeda = x;
            }
        }

        if (sheeda==null) return;

        sheeda = new Unit(sheeda, 5, 2, 0, 40);
        g.add((Unit) sheeda);

        System.out.println(g);
    }
}
