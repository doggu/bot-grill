package events.devTools;

import events.MessageListener;

public class DevTools extends MessageListener {
    public void onCommand() {
        System.out.println(e.getMessage().getContentRaw());
        switch(args[0].toLowerCase()) {
            case "kill":
                main.BotMain.bot_grill.shutdownNow();
                System.exit(1);
                break;
            case "sendmessage":
                StringBuilder message = new StringBuilder();
                for (int i=1; i<args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                sendMessage(message.toString());
                break;
        }
    }

    public boolean isCommand() {
        return e.getAuthor().getId().equals("125857288807251968"); //doggu, me
    }

    public char getPrefix() {
        return '&';
    }
}
