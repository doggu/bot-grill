package events.gameroom.flow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private HashMap<Character,Color> COLOR_CODES = getColors();
    private HashMap<Character,Color> getColors() {
        HashMap<Character,Color> colors = new HashMap<>();
        colors.put('a',Color.RED);
        colors.put('b',Color.GREEN);
        colors.put('c',Color.BLUE);
        colors.put('d',Color.YELLOW);
        colors.put('e',Color.ORANGE);
        colors.put('f',Color.CYAN);
        colors.put('g',Color.PINK);
        colors.put('h',new Color(172, 0, 0));
        colors.put('i',new Color(109, 0, 133));
        return colors;
    }



    private static final Character[][] DOTS = {
            {'n','n','n','n','n','i','c','e','n','n',},
            {'n','n','n','n','n','n','n','n','n','n',},
            {'n','f','n','n','n','n','n','f','n','e',},
            {'n','n','h','c','n','n','n','n','n','n',},
            {'b','n','n','n','n','n','n','n','g','n',},
            {'n','d','n','n','n','n','n','n','n','n',},
            {'n','n','n','b','a','n','n','n','g','n',},
            {'n','n','n','n','n','n','n','h','n','n',},
            {'n','d','n','n','i','n','n','n','n','a',},
            {'n','n','n','n','n','n','n','n','n','n',},
    };

    private Color[][] game;
    private ArrayList<Color> colors;
    private ArrayList<ArrayList<Point>> lines = new ArrayList<>();



    Board() {
        game = new Color[DOTS.length][DOTS[0].length];
        for (int i=0; i<DOTS.length; i++)
            for (int j=0; j<DOTS[i].length; j++)
                game[i][j] = COLOR_CODES.get(DOTS[i][j]);
        colors = new ArrayList<>();
        for (Color[] dots : game)
            for (Color dot : dots)
                if (dot != null)
                    if (!colors.contains(dot)) colors.add(dot);
    }






    boolean drawLine(Point p, char[] dx) throws NullPointerException, IndexOutOfBoundsException {
        Color start = game[p.x][p.y];

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

        if (!game[path.x][path.y].equals(start)) return false;
        lines.add(groundCover);
        return true;
    }

    void undo() throws IndexOutOfBoundsException {
        if (lines.size()==0) throw new IndexOutOfBoundsException();
        lines.remove(lines.size()-1);
    }

    void clear() throws NullPointerException {
        if (lines.size()==0) throw new NullPointerException();
        else
            lines = new ArrayList<>();
    }

    boolean completed() {
        return lines.size()==colors.size();
    }



    Point getDimensions() {
        return new Point(game.length, game[0].length);
        //gee i wonder if i got the dimension order properly huOooooDe doOO
    }



    //must be a number divisible by 32 and 3 (96 is the minimum)
    private static final int SCALE = 96;

    File printBoard() {
        BufferedImage mapState =
                new BufferedImage(
                        SCALE*(game[0].length+1),
                        SCALE*(game.length+1),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mapState.createGraphics();
        graphics.setBackground(Color.black);


        //draw map background
        //grid
        graphics.setColor(Color.GRAY);
        for (int i = 0; i < DOTS[0].length+1; i++)
            graphics.fillRect(SCALE*(1+i)-SCALE/32,0,SCALE/16, SCALE*(DOTS.length));
        for (int i = 0; i < DOTS.length+1; i++)
            graphics.fillRect(SCALE,i*SCALE-SCALE/32,SCALE*(DOTS[0].length), SCALE/16);
        //notation
        graphics.setColor(Color.WHITE);
        String name = "Courier New";
        int style = Font.PLAIN;
        int fontSize = 96;
        Font font = new Font(name, style, fontSize);
        FontRenderContext fontRenderContext = new FontRenderContext(null, true, false);
        for (int i = 0; i < DOTS[0].length; i++)
            new TextLayout(String.valueOf(DOTS.length-i),font, fontRenderContext)
                    .draw(graphics,SCALE/6,SCALE*(1+i)-SCALE/6);
            //graphics.drawString(String.valueOf((DOTS.length-i)),0,SCALE*i);
        for (int i = 0; i < DOTS.length; i++)
            new TextLayout(String.valueOf((char)('A'+i)),font, fontRenderContext)
                    .draw(graphics,SCALE*7/6+SCALE*i,SCALE*(DOTS[0].length+1)-SCALE/6);
            //graphics.drawString(String.valueOf((char)('A'+i)),SCALE+SCALE*i,SCALE*DOTS[0].length);

        //draw dots
        for (int i=0; i<DOTS.length; i++) {
            for (int j=0; j<DOTS[i].length; j++) {
                if (game[i][j]!=null) {
                    graphics.setColor(game[i][j]);
                    graphics.fillOval(SCALE+SCALE*j+SCALE/6,SCALE*i+SCALE/6,SCALE*2/3,SCALE*2/3);
                }
            }
        }

        //draw paths
        for (ArrayList<Point> line:lines) {
            Point dot = line.get(0);
            graphics.setColor(game[dot.x][dot.y]);
            for (int i=0; i<line.size()-1; i++) {
                Point start = line.get(i);
                Point end = line.get(i+1);
                if (end.x<start.x||end.y<start.y) {
                    Point temp = start;
                    start = end;
                    end = temp;
                }
                graphics.fillRoundRect(SCALE+SCALE*start.y+SCALE/3,SCALE*start.x+SCALE/3,
                        SCALE*(end.y-start.y)+SCALE/3,SCALE*(end.x-start.x)+SCALE/3,
                        SCALE/3,SCALE/3);
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
