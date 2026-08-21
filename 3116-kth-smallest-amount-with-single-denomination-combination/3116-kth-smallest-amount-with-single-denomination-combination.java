class Solution {

    long gcd(long a, long b) {
        while (b != 0) {
            long temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }

    long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }

    long count(long x, int[] coins) {
        int n = coins.length;
        long ans = 0;

        // All non-empty subsets
        for (int mask = 1; mask < (1 << n); mask++) {

            long currentLCM = 1;
            boolean valid = true;

            for (int i = 0; i < n; i++) {

                if ((mask & (1 << i)) != 0) {

                    currentLCM = lcm(currentLCM, coins[i]);

                    // LCM already bigger than x
                    if (currentLCM > x) {
                        valid = false;
                        break;
                    }
                }
            }

            if (!valid) continue;

            // Odd number of coins -> add
            // Even number of coins -> subtract
            if (Integer.bitCount(mask) % 2 == 1) {
                ans += x / currentLCM;
            } else {
                ans -= x / currentLCM;
            }
        }

        return ans;
    }

    public long findKthSmallest(int[] coins, int k) {

        long low = 1;

        long minCoin = Integer.MAX_VALUE;

        for (int coin : coins) {
            minCoin = Math.min(minCoin, coin);
        }

        long high = minCoin * k;

        while (low < high) {

            long mid = low + (high - low) / 2;

            if (count(mid, coins) >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        return low;
    }
}