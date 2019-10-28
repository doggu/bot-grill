package stem.science.unitConverter;

import stem.science.unitConverter.units.Unit;

import java.util.function.Function;

public class Converter implements Function<Double, Double> {
    private final Unit from, to;



    public Converter(Unit from, Unit to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Double apply(Double aDouble) {
        return aDouble*(from.scaleToSI()/to.scaleToSI());
    }                   //todo: more intelligent conversion might be
                        // possible with comparisons and eliminations
}
