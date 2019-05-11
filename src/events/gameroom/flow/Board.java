package events.gameroom.flow;

import events.commands.Command;
import utilities.feh.heroes.character.Hero;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Board {
    //visually, this board is upside down
    private static final Color[][] DOTS = {
            {null, null, null, null, null, null, null},
            {Color.BLUE, Color.YELLOW, null, null, null, Color.CYAN, Color.GREEN},
            {Color.ORANGE, Color.BLUE, null, null, null, null, Color.RED},
            {null, null, Color.YELLOW, null, null, null, null},
            {null, null, null, null, null, Color.CYAN, null},
            {null, null, null, Color.ORANGE, null, Color.GREEN, null},
            {Color.RED, null, null, null, null, null, null},
    };

    private Color[][] game;
    private ArrayList<Color> lines = new ArrayList<>();



    public Board() {
        game = DOTS;
    }



    public boolean drawLine(Point p, char[] dx) throws NullPointerException, IndexOutOfBoundsException {
        Color start = DOTS[p.x][p.y];

        Point path = (Point) p.clone();
        ArrayList<Point> groundCover = new ArrayList<>();
        if (start==null) throw new NullPointerException();
        int i;
        for (i=0; i<dx.length; i++) {
            switch (dx[i]) {
                case 'u':
                case 'y':
                    path.y+= 1;
                    break;
                case 'd':
                case 'h':
                    path.y-= 1;
                    break;
                case 'l':
                case 'g':
                    path.x-= 1;
                    break;
                case 'r':
                case 'j':
                    path.x+= 1;
                    break;
            }
            if (game[path.x][path.y]!=null||groundCover.contains(path)||!game[path.x][path.y].equals(start))
                throw new IndexOutOfBoundsException();
            groundCover.add((Point)path.clone());
        }

        if (!DOTS[path.x][path.y].equals(start)) return false;
        for (Point x:groundCover) game[x.x][x.y] = start;
        return true;
    }

    private static final int SCALE = 64;

    public File printBoard() {
        BufferedImage mapState =
                new BufferedImage(
                        SCALE*game[0].length,
                        SCALE*game.length,
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mapState.createGraphics();
        graphics.setBackground(Color.black);

        for (int i=0; i<game.length; i++) {
            for (int j=0; j<game[i].length; j++) {
                if (game[i][j]!=null) {
                    graphics.draw(new Rectangle(SCALE*j,SCALE*i,SCALE,SCALE));
                }
            }
        }

        /*
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


        */

        graphics.dispose();
        File image = new File("./src/events/gameroom/mapTest/gameMap.png");
        try {
            ImageIO.write(mapState, "png", image);
        } catch (IOException g) { System.out.println("guess we have a blank map"); }
        return image;
    }

    public static void main(String[] args) {

    }
}
