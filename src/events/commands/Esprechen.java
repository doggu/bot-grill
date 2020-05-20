package events.commands;

public class Esprechen extends Command {
    private static final String HASH = "#️⃣";
    private static final String[] REGIONAL_LETTERS = {
            "🇦",
            "🇧",
            "🇨",
            "🇩",
            "🇪",
            "🇫",
            "🇬",
            "🇭",
            "🇮",
            "🇯",
            "🇰",
            "🇱",
            "🇲",
            "🇳",
            "🇴",
            "🇵",
            "🇶",
            "🇷",
            "🇸",
            "🇹",
            "🇺",
            "🇻",
            "🇼",
            "🇽",
            "🇾",
            "🇿"
    };
    @Override
    public void onCommand() {
        StringBuilder msg = new StringBuilder(HASH);
        String req = e.getMessage().getContentRaw().toLowerCase();

        for (int i=1; i<req.length(); i++) {
            try {
                msg.append(REGIONAL_LETTERS[req.charAt(i)-'a']);
            } catch (IndexOutOfBoundsException ioobe) {
                msg.append(req.charAt(i));
            }
        }

        sendMessage(msg.toString());
    }

    @Override
    public boolean isCommand() {
        return e.getMessage().getContentRaw().charAt(0)=='#';
    }

    public char getPrefix() {
        return '#';
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getFullDescription() {
        return null;
    }
}
