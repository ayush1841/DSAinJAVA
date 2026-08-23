class Solution {
    public boolean sumGame(String num) {

        int n = num.length();

        int leftSum = 0;
        int rightSum = 0;

        int leftQuestion = 0;
        int rightQuestion = 0;

        // Left half
        for (int i = 0; i < n / 2; i++) {

            if (num.charAt(i) == '?') {
                leftQuestion++;
            } else {
                leftSum += num.charAt(i) - '0';
            }
        }

        // Right half
        for (int i = n / 2; i < n; i++) {

            if (num.charAt(i) == '?') {
                rightQuestion++;
            } else {
                rightSum += num.charAt(i) - '0';
            }
        }

        // Odd number of '?' means Alice wins
        if ((leftQuestion + rightQuestion) % 2 == 1) {
            return true;
        }

        // Check whether Bob can make both sums equal
        return leftSum - rightSum
                != (rightQuestion - leftQuestion) / 2 * 9;
    }
}