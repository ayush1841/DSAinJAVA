import java.util.*;

class Solution {
    public int maxNumberOfFamilies(int n, int[][] reservedSeats) {

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int[] seat : reservedSeats) {
            int row = seat[0];
            int s = seat[1];

            if (s >= 2 && s <= 9) {
                map.put(row, map.getOrDefault(row, 0) | (1 << (s - 2)));
            }
        }

        int ans = 2 * n;

        int left = 0b00001111;
        int middle = 0b00111100;
        int right = 0b11110000;

        for (int mask : map.values()) {

            ans -= 2;

            boolean canLeft = (mask & left) == 0;
            boolean canMiddle = (mask & middle) == 0;
            boolean canRight = (mask & right) == 0;

            if (canLeft && canRight) {
                ans += 2;
            } 
            else if (canLeft || canMiddle || canRight) {
                ans += 1;
            }
        }

        return ans;
    }
}