import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class HigherOrderUtils {
    public static interface NamedBiFunction<T, U, R> extends BiFunction<T, U, R>{
        String name();
    }

    public static NamedBiFunction<Double,Double,Double> add = new NamedBiFunction<Double,Double,Double>(){
        public String name(){
            return "plus";
        }

        public Double apply(Double a, Double b){
            return a + b;
        }
    };

    public static final NamedBiFunction<Double, Double, Double> subtract = new NamedBiFunction<Double, Double, Double>() {
        @Override
        public String name() {
            return "minus";
        }

        @Override
        public Double apply(Double a, Double b) {
            return a - b;
        }
    };

    public static final NamedBiFunction<Double, Double, Double> multiply = new NamedBiFunction<Double, Double, Double>() {
        @Override
        public String name() {
            return "mult";
        }

        @Override
        public Double apply(Double a, Double b) {
            return a * b;
        }
    };

    public static final NamedBiFunction<Double, Double, Double> divide = new NamedBiFunction<Double, Double, Double>() {
        @Override
        public String name() {
            return "div";
        }

        @Override
        public Double apply(Double a, Double b) {
            if (b == 0) {
                throw new ArithmeticException("Division by zero is being attempted");
            }
            return a / b;
        }
    };

    /**
     * Applies a given list of bifunctions -- functions that take two arguments of a certain type
     * and produce a single instance of that type -- to a list of arguments of that type. The
     * functions are applied in an iterative manner, and the result of each function is stored in
     * the list in an iterative manner as well, to be used by the next bifunction in the next
     * iteration. For example, given
     * List<Double> args = Arrays.asList(-0.5, 2d, 3d, 0d, 4d), and
     * List<NamedBiFunction<Double, Double, Double>> bfs = Arrays.asList(add, multiply, add, divide),
     * <code>zip(args, bfs)</code> will proceed as follows:
     * - the result of add(-0.5, 2.0) is stored in index 1 to yield args = [-0.5, 1.5, 3.0, 0.0, 4.0]
     * - the result of multiply(1.5, 3.0) is stored in index 2 to yield args = [-0.5, 1.5, 4.5, 0.0, 4.0]
     * - the result of add(4.5, 0.0) is stored in index 3 to yield args = [-0.5, 1.5, 4.5, 4.5, 4.0]
     * - the result of divide(4.5, 4.0) is stored in index 4 to yield args = [-0.5, 1.5, 4.5, 4.5, 1.125]
     *
     * @param args the arguments over which <code>bifunctions</code> will be applied.
     * @param bifunctions the list of bifunctions that will be applied on <code>args</code>.
     * @param <T> the type parameter of the arguments (e.g., Integer, Double)
     * @return the item in the last index of <code>args</code>, which has the final result
     * of all the bifunctions being applied in sequence.
     *
     * @throws IllegalArgumentException if the number of bifunction elements and the number of argument
     * elements do not match up as required.
     */
    public static <T> T zip(List<T> args, List<? extends BiFunction<T, T, T>> bifunctions) {
        if (args.size() != bifunctions.size() + 1) {
            throw new IllegalArgumentException("Number of arguments and number of bifunctions do not match.");
        }
        T result = args.get(0);
        for (int i = 0; i < bifunctions.size(); i++) {
            BiFunction<T, T, T> bifunction = bifunctions.get(i);
            T arg1 = result;
            T arg2 = args.get(i + 1);
            result = bifunction.apply(arg1, arg2);
        }
        return result;
    }

    public static void testZip() {
        // Test case 1 - Documentation example
        List<Double> numbers1 = Arrays.asList(-0.5, 2d, 3d, 0d, 4d);
        List<NamedBiFunction<Double, Double, Double>> operations1 = Arrays.asList(add, multiply, add, divide);
        Double expected1 = 1.125;
        Double result1 = zip(numbers1, operations1);
        System.out.println("\nExpected: " + expected1);
        System.out.println("Actual: " + result1);
        assert expected1.equals(result1) : "Test case 1 failed";

        // Test case 2 - Concatenation example
        List<String> strings2 = Arrays.asList("a", "n", "t");
        BiFunction<String, String, String> concat2 = (s, t) -> s + t;
        String expected2 = "ant";
        String result2 = zip(strings2, Arrays.asList(concat2, concat2));
        System.out.println("\nExpected: " + expected2);
        System.out.println("Actual: " + result2);
        assert expected2.equals(result2) : "Test case 2 failed";

        // Test case 3 - Integer addition example
        List<Integer> numbers3 = Arrays.asList(1, 2, 3,7,7);
        BiFunction<Integer, Integer, Integer> sum = (a,b) ->a + b;
        Integer expected3 = 6;
        Integer result3 = zip(numbers3,Arrays.asList(sum, sum, sum, sum));
        System.out.println("\nExpected: " + expected3);
        System.out.println("Actual: " + result3);
        assert expected3.equals(result3) : "Test case 3 failed";

        // Test case 4 - Double subtraction example
        List<Double> numbers4 = Arrays.asList(5d, 2d, 1d, 4d);
        BiFunction<Double, Double, Double> subtract = (a,b) ->a - b;
        Double expected4 = -2d;
        Double result4 = zip(numbers4, Arrays.asList(subtract, subtract, subtract));
        System.out.println("\nExpected: " + expected4);
        System.out.println("Actual: " + result4);
        assert expected4.equals(result4) : "Test case 4 failed";

        // Test case 5 - String concatenation with delimiter
        List<String> strings5 = Arrays.asList("a", "b", "c", "d");
        BiFunction<String, String, String> concatWithDelimiter = (s, t) -> s + "-" + t;
        String expected5 = "a-b-c-d";
        String result5 = zip(strings5, Arrays.asList(concatWithDelimiter, concatWithDelimiter, concatWithDelimiter));
        System.out.println("\nExpected: " + expected5);
        System.out.println("Actual: " + result5);
        assert expected5.equals(result5) : "Test case 5 failed";

        // Test case 6 - Integer multiplication example
        List<Integer> numbers6 = Arrays.asList(2, 3, 4, 5);
        BiFunction<Integer, Integer, Integer> multiply = (a,b) ->a * b;
        Integer expected6 = 120;
        Integer result6 = zip(numbers6,Arrays.asList(multiply, multiply, multiply));
        System.out.println("\nExpected: " + expected6);
        System.out.println("Actual: " + result6);
        assert expected6.equals(result6) : "Test case 6 failed";
    }



    public static void main(String[] args) {
        testZip();
    }
}