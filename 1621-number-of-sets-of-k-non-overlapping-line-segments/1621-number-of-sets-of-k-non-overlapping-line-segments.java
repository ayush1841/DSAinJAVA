class Solution {
    static final long MOD = 1_000_000_007;

    public int numberOfSets(int n, int k) {
        int N = n + k - 1;
        long[] fact = new long[N + 1];
        long[] invFact = new long[N + 1];

        fact[0] = 1;
        for (int i = 1; i <= N; i++)
            fact[i] = fact[i - 1] * i % MOD;

        invFact[N] = pow(fact[N], MOD - 2);

        for (int i = N; i > 0; i--)
            invFact[i - 1] = invFact[i] * i % MOD;

        return (int)(fact[N] * invFact[2 * k] % MOD
                * invFact[N - 2 * k] % MOD);
    }

    long pow(long a, long b) {
        long res = 1;

        while (b > 0) {
            if ((b & 1) == 1)
                res = res * a % MOD;

            a = a * a % MOD;
            b >>= 1;
        }

        return res;
    }
}