package events.gameroom.mapTest;

public class Board {
    private final int width, height;
    private final Tile[][] map;



    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Tile[width][height];
    }




}
