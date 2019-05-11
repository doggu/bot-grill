package events.commands;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Draw extends Command {
    private static final int WIDTH = 6, HEIGHT = 8, SCALE = 64;
    private static final String FACE_PATH = "./libs/heroes";
    private static final ArrayList<BufferedImage> heroFaces = getImagesFromFolder(new File(FACE_PATH));

    //TODO: the function below can probably be lambda'd or something
    private static ArrayList<BufferedImage> getImagesFromFolder(File folder) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        File[] fileNames = folder.listFiles();
        if (fileNames==null) throw new NullPointerException();
        for (File x:fileNames) {
            try {
                images.add(ImageIO.read(x));
            } catch (IOException notAnImageIGuess) {
                System.out.println("ran into a file that isn't an image: "+x.getName());
                //TODO: make recursive? probably not
            }
        }

        return images;
    }



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
            BufferedImage face = heroFaces.get((int)(Math.random()*heroFaces.size()));
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

    public String getName() { return "Draw"; }
    public String getDescription() { return "a test for still 2D graphics in Discord!"; }
    public String getFullDescription() {
        return "A small demo which allows me to draw you a picture with your chosen text.\n" +
                "\tSyntax: \"?draw [text]\n" +
                "any words after the initial argument (draw) will be drawn into the image!";
    }
}
