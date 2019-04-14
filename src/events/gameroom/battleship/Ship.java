package events.gameroom.battleship;

public class Ship {
    private final String name;
    private final ShipSpace[] spaces;
    private boolean placed;



    public Ship(String name, int length) {
        this.name = name;
        spaces = new ShipSpace[length];
    }



    public String getName() { return name; }
    public int getLength() { return spaces.length; }



    public void place() { placed = true; }
    public boolean isPlaced() { return placed; }
}
