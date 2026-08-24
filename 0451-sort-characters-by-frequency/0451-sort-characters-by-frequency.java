class Solution {
    public String frequencySort(String s) {

        // 1. Count frequency of each character
        HashMap<Character, Integer> map = new HashMap<>();

        for (char ch : s.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }

        // 2. Put characters into an array
        Character[] arr = map.keySet().toArray(new Character[0]);

        // 3. Sort according to frequency
        Arrays.sort(arr, (a, b) -> map.get(b) - map.get(a));

        // 4. Build answer
        StringBuilder ans = new StringBuilder();

        for (char ch : arr) {
            int frequency = map.get(ch);

            for (int i = 0; i < frequency; i++) {
                ans.append(ch);
            }
        }

        return ans.toString();
    }
}