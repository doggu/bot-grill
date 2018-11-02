package utilities.fehUnits.summoning;

import utilities.fehUnits.heroes.Character;
import utilities.fehUnits.heroes.UnitDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Banner {
    private final String name;
    private final List<Character> characters = UnitDatabase.characters;
    private final List<Character> rarityFPool;
    private final List<Character> rarity5Pool;
    private final List<Character> rarity4Pool;
    private final List<Character> rarity3Pool;

    private double rarityFrate;
    private double rarity5rate;
    private double rarity4rate;
    private double rarity3rate;

    private final GregorianCalendar startDate, endDate;

    //TODO: generate banner type based on name or request info?
    public Banner(String name, List<Character> focusUnits, GregorianCalendar startDate, GregorianCalendar endDate) {
        this.name = name;
        this.rarityFPool = focusUnits;
        this.startDate = startDate;
        this.endDate = endDate;

        List<List<Character>> pools = generatePools();
        rarity5Pool = pools.get(0);
        rarity4Pool = pools.get(1);
        rarity3Pool = pools.get(2);
        generateRates();
    }

    /**
     * generates the summoning pools of a banner based on when the banner was released,
     * determining which units were part of the normal summoning pool at that time
     *
     * directly modifies rarity pool fields
     */
    private List<List<Character>> generatePools() {
        List<Character> rarity5Pool = new ArrayList<>();
        List<Character> rarity4Pool = new ArrayList<>();
        List<Character> rarity3Pool = new ArrayList<>();
        List<List<Character>> pools = new ArrayList<>();
        pools.add(rarity5Pool);
        pools.add(rarity4Pool);
        pools.add(rarity3Pool);

        //must have this date to accurately represent summoning pools for historical banners
        GregorianCalendar poolCutoff = (GregorianCalendar) startDate.clone();

        //creates a list of characters that could be summoned in normal pools
        List<Character> summonableCharacters = new ArrayList<>();
        for (Character x:characters)
            if (x.isInNormalPool())
                summonableCharacters.add(x);

        for (Character x:summonableCharacters) {
            GregorianCalendar characterReleaseDate = x.getReleaseDate();

            //if (characterReleaseDate.compareTo(poolCutoff)>0) {
                switch (x.getRarity()) {
                    case 1:
                    case 2:
                    case 3:
                        rarity3Pool.add(x);
                        rarity4Pool.add(x);
                        break;
                    case 4:
                        rarity4Pool.add(x);
                        rarity5Pool.add(x);
                        break;
                    case 5:
                        rarity5Pool.add(x);
                        break;
                }
            //}
        }

        return pools;
    }

    private void generateRates() {
        //TODO: KEEP NOTE: pity rates are added based on the ratio between focus and normal 5* pool (always 0.50% total)
        //TODO: e.x. Hero Fest (the only real example here): 5%, 3% --->


    }

    public String getName() { return name; }
    public GregorianCalendar getStartDate() { return startDate; }
    public GregorianCalendar getEndDate() { return endDate; }

    public List<Character> getRarityFPool() { return new ArrayList<>(rarityFPool); }
    public List<Character> getRarity5Pool() { return new ArrayList<>(rarity5Pool); }
    public List<Character> getRarity4Pool() { return new ArrayList<>(rarity4Pool); }
    public List<Character> getRarity3Pool() { return new ArrayList<>(rarity3Pool); }



    public static void main(String[] args) {
        String name = "New Heroes: Farfetched Heroes";
        List<Character> focuses = new ArrayList<>();
        focuses.add(new Character("Mia: Lady of Blades"));
        focuses.add(new Character("Lute: Prodigy"));
        focuses.add(new Character("Dorcas: Serene Warrior"));
        /*
        GregorianCalendar start = new GregorianCalendar(2017,10,15),
                end = new GregorianCalendar(2017,11,4);
        */
        GregorianCalendar start = new GregorianCalendar(2018,8,14),
                end = new GregorianCalendar(2018,9,10);


        //TODO: NOTE: MONTH IS ZERO BASED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        Banner g = new Banner(name, focuses, start, end);

        int i;
        for (i=0; true; i++) {
            int value = end.compareTo(start);
            //System.out.println(end.compareTo(start));
            if (value<=0) break;
            start.add(Calendar.DAY_OF_MONTH, 1);
        }

        System.out.println(i);
    }
}






















