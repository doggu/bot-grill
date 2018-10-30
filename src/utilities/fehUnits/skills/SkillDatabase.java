package utilities.fehUnits.skills;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SkillDatabase {
    //currently only for basic info, such as SP cost and description
    private static List<Skill> processFiles(
                Scanner swords, Scanner lances, Scanner axes,
                Scanner redTomes, Scanner blueTomes, Scanner greenTomes,
                Scanner bows, Scanner breaths, Scanner daggers, Scanner staves,
                Scanner assists, Scanner specials,
                Scanner passivesA, Scanner passivesB, Scanner passivesC,
                Scanner passivesS) {

        List<Skill> skills = new ArrayList<>();

        //TODO: do weapons later

        Scanner[] weaponScanners = {
                swords,
                lances,
                axes,
                redTomes,
                blueTomes,
                greenTomes,
                bows,
                breaths,
                daggers,
                staves
        };

        List<Weapon> weapons = new ArrayList<>();
        //int TEST_VALUE = 0;
        for (Scanner wepTypeScanner:weaponScanners) {
            while (wepTypeScanner.hasNextLine()) {
                Scanner line = new Scanner(wepTypeScanner.nextLine());
                line.useDelimiter("\t");
                String name = line.next();
                int mt = Integer.parseInt(line.next());
                int rng = Integer.parseInt(line.next());
                String description = line.next();
                while (!line.hasNext()) {
                    line = new Scanner(wepTypeScanner.nextLine());
                    line.useDelimiter("\t");
                    description+= " "+line.next();
                    //TEST_VALUE++; System.out.println("woah i did line "+TEST_VALUE+" (desc. addition)");
                }

                int cost = Integer.parseInt(line.next());
                boolean exclusive = line.next().equals("Yes");

                Weapon weapon = new Weapon(name, description, 'W', cost, exclusive, mt, rng);

                weapons.add(weapon);
                //TEST_VALUE++; System.out.println("woah i did line "+TEST_VALUE);
            }
        }

        skills.addAll(weapons);

        //assist skills
        List<Assist> assistSkills = new ArrayList<>();
        while (assists.hasNextLine()) {
            Scanner line = new Scanner(assists.nextLine());
            line.useDelimiter("\t");
            String name = line.next();
            String description = line.next();
            while (!line.hasNext()) {
                line = new Scanner(assists.nextLine());
                line.useDelimiter("\t");
                description+= " "+line.next();
            }
            int cost = Integer.parseInt(line.next());
            int rng = Integer.parseInt(line.next());
            boolean exclusive = cost>300;       // TODO: this data needs to be added into the file later
                                                // (doesn't work for Rally Up Atk+ or Rally Spd/Def+

            Assist assist = new Assist(name, description, 'A', cost, exclusive, rng);
            assistSkills.add(assist);
        }

        skills.addAll(assistSkills);

        List<Special> specialSkills = new ArrayList<>();
        while(specials.hasNextLine()) {
            Scanner line = new Scanner(specials.nextLine());
            line.useDelimiter("\t");
            String name = line.next();
            String description = line.next();
            while (!line.hasNext()) {
                line = new Scanner(specials.nextLine());
                line.useDelimiter("\t");
                description+= " "+line.next();
            }
            int cost = Integer.parseInt(line.next());
            int cd = Integer.parseInt(line.next());
            boolean exclusive = cost>300;       // TODO: same here
                                                // (doesn't work for galeforce and aether)


            Special special = new Special(name, description, 'A', cost, exclusive, cd);
            specialSkills.add(special);
        }

        skills.addAll(specialSkills);

        //passive skills
        List<Skill> aSkills = processPassives(passivesA, 'a');
        List<Skill> bSkills = processPassives(passivesB, 'b');
        List<Skill> cSkills = processPassives(passivesC, 'c');
        List<Skill> sSkills = processPassives(passivesS, 's');

        skills.addAll(aSkills);
        skills.addAll(bSkills);
        skills.addAll(cSkills);
        skills.addAll(sSkills);

        return skills;
    }

    private static List<Skill> processPassives(Scanner list, char slot) {
        List<Skill> passives = new ArrayList<>();

        boolean TESTING = false;
        int TEST_VALUE = 0;
        while (list.hasNextLine()) {
            Scanner line = new Scanner(list.nextLine());
            line.useDelimiter("\t");
            line.next(); //discard image data

            String name = line.next();
            String description = line.next();
            while (!line.hasNext()) {
                line = new Scanner(list.nextLine());
                line.useDelimiter("\t");
                description+= " "+line.next();

                if (TESTING) { TEST_VALUE++; System.out.println("woah i did line "+TEST_VALUE); }
            }

            int cost = Integer.parseInt(line.next());
            boolean exclusive = line.next().equals("Yes");

            Skill skill = new Skill(name, description, slot, cost, exclusive);
            passives.add(skill);

            if (TESTING) { TEST_VALUE++; System.out.println("woah i did line "+TEST_VALUE); }
        }

        return passives;
    }

    public static List<Skill> getList() {
        File swordsFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\swords.txt");
        File lancesFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\lances.txt");
        File axesFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\axes.txt");
        File redTomesFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\redTomes.txt");
        File blueTomesFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\blueTomes.txt");
        File greenTomesFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\greenTomes.txt");
        File bowsFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\bows.txt");
        File breathsFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\breaths.txt");
        File daggersFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\daggers.txt");
        File stavesFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\weapons\\staves.txt");
        //middle button selection is a life saver
        Scanner swords;
        try {
            swords = new Scanner(swordsFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck swords");
            throw new Error();
        }
        Scanner lances;
        try {
            lances = new Scanner(lancesFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck lances");
            throw new Error();
        }
        Scanner axes;
        try {
            axes = new Scanner(axesFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck axes");
            throw new Error();
        }
        Scanner redTomes;
        try {
            redTomes = new Scanner(redTomesFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck redTomes");
            throw new Error();
        }
        Scanner blueTomes;
        try {
            blueTomes = new Scanner(blueTomesFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck blueTomes");
            throw new Error();
        }
        Scanner greenTomes;
        try {
            greenTomes = new Scanner(greenTomesFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck greenTomes");
            throw new Error();
        }
        Scanner bows;
        try {
            bows = new Scanner(bowsFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck bows");
            throw new Error();
        }
        Scanner breaths;
        try {
            breaths = new Scanner(breathsFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck breaths");
            throw new Error();
        }
        Scanner daggers;
        try {
            daggers = new Scanner(daggersFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck daggers");
            throw new Error();
        }
        Scanner staves;
        try {
            staves = new Scanner(stavesFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck staves");
            throw new Error();
        }

        File assistsFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\assists.txt");
        File specialsFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\specials.txt");
        File passivesAFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\passives\\a.txt");
        File passivesBFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\passives\\b.txt");
        File passivesCFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\passives\\c.txt");
        File passivesSFile = new File(".\\src\\utilities\\fehUnits\\skills\\sources\\passives\\s.txt");

        Scanner assists;
        try {
            assists = new Scanner(assistsFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck assists");
            throw new Error();
        }
        Scanner specials;
        try {
            specials = new Scanner(specialsFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck specials");
            throw new Error();
        }
        Scanner passivesA;
        try {
            passivesA = new Scanner(passivesAFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck passivesA");
            throw new Error();
        }
        Scanner passivesB;
        try {
            passivesB = new Scanner(passivesBFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck passivesB");
            throw new Error();
        }
        Scanner passivesC;
        try {
            passivesC = new Scanner(passivesCFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck passivesC");
            throw new Error();
        }
        Scanner passivesS;
        try {
            passivesS = new Scanner(passivesSFile);
        } catch (FileNotFoundException g) {
            System.out.println("fuck passivesS");
            throw new Error();
        }

        List<Skill> skills = processFiles(
                swords,
                lances,
                axes,
                redTomes,
                blueTomes,
                greenTomes,
                bows,
                breaths,
                daggers,
                staves,
                assists,
                specials,
                passivesA,
                passivesB,
                passivesC,
                passivesS
        );
        
        return skills;
    }

    public static void main(String[] args) {
        List<Skill> skills = getList();

        for (Skill x:skills)
            System.out.println(x);
    }
}
