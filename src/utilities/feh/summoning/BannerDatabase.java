package utilities.feh.summoning;

import utilities.WebScalper;
import utilities.feh.heroes.character.Hero;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;



//RARITY CHANGE DATE: APRIL 10 2018
//TODO: this should be implemented sooner or later
//this should be in Banner tbh

public class BannerDatabase extends WebScalper {
    public static final List<Banner> BANNERS = getList();

    private static List<Banner> getList() {
        IntStream data = null;

        try {
            BufferedReader focusArchive = readWebsite("https://feheroes.gamepedia.com/Summoning_Focus_archive");
            String line;
            while ((line = focusArchive.readLine()) != null) {
                if (line.length() > 100000) { //reliable coding inc
                    data = line.chars();
                    break;
                }
            }
        } catch (IOException g) { System.out.println("banners had an issue"); throw new Error(); }

        if (data==null) throw new Error();



        Iterator<String> items = getItems(data).iterator();
        List<Banner> banners = new ArrayList<>();

        Banner x;
        while (items.hasNext()) {
            String n1 = items.next();
            String n2 = items.next();
            while (!n1.equals(n2)) {
                n1 = n2;
                n2 = items.next();
            }
            String bannerName = n1;

            //check alignment
            if (!items.next().equals("Featured Units")) {
                System.out.println(n1+"/"+n2+" ran into issue in featured units");
            }

            ArrayList<String> featuredUnitNames = new ArrayList<>();
            String name;
            //gather names until new data appears
            while (!(name = items.next()).equals("Start Date")) {
                featuredUnitNames.add(name);
            }
            ArrayList<Hero> featuredUnits = new ArrayList<>();
            for (String hero:featuredUnitNames) {
                try {
                    featuredUnits.add(new Hero(hero));
                } catch (Error g) {
                    System.out.println("BannerDatabase could not find character: "+hero);
                }
            }

            GregorianCalendar startDate;
            if (name.equals("Start Date")) {
                String[] startDateStr = items.next().split("-");
                int year = Integer.parseInt(startDateStr[0]);
                int month = Integer.parseInt(startDateStr[1]);
                int day = Integer.parseInt(startDateStr[2]);
                startDate = new GregorianCalendar(year, month, day,0,0,0);
            } else {
                System.out.println("start date did not align properly with "+n1+"/"+n2);
                throw new Error();
            }

            GregorianCalendar endDate;
            if (items.next().equals("End Date")) {
                String[] endDateStr = items.next().split("-");
                int year = Integer.parseInt(endDateStr[0]);
                int month = Integer.parseInt(endDateStr[1]);
                int day = Integer.parseInt(endDateStr[2]);
                endDate = new GregorianCalendar(year, month, day, 23, 59, 59);
            } else {
                System.out.println("end date did not align properly with "+n1+"/"+n2);
                throw new Error();
            }

            x = new Banner(bannerName, featuredUnits, startDate, endDate);
            banners.add(x);
        }



        System.out.println("finished processing banners.");
        return banners;
    }



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String line;
        while (!(line = input.nextLine()).equals("quit")) {
            for (Banner x:BANNERS) {
                if (x.getName().contains(line)) {
                    System.out.println(x.getName());
                    System.out.println("featuring:");
                    for (Hero y:x.getRarityFPool()) {
                        System.out.println("\t\t"+y.getFullName());
                    }
                }
            }
        }
    }
}
