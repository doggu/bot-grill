package events.gameroom.battleship;

import net.dv8tion.jda.core.entities.User;

public class BoardNotReadyException extends Exception {
    public BoardNotReadyException(User culprit) {
        super(culprit.getName()+"\'s board is not ready for play!");
    }
}
