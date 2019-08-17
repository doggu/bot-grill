package feh.battle.map;

import feh.battle.FieldedUnit;

import java.awt.*;
import java.util.HashMap;

public class SpurMap extends HashMap<Point, int[]> {
    private final int xSize, ySize;



    SpurMap(Point boardSize) {
        this.xSize = boardSize.x;
        this.ySize = boardSize.y;
    }




    public int[] spursAt(Point p) { return super.get(p); }


    /**
     * adds the spheres of influence of spurs/drives from a unit on the board.
     * @param unit the unit who is providing the combat buffs
     */
    private void drive(FieldedUnit unit) {
        Point unitPos = unit.getPos();
    }




}
