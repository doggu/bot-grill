package utilities;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;

public class WebCache extends File {
    private static final String DIRECTORY = "./webCache";

    private final String website;



    public WebCache(String website, String subdir) {
        super(generateDirectory(website, subdir));

        this.website = website;

        if (!exists()) {
            if (!update()) {
                throw new Error("unable to generate file contents!");
            }
        }
    }

    /**
     * Updates the contents of a file based on the internet.
     *
     * @return true if update completed successfully, false if otherwise.
     */
    public boolean update() {
        if (!delete()) {
            if (!exists()) {
                if (!getParentFile().mkdirs()) //path already exists????? please????
                try {
                    if (!createNewFile()) throw new Error("createNewFile returned false");
                } catch (IOException ioe) {
                    throw new Error("IOException while creating new file");
                }
            } else
                throw new Error("unable to delete file " + website);
        }

        BufferedReader data = readWebsite(website);

        if (data==null) throw new Error("nothing to read from "+website+"!");

        FileWriter writer;

        try {
            writer = new FileWriter(this);
        } catch (IOException ioe) {
            throw new Error("IOException while creating FileWriter");
        }

        try {
            String line;
            while ((line = data.readLine()) != null) {
                writer.write(line+'\n');
            }
            data.close();
        } catch (IOException ioe) {
            throw new Error("IOException while reading website data");
        }

        return true;
    }



    /**
     * Constructs the full directory of a file based on local constants and a website URL.
     *
     * @param website full URL of the website.
     * @param subdir a provided subdirectory, which may be null, in which the website directory will be contained.
     *
     * @return the fully generated directory.
     */
    private static String generateDirectory(String website, String subdir) {
        return DIRECTORY + //if forward slashes are missing
                (subdir!=null?(subdir.charAt(0)!='/'?'/':"")+subdir:"") +
                '/' + website.replaceAll("[/.:]", "_") +
                ".txt";
    }

    private static BufferedReader readWebsite(String website) {
        System.out.print("reading "+website+"... ");
        long start = System.nanoTime();

        // courtesy of Stas Yak at:
        // https://stackoverflow.com/questions/238547/how-do-you-programmatically-download-a-webpage-in-java
        URL url;
        InputStream is;
        BufferedReader br;

        try {
            url = new URL(website);
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        System.out.println("done (" +
                new BigDecimal((System.nanoTime()-start)/1000000000.0).round(new MathContext(3)) +
                " s)!");
        return br;
    }
}
