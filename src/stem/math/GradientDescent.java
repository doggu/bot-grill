package stem.math;

import java.util.Scanner;
import java.util.function.Function;
import java.lang.Math;

public class GradientDescent {
    private static double gamma = 0.01;
    private static double precision = 0.00000001;


    public static double gradientDescent(Function<Double,Double> f, double curX) {
        double previousStepSize = 1.0;
        double prevX;

        while (Math.abs(previousStepSize) > precision) {
            prevX = curX;
            curX -= gamma*f.apply(prevX);
            previousStepSize = curX - prevX;
        }
        return curX;
    }

    public static void main(String[] args) {
        System.out.print("enter a function: ");
        Scanner input = new Scanner(System.in);
        String[] arguments = input.nextLine().split(" ");
        while (!arguments[0].equals("quit")) {
            if (arguments.length<2) {
                System.out.println("incorrect format!");
                System.out.print("enter a function: ");
                arguments = input.nextLine().split(" ");
                continue;
            }
            double start = Double.parseDouble(arguments[0]);
            String function = arguments[1];
            try {
                Function<Double,Double> f = new MathParse(function).getFunction();
                System.out.println(GradientDescent.gradientDescent(f, start));
            } catch (Error format) {
                System.out.println("incorrect format!");
            }

            System.out.print("enter a function: ");
            arguments = input.nextLine().split(" ");
        }
    }
}
