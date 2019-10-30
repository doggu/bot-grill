package events.devTools;

import events.MessageListener;
import feh.characters.HeroDatabase;
import feh.characters.hero.Hero;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Draw extends MessageListener {
    private static final int WIDTH = 6, HEIGHT = 8, SCALE = 64;
    private static final String FACE_PATH = "./libs/heroes";



    @Override
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("DrawTest");
    }

    public void onCommand() {
        //test for drawing maps
        int width = WIDTH*SCALE;
        int height = HEIGHT*SCALE;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();



        ArrayList<Point> coords = new ArrayList<>();
        for (int i=0; i<10; i++) {
            //x will always be intiialized in while loop, but y may not
            //but it's fine because y does not have to be created until x is valid
            Point p;
            while (coords.contains(
                    p = new Point(((int)(Math.random()*WIDTH))*SCALE,((int)(Math.random()*HEIGHT))*SCALE)))
                /*do nothing*/;
            coords.add(p);

            //TODO: make this less stupid when i'm less stupid
            Hero r = HeroDatabase.HEROES.get((int) (Math.random()* HeroDatabase.HEROES.size()));
            BufferedImage face;
            try {
                face = ImageIO.read(r.getPortraitLink());
            } catch (IOException ioe) {
                new Error("could not read image: "+r.getPortraitLink()).printStackTrace();
                continue;
            }
            double scale = 64.0/face.getHeight();
            g2d.drawImage(
                    face,
                    //there must be a better way to do this (or not)
                    new AffineTransformOp(
                            new AffineTransform(scale,0,0, scale,0 ,0),
                            AffineTransformOp.TYPE_BICUBIC),
                    p.x, p.y);
        }

        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        // Save as PNG
        File file = new File("myimage.png");
        try {
            ImageIO.write(bufferedImage, "png", file);
            sendFile(file);
        } catch (IOException g) {
            System.out.println("houston we got a problem");
        }
    }



    public char getPrefix() { return '&'; }
}
