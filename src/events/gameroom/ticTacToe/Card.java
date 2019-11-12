package events.gameroom.ticTacToe;

public class Card {
    private char color;
    private final int number;



    Card(char color, byte number) {
        this.color = color;
        this.number = number;
    }



    enum Special {
        PLUS_2, PLUS_4, COLOR_CHANGE,
    }



    boolean colorChooseable() {
        return color=='n';
    }
    //returns false if you're a bad programmer
    boolean setColor(char color) {
        if (color=='n') {
            this.color = color;
            return true;
        }

        return false;
    }

}
