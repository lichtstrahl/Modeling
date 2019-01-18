package iv.root.modeling.util;

public class Factorial {
    public static long factorial(long n)
    {
        long ret = 1;
        for (long i = 1; i <= n; ++i) ret *= i;
        return ret;
    }
}
