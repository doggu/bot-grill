package utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class JSoupTest {
    public static void main(String[] args) {
        File input = new File("./webCache back/feh/herodata/https___feheroes_gamepedia_com_Level_1_stats_table.txt");
        Document f;
        try {
            f = Jsoup.parse(input, "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        Elements tables = f.select("table");
        Elements rows = tables.select("tr");
        Elements items = rows.select("td");

        for (Element x:items)
            if (x.hasText()) {
                System.out.println(x.text());
            } else {
                Element image = null;
                try {
                    image = x.select("img").get(0);
                } catch (IndexOutOfBoundsException ioobe) {
                    //nothin
                }
                if (image!=null) {
                    System.out.println("\t"+image.attributes().get("alt"));
                    System.out.println("\t"+image.attributes().get("src"));
                }
            }
    }
}