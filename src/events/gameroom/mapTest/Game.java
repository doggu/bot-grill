package events.gameroom.mapTest;

import events.commands.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import utilities.feh.heroes.UnitDatabase;
import utilities.feh.heroes.character.Hero;

import java.util.ArrayList;

public class Game extends Command {
    private final ArrayList<User> players;
    private final MessageChannel channel;

    private final int MAP_WIDTH = 6, MAP_HEIGHT = 8;
    //TODO: make this based on lobby info once this actually becomes useful
    private final char[][] board = {
            {' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' '},
    };
    //TODO: this should be Units eventually (with owners, identifiers, and other good stuff)
    private Hero[][] positions = {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
    };



    Game(ArrayList<User> players, MessageChannel channel) {
        this.players = players;
        this.channel = channel;
        startGame();
    }



    private void startGame() { sendMessage("generated a new map.\n"+printMap()); }

    private void addUnit() {
        String position = args[1];
        int[] pos = getPosition(position);
        if (!inBounds(pos)) { sendMessage("error: position index ("+position+") is out of bounds!"); return; }
        String name = args[2];
        for (int i = 3; i<args.length; i++) name+= " "+args[i];
        if (positions[pos[0]][pos[1]]!=null) {
            sendMessage("cannot place a unit on top of an already existing unit!");
            log("user attempted to place a unit on top of "+positions[pos[0]][pos[1]]);
            return;
        }
        Hero chosen = null;
        for (Hero x:UnitDatabase.HEROES) {
            if (x.getFullName().toString().equalsIgnoreCase(name)) {
                chosen = x;
            }
        }

        if (chosen==null) {
            sendMessage("could not find your specified hero. please try again.");
            return;
        } else {
            positions[pos[0]][pos[1]] = chosen;
            sendMessage(
                    printMap()+"\n"+
                    "placed "+chosen+" at "+position);
            log(e.getAuthor()+" placed "+chosen+" at "+position);
            return;
        }
    }

    private void moveUnit() {
        String selection = args[1];
        String destination = args[3];
        //TODO: implement point class (sorry can't right now i'm busy)
        // (i need to stop talking to myself on public internet space)
        int[] s = getPosition(selection);
        int[] d = getPosition(destination);
        if (!inBounds(s)) { sendMessage("error: selector index ("+selection+") is out of bounds!"); return; }
        if (!inBounds(d)) { sendMessage("error: destination index ("+destination+") is out of bounds!"); return; }

        if (s==d) {
            sendMessage("you cannot move a unit to their current location! (at least not yet, there isn't any turns)");
            return;
        }
        for (int i=1; i<4; i++) if (args[i].length()!=2) {
            sendMessage("incorrect format! please try again.");
            log("incorret moveUnit format: "+selection+" "+args[2]+" "+destination);
            return;
        }

        Hero unit = positions[s[0]][s[1]];
        if (unit==null) {
            sendMessage("did not find a unit at your provided indices.");
            log("could not find player's unit at "+selection);
            return;
        }
        if (unit.getMoveType().getRange()<distanceTraveled(s, d)) {
            sendMessage("that unit cannot move that far! try again.");
            log(e.getAuthor()+" attempted to move "+
                    unit+" ("+unit.getMoveType().getRange()+" mov) "+distanceTraveled(s, d)+" spaces.");
            return;
        }
        Hero target = positions[d[0]][d[1]];
        if (target!=null) {
            sendMessage("cannot place a unit on top of another! please try again.");
            log(e.getAuthor()+" attempted to place "+unit+" on top of "+target+".");
            return;
        }

        positions[d[0]][d[1]] = positions[s[0]][s[1]];
        positions[s[0]][s[1]] = null;

        sendMessage(unit+" moved from "+selection+" to "+destination+".\n"+
                printMap());
        log(e.getAuthor()+" moved "+unit+" from "+selection+" to "+destination+".");
    }



    private String printMap() {
        //stupid non-uniform spacing (these chars be pretty thicc tho)
        /*
        String chars1 = "┏┳┓";
        String chars2 = "┣━╋━┫";
        String chars3 = "┗━┻━┛";
        String chars4 = "┣┃┫";

        String top = "┏━┳━┳━┳━┳━┳━┓";
        String body = "┣━╋━╋━╋━╋━╋━┫";
        String bottom = "┗━┻━┻━┻━┻━┻━┛";
        */
        String top = "+ - + - + - + - + - + - +";
        String body = top;
        String bottom = top;


        StringBuilder map = new StringBuilder("```\n");

        map.append(top).append('\n');
        for (int i=0; i<MAP_HEIGHT; i++) {
            map.append("| ");
            for (int j=0; j<MAP_WIDTH; j++) {
                if (positions[i][j]!=null) map.append('u');
                else map.append(board[i][j]);
                map.append(" | ");
            }
            map.append('\n').append((i==7)?bottom:body).append('\n');
        }
        map.append("```");

        return map.toString();
    }
    private int[] getPosition(String coordinates) {
        int[] x = { coordinates.charAt(0)-'a', coordinates.charAt(1)-'1' };
        return x;
    }
    private boolean inBounds(int[] pos) {
        for (int x:pos) if (x<0) return false;
        if (pos[0]>=MAP_HEIGHT) return false;
        if (pos[1]>=MAP_WIDTH) return false;

        return true;
    }
    private int distanceTraveled(int[] s, int[] d) {
        return (Math.abs(s[0]-d[0])+Math.abs(s[1]-d[1]));
    }



    @Override
    public void onCommand() {
        //wtf am i doing
    }

    @Override
    public boolean isCommand() {
        if (!players.contains(e.getAuthor())) return false;
        switch(args[0].toLowerCase()) {
            case "addunit":
                if (args.length>=4)
                    addUnit();
                break;
            case "unit":
                if (args.length==4)
                    moveUnit();
                break;
        }
        return false;
    }

    @Override
    protected Message sendMessage(String message) {
        return channel.sendMessage(message).complete();
    }
}
