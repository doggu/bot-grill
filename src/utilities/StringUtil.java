package utilities;

import java.util.List;

//StringUtil class #435897647984356789223564879345678923564792780369547903568479
public class StringUtil {
    public static String join(List<String> strings) {
        StringBuilder joined = new StringBuilder();
        for (String s:strings) {
            joined.append(s).append(' ');
        }

        return joined.substring(0, joined.length()-1);
    }
}
