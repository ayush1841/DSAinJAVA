class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();

        // pal[i][j] = true if s[i...j] is palindrome
        boolean[][] pal = new boolean[n][n];

        // Check all substrings
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {

                int j = i + len - 1;

                if (len == 1) {
                    pal[i][j] = true;
                } 
                else if (len == 2) {
                    pal[i][j] = (s.charAt(i) == s.charAt(j));
                } 
                else {
                    pal[i][j] =
                        (s.charAt(i) == s.charAt(j)) &&
                        pal[i + 1][j - 1];
                }
            }
        }

        // dp[i] = maximum palindromes using first i characters
        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {

            // Option 1: Don't end a palindrome at i-1
            dp[i] = dp[i - 1];

            // Option 2: Take a palindrome ending at i-1
            for (int j = 0; j < i; j++) {

                int len = i - j;

                if (len >= k && pal[j][i - 1]) {
                    dp[i] = Math.max(
                        dp[i],
                        dp[j] + 1
                    );
                }
            }
        }

        return dp[n];
    }
}