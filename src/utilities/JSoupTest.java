package utilities;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JSoupTest {
    public static void main(String[] args) {
        Connection web = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_chemical_elements");
        Document f;
        try {
            f = web.get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        Element table = f.select("table").get(0);

        System.out.println(table);
    }
}