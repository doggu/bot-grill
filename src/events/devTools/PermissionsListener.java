package events.devTools;

import events.MessageListener;
import net.dv8tion.jda.core.entities.User;
import utilities.permissions.DumbRequest;
import utilities.permissions.InsufficientPermissionsException;
import utilities.permissions.OverextendingPermissionException;
import utilities.permissions.Permissions;

import java.io.IOException;

public class PermissionsListener extends MessageListener {
    public boolean isCommand() { return args[0].equalsIgnoreCase("Permissions"); }

    public void onCommand() {
        if (args.length==1) {
            sendMessage("your permission level is: " +
                    Permissions.getPermissions(e.getAuthor().getIdLong()));
            return;
        }

        switch (args[1]) {
            case "set":
                if (args.length!=4) {
                    sendMessage("Syntax: `?Permissions set [target id] [level]`");
                }

                long idSet;
                try {
                    idSet = Long.parseLong(args[2]);
                } catch (NumberFormatException nfe) {
                    sendMessage("incorrect ID format! please try again.");
                    return;
                }

                int pLevel;
                try {
                    pLevel = Integer.parseInt(args[3]);
                } catch (NumberFormatException nfe) {
                    sendMessage("incorrect permission format! please try again.");
                    return;
                }

                try {
                    Permissions.setPermissions(e.getAuthor().getIdLong(), idSet, pLevel);
                    sendMessage("successfully changed/added permissions for "+e.getJDA().getUserById(idSet));
                } catch (InsufficientPermissionsException ipe) {
                    sendMessage("you do not have sufficient permissions to modify the target!");
                } catch (OverextendingPermissionException ope) {
                    sendMessage("you cannot elevate a target to a permission level higher than yours!");
                } catch (DumbRequest dr) {
                    sendMessage("the target is already at that permission level!");
                }
                break;
            case "get":
                if (args.length<3) {
                    sendMessage("please either specify the ID of the user or mention them " +
                            "to see their permission level.");
                    return;
                }
                long idGet;
                try {
                    idGet = Long.parseLong(args[2]);
                } catch (NumberFormatException nfe) {
                    sendMessage("incorrect ID format! please try again.");
                    return;
                }

                User suspect = e.getJDA().getUserById(idGet);
                if (suspect==null) {
                    sendMessage("could not find your specified target!");
                    return;
                }

                sendMessage("their permission level is: "+Permissions.getPermissions(idGet));
                break;
            case "refresh":
                try {
                    Permissions.refresh();
                    sendMessage("permissions refreshed!");
                } catch (IOException ioe) {
                    sendMessage("refresh unsuccessful: "+ioe.getMessage());
                }
                break;
        }
    }

    public char getPrefix() { return '&'; }
}
