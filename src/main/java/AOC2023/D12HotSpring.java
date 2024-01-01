package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D12HotSpring {
    // char[], int[] pattern
    List<char[]> springs = new ArrayList<>();
    List<char[]> springsII = new ArrayList<>();
    List<List<Integer>> conditionsII = new ArrayList<>();
    List<List<Integer>> conditions = new ArrayList<>();
    List<Integer> maxBroken = new ArrayList<>();
    int[][] dp;

    Map<Long, Long> dpII = new HashMap<>();
    public D12HotSpring(List<String> input) {
        for (String line : input) {
            String[] parts = line.split(" ");
            String first = parts[0].trim();
            String second = parts[1].trim();
            springs.add(first.toCharArray());
            springsII.add(fiveTimes(first, '?').toCharArray());
            second = fiveTimes(second, ',');

            String[] pats = parts[1].split(",");
            List<Integer> cond = new ArrayList<>();
            int curBroken = 0;
            for (String p : pats) {
                if (p.isBlank() || p.isEmpty()) continue;
                int curNum = Integer.parseInt(p);
                cond.add(curNum);
                curBroken += curNum;
            }
            conditions.add(cond);

            List<Integer> condII = new ArrayList<>();
            for(String p : second.split(",")) {
                condII.add(Integer.parseInt(p));
            }
            conditionsII.add(condII);
//            maxBroken.add(curBroken);
        }
    }

    public long getSumII() {
        long res  = 0;
        for (int i = 0; i < springsII.size(); i++) {
            dpII = new HashMap<>();
            long curres = dfsII(springsII.get(i), 0, 0, 0, conditionsII.get(i));
//            System.out.println(Arrays.toString(springsII.get(i)) + "====>" + curres);
            res += curres;
        }
        return res;
    }

    private String fiveTimes(String cur, char spec) {
        StringBuilder res = new StringBuilder(cur);
        for (int i = 0; i < 4; i++) {
            res.append(spec);
            res.append(cur);
        }
        return res.toString();

    }

    public int getSumOfArrangements() {
        int res = 0;
        for (int i = 0; i < springs.size(); i++) {
            char[] cur = springs.get(i);
            int max = maxBroken.get(i);
            for (char c : cur) {
                if (c == '#') max--;
            }
            dp = new int[cur.length + 1][2];
            res += dfs(cur, 0, max, i);
        }
        return res;
    }

    public int getSumIITest() {
        int res = 0;
        for (int i = 0; i < springs.size(); i++) {
            char[] cur = springs.get(i);
            dpII = new HashMap<>();
            long curres= dfsII(cur, 0, 0, 0, conditions.get(i));
            System.out.println(Arrays.toString(cur) + "====>" + curres);
//            System.out.println(dpII);
        }
        return res;
    }



    private long dfsII(char[] A, int index, int cursize, int pos, List<Integer> list) {
        if ((cursize > 0 && pos >= list.size()) || ( pos < list.size() && cursize > list.get(pos))) return 0;

        if (index >= A.length) {
            return (pos >= list.size() ||( pos == list.size() - 1 && cursize == list.get(pos))) ? 1 : 0;
        }

        long hash = rollhash(index, cursize, pos);

        if (dpII.containsKey(hash)) {
            return dpII.get(hash);
        }

        long res = 0;
        if (A[index] == '.') {
            if (cursize == 0) {
                res = dfsII(A, index + 1, 0, pos, list);
            } else if (cursize == list.get(pos)) {
                res = dfsII(A, index + 1, 0, pos + 1, list);
            }
        } else if (A[index] == '#') {
            res = dfsII(A, index + 1, cursize + 1, pos, list);
        } else {
            if (cursize == 0) {
                res = dfsII(A, index + 1, 0, pos, list);
            } else if (cursize == list.get(pos)) {
                res = dfsII(A, index + 1, 0, pos + 1, list);
            }
            res += dfsII(A, index + 1,cursize + 1, pos, list);
        }

        dpII.put(hash, res);
        return res;

    }

    private long rollhash(int index, int cursize, int pos) {
        long res = index;
        res = res * 31 + cursize;
        res = res * 31 + pos;
        return res;
    }


    private int dfs(char[] A, int index, int maxBroken, int row) {
        if (maxBroken < 0) return 0;
        if (index >= A.length) {
            return isAcceptable(A, row) ? 1 : 0;
        }

        int res = 0;
        if (A[index] == '.' || A[index] == '#') {
            return dfs(A, index + 1, maxBroken, row);
        } else {
            A[index] = '.';
            res += dfs(A, index + 1, maxBroken, row);
            A[index] = '#';
            res += dfs(A, index + 1, maxBroken - 1, row);
            A[index] = '?';
        }
        return res;

    }

    private boolean isAcceptable(char[] A, int row ) {
        List<Integer> condition = conditions.get(row);
        int pos = 0;
        for (int i = 0; i < A.length; i++) {
            if (A[i] == '#') {
                int curLen = 1;
                while( i + 1 < A.length && A[i + 1] == '#'){
                    i++;
                    curLen++;
                }
                if (curLen != condition.get(pos++)) {
                    return false;
                }
            }
        }
        return pos == condition.size();
    }



    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D12");

        long start = System.currentTimeMillis();
        D12HotSpring ans = new D12HotSpring(input);

//        long res = ans.getSumIITest();
        long res = ans.getSumII();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
