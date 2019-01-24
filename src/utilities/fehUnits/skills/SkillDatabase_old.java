package utilities.fehUnits.skills;

import utilities.ScannerUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SkillDatabase_old {
    private static final boolean TESTING = false;
    //currently only for basic info, such as SP cost and description
    private static List<Skill> processFiles(
                Scanner swords, Scanner lances, Scanner axes,
                Scanner redTomes, Scanner blueTomes, Scanner greenTomes,
                Scanner bows, Scanner breaths, Scanner daggers, Scanner staves,
                Scanner assists, Scanner specials,
                Scanner passivesA, Scanner passivesB, Scanner passivesC,
                Scanner passivesS) {

        List<Skill> skills = new ArrayList<>();

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
                }

                int cost = Integer.parseInt(line.next());
                boolean exclusive = line.next().equals("Yes");

                Weapon weapon = new Weapon(name, description, 'W', cost, exclusive, mt, rng);

                weapons.add(weapon);
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

            if (TESTING) System.out.println(description);

            int cost = Integer.parseInt(line.next());
            int cd = Integer.parseInt(line.next());
            boolean exclusive = cost>300;       // TODO: same here
                                                // (doesn't work for galeforce and aether)


            Special special = new Special(name, description, 'S', cost, exclusive, cd);
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
            }

            if (TESTING) System.out.println(description);

            int cost = Integer.parseInt(line.next());
            boolean exclusive = line.next().equals("Yes");

            Skill skill = new Passive(name, description, slot, cost, exclusive);
            passives.add(skill);
        }

        return passives;
    }

    public static List<Skill> getList() {
        String[] swordsPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "swords.txt"};
        String[] lancesPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "lances.txt"};
        String[] axesPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "axes.txt"};
        String[] redTomesPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "redTomes.txt"};
        String[] blueTomesPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "blueTomes.txt"};
        String[] greenTomesPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "greenTomes.txt"};
        String[] bowsPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "bows.txt"};
        String[] breathsPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "breaths.txt"};
        String[] daggersPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "daggers.txt"};
        String[] stavesPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "weapons", "staves.txt"};
        //middle button selection is a life saver

        File swordsFile = ScannerUtil.createFile(swordsPath);
        File lancesFile = ScannerUtil.createFile(lancesPath);
        File axesFile = ScannerUtil.createFile(axesPath);
        File redTomesFile = ScannerUtil.createFile(redTomesPath);
        File blueTomesFile = ScannerUtil.createFile(blueTomesPath);
        File greenTomesFile = ScannerUtil.createFile(greenTomesPath);
        File bowsFile = ScannerUtil.createFile(bowsPath);
        File breathsFile = ScannerUtil.createFile(breathsPath);
        File daggersFile = ScannerUtil.createFile(daggersPath);
        File stavesFile = ScannerUtil.createFile(stavesPath);

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

        String[] assistsPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "assists.txt"};
        String[] specialsPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "specials.txt"};
        String[] passivesAPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "passives", "a.txt"};
        String[] passivesBPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "passives", "b.txt"};
        String[] passivesCPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "passives", "c.txt"};
        String[] passivesSPath = {".", "src", "utilities", "fehUnits", "skills", "sources", "passives", "s.txt"};

        File assistsFile = ScannerUtil.createFile(assistsPath);
        File specialsFile = ScannerUtil.createFile(specialsPath);
        File passivesAFile = ScannerUtil.createFile(passivesAPath);
        File passivesBFile = ScannerUtil.createFile(passivesBPath);
        File passivesCFile = ScannerUtil.createFile(passivesCPath);
        File passivesSFile = ScannerUtil.createFile(passivesSPath);

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
