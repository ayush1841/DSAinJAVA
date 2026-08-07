class Solution {
    public int[] findErrorNums(int[] nums) {

        int n = nums.length;

        boolean[] seen = new boolean[n + 1];

        int duplicate = -1;
        int missing = -1;

        // Find duplicate
        for (int num : nums) {
            if (seen[num]) {
                duplicate = num;
            } else {
                seen[num] = true;
            }
        }

        // Find missing
        for (int i = 1; i <= n; i++) {
            if (!seen[i]) {
                missing = i;
                break;
            }
        }

        return new int[]{duplicate, missing};
    }
}