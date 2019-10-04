package events.commands.stem.chem;

public enum CharIdentity {
    LETTER_U, LETTER_L, NUMBER, PAREN_O, PAREN_C;

    public static CharIdentity getIdentity(char input) {
        if (input>='A'&&input<='Z')
            return LETTER_U;
        if (input>='a'&&input<='z')
            return LETTER_L;

        if (input=='(')
            return PAREN_O;
        if (input==')')
            return PAREN_C;

        if (input>='0'&&input<='9')
            return NUMBER;

        return null;
    }
}
