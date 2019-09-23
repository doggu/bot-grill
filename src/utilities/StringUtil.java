package utilities;

import java.util.List;

//StringUtil class #43589764798435678922356487934567892356479278036954790356847982056
public class StringUtil {
    public static String join(List<String> strings) {
        StringBuilder joined = new StringBuilder();
        for (String s:strings) {
            joined.append(s).append(' ');
        }

        return joined.substring(0, joined.length()-1);
    }
}
