package utilities.math;

import java.util.function.Function;
import java.lang.Math;

public class GradientDescent {
    private static double gamma = 0.01;
    private static double precision = 0.00001;


    public static double gradientDescent(Function<Double,Double> f, double curX) {
        double previousStepSize = 1.0;
        double prevX;

        while (Math.abs(previousStepSize) > precision) {
            prevX = curX;

            curX -= gamma * f.apply(prevX);

            double stepSize = curX - prevX;

            previousStepSize = curX - prevX;
        }
        return curX;
    }

    public static void main(String[] args) {
        Function<Double,Double> df = x ->  4 * Math.pow(x, 3) - 9 * Math.pow(x, 2);
        double res = gradientDescent(df, 6);
        System.out.printf("The local minimum occurs at %f", res);
    }
}
