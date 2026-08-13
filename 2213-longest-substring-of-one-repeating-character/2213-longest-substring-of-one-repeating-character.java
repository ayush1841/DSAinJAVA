class Solution {

    class Node {
        int prefix, suffix, best, len;
        char leftChar, rightChar;
    }

    Node[] tree;
    char[] s;

    private Node merge(Node left, Node right) {
        Node res = new Node();

        res.len = left.len + right.len;
        res.leftChar = left.leftChar;
        res.rightChar = right.rightChar;

        // Prefix
        res.prefix = left.prefix;
        if (left.prefix == left.len && left.rightChar == right.leftChar) {
            res.prefix = left.len + right.prefix;
        }

        // Suffix
        res.suffix = right.suffix;
        if (right.suffix == right.len && left.rightChar == right.leftChar) {
            res.suffix = right.len + left.suffix;
        }

        // Best
        res.best = Math.max(left.best, right.best);
        if (left.rightChar == right.leftChar) {
            res.best = Math.max(res.best, left.suffix + right.prefix);
        }

        return res;
    }

    private void build(int idx, int l, int r) {
        if (l == r) {
            tree[idx] = new Node();
            tree[idx].prefix = 1;
            tree[idx].suffix = 1;
            tree[idx].best = 1;
            tree[idx].len = 1;
            tree[idx].leftChar = s[l];
            tree[idx].rightChar = s[l];
            return;
        }

        int mid = (l + r) / 2;

        build(idx * 2, l, mid);
        build(idx * 2 + 1, mid + 1, r);

        tree[idx] = merge(tree[idx * 2], tree[idx * 2 + 1]);
    }

    private void update(int idx, int l, int r, int pos, char ch) {

        if (l == r) {
            s[pos] = ch;

            tree[idx].leftChar = ch;
            tree[idx].rightChar = ch;
            tree[idx].prefix = 1;
            tree[idx].suffix = 1;
            tree[idx].best = 1;
            return;
        }

        int mid = (l + r) / 2;

        if (pos <= mid)
            update(idx * 2, l, mid, pos, ch);
        else
            update(idx * 2 + 1, mid + 1, r, pos, ch);

        tree[idx] = merge(tree[idx * 2], tree[idx * 2 + 1]);
    }

    public int[] longestRepeating(String str, String queryCharacters, int[] queryIndices) {

        int n = str.length();

        s = str.toCharArray();
        tree = new Node[4 * n];

        build(1, 0, n - 1);

        int[] ans = new int[queryIndices.length];

        for (int i = 0; i < queryIndices.length; i++) {
            update(1, 0, n - 1, queryIndices[i], queryCharacters.charAt(i));
            ans[i] = tree[1].best;
        }

        return ans;
    }
}