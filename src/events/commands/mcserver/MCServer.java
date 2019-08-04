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
    private BufferedWriter writer;



    public void run() {
        try {
            server = Runtime.getRuntime().exec(COMMAND, null, new File(WORKING_DIRECTORY));

            printLinesConcurrently("serverOut: ", server.getInputStream()).run();
            printLinesConcurrently("serverErr: ", server.getErrorStream()).run();

            writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            server.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } finally {
            ServerInput.server = null;
            ServerInput.app = null;
            try {
                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("the writer didn't close properly!");
            }
            writer = null;
        }
    }



    //true if successful, false if catch
    boolean sendCommand(String command) {
        try {
            writer.write(command);
            writer.flush();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }



    private static Thread printLinesConcurrently(String cmd, InputStream ins) {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));

        Runnable printer = () -> {
            try {
                String line;
                while((line = in.readLine())!=null) {
                    System.out.println(cmd+line);
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










