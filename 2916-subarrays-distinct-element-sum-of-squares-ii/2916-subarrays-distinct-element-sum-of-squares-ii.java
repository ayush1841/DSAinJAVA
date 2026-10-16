class Solution {
    private static final long MOD = 1_000_000_007L;

    private int n;
    private long[] sum;
    private long[] squareSum;
    private long[] lazy;

    public int sumCounts(int[] nums) {
        n = nums.length;

        sum = new long[4 * n];
        squareSum = new long[4 * n];
        lazy = new long[4 * n];

        int[] lastSeen = new int[100001];
        java.util.Arrays.fill(lastSeen, -1);

        long ans = 0;

        for (int i = 0; i < n; i++) {
            int prev = lastSeen[nums[i]];

            // Update distinct counts for subarrays ending at i
            update(1, 0, n - 1, prev + 1, i);

            // Sum of squared distinct counts for subarrays ending at i
            ans = (ans + squareSum[1]) % MOD;

            lastSeen[nums[i]] = i;
        }

        return (int) ans;
    }

    private void update(int node, int start, int end, int left, int right) {
        if (start > right || end < left) {
            return;
        }

        if (start >= left && end <= right) {
            apply(node, start, end, 1);
            return;
        }

        pushDown(node, start, end);

        int mid = start + (end - start) / 2;

        update(node * 2, start, mid, left, right);
        update(node * 2 + 1, mid + 1, end, left, right);

        sum[node] = (sum[node * 2] + sum[node * 2 + 1]) % MOD;

        squareSum[node] =
            (squareSum[node * 2] + squareSum[node * 2 + 1]) % MOD;
    }

    private void apply(int node, int start, int end, long val) {
        long len = end - start + 1L;

        squareSum[node] = (
            squareSum[node]
            + 2L * val % MOD * sum[node] % MOD
            + len % MOD * val % MOD * val % MOD
        ) % MOD;

        sum[node] = (sum[node] + len * val % MOD) % MOD;

        lazy[node] = (lazy[node] + val) % MOD;
    }

    private void pushDown(int node, int start, int end) {
        if (lazy[node] == 0 || start == end) {
            return;
        }

        int mid = start + (end - start) / 2;

        apply(node * 2, start, mid, lazy[node]);
        apply(node * 2 + 1, mid + 1, end, lazy[node]);

        lazy[node] = 0;
    }
}