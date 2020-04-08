package events.stem.math;

import events.commands.Command;
import stem.math.matrix.Matrix;
import stem.math.matrix.NotSquareException;

public class Determinant extends Command {
    @Override
    public boolean isCommand() {
        return args[0].contains("determinant"); }

    @Override
    public String getName() {
        return "Determinant"; }

    @Override
    public String getDescription() {
        return "Find the determinant of an n-by-n matrix"; }

    @Override
    public String getFullDescription() {
        return "Syntax:\n" +
                "\t\"?Determinant\n" +
                "```" +
                "a11, a12, a13 ... a1n\n" +
                "a21, a22, a23 ... a2n\n" +
                "a31, a32, a33 ... a3n\n" +
                "...  ...  ... ... a4n\n" +
                "an1, an2, an3 ... ann```\"\n" +
                "(write out the code block)";
    }

    @Override
    public void onCommand() {
        String matrix_raw = e.getMessage().getContentRaw();
        matrix_raw = matrix_raw.substring(matrix_raw.indexOf("```")+3);
        int last = !matrix_raw.contains("\n```") ?
                matrix_raw.indexOf("```") : matrix_raw.indexOf("\n```");
        matrix_raw = matrix_raw.substring(0, last);

        String[] rows = matrix_raw.split("\n");
        String[][] matrix = new String[rows.length][];
        for (int i=0; i<rows.length; i++) {
            matrix[i] = rows[i].split(", ");
        }

        System.out.println("LEN: "+matrix.length);
        System.out.println("wid: "+matrix[0].length);

        int[][] nums = new int[matrix.length][matrix[0].length];

        System.out.println("LEN: "+nums.length);
        System.out.println("wid: "+nums[0].length);

        for (int i=0; i<nums.length; i++) {
            for (int j=0; j<nums[0].length; j++) {
                try {
                    nums[i][j] = Integer.parseInt(matrix[i][j]);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    return;
                }
            }
        }

        Matrix m = new Matrix(nums);

        try {
            sendMessage("the determinant of your matrix is "+m.determinant());
        } catch (NotSquareException ex) {
            ex.printStackTrace();
        }
    }
}
