package stem.science.unitConverter;

import org.jetbrains.annotations.NotNull;
import stem.science.unitConverter.units.Unit;

import java.util.function.Function;

public class Converter implements Function<Double, Double> {
    //private final Unit from, to;



    public Converter() {
    }
    @NotNull
    @Override
    public <V> Function<V, Double> compose(@NotNull Function<? super V, ? extends Double> before) {
        return null;
    }

    @NotNull
    @Override
    public <V> Function<Double, V> andThen(@NotNull Function<? super Double, ? extends V> after) {
        return null;
    }

    @Override
    public Double apply(Double aDouble) {
        return null;
    }
}
