package utilities;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JSoupTest {
    public static void main(String[] args) {
        Connection web = Jsoup.connect("https://feheroes.gamepedia.com/Hero_skills_table");
        Document f;
        try {
            f = web.get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        Elements rawTables = f.select("table");
        Elements tables = f.select("table");
        for (Element table:rawTables) {
            if (table.attributes().get("style")
                    .equals("display:inline-table;" +
                            "border:1px solid #a2a9b1;" +
                            "border-collapse:collapse;" +
                            "width:24em;" +
                            "margin:0.5em 0;" +
                            "background-color:#f8f9fa")) {
                tables.add(table);
                //System.out.println(table.attributes().get("style"));
            }
            else
                ;//System.out.println(table.attributes().get("style"));

        }



        for (Element table:tables) {
            Elements info = table.select("tr");

            if (info.size()!=6) {
                //new Error("unexpected table size found!").printStackTrace();
                try {
                    //System.out.println(info.get(0).text());
                } catch (IndexOutOfBoundsException ioobe) {
                    //it's fine
                }
                continue;
            }

            //0 is owner portrait(s)

            String name = info.get(1).text();
            String stats = info.get(2).text();
            String effect1 = info.get(3).text();
            String effect2 = info.get(4).text();
            //String cost = info.get(5).text();

            String[] modifiers = stats.split(" ");

            if (modifiers.length%2!=0) {
                System.out.println(name);
            }
        }



        System.out.println(tables);

        //for (Element item:items) System.out.println(item.text());

        /*
        for (Element x:items) {
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
        */
    }
}