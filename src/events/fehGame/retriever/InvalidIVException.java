package events.fehGame.retriever;

public class InvalidIVException extends Exception {
    InvalidIVException() {
        super("IVs cannot be applied to GHB/TT units.");
    }
}
