package events.gameroom.mapTest;

import feh.heroes.character.MovementClass;
import feh.heroes.unit.Unit;
import feh.heroes.character.Hero;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private final BufferedImage mapImage;
    private final int width, height, scale;

    private final Tile[][] map = {
            {Tile.WALL,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS},
            {Tile.WALL,Tile.PLAINS_DEFENSIVE,Tile.PLAINS_DEFENSIVE,
                    Tile.POND,Tile.PLAINS_DEFENSIVE,Tile.PLAINS_DEFENSIVE},
            {Tile.WALL,Tile.PLAINS_DEFENSIVE,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS_DEFENSIVE},
            {Tile.WALL,Tile.PLAINS,Tile.TRENCHES,Tile.PLAINS,Tile.TRENCHES,Tile.PLAINS},
            {Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.TRENCHES,Tile.PLAINS,Tile.PLAINS},
            {Tile.PLAINS,Tile.PLAINS,Tile.FOREST,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS},
            {Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.FOREST,Tile.PLAINS},
            {Tile.FOREST,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS,Tile.PLAINS},
    }; //for map Z0028.jpg

    //TODO: should be Units eventually (but i'm not about to implement barracks just yet)
    private HashMap<Hero, Point> unitPositions = new HashMap<>();
    private HashMap<Point, Hero> positionUnits = new HashMap<>();




    Board(int width, int height) {
        try {
            this.mapImage = ImageIO.read(new File("./src/events/gameroom/mapTest/Z0028.png"));
        } catch (IOException notFoundOrSomething) {
            System.out.println("couldn't find an image that's literally right there");
            throw new Error();
        }
        this.width = width;
        this.height = height;
        int scale = mapImage.getHeight()/height;
        if (scale!=mapImage.getWidth()/width) {
            System.out.println("the scale is not perfect");
            scale = (int)((double)(scale+mapImage.getWidth()/width)/2);
        }
        this.scale = scale;
    }



    private int taxiCabDistance(Point a, Point b) {
        return ((int)(Math.abs(a.getX()-b.getX())+Math.abs(a.getX()-b.getY()))); }
    private ArrayList<ArrayList<Tile>> getAllPaths(ArrayList<Point> path, Point destination) {
        //todo: assumes range is less than or equal to 3 (ya never know with IS)
        int dX = (int) (path.get(path.size()-1).getX()-destination.getX()),
            dY = (int) (path.get(path.size()-1).getY()-destination.getY());

        ArrayList<ArrayList<Tile>> allPaths = new ArrayList<>();

        ArrayList<Point> pathX = new ArrayList<>(path);
        if (dX!=0) {
            Point newP = new Point(dX+(dX>0?-1:1), dY);

            pathX.add(newP);
            allPaths.addAll(getAllPaths(pathX, destination));
        }

        if (dY!=0) {
            Point newP = new Point(dX, dY+(dY>0?-1:1));

            pathX.add(newP);
            allPaths.addAll(getAllPaths(pathX, destination));
        }

        if (dX==0&&dY==0) {
            ArrayList<Tile> tilePath = new ArrayList<>();
            for (Point p:path) {
                tilePath.add(map[p.x][p.y]);
            }
            allPaths.add(tilePath);
        }

        return allPaths;
    }
    boolean canMove(Hero unit, Point destination) {
        Point originalPos = unitPositions.get(unit);
        MovementClass moveType = unit.getMoveType();

        int distance = taxiCabDistance(originalPos, destination);

        if (distance>moveType.getRange())
            return false;

        ArrayList<Point> start = new ArrayList<>();
        start.add(unitPositions.get(unit));
        ArrayList<ArrayList<Tile>> paths = getAllPaths(start, destination);

        for (ArrayList<Tile> path:paths) {
            path.remove(0); //don't count starting Tile
            int movesRemaining = moveType.getRange();
            for (Tile t:path) {
                switch (moveType) {
                    case INFANTRY:
                    case ARMORED:
                    case CAVALRY:
                    case FLYING:
                }
            }
        }



        return true; //todo: finish
    }

    void moveUnit(Hero unit, Point destination) {
        Point originalPos = unitPositions.get(unit);
        unitPositions.remove(unit);
        positionUnits.remove(originalPos);
        unitPositions.put(unit, destination);
        positionUnits.put(destination, unit);
    }

    void addUnit(Point pos, Hero unit) {
        positionUnits.put(pos, unit);
        unitPositions.put(unit, pos);
    }



    public Point getPos(Unit unit) { return unitPositions.get(unit); }
    Hero getUnit(Point pos) { return positionUnits.get(pos); }

    int getWidth() { return width; }
    int getHeight() { return height; }



    File drawBoard() {
        BufferedImage mapState =
                new BufferedImage(
                        mapImage.getWidth(),
                        mapImage.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mapState.createGraphics();
        graphics.drawImage(mapImage, null, 0, 0);

        for (Hero unit:positionUnits.values()) {
            String name = unit.getFullName().getName();
            BufferedImage face;
            try {
                face = ImageIO.read(new File("./libs/heroes/"+name+".png"));
            } catch (IOException faceNotFound) {
                //this currently works for those with only alts (Charlotte, Greil, etc.)
                // and those not in the library yet because mass duel simulator is a bit slow
                System.out.println("could not find "+unit.getFullName());
                try {
                    face = ImageIO.read(new File("./libs/heroes/nohero.png"));
                } catch (IOException noFaceNotFound) {
                    System.out.println("could not find the fuckin null placeholder");
                    throw new Error();
                }
            }
            double scale = this.scale/(double) face.getHeight();

            Point pos = unitPositions.get(unit);

            graphics.drawImage(
                    face,
                    new AffineTransformOp(
                            new AffineTransform(scale,0,0, scale,0 ,0),
                            AffineTransformOp.TYPE_BICUBIC),
                    (int)pos.getX()*this.scale, (int)pos.getY()*this.scale);
        }


        graphics.dispose();

        File image = new File("./src/events/gameroom/mapTest/gameMap.png");
        try {
            ImageIO.write(mapState, "png", image);
        } catch (IOException g) { System.out.println("guess we have a blank map"); }
        return image;
    }



    //yet another HashMap object identity test because i refuse to read books properly
    static void main(String[] args) {
        HashMap<Point, String> positions = new HashMap<>();
        positions.put(new Point(2,4), "i love hot girls");
        System.out.println(positions.get(new Point(2, 4)));
        HashMap<String, Point> positions2 = new HashMap<>();
        positions2.put("i love hot girls", new Point(2, 4));
        System.out.println(positions2.get("i love hot girls"));
    }
}
