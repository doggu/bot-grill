package events.commands;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Draw extends Command {
    @Override
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Draw");
    }

    public void onCommand() {
        //test for drawing maps
        int WIDTH = 6, HEIGHT = 8, SCALE = 113;
        int width = WIDTH*SCALE;
        int height = HEIGHT*SCALE;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        ArrayList<BufferedImage> heroFace = new ArrayList<>();
        File folder = new File("./libs/heroes");
        File[] fileNames = folder.listFiles();
        for (File x:fileNames) {
            try {
                heroFace.add(ImageIO.read(x));
            } catch (IOException notAnImageIGuess) {
                System.out.println("ran into a file that isn't an image: "+x.getName());
            }
        }


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

            g2d.drawImage(heroFace.get((int)(Math.random()*heroFace.size())), null, x, y);
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
