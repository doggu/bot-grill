package utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JSoupTest {
    public static void main(String[] args) {
        Document f;
        try {
            f = Jsoup.connect("https://feheroes.gamepedia.com/Level_1_stats_table").get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        System.out.println(f.body().after("<table class=\"sortable wikitable").before("</thead>"));
    }
}
