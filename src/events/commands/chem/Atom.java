package events.commands.chem;

public class Atom {
    private final String symbol, name;
    private final int number;

    Atom(String symbol, String name, int number) {
        this.symbol = symbol;
        this.name = name;
        this.number = number;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public int getNumber() { return number; }
}
