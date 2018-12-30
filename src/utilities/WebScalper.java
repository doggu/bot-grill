package utilities;

import utilities.fehUnits.skills.Passive;
import utilities.fehUnits.skills.Skill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class WebScalper {
    private static String stripHTML(String line) {
        char[] charArr = line.toCharArray();
        List<Character> chars = new ArrayList<>();
        for (char x : charArr)
            chars.add(x);

        boolean tag = false;
        for (int i = 0; i < chars.size(); i++) {
            if (chars.get(i) == '<') tag = true;
            if (chars.get(i) == '>') {
                tag = false;
                chars.remove(i);
                i--;
                continue;
            }
            if (tag) {
                chars.remove(i);
                i--;
            }

        }

        StringBuilder text = new StringBuilder();
        for (char x : chars) {
            text.append(x);
        }
        return text.toString();
    }

    private static String[][] getTables(BufferedReader br) throws IOException {


        String[][] placeholder = new String[3][3];
        return placeholder;
    }



    private static void testPassives() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://feheroes.gamepedia.com/Passives").openConnection().getInputStream()));


        /*
        // read each line and write to System.out
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        */

        //read important lines
        String line;

        ArrayList<Passive> aPassives = new ArrayList<>();
        ArrayList<Passive> bPassives = new ArrayList<>();
        ArrayList<Passive> cPassives = new ArrayList<>();
        ArrayList<Passive> sPassives = new ArrayList<>();

        HashMap<Character, ArrayList<Passive>> lists = new HashMap<>();

        lists.put('a', new ArrayList<>());
        lists.put('b', new ArrayList<>());
        lists.put('c', new ArrayList<>());
        lists.put('s', new ArrayList<>());

        int listNumber = 0;
        int skillNumber = 0;

        while ((line = br.readLine()) != null) {
            if (line.contains("<div style=")) {
                char listChar;
                switch (listNumber) {
                    case 0:
                        listChar = 'a';
                        break;
                    case 1:
                        listChar = 'b';
                        break;
                    case 2:
                        listChar = 'c';
                        break;
                    case 3:
                        listChar = 's';
                        break;
                    default:
                        throw new Error();
                }
                ArrayList<Passive> list = lists.get(listChar);
                //System.out.println("skill list "+listNumber);
                while (!(line = br.readLine()).contains("</div>")) {
                    if (line.equals("<tr>")) {
                        //System.out.println("skill "+skillNumber);

                        /*
                        while (!(line = br.readLine()).contains("</tr>")) {
                            //System.out.println(stripHTML(line));
                        }
                        */

                        ///*
                        String img = br.readLine();
                        String name = stripHTML(br.readLine());
                        String description = stripHTML(br.readLine());
                        String costStr = stripHTML(br.readLine());
                        int cost = Integer.parseInt(costStr);
                        boolean exclusive = stripHTML(br.readLine()).contains("Yes");

                        try {
                            Passive k = new Passive(name, description, listChar, cost, exclusive);
                            list.add(k);
                        } catch (Error fuckinLazyProgrammingWithThisErrorMessage) {
                            System.out.println("somthin went wrang");
                            System.out.println(name);
                        }
                        //*/
                        skillNumber++;
                    } else {
                        //System.out.println(line);
                    }
                }

                System.out.println("number of skills: " + skillNumber);

                if (listNumber == 3) break;
                skillNumber = 1;

                listNumber++;
            }
        }

        //System.out.println("number of lists: "+listNumber);

        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String[] paramsStr = input.nextLine().split(" ");
            char list;
            int skill;
            try {
                list = paramsStr[0].charAt(0);
                skill = Integer.parseInt(paramsStr[1]);
            } catch (IndexOutOfBoundsException | NumberFormatException g) {
                System.out.println("incorrect syntax");
                continue;
            }

            try {
                System.out.println(lists.get(list).get(skill).toString());
            } catch (IndexOutOfBoundsException f) {
                System.out.println("out of range");
            }
        }
    }



    public static void main(String[] args) throws IOException {
        // Make a URL to the web page
        URL url = new URL("https://feheroes.gamepedia.com/Passives");

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();

        // Once you have the Input Stream, it's just plain old Java IO stuff.

        // For this case, since you are interested in getting plain-text web page
        // I'll use a reader and output the text content to System.out.

        // For binary content, it's better to directly read the bytes from stream and write
        // to the target file.


        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        testPassives();
    }
}