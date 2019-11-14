package events.gameroom.uno;

import java.util.ArrayList;
import java.util.Arrays;

//the deck from which cards are dealt to uno players
//could definitely support further abstraction of card class in the future
public class Deck extends ArrayList<Card> {
    private static final ArrayList<Card> NEW_DECK;
    static {
        char[] colors = { 'r', 'y', 'g', 'b', 'n' };
        int[] shuffle = new int[108];
        for (int i=0; i<shuffle.length; i++) {
            int temp = shuffle[i];
            int r = (int) (Math.random()*shuffle.length);
            shuffle[i] = shuffle[r];
            shuffle[r] = temp;
        }
        Card[] deck = new Card[108];
        for (int i=0; i<4; i++) {
            for (int j=0; j<2; j++) {
                int k;
                for (k=j; k<14; k++) {
                    deck[shuffle[(i+1)*(j+1)*k]]
                            = new Card(colors[k==13?i:4], k);
                }
            }
        }

        NEW_DECK = new ArrayList<>(Arrays.asList(deck));
    }

    Deck() {
        super(NEW_DECK);
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
        return color=='n'; //'n' is directly related to card number being 13
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