package utilities.permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Permissions {
    private static final String PERMISSIONS_FILE_LOCATION = "./src/main/permissions.txt";
    private static final File PERMISSIONS_FILE = new File(PERMISSIONS_FILE_LOCATION);

    private static final HashMap<Long, Integer> PERMISSIONS_MAP;

    static {
        PERMISSIONS_MAP = new HashMap<>();

        Scanner file;
        try {
            file = new Scanner(PERMISSIONS_FILE);

            while (file.hasNextLine()) {
                String line = file.nextLine();
                String[] nums = line.split(" ");
                if (nums.length!=2) {
                    System.out.println("unprecedented spaces found: "+line);
                    continue;
                }

                PERMISSIONS_MAP.put(Long.parseLong(nums[0]), Integer.parseInt(nums[1]));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }



    public static int getPermissions(long id) {
        Integer permission = PERMISSIONS_MAP.get(id);
        return permission==null?0:permission;
    }

    private static boolean isElevated(int requestor, int target) {
        return requestor>=target;
    }

    public static void setPermissions(long requestor, long target, int permission)
            throws InsufficientPermissionsException, OverextendingPermissionException, DumbRequest {
        if (!isElevated(getPermissions(requestor), getPermissions(target)))
            throw new InsufficientPermissionsException();
        if (!isElevated(getPermissions(requestor), permission))
            throw new OverextendingPermissionException();
        if (getPermissions(target)==permission)
            throw new DumbRequest();
        PERMISSIONS_MAP.remove(target);
        PERMISSIONS_MAP.put(target, permission);
    }
}
