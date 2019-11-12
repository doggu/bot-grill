package events.gameroom.uno;

import java.util.ArrayList;
import java.util.Arrays;

//the deck from which cards are dealt to uno players
//could definitely support further abstraction of card class in the future
public class Deck extends ArrayList<Card> {
    private static final ArrayList<Card> NEW_DECK;
    static {
        char[] colors = { 'r', 'y', 'g', 'b' };
        Card[] deck = new Card[108];
        for (int i=0; i<4; i++) {
            for (int j=0; j<2; j++) {
                int k;
                for (k=j; k<13; k++) {
                    deck[(i+1)*(j+1)*k] = new Card(colors[i], k);
                }
                deck[(i+1)*(j+1)*k] = new Card('n', k);
            }
        }

        NEW_DECK = new ArrayList<>(Arrays.asList(deck));
    }

    Deck() {
        super(NEW_DECK);
        shuffle();
    }

    private void swap(int i, int j) {
        Card temp = super.get(i);
        super.set(i, super.get(j));
        super.set(j, temp);
    }
    private void shuffle() {
        for (int i=0; i<super.size(); i++)
            swap(i, (int)(Math.random()*super.size()));
    }
}

class Card {
    private char color;
    private final int number;



    Card(char color, int number) {
        this.color = color;
        this.number = number;
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