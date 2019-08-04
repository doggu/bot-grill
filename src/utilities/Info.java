package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Info {
    public static void main(String[] args) {
        File src = new File("./src");

        ArrayList<File> code = new ArrayList<>(Arrays.asList(src.listFiles()));

        ArrayList<File> files = getFiles(code);

        Scanner counter;

        int lines = 0;
        int chars = 0;

        for (File x:files) {
            try {
                counter = new Scanner(x);
            } catch (FileNotFoundException fnfe) {
                //well fuck you then
                continue;
            }

            while (counter.hasNextLine()) {
                String line = counter.nextLine();
                if (!line.equals("")) {
                    lines++;
                    chars+= line.length();
                }
            }
        }

        System.out.println("lines of code (that arent blank): "+lines);
        System.out.println("total characters: "+chars);
    }

    private static ArrayList<File> getFiles(ArrayList<File> origin) {
        ArrayList<File> files = new ArrayList<>();

        for (File x:origin) {
            if (x.isDirectory()) {
                try {
                    files.addAll(getFiles(new ArrayList<>(Arrays.asList(x.listFiles()))));
                } catch (NullPointerException npe) {
                    //it's empty
                }
            } else {
                files.add(x);
            }
        }

        return new ArrayList<>(files);
    }
}
