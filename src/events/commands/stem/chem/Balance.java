package events.commands.stem.chem;

import events.commands.Command;
import stem.science.chem.particles.ChemicalElement;
import stem.science.chem.particles.ElementDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Balance extends Command {
    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("b(alance)?");
    }

    @Override
    public String getName() {
        return "Balance";
    }

    @Override
    public String getDescription() {
        return "balance chemical equations!";
    }

    @Override
    public String getFullDescription() {
        return "seriously, it balances chemical equations";
    }

    @Override
    public void onCommand() {



        ArrayList<String> args = new ArrayList<>(Arrays.asList(this.args));

        args.remove(0);
        System.out.println(args);

        if (args.size()<4) {
            //todo:
            System.out.println("please include at least three reactants and products " +
                    "(otherwise it's already balanced naturally)");
            return;
        }

        if (!args.contains(">")) {
            //todo:
            System.out.println("i could not find your arrow (use symbol \">\")");
            return;
        }

        ArrayList<String> reactantsStr =
                new ArrayList<>(args.subList(0, args.indexOf(">")));
        System.out.println(reactantsStr);

        ArrayList<String> productsStr =
                new ArrayList<>(args.subList(args.indexOf(">")+1, args.size()));
        System.out.println(productsStr);

        ArrayList<ArrayList<ChemicalElement>> reactants = new ArrayList<>();
        ArrayList<ArrayList<ChemicalElement>> products = new ArrayList<>();

        for (String x: reactantsStr) reactants.add(MolarMass.getElements(x));
        for (String x: productsStr) products.add(MolarMass.getElements(x));

        //boolean balanced = getBalanced(reactants, products);

        ArrayList<ChemicalElement> involvedElements = new ArrayList<>();

        for (ArrayList<ChemicalElement> list:reactants) {
            for (ChemicalElement e:list) {
                if (!involvedElements.contains(e)) involvedElements.add(e);
            }
        }

        /*
        while (!balanced&&f<6) {
            for (ChemicalElement x:involvedElements) {
                int rTotal = getTotalCount(x, reactants), pTotal = getTotalCount(x, products);

                if (rTotal==pTotal) {
                    System.out.println(x+" balanced");
                } else {
                    System.out.println(x+" not balanced");
                    if (rTotal>pTotal) {
                        ArrayList<ChemicalElement> bestModifier = null;
                        for (ArrayList<ChemicalElement> product : products) {
                            if (product.contains(x)) {
                                if (bestModifier == null) bestModifier = product;
                                else {
                                    if (getCount(x, bestModifier) > getCount(x, product)) {
                                        bestModifier = product;
                                    }
                                }
                            }
                        }
                        if (bestModifier!=null) {
                            for (int i = 0; i < rTotal-pTotal; i += getCount(x, bestModifier)) {
                                System.out.println(bestModifier);
                                products.add(bestModifier);
                            }

                            break;
                        }
                    } else if (rTotal<pTotal) {
                        ArrayList<ChemicalElement> bestModifier = null;
                        for (ArrayList<ChemicalElement> reactant : reactants) {
                            if (reactant.contains(x)) {
                                if (bestModifier == null) bestModifier = reactant;
                                else {
                                    if (getCount(x, bestModifier) > getCount(x, reactant)) {
                                        bestModifier = reactant;
                                    }
                                }
                            }
                        }
                        if (bestModifier!=null) {
                            for (int i = 0; i < pTotal-rTotal; i += getCount(x, bestModifier)) {
                                System.out.println(bestModifier);
                                reactants.add(bestModifier);
                            }

                            break;
                        }
                    }
                }
            }

            System.out.println("a loop has passed");
            f++;

            balanced = getBalanced(reactants, products);
        }
        */



        int[] rCount = coefficients(reactants);
        int[] pCount = coefficients(products);

        for (int i:rCount)
            System.out.println(i);
        for (int i:pCount)
            System.out.println(i);

        System.out.println(reactants);
        System.out.println(products);


        StringBuilder message = new StringBuilder();

        for (int i=0; i<reactants.size(); i++) {
            message.append(rCount[i]).append(reactantsStr.get(i)).append(' ');
        }

        message.append("> ");

        for (int i=0; i<productsStr.size(); i++) {
            message.append(rCount[i]).append(productsStr.get(i)).append(' ');
        }

        System.out.println(message);
    }

    private static int[] coefficients(ArrayList<ArrayList<ChemicalElement>> compounds) {
        HashMap<ArrayList<ChemicalElement>, Integer> coeffs = new HashMap<>();

        for (int i=0; i<compounds.size(); i++) {
            if (coeffs.get(compounds.get(i))==null) {
                coeffs.put(compounds.get(i), 1);
            } else {
                coeffs.put(compounds.get(i), coeffs.get(compounds.get(i))+1);
            }
        }
        System.out.println("aaa");

        int[] coefficients = new int[coeffs.keySet().size()];

        for (int i=0; i<compounds.size(); i++) {
            coefficients[i] = coeffs.get(compounds.get(i));

            while (compounds.size()>i+1) {
                if (compounds.get(i)==compounds.get(i+1)) {
                    compounds.remove(i+1);
                } else break;

                System.out.println("bbb");
            }
        }

        return coefficients;
    }

    private static boolean getBalanced(ArrayList<ArrayList<ChemicalElement>> reactants,
            ArrayList<ArrayList<ChemicalElement>> products) {
        if (totalSize(reactants)==totalSize(products)) {
            for (ChemicalElement x: ElementDatabase.ELEMENTS) {
                if (getTotalCount(x, products)!= getTotalCount(x, reactants)) {
                    return false;
                }

            }

            return true;
        }

        return false;
    }

    private static int totalSize(ArrayList<ArrayList<ChemicalElement>> elements) {
        int size = 0;
        for (ArrayList<ChemicalElement> x:elements) {
            size+= x.size();
        }

        return size;
    }

    private static boolean reallyContains(ChemicalElement element, ArrayList<ArrayList<ChemicalElement>> compounds) {
        return getTotalCount(element, compounds)!=0;
    }

    private static int getCount(ChemicalElement element, ArrayList<ChemicalElement> compound) {
        int count = 0;
        for (ChemicalElement e:compound) {
            if (element==e) count++;
        }

        return count;
    }
    private static int getTotalCount(ChemicalElement element, ArrayList<ArrayList<ChemicalElement>> compounds) {
        int count = 0;
        for (ArrayList<ChemicalElement> compound:compounds) {
            count+= getCount(element, compound);
        }

        return count;
    }

    public static void main(String[] args) {
        Balance f = new Balance();

        String problem = "balance C3H8O O2 > H2O CO2";
        //String problem = "balance NaOH H2SO4 > Na2SO4 H2O";

        f.args = problem.split(" ");

        f.onCommand();
    }
}
