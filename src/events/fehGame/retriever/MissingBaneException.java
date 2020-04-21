package events.fehGame.retriever;

public class MissingBaneException extends Exception {
    MissingBaneException() {
        super("you must include a bane or add merges to create a valid hero.");
    }
}
