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
    private static final ArrayList<BufferedImage> heroFaces = getFaces();
    private static ArrayList<BufferedImage> getFaces() {
        File folder = new File("./libs/heroes");
        ArrayList<BufferedImage> heroFaces = getImagesFromFolder(folder);
        return heroFaces;
    }
    //TODO: the function below can probably be lambda'd or something
    private static ArrayList<BufferedImage> getImagesFromFolder(File folder) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        File[] fileNames = folder.listFiles();
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
        return args[0].equalsIgnoreCase("Draw");
    }

    public void onCommand() {
        //test for drawing maps
        int width = WIDTH*SCALE;
        int height = HEIGHT*SCALE;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();



        ArrayList<Integer> previousX = new ArrayList<>();
        ArrayList<Integer> previousY = new ArrayList<>();
        for (int i=0; i<10; i++) {
            //x will always be intiialized in while loop, but y may not
            int x, y = ((int)(Math.random()*HEIGHT))*SCALE;
            while (previousX.contains(x = ((int)(Math.random()*WIDTH))*SCALE) &&
                    previousY.contains(y = ((int)(Math.random()*HEIGHT))*SCALE))
                /*restart*/;
            previousX.add(x);
            previousY.add(y);

            //TODO: make this less stupid when i'm less stupid
            BufferedImage face = heroFaces.get((int)(Math.random()*heroFaces.size()));
            double scale = 64.0/face.getHeight();
            g2d.drawImage(
                    face,
                    //there must be a better way to do this (or not)
                    new AffineTransformOp(
                            new AffineTransform(scale,0,0, scale,0 ,0),
                            AffineTransformOp.TYPE_BICUBIC),
                    x, y);
        }

        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        // Save as PNG
        File file = new File("myimage.png");
        try {
            ImageIO.write(bufferedImage, "png", file);
            e.getChannel().sendFile(file).complete();
        } catch (IOException g) {
            System.out.println("houston we got a problem");
        }
    }
}
