package discordUI;

public class UItils {
    public String wrapCode(String s) {
        return '`'+s+'`';
    }
    public StringBuilder wrapCode(StringBuilder s) {
        return s.insert(0,'`').append('`');
    }
}
