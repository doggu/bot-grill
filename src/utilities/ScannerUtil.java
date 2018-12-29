package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ScannerUtil {
    private static final boolean isWindows = System.getProperty("os.name").contains("Windows");

    public static char getPathChar() {
        if (isWindows) return '\\';
        else return '/';
    }

    public static File createFile(String[] path) {
        StringBuilder pathStr = new StringBuilder();
        for (String x:path)
            pathStr.append(x).append(getPathChar());
        return new File(pathStr.toString());
    }
}
