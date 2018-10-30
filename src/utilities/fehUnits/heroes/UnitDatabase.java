package utilities.fehUnits.heroes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class UnitDatabase {
    public static final ArrayList<Character> characters = getList();

    private static ArrayList<Character> processFiles(Scanner heroList, Scanner LV1Stats, Scanner unitGrowths) {
        ArrayList<Character> characters = new ArrayList<>();

        while (heroList.hasNextLine()&&LV1Stats.hasNextLine()&&unitGrowths.hasNextLine()) {
            Character x;
            String name, epithet, origin, color, weaponType, moveType;
            int rarity;
            boolean summonable, isInNormalPool;
            GregorianCalendar dateReleased;
            int[] stats, statGrowths;



            //heroList - name, epithet, origin, color, wep type, move type, rarity, obtain method, release date
            if (heroList.hasNextLine()) {
                Scanner line = new Scanner(heroList.nextLine());
                line.useDelimiter("\t");
                line.next(); //remove image data

                name = line.next();
                //if (name.split(" ").length>1) System.out.println(name);
                epithet = line.next();
                origin = line.next();

                Scanner weaponTypeData = new Scanner(line.next());
                weaponTypeData.next();
                weaponTypeData.next(); //get rid of "Icon Class"

                color = weaponTypeData.next();
                weaponType = weaponTypeData.next();
                weaponType = weaponType.substring(0, weaponType.indexOf('.'));

                moveType = line.next();
                moveType = moveType.substring(moveType.lastIndexOf(' ')+1, moveType.indexOf('.'));

                String rarityDataStr = line.next();
                if (!line.hasNext()) {
                    line = new Scanner(heroList.nextLine());
                    line.useDelimiter("\t");
                    rarityDataStr+= "\t"+line.next();
                }
                Scanner rarityData = new Scanner(rarityDataStr);
                rarityData.useDelimiter("\t");
                rarity = Integer.parseInt(rarityData.next().charAt(0)+"");

                //if unit has special title (Tempest Trials, Grand Hero Battle, Legendary, etc.
                if (rarityData.hasNext()) {
                    String obtainType = rarityData.next();
                    switch (obtainType) {
                        case "*":
                            summonable = true;
                            isInNormalPool = true;
                            break;
                        case "Story":
                        case "Grand Hero Battle":
                        case "Tempest Trials":
                            summonable = false;
                            isInNormalPool = false;
                            break;
                        //separate because they could be part of a summoning focus
                        case "Special":
                        case "Legendary":
                            summonable = true;
                            isInNormalPool = false;
                            break;
                        default:
                            System.out.println("obtaining method wasn't accounted for: "+obtainType);
                            throw new Error();
                    }
                } else {
                    summonable = true;
                    isInNormalPool = true;
                }

                Scanner dateReleaseData = new Scanner(line.next());
                dateReleaseData.useDelimiter("-");
                int year = Integer.parseInt(dateReleaseData.next());
                int month = Integer.parseInt(dateReleaseData.next());
                int day = Integer.parseInt(dateReleaseData.next());

                dateReleased = new GregorianCalendar(year, month, day);
            } else {
                System.out.println("something is WAY wrong here");
                throw new Error();
            }



            //lv1 stats - lv1 stats (also contains: name, epithet, color, wep type, move type, lv1 BST)
            if (LV1Stats.hasNextLine()) {
                Scanner line = new Scanner(LV1Stats.nextLine());
                line.useDelimiter("\t");
                //TODO: maybe later use this for error checking?
                line.next(); //remove image data
                line.next(); //remove name+epithet
                line.next(); //remove color/weapon type
                line.next(); //remove move type

                stats = new int[5];
                for (int i=0; i<5; i++) stats[i] = Integer.parseInt(line.next());

                //class can calculate this value itself
                int lv1BST = Integer.parseInt(line.next());
            } else {
                System.out.println("something is WAY wrong here");
                throw new Error();
            }



            //unitGrowths - growth rates (also contains: "[name]: [epithet]", color, wep type, move type, release date
            if (unitGrowths.hasNextLine()) {
                unitGrowths.nextLine(); //remove unit data
                unitGrowths.nextLine(); //remove more unit data
                Scanner line = new Scanner(unitGrowths.nextLine());
                line.useDelimiter("\t");
                line.next(); //remove move type
                line.next(); //remove lv1 bst(?)
                line.next(); //remove total growths
                line.next(); //remove lv1 bst AND total growths

                String[] growthStrArr = {
                        line.next(),
                        line.next(),
                        line.next(),
                        line.next(),
                        line.next()
                };

                statGrowths = new int[5];

                for (int i=0; i<5; i++)
                    statGrowths[i] = Integer.parseInt(growthStrArr[i].substring(0, growthStrArr[i].indexOf("%")));
            } else {
                System.out.println("something is WAY wrong here");
                throw new Error();
            }

            x = new Character(name, epithet, origin, color, weaponType, moveType,
                    rarity, summonable, isInNormalPool, dateReleased, stats, statGrowths);

            characters.add(x);
        }



        return characters;
    }

	/* General Format, Growths (two characters):

	Abel The Panther Face FC.png	Abel: The Panther	Icon Class Blue Lance.png
	Blue Lance	Icon Move Cavalry.png
	Cavalry	46	250%	46, 250%	50%	60%	55%	40%	45%	2017-02-02
	Alfonse Prince of Askr Face FC.png	Alfonse: Prince of Askr	Icon Class Red Sword.png
	Red Sword	Icon Move Infantry.png
	Infantry	47	255%	47, 255%	55%	60%	45%	55%	40%	2017-02-02
	...

	 */

	/* General Format, LV1 Stats (two characters):

	Abel The Panther Face FC.png	Abel: The Panther	Icon Class Blue Lance.png	Icon Move Cavalry.png	17	7	8	8	6	46
	Alfonse Prince of Askr Face FC.png	Alfonse: Prince of Askr	Icon Class Red Sword.png	Icon Move Infantry.png	19	9	6	8	5	47
	...

	 */

    public static void main(String[] args) {
        ArrayList<Character> characters = getList();

        Scanner console = new Scanner(System.in);
        String character = console.nextLine().toLowerCase();
        while(!character.equals("quit")) {
            for (Character x:characters)
                if (x.getName().toLowerCase().equals(character))
                    System.out.println(x.toString());

            character = console.nextLine();
        }
    }

    private static ArrayList<Character> getList() {
        File growthsFile = new File(".\\src\\utilities\\fehUnits\\heroes\\sources\\UnitGrowths.txt");
        File lv1StatsFile = new File(".\\src\\utilities\\fehUnits\\heroes\\sources\\LV1Stats.txt");
        File heroListFile = new File(".\\src\\utilities\\fehUnits\\heroes\\sources\\UnitTyping.txt");

        Scanner growths;
        try {
            growths = new Scanner(growthsFile);
        } catch (FileNotFoundException e) {
            System.out.println("growths brok");
            e.printStackTrace();
            throw new Error();
        }
        Scanner lv1Stats;
        try {
            lv1Stats = new Scanner(lv1StatsFile);
        } catch (FileNotFoundException e) {
            System.out.println("stats brok");
            e.printStackTrace();
            throw new Error();
        }
        Scanner heroList;
        try {
            heroList = new Scanner(heroListFile);
        } catch (FileNotFoundException e) {
            System.out.println("hurro list brok");
            e.printStackTrace();
            throw new Error();
        }

        return processFiles(heroList, lv1Stats, growths);
    }
}
