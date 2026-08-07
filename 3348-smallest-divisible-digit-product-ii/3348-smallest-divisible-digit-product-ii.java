class Solution {

    /*
        Digit -> {number of 2s, 3s, 5s, 7s}

        1 -> 1
        2 -> 2
        3 -> 3
        4 -> 2^2
        5 -> 5
        6 -> 2 * 3
        7 -> 7
        8 -> 2^3
        9 -> 3^2
    */
    private static final int[][] FACTOR = {
        {0, 0, 0, 0}, // 0
        {0, 0, 0, 0}, // 1
        {1, 0, 0, 0}, // 2
        {0, 1, 0, 0}, // 3
        {2, 0, 0, 0}, // 4
        {0, 0, 1, 0}, // 5
        {1, 1, 0, 0}, // 6
        {0, 0, 0, 1}, // 7
        {3, 0, 0, 0}, // 8
        {0, 2, 0, 0}  // 9
    };

    /*
        t <= 10^14

        Maximum possible:
        exponent of 2 <= 46
        exponent of 3 <= 29

        60 is safely enough.
    */
    private static final int MAX = 60;

    /*
        min23[a][b] =
        minimum number of digits needed to produce
        AT LEAST 2^a * 3^b
    */
    private int[][] min23;


    public String smallestNumber(String num, long t) {

        // -----------------------------------------
        // STEP 1: Factorize t
        // -----------------------------------------

        long[] need = new long[4];

        int[] primes = {2, 3, 5, 7};

        for (int i = 0; i < 4; i++) {

            while (t % primes[i] == 0) {

                need[i]++;
                t /= primes[i];
            }
        }

        /*
            If anything is left, it contains
            a prime other than 2,3,5,7.

            No digit 1..9 can provide that factor.
        */
        if (t != 1) {
            return "-1";
        }


        // -----------------------------------------
        // STEP 2: Build DP for 2 and 3
        // -----------------------------------------

        buildDP();


        int n = num.length();


        // -----------------------------------------
        // STEP 3: Count factors in num
        // -----------------------------------------

        long[] have = new long[4];

        for (int i = 0; i < n; i++) {

            int d = num.charAt(i) - '0';

            /*
                0 has no useful factor contribution,
                and it cannot be part of final answer.
            */
            if (d == 0) {
                continue;
            }

            for (int j = 0; j < 4; j++) {
                have[j] += FACTOR[d][j];
            }
        }


        // -----------------------------------------
        // STEP 4: If num itself works
        // -----------------------------------------

        /*
            But num must also be zero-free.
        */
        if (num.indexOf('0') == -1 && enough(have, need)) {
            return num;
        }


        // -----------------------------------------
        // STEP 5:
        // Try to make answer with SAME length
        // -----------------------------------------

        /*
            firstZero = first position containing 0.

            If we change a position AFTER firstZero,
            the zero remains in the prefix -> invalid.

            So only positions <= firstZero are useful.
        */
        int firstZero = num.indexOf('0');

        if (firstZero == -1) {
            firstZero = n;
        }


        /*
            We go RIGHT -> LEFT.

            Why?

            We want the smallest number >= num.

            Therefore we should change the latest
            possible position.
        */
        for (int i = n - 1; i >= 0; i--) {

            int current = num.charAt(i) - '0';


            /*
                Remove current digit from "have".

                Now have represents digits
                BEFORE position i.
            */
            if (current != 0) {

                for (int j = 0; j < 4; j++) {
                    have[j] -= FACTOR[current][j];
                }
            }


            /*
                If there is already a zero before i,
                then prefix would contain zero.

                Therefore this position cannot work.
            */
            if (i > firstZero) {
                continue;
            }


            /*
                Try a larger digit.

                Example:
                current = 3

                Try:
                4,5,6,7,8,9
            */
            for (int d = current + 1; d <= 9; d++) {

                long[] remaining = new long[4];


                /*
                    Required factors still needed
                    after taking:

                    prefix + new digit
                */
                for (int j = 0; j < 4; j++) {

                    remaining[j] = Math.max(
                        0,
                        need[j]
                            - have[j]
                            - FACTOR[d][j]
                    );
                }


                int suffixLength = n - i - 1;


                /*
                    Can the suffix satisfy all
                    remaining factors?
                */
                if (possible(remaining, suffixLength)) {

                    StringBuilder ans = new StringBuilder();

                    // Prefix
                    ans.append(num, 0, i);

                    // Changed digit
                    ans.append(d);

                    /*
                        Build smallest possible suffix.
                    */
                    ans.append(
                        buildSmallest(
                            remaining,
                            suffixLength
                        )
                    );

                    return ans.toString();
                }
            }
        }


        // -----------------------------------------
        // STEP 6:
        // Same length impossible.
        //
        // Build a number with more digits.
        // -----------------------------------------

        /*
            For a larger length, the smallest
            possible first digit is 1.

            But 1 may not provide enough factors,
            so try 1..9.
        */

        for (int length = n + 1; ; length++) {

            for (int first = 1; first <= 9; first++) {

                long[] remaining = new long[4];


                /*
                    Factors needed after first digit.
                */
                for (int j = 0; j < 4; j++) {

                    remaining[j] = Math.max(
                        0,
                        need[j] - FACTOR[first][j]
                    );
                }


                int suffixLength = length - 1;


                if (possible(
                        remaining,
                        suffixLength
                    )) {

                    StringBuilder ans =
                        new StringBuilder();

                    ans.append(first);

                    ans.append(
                        buildSmallest(
                            remaining,
                            suffixLength
                        )
                    );

                    return ans.toString();
                }
            }
        }
    }


    // =================================================
    // DP
    // =================================================

    private void buildDP() {

        final int INF = 1_000_000;


        /*
            exact[a][b] =
            minimum digits whose factor product
            contains EXACTLY:

            2^a * 3^b
        */
        int[][] exact =
            new int[MAX + 1][MAX + 1];


        for (int i = 0; i <= MAX; i++) {
            java.util.Arrays.fill(
                exact[i],
                INF
            );
        }


        exact[0][0] = 0;


        /*
            Only these digits are relevant
            for factors 2 and 3:

            2 -> 2
            3 -> 3
            4 -> 2^2
            6 -> 2*3
            8 -> 2^3
            9 -> 3^2
        */
        int[] digits = {2, 3, 4, 6, 8, 9};


        for (int a = 0; a <= MAX; a++) {

            for (int b = 0; b <= MAX; b++) {

                if (exact[a][b] == INF) {
                    continue;
                }


                for (int d : digits) {

                    int add2 = FACTOR[d][0];
                    int add3 = FACTOR[d][1];


                    if (a + add2 <= MAX &&
                        b + add3 <= MAX) {

                        exact[a + add2][b + add3] =
                            Math.min(
                                exact[a + add2][b + add3],
                                exact[a][b] + 1
                            );
                    }
                }
            }
        }


        /*
            Convert exact -> AT LEAST.

            min23[a][b] =
            minimum digits that provide
            at least a twos and b threes.
        */
        min23 =
            new int[MAX + 1][MAX + 1];


        for (int i = 0; i <= MAX; i++) {
            java.util.Arrays.fill(
                min23[i],
                INF
            );
        }


        for (int a = MAX; a >= 0; a--) {

            for (int b = MAX; b >= 0; b--) {

                min23[a][b] = exact[a][b];


                if (a < MAX) {

                    min23[a][b] =
                        Math.min(
                            min23[a][b],
                            min23[a + 1][b]
                        );
                }


                if (b < MAX) {

                    min23[a][b] =
                        Math.min(
                            min23[a][b],
                            min23[a][b + 1]
                        );
                }
            }
        }
    }


    // =================================================
    // Check whether factors fit in given digits
    // =================================================

    private boolean possible(
        long[] need,
        int length
    ) {

        /*
            Only digit 5 can provide factor 5.

            Only digit 7 can provide factor 7.

            Therefore every required 5 and 7
            needs one separate digit.
        */
        long fiveAndSeven =
            need[2] + need[3];


        if (fiveAndSeven > length) {
            return false;
        }


        /*
            Remaining positions are for
            factors 2 and 3.
        */
        int remainingSlots =
            length - (int) fiveAndSeven;


        int need2 = (int) need[0];
        int need3 = (int) need[1];


        int required23 =
            min23[need2][need3];


        return required23 <= remainingSlots;
    }


    // =================================================
    // Construct smallest suffix
    // =================================================

    private String buildSmallest(
        long[] need,
        int length
    ) {

        StringBuilder ans =
            new StringBuilder(length);


        for (int pos = 0; pos < length; pos++) {

            int remainingSlots =
                length - pos - 1;


            /*
                Try digits in increasing order.

                1 is allowed and consumes no factor,
                which makes it very useful.
            */
            for (int d = 1; d <= 9; d++) {

                long[] next =
                    new long[4];


                /*
                    Factors still needed after
                    choosing digit d.
                */
                for (int j = 0; j < 4; j++) {

                    next[j] = Math.max(
                        0,
                        need[j] - FACTOR[d][j]
                    );
                }


                /*
                    If remaining positions can
                    satisfy everything, choose d.
                */
                if (possible(
                        next,
                        remainingSlots
                    )) {

                    ans.append(d);

                    need = next;

                    break;
                }
            }
        }


        return ans.toString();
    }


    // =================================================
    // Check if current number has enough factors
    // =================================================

    private boolean enough(
        long[] have,
        long[] need
    ) {

        for (int i = 0; i < 4; i++) {

            if (have[i] < need[i]) {
                return false;
            }
        }

        return true;
    }
}