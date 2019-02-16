package events.gameroom.flow;

import events.commands.Command;

import java.awt.*;
import java.util.ArrayList;

public class Board {
    private static final Color[][] DOTS = {
            {null, null, null, null, null, null, null},
            {Color.BLUE, Color.YELLOW, null, null, null, Color.CYAN, Color.GREEN},
            {Color.ORANGE, Color.BLUE, null, null, null, null, Color.RED},
            {null, null, Color.YELLOW, null, null, null, null},
            {null, null, null, null, null, Color.CYAN, null},
            {null, null, null, Color.ORANGE, null, Color.GREEN, null},
            {Color.RED, null, null, null, null, null, null},
    };
    private ArrayList<Color> lines = new ArrayList<>();



    public Board() {

    }
}
