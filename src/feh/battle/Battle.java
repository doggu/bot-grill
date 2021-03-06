package feh.battle;

import events.gameroom.mapTest.Board;

import java.util.ArrayList;

public class Battle {
    private final int turn;
    private final int turnLimit;
    private final Board map;
    private final ArrayList<FieldedUnit> team1, team2;


    public Battle(ArrayList<FieldedUnit> team1, ArrayList<FieldedUnit> team2, Board map) {
        this.team1 = team1;
        this.team2 = team2;
        this.map = map;

        this.turn = 1;
        this.turnLimit = Integer.MAX_VALUE;
    }



    public boolean isOddTurn() { return turn%2==1; }
    public boolean isEvenTurn() { return !isOddTurn(); }


}
