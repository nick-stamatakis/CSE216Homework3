import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BijectionGroup {

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

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static <T> Group<Function<T, T>> bijectionGroup(Set<T> elements) {
        return new Group<Function<T, T>>() {
            @Override
            public Function<T, T> binaryOperation(Function<T, T> f, Function<T, T> g) {
                return x -> f.apply(g.apply(x));
            }

            @Override
            public Function<T, T> identity() {
                return x -> x;
            }

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
    private static <T> Function<T, T> compose(Function<T, T> f, Function<T, T> g) {
        return x -> f.apply(g.apply(x));
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
        a_few.forEach(n -> System.out.printf("%d --> %d; ", n, f1.apply(n)));
        a_few.forEach(n -> System.out.printf("%d --> %d; ", n, f2.apply(n)));
        a_few.forEach(n -> System.out.printf("%d --> %d; ", n, id.apply(n)));
        }

//public static void main(String... args) {
//    Set<Integer> a_few = Stream.of(1, 2, 3,4).collect(Collectors.toSet());
//    Set<Function<Integer, Integer>> bijections = bijectionsOf(a_few);
//    bijections.forEach(aBijection -> {
//        a_few.forEach(n -> System.out.printf("%d --> %d; ", n, aBijection.apply(n)));
//        System.out.println();
//    });
//    Group<Function<Integer, Integer>> g = bijectionGroup(a_few);
//
//    // Test inverse of identity function
//    Function<Integer, Integer> id = g.identity();
//    Function<Integer, Integer> idInverse = g.inverseOf(id);
//    System.out.println("\nIDENTITY & IDENTITY INVERSE\n");
//    a_few.forEach(n -> System.out.printf("%d --> %d; ", n, id.apply(n)));
//    System.out.println();
//    a_few.forEach(n -> System.out.printf("%d --> %d; ", n, idInverse.apply(n)));// Expect identity function
//
//    // Test inverse of bijection
//    System.out.println("\n\nF1 and F1 INVERSE\n");
//    Function<Integer, Integer> f1 = bijections.stream().findFirst().get();
//    Function<Integer, Integer> f2 = g.inverseOf(f1);
//    System.out.println();
//    a_few.forEach(n -> System.out.printf("%d --> %d; ", n, f1.apply(n)));
//    System.out.println();
//    a_few.forEach(n -> System.out.printf("%d --> %d; ", n, f2.apply(n)));// Expect inverse of f1
//
//    // Test inverse of function with no inverse in group
//    System.out.println("\n\nSHOULD HAVE NO INVERSE\n");
//    Function<Integer, Integer> f3 = x -> x + 1;
//    try {
//        Function<Integer, Integer> f3Inverse = g.inverseOf(f3);
//        System.out.println();
//        a_few.forEach(n -> System.out.printf("%d --> %d; ", n, f3.apply(n)));
//        System.out.println();
//        a_few.forEach(n -> System.out.printf("%d --> %d; ", n, f3Inverse.apply(n)));// Expect IllegalArgumentException
//    } catch (IllegalArgumentException e) {
//        System.out.println(e.getMessage());
//    }
//
//    // Test function composition
//    System.out.println("\n\nFUNCTION COMPOSITION\n");
//    Function<Integer, Integer> composed = compose(f1, f2);
//    System.out.println();
//    a_few.forEach(n -> System.out.printf("%d --> %d; ", n, composed.apply(n))); // Expect identity function
//
//    // Test function composition with identity
//    System.out.println("\n\nCOMPOSITION WITH IDENTITY\n");
//    Function<Integer, Integer> composedWithIdentity = compose(f1, id);
//    System.out.println();
//    a_few.forEach(n -> System.out.printf("%d --> %d; ", n, composedWithIdentity.apply(n))); // Expect f1
//}

}

