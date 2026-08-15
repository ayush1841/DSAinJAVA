class Solution {
    public int longestSubsequence(int[] nums) {
        int xor = 0;
        int zeros = 0;

        for (int num : nums) {
            xor ^= num;

            if (num == 0) {
                zeros++;
            }
        }

        // Case 1: Whole array has non-zero XOR
        if (xor != 0) {
            return nums.length;
        }

        // Case 2: All elements are zero
        if (zeros == nums.length) {
            return 0;
        }

        // Case 3: XOR is 0, but at least one non-zero element exists
        return nums.length - 1;
    }
}