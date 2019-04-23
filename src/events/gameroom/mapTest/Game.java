package events.gameroom.mapTest;

import events.commands.Command;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import utilities.feh.heroes.UnitDatabase;
import utilities.feh.heroes.character.Hero;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Game extends Command {
    private final ArrayList<User> players;
    private final MessageChannel channel;

    private Board board;



    Game(ArrayList<User> players, MessageChannel channel) {
        this.players = players;
        this.channel = channel;
        this.board = new Board(6, 8);
        startGame();
    }



    private void startGame() {
        sendFile(board.drawBoard(),"generated a new map."); }

    private void addUnit() {
        String position = args[1];
        Point pos = getPosition(position);
        if (!inBounds(pos)) { sendMessage("error: position index ("+position+") is out of bounds!"); return; }
        String name = args[2];
        for (int i = 3; i<args.length; i++) name+= " "+args[i];
        if (board.getUnit(pos)!=null) {
            sendMessage("cannot place a unit on top of an already existing unit!");
            log("user attempted to place a unit on top of "+board.getUnit(pos));
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
            board.addUnit(pos, chosen);
            sendFile(
                    board.drawBoard(),
                    "placed "+chosen+" at "+position);
            log(e.getAuthor()+" placed "+chosen+" at "+position);
            return;
        }
    }

    private void moveUnit() {
        String selection = args[1];
        String destination = args[3];
        Point s = getPosition(selection);
        Point d = getPosition(destination);
        if (!inBounds(s)) { sendMessage("error: selector index ("+selection+") is out of bounds!"); return; }
        if (!inBounds(d)) { sendMessage("error: destination index ("+destination+") is out of bounds!"); return; }

        //TODO: .equals()?
        if (s==d) {
            sendMessage("you cannot move a unit to their current location! (at least not yet, there aren't any turns)");
            return;
        }
        for (int i=1; i<4; i++) if (args[i].length()!=2) {
            sendMessage("incorrect format! please try again.");
            log("incorrect moveUnit format: "+selection+" "+args[2]+" "+destination);
            return;
        }

        Hero unit = board.getUnit(s);
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
        Hero target = board.getUnit(d);
        if (target!=null) {
            sendMessage("cannot place a unit on top of another! please try again.");
            log(e.getAuthor()+" attempted to place "+unit+" on top of "+target+".");
            return;
        }

        board.moveUnit(unit, d);

        sendFile(board.drawBoard(),unit+" moved from "+selection+" to "+destination+".\n");
        log(e.getAuthor()+" moved "+unit+" from "+selection+" to "+destination+".");
    }



    /*
    private String printMap() {
        //stupid non-uniform spacing (these chars be pretty thicc tho)
        String chars1 = "┏┳┓";
        String chars2 = "┣━╋━┫";
        String chars3 = "┗━┻━┛";
        String chars4 = "┣┃┫";

        String top = "┏━┳━┳━┳━┳━┳━┓";
        String body = "┣━╋━╋━╋━╋━╋━┫";
        String bottom = "┗━┻━┻━┻━┻━┻━┛";



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
    */

    private Point getPosition(String coordinates) {
        Point pos = new Point(coordinates.charAt(0)-'a', coordinates.charAt(1)-'1');
        return pos;
    }
    private boolean inBounds(Point pos) {
        if (pos.getX()<0) return false;
        if (pos.getY()<0) return false;
        if (pos.getX()>=board.getWidth()) return false;
        if (pos.getY()>=board.getHeight()) return false;

        return true;
    }
    private int distanceTraveled(Point s, Point d) {
        return (int)(Math.abs(s.getX()-d.getX())+Math.abs(s.getY()-d.getY()));
    }



    @Override
    public void onCommand() {
        switch(args[0].toLowerCase()) {
            case "addunit":
                if (args.length>=4) {
                    addUnit();
                } else
                    sendMessage("incorrect format. please try again.");
                break;
            case "unit":
                if (args.length==4) {
                    moveUnit();
                } else
                    sendMessage("incorrect format. please try again.");
                break;
        }
    }

    @Override
    public boolean isCommand() {
        if (!players.contains(e.getAuthor())) return false;
        switch(args[0].toLowerCase()) {
            case "addunit":
            case "unit":
                return true;
        }
        return false;
    }

    @Override
    protected Message sendMessage(String message) {
        return channel.sendMessage(message).complete();
    }

    protected Message sendFile(File file) {
        return channel.sendFile(file).complete();
    }
    protected Message sendFile(File file, String message) {
        return channel.sendFile(file, new MessageBuilder(message).build()).complete();
    }
}
