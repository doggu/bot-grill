package events.commands.mcserver;

import java.io.*;
import java.util.Scanner;

public class MCServer implements Runnable {
    private static final String WORKING_DIRECTORY = ".\\libs\\server\\";
    private static final String MIN_WAM = "2048M", MAX_WAM = "4096M";
    private static final boolean GUI = false;

    private static final String COMMAND = "java "+
            ((MIN_WAM!=null)?"-Xms"+MIN_WAM+" ":"")+
            ((MAX_WAM!=null)?"-Xmx"+MAX_WAM+" ":"")+
            "-jar server.jar"+(GUI?"":" nogui");



    private Process server;



    public void run() {
        try {
            server = Runtime.getRuntime().exec(COMMAND, null, new File(WORKING_DIRECTORY));

            try {
                printLinesConcurrently("serverOut: ", server.getInputStream()).run();
                printLinesConcurrently("serverErr: ", server.getErrorStream()).run();
            } catch (Exception f) {
                f.printStackTrace();
            }
        } catch (IOException ioe) {
            System.out.println("honestly don't expect this to work anyway");
        }

        ServerInput.server = null;
        ServerInput.app = null;
    }



    //true if successful, false if catch
    public boolean sendCommand(String command) {
        OutputStream stdin = getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        try {
            writer.write(command);
            writer.flush();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }



    public OutputStream getOutputStream() {
        return server.getOutputStream();
    }

    //taken from https://www.journaldev.com/937/compile-run-java-program-another-java-program

    private static Thread printLinesConcurrently(String cmd, InputStream ins) {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));

        Runnable printer = () -> {
            try {
                String line;
                while((line = in.readLine())!=null) {
                    System.out.println(line);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        };

        return new Thread(printer);
    }

    public static void main(String[] args) {
        MCServer runnable = new MCServer();

        Thread server = new Thread(runnable);
        server.setDaemon(true);
        server.start();

        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            runnable.sendCommand(input.nextLine()+"\r");
        }
    }
}










