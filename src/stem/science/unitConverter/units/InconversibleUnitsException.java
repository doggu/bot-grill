package stem.science.unitConverter.units;

public class InconversibleUnitsException extends Exception {
    public InconversibleUnitsException(int[] from, int[] to) {
        super("cannot convert from:\n\t\t"+print(from)+"\nto:\n\t\t"+print(to));
    }

    private static String print(int[] config) {
        StringBuilder message = new StringBuilder(config[0]);
        for (int i=1; i<config.length; i++)
            message.append(' ').append(config[i]);
        return message.toString();
    }
}
