import java.util.HashMap;
import java.util.Map;

class Solution {
    private static final long MOD = 1_000_000_007L;
    private static final long BASE = 31L;

    public String longestDupSubstring(String s) {
        int n = s.length();

        long[] prefix = new long[n + 1];
        long[] power = new long[n + 1];

        power[0] = 1;

        for (int i = 0; i < n; i++) {
            int value = s.charAt(i) - 'a' + 1;

            prefix[i + 1] =
                (prefix[i] * BASE + value) % MOD;

            power[i + 1] = power[i] * BASE % MOD;
        }

        int left = 1;
        int right = n - 1;
        int bestStart = -1;
        int bestLength = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            int start = findDuplicate(
                s, mid, prefix, power
            );

            if (start != -1) {
                bestStart = start;
                bestLength = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (bestStart == -1) {
            return "";
        }

        return s.substring(bestStart, bestStart + bestLength);
    }

    private int findDuplicate(
        String s,
        int len,
        long[] prefix,
        long[] power
    ) {
        Map<Long, Integer> seen = new HashMap<>();
        int n = s.length();

        for (int i = 0; i + len <= n; i++) {
            long hash = (
                prefix[i + len]
                - prefix[i] * power[len] % MOD
                + MOD
            ) % MOD;

            Integer previous = seen.putIfAbsent(hash, i);

            if (previous != null) {
                // Verify actual substrings to handle hash collisions.
                String first = s.substring(previous, previous + len);
                String current = s.substring(i, i + len);

                if (first.equals(current)) {
                    return i;
                }
            }
        }

        return -1;
    }
}