package feh.summoning;

import com.google.gson.stream.JsonReader;
import feh.FEHeroesCache;
import feh.characters.hero.Hero;
import utilities.StringUtil;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class BannerDatabase extends Database<Banner> {
    public static BannerDatabase DATABASE;
    public static ArrayList<Banner> BANNERS;


    private static final String
            /*
            contains:
                gamepedia page name
                    can be parsed to form user-facing name
                unique identifier (clears up banner revival ambiguity)
                start time
                end time
                banner type (so i don't have to manually parse "Hero Fest")
                date precisions (still not confident on what these do)
             */
            BANNERS_INFO = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=SummoningFocuses" +
                    "&&fields=" +
                            "_pageName%3DPage%2C" +
                            "WikiName%3DWikiName%2C" +
                            "Name%3DName%2C" +
                            "StartTime%3DStartTime%2C" +
                            "EndTime%3DEndTime%2C" +
                            "BannerType%3DBannerType" +
                    "&&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60WikiName%60%2C" +
                            "%60Name%60%2C" +
                            "%60StartTime%60%2C" +
                            "%60EndTime%60" +
                    "&limit=4096" +
                    "&format=json",
            /*
            contains:
                unit and their presence in a banner (per entry)
                their rarity for the banner
            (still no % rates anywhere to be found on gamepedia afaik)
             */
            BANNER_HEROES = "https://feheroes.gamepedia.com/index.php" +
                    "?title=Special:CargoExport" +
                    "&tables=SummoningFocusUnits" +
                    "&&fields=" +
                            "_pageName%3DPage%2C" +
                            "WikiName%3DWikiName%2C" +
                            "Unit%3DUnit%2C" +
                            "Rarity%3DRarity" +
                    "&&order+by=" +
                            "%60_pageName%60%2C" +
                            "%60WikiName%60%2C" +
                            "%60Unit%60%2C" +
                            "%60Rarity%60" +
                    "&limit=4096" +
                    "&format=json";

    private static final FEHeroesCache //FOCUS_ARCHIVE_FILE,
    BANNERS_INFO_FILE, BANNER_HEROES_FILE;

    static {
//        FOCUS_ARCHIVE_FILE = new FEHeroesCache(FOCUS_ARCHIVE);
        BANNERS_INFO_FILE = new FEHeroesCache(BANNERS_INFO);
        BANNER_HEROES_FILE = new FEHeroesCache(BANNER_HEROES);


        DATABASE = new BannerDatabase();
        BANNERS = DATABASE.getList();
    }

    @Override
    protected WebCache[] getOnlineResources() {
        return new WebCache[] {
//                FOCUS_ARCHIVE_FILE,
                BANNERS_INFO_FILE,
                BANNER_HEROES_FILE
        };
    }


    @Override
    protected ArrayList<Banner> getList() {
        //note: if the type is "Free Summon," just skip the banner, it's a can
        //of worms that i should deal with when the rest of my feh framework
        //feels rigorous enough to handle the data

        try {
            return getBanners();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }


    private ArrayList<Banner> getBanners() throws IOException {
        final String BANNER_DATE_FORMAT = "YYYY-MM-DD HH:MM:SS";
        ArrayList<Banner> banners = new ArrayList<>();

        JsonReader
                infoReader = new JsonReader(new FileReader(BANNERS_INFO_FILE)),
                heroReader = new JsonReader(new FileReader(BANNER_HEROES_FILE));

        infoReader.beginArray();

        heroReader.beginArray();
        //create "buffers" for hero info to push into banner lists as needed
        String  heroBannerPage,
                heroBannerWikiname,
                heroName;
        int     heroRarity;

        while (infoReader.hasNext()) {
            /*
            {
                "Page": "Special Hero Summon (Year 2)",
                "WikiName": "Special Hero Summon Year 2 20190209",
                "Name": "Special Hero Summon (Year 2)",
                "StartTime": "2019-02-09 07:00:00",
                "EndTime": "2019-02-16 06:59:59",
                "BannerType": "Free Summon",
                "StartTime__precision": 0,
                "EndTime__precision": 0
            },
             */

            infoReader.beginObject();
            //page
            infoReader.skipValue();
            String page = infoReader.nextString();
            //wikiname
            infoReader.skipValue();
            String wikiname = infoReader.nextString();
            //name
            infoReader.skipValue();
            String name = infoReader.nextString();
            //start time
            infoReader.skipValue();
            String startTime = infoReader.nextString();
            //end time
            infoReader.skipValue();
            String endTime = infoReader.nextString();
            //banner type
            infoReader.skipValue();
            String bannerType = infoReader.nextString();
            //date precisions
            infoReader.skipValue();
            infoReader.skipValue();
            infoReader.skipValue();
            infoReader.skipValue();

            infoReader.endObject();


            ArrayList<Hero> focusUnits = new ArrayList<>();

            do {
                /*
                {
                    "Page": "3rd Anniversary Hero Fest, Part 1",
                    "WikiName": "3rd Anniversary Hero Fest Part 1 20200202",
                    "Unit": "Micaiah Queen of Dawn",
                    "Rarity": 5
                },
                 */
                heroReader.beginObject();
                //page
                heroReader.skipValue();
                heroBannerPage = heroReader.nextString();
                //wikiname ("[banner name] [(banner component)] [(date)]")
                heroReader.skipValue();
                heroBannerWikiname = heroReader.nextString();
                //unit (format is colonless)
                heroReader.skipValue();
                heroName = heroReader.nextString();
                //rarity
                heroReader.skipValue();
                heroRarity = heroReader.nextInt();

                heroReader.endObject();

                if (wikiname.equals(heroBannerWikiname)) {
                    focusUnits.add(new Hero(heroName));
                } else {
                    break; //gah
                }
            } while (heroReader.hasNext());
            GregorianCalendar startDate, endDate;
            startDate = StringUtil.getDate(startTime, BANNER_DATE_FORMAT);
            try {
                endDate = StringUtil.getDate(endTime, BANNER_DATE_FORMAT);
            } catch (IndexOutOfBoundsException ioobe) {
                switch(page.toLowerCase()) {
                    case "new heroes: arrival of the brave":
                    case "new heroes: brave echoes":
//                        endDate = null; //this would require a big refactoring
                                          //but, as usual, would be more correct
                        endDate = StringUtil.getDate(
                                "2038-01-19 03:14:07",
                                BANNER_DATE_FORMAT);
                        break;
                    default:
                        System.out.println(page);
                        throw new Error();
                }
            }
            Banner banner = new Banner(page, focusUnits, startDate, endDate);

            banners.add(banner);
        }

        return banners;
    }


    @Override
    public ArrayList<Banner> findAll(String input) {
        ArrayList<Banner> all = new ArrayList<>();
        for (Banner x:BANNERS) {
            if (x.getName().equals(input)) {
                all.add(x);
            }
        }

        return all;
    }

    @Override
    public Banner getRandom() {
        return BANNERS.get((int)(Math.random()*BANNERS.size()));
    }

    private static GregorianCalendar getDate(String date)
            throws IllegalArgumentException {
        String[] endDateStr = date.split("-");
        int year = Integer.parseInt(endDateStr[0]);
        int month = Integer.parseInt(endDateStr[1])-1;
        int day = Integer.parseInt(endDateStr[2]);
        int hour = 23, minute = 59, second = 59;
        //noinspection MagicConstant
        return new GregorianCalendar(year, month, day, hour, minute, second);
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
