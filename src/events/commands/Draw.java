package events.commands;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class Draw extends Command {
    @Override
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Draw");
    }

    public void onCommand() {
        /*
        if (args.length<5) return;



        for (int i=1; i<5; i++) {
            switch(args[i].toLowerCase()) {
                case "red":
                case "blue":
                case "green":
                case "black":
                case "white":
            }
        }
        */

        int width = 250;
        int height = 250;

        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
        // fill all the image with white
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        // create a circle with black
        g2d.setColor(Color.black);
        g2d.fillOval(0, 0, width, height);
        // create a string with yellow
        g2d.setColor(Color.yellow);
        if (args.length>1) {
            String message = args[1];
            if (args.length>2)
                for (int i=2; i<args.length; i++)
                    message+= " "+args[i];

            g2d.drawString(message, 50, 120);
        }

        BufferedImage heroFace;
        //BufferedImageOp = new BufferedImageOp();
        try {
            heroFace = ImageIO.read(new File("./libs/heroes/Selena.png"));
            g2d.drawImage(heroFace, null, 20, 20);

        } catch (IOException g) {
            System.out.println("uh oh");
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

        //e.getChannel().sendFile();
    }

    public static void main(String[] args) {
        //test for drawing maps
        int width = 384;
        int height = 512;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        BufferedImage heroFace;
        //BufferedImageOp = new BufferedImageOp();
        try {
            heroFace = ImageIO.read(new File("./libs/heroes/Selena.png"));
            g2d.drawImage(heroFace, null, 20, 20);

        } catch (IOException g) {
            System.out.println("uh oh");
        }

        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        // Save as PNG
        File file = new File("myimage.png");
    }
}
