package events.gameroom.flow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Board {
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
    private ArrayList<Color> colors;
    private ArrayList<ArrayList<Point>> lines = new ArrayList<>();



    Board() {
        game = DOTS.clone();
        colors = new ArrayList<>();
        for (Color[] dots : game)
            for (Color dot : dots)
                if (dot != null)
                    if (!colors.contains(dot)) colors.add(dot);
    }






    boolean drawLine(Point p, char[] dx) throws NullPointerException, IndexOutOfBoundsException {
        Color start = DOTS[p.x][p.y];

        Point path = (Point) p.clone();
        ArrayList<Point> groundCover = new ArrayList<>();
        groundCover.add(p);
        if (start==null) throw new NullPointerException();
        int i;
        for (i=0; i<dx.length; i++) {
            switch (dx[i]) {
                case 'u':
                case 'y':
                    path.x-= 1;
                    break;
                case 'd':
                case 'h':
                    path.x+= 1;
                    break;
                case 'l':
                case 'g':
                    path.y-= 1;
                    break;
                case 'r':
                case 'j':
                    path.y+= 1;
                    break;
            }
            if (game[path.x][path.y]!=null)
                if (!game[path.x][path.y].equals(start))
                    throw new IndexOutOfBoundsException();
            groundCover.add((Point)path.clone());
        }

        if (!DOTS[path.x][path.y].equals(start)) return false;
        lines.add(groundCover);
        return true;
    }

    void undo() throws IndexOutOfBoundsException {
        if (lines.size()==0) throw new IndexOutOfBoundsException();
        lines.remove(lines.size()-1);
    }

    boolean completed() {
        return lines.size()==colors.size();
    }



    Point getDimensions() {
        return new Point(game.length, game[0].length);
        //gee i wonder if i got the dimension order properly huOooooDe doOO
    }

    private static final int SCALE = 128;

    File printBoard() {
        BufferedImage mapState =
                new BufferedImage(
                        SCALE*game[0].length,
                        SCALE*game.length,
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mapState.createGraphics();
        graphics.setBackground(Color.black);



        //draw map grid
        graphics.setColor(Color.GRAY);
        for (int i = 0; i < DOTS[0].length+1; i++)
            graphics.fillRect(i*SCALE-SCALE/32,0,SCALE/16, SCALE*(DOTS.length));
        for (int i = 0; i < DOTS.length+1; i++)
            graphics.fillRect(0,i*SCALE-SCALE/32,SCALE*(DOTS[0].length), SCALE/16);


        //draw dots
        for (int i=0; i<DOTS.length; i++) {
            for (int j=0; j<DOTS[i].length; j++) {
                if (DOTS[i][j]!=null) {
                    graphics.setColor(game[i][j]);
                    graphics.fillOval(SCALE*j+SCALE/8,SCALE*i+SCALE/8,SCALE*3/4,SCALE*3/4);
                }
            }
        }

        //draw paths
        for (ArrayList<Point> line:lines) {
            Point dot = line.get(0);
            graphics.setColor(DOTS[dot.x][dot.y]);
            for (int i=0; i<line.size()-1; i++) {
                Point start = line.get(i);
                Point end = line.get(i+1);
                if (end.x<start.x||end.y<start.y) {
                    Point temp = start;
                    start = end;
                    end = temp;
                }
                graphics.fillRoundRect(SCALE*start.y+SCALE/4,SCALE*start.x+SCALE/4,
                        SCALE*(end.y-start.y)+SCALE/2,SCALE*(end.x-start.x)+SCALE/2,
                        SCALE/2,SCALE/2);
            }
        }

        graphics.dispose();
        File image = new File("./src/events/gameroom/flow/gameMap.png");
        try {
            ImageIO.write(mapState, "png", image);
        } catch (IOException g) { System.out.println("guess we have a blank map"); }
        return image;
    }

    public static void main(String[] args) {

    }
}
