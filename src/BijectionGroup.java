import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CSE 216 HW3
 * Nicholas Stamatakis
 * ID: 114140995
 * R04
 *
 * Contains bijectionsOf method and Bijective Group class
 *
 */

public class BijectionGroup {
     /**
      * Returns a set of all bijections of the input domain set.
      * @param domain the domain set to generate bijections for
      * @return a set of all bijections of the input domain set
      * @param <T> the type of the elements in the domain set
     */
    public static <T> Set<Function<T, T>> bijectionsOf(Set<T> domain) {
        if (domain.isEmpty()) {
            throw new IllegalArgumentException("Domain set cannot be empty.");
        }
        List<T> domainList = new ArrayList<>(domain);
        List<List<T>> permutations = new ArrayList<>();
        generatePermutations(domainList.size(), domainList, permutations);
        Set<Function<T, T>> bijections = new LinkedHashSet<>(); // Changed to LinkedHashSet
        for (List<T> permutation : permutations) {
            Function<T, T> bijection = x -> permutation.get(domainList.indexOf(x));
            bijections.add(bijection);
        }
        return bijections;
    }

    /**
     Generates all permutations of a given list of elements and adds them to the given list of permutations.
     @param n the size of the list of elements
     @param elements the list of elements to permute
     @param permutations the list of permutations to add the generated permutations to
     @param <T> the type of the elements in the list
     */
    private static <T> void generatePermutations(int n, List<T> elements, List<List<T>> permutations) {
        if (n == 1) {
            permutations.add(new ArrayList<>(elements));
        } else {
            for (int i = 0; i < n - 1; i++) {
                generatePermutations(n - 1, elements, permutations);
                int j = n % 2 == 0 ? i : 0;
                swap(elements, j, n - 1);
            }
            generatePermutations(n - 1, elements, permutations);
        }
    }

    /**
     * Swaps the elements at the given indices in the given list.
     * @param list the list of elements to swap
     * @param i the index of the first element to swap
     * @param j the index of the second element to swap
     * @param <T> the type of the elements in the list
     */
    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     *
     * @param elements
     * Set of finite elements
     * @return
     * @param <T>
     */
    public static <T> Group<Function<T, T>> bijectionGroup(Set<T> elements) {
        return new Group<Function<T, T>>() {
            /**
             * Binary operation of the bijection group, where the result is a function composed of two functions
             * applied in sequence.
             *
             * @param f The first function.
             * @param g The second function.
             * @return A function composed of f and g applied in sequence.
             */
            @Override
            public Function<T, T> binaryOperation(Function<T, T> f, Function<T, T> g) {
                return x -> f.apply(g.apply(x));
            }

            /**
             * Identity element of the bijection group, which is the identity function.
             *
             * @return The identity function.
             */
            @Override
            public Function<T, T> identity() {
                return x -> x;
            }

            /**
             * Inverse element of a function in the bijection group, which is the function that "undoes" the given function.
             *
             * @param f The function to find the inverse of.
             * @return The inverse function of f.
             * //@throws IllegalArgumentException if f has no inverse in the group.
             */
            @Override
            public Function<T, T> inverseOf(Function<T, T> f) {
                return x -> {
                    for (T y : elements) {
                        if (Objects.equals(f.apply(y), x)) {
                            return y;
                        }
                    }
                    throw new IllegalArgumentException("Function has no inverse in this group.");
                };
            }
        };
    }

    /**
     * Prints the mapping of each element in the given set a_few to the corresponding output of applying the
     * given function f on it.
     *
     * @param a_few the set of elements to be mapped
     * @param f the function to apply on each element
     * @param <T> the type of elements in the set
     */
    public static <T> void print(Set<? extends T> a_few, Function<T,T> f) {
        a_few.forEach(n -> System.out.printf("%s --> %s; ", n, f.apply(n)));
        System.out.println();
    }

    /**
     * @param n
     * @param a_few
     * @param bijections
     * @return the nth bijection in the given set of bijections after checking that the index is valid and
     * printing the mapping of each element in a_few through the nth bijection.
     * @param <T>
     */
    public static <T> Function<T,T> findNthBijection(int n, Set<T> a_few, Set<Function<T,T>> bijections) {
        if (n < 0 || n >= bijections.size())
            throw new IllegalArgumentException("Index is out of Bounds");
        // Create a list of the bijections to access the nth bijection
        List<Function<T, T>> bijectionList = new ArrayList<>(bijections);
        // Get the nth bijection from the list
        Function<T, T> nthBijection = bijectionList.get(n);
        // Print the mapping of each element in a_few through the nth bijection
        return nthBijection;
    }

    public static void main(String[] args) {
        Set<Integer> a_few = Stream.of(1, 2, 3,4).collect(Collectors.toSet());
        Set<Function<Integer, Integer>> bijections = bijectionsOf(a_few);
        bijections.forEach(aBijection -> {
            a_few.forEach(n -> System.out.printf("%d --> %d; ", n, aBijection.apply(n)));
            System.out.println();
        });
        Group<Function<Integer, Integer>> g = bijectionGroup(a_few);
        Function<Integer, Integer> f1 = bijections.stream().findFirst().get();
        Function<Integer, Integer> f2 = g.inverseOf(f1);
        Function<Integer, Integer> id = g.identity();

        //Added by me
//        Function<Integer, Integer> comp = g.binaryOperation(f1,findNthBijection(5, a_few, bijections));
//        print(a_few, f1);
//        print(a_few, f2);
//        print(a_few, id);
//        print(a_few, findNthBijection(5, a_few, bijections));
//        print(a_few, comp);
//        System.out.println();
        }
}