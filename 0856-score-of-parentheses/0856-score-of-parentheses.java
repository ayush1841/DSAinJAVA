class Solution {
    public int scoreOfParentheses(String s) {
        Stack<Integer> st = new Stack<>();
        st.push(0);

        for (char c : s.toCharArray()) {
            if (c == '(') {
                st.push(0);
            } else {
                int curr = st.pop();
                int score = (curr == 0) ? 1 : 2 * curr;
                st.push(st.pop() + score);
            }
        }

        return st.pop();
    }
}