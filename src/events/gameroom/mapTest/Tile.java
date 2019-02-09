package events.gameroom.mapTest;

public enum Tile {
    PLAINS("Plains", false, true, true);



    private final String name;
    private final boolean
            wall,
            flyerPassable,
            cavalryPassable;



    Tile(String name, boolean wall, boolean flyerPassable, boolean cavalryPassable) {
        this.name = name;
        this.wall = wall;
        this.flyerPassable = flyerPassable;
        this.cavalryPassable = cavalryPassable;
    }
}
