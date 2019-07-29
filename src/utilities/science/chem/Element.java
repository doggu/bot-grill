package utilities.science.chem;

public class Element {
    private final String symbol, name;
    private final int number;

    Element(String symbol, String name, int number) {
        this.symbol = symbol;
        this.name = name;
        this.number = number;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public int getNumber() { return number; }
}
