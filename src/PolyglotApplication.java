import org.graalvm.polyglot.Context;

/**
 * A simple GraalVM Polyglot Application
 *
 * @author Amir Khordadi
 */
public class PolyglotApplication {
    public static void main(String[] args) {
        int n = 12;
        System.out.printf("Calculating factorial(%d)... \n", n);
        FactorialPolyglot factorialPolyglot = new FactorialPolyglot();
        int factorial = factorialPolyglot.factorial(n);
        System.out.printf("Result: %d\n", factorial);
    }

    public static class FactorialPolyglot {
        /**
         * Calculates factorial of an integer using recursion.
         * For different input values, it uses different programming languages.
         *
         * @param n the input to factorial function.
         * @return the result of factorial
         */
        public int factorial(int n) {
            if (n == 0 || n == 1) {
                System.out.printf("[Java] factorial(%d)\n", n);
                return 1;
            }
            switch (n % 4) {
                case 0:
                    System.out.printf("[JavaScript] factorial(%d)\n", n);
                    try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
                        context.getBindings("js").putMember("FactorialPolyglot", new FactorialPolyglot());
                        context.getBindings("js").putMember("n", n);
                        return context.eval("js", "n * FactorialPolyglot.factorial(n - 1)").asInt();
                    }
                case 1:
                    System.out.printf("[R] factorial(%d)\n", n);
                    try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
                        context.getBindings("R").putMember("FactorialPolyglot", new FactorialPolyglot());
                        context.getBindings("R").putMember("n", n);
                        return context.eval("R", "n * FactorialPolyglot$factorial(n - 1)").asInt();
                    }
                case 2:
                    System.out.printf("[Ruby] factorial(%d)\n", n);
                    try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
                        context.getPolyglotBindings().putMember("FactorialPolyglot", new FactorialPolyglot());
                        context.getPolyglotBindings().putMember("n", n);
                        return context.eval("ruby",
                                "Polyglot.import('n') * Polyglot.import('FactorialPolyglot')[:factorial].call(Polyglot.import('n') - 1)").asInt();
                    }
                case 3:
                    System.out.printf("[Python] factorial(%d)\n", n);
                    try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
                        context.getPolyglotBindings().putMember("FactorialPolyglot", new FactorialPolyglot());
                        context.getPolyglotBindings().putMember("n", n);
                        return context.eval("python",
                                "import polyglot\n" +
                                        "polyglot.import_value('n') * polyglot.import_value('FactorialPolyglot').factorial(polyglot.import_value('n') - 1)").asInt();
                    }
                default:
                    System.out.printf("[Java] factorial(%d)\n", n);
                    return n * factorial(n - 1);
            }
        }
    }
}
