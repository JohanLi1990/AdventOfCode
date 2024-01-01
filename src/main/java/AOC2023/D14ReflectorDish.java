package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D14ReflectorDish {

    List<String> graph;
    int M, N;

    char[][] A;
    public D14ReflectorDish(List<String> input) {
        //
        graph = new ArrayList<>(input);
        M = input.size();
        N = input.get(0).length();

        A = new char[M][];
        for (int i = 0; i < M; i++) {
            A[i] = input.get(i).toCharArray();
        }

    }

    public long getLoadFromNCycle(int cycles) {
        char[][] temp = getCopy(A);
//        Map<Integer, Integer> map = new HashMap<>()
//        System.out.println(totalLoadArray(temp));
        int num = cycles;
        Map<Long, Integer> map = new HashMap<>();
        Map<Integer, Integer> recurrence = new HashMap<>();
        int recur = -1;
        for (int i = 0; i < num; i++) {
            cycle(temp);
//            print2DArr(temp);
            long cur = totalLoadArray(temp);
            System.out.println(cur);
            if (map.containsKey(cur)) {
                recur = i - map.get(cur);
            }
            if (recur > 0 && recur != 1) {
                recurrence.put(recur, recurrence.getOrDefault(recur, 0) + 1);
                if (recurrence.get(recur) > 20) {
                    num -= (i + 1);
                    break;
                }
            }

            map.put(cur, i);
        }

        num %= recur;
        for (int i = 0; i < num; i++) {
            cycle(temp);
        }

        long res = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (temp[i][j] == 'O') {
                    res += M - i;
                }
            }
        }
        return res;
    }

    private static void print2DArr(char[][] temp) {
        System.out.println("==================");
        for(char[] row : temp) {
            System.out.println(Arrays.toString(row));
        }
    }

    private void cycle(char[][] A) {
        // N
        for (int i = 1; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (A[i][j] == 'O') {
                    int k = i;
                    while(k - 1 >= 0 && A[k - 1][j] == '.') {
                        k--;
                    }
                    A[i][j] = '.';
                    A[k][j] = 'O';
                }
            }
        }

        // W
        for (int j = 1; j < N; j++) {
            for (int i = 0; i < M; i++) {
                if (A[i][j] == 'O') {
                    int k = j;
                    while(k - 1 >= 0 && A[i][k - 1] == '.') {
                        k--;
                    }
                    A[i][j] = '.';
                    A[i][k] = 'O';
                }
            }
        }

        //S
        for (int i = M - 2; i >= 0; i--) {
            for (int j = 0; j < N; j++) {
                if (A[i][j] == 'O') {
                    int k = i;
                    while(k + 1 < M && A[k + 1][j] == '.') {
                        k++;
                    }
                    A[i][j] = '.';
                    A[k][j] = 'O';
                }
            }
        }

        // E
        for (int j = N - 2; j >= 0; j--) {
            for (int i = 0; i < M; i++) {
                if (A[i][j] == 'O') {
                    int k = j;
                    while(k + 1 < N && A[i][k + 1] == '.') {
                        k++;
                    }
                    A[i][j] = '.';
                    A[i][k] = 'O';
                }
            }
        }

    }

    private char[][] getCopy(char[][] A) {
        char[][] res = new char[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            System.arraycopy(A[i], 0, res[i], 0, A[0].length);
        }
        return res;
    }

    public long totalLoadArray(char[][] A) {
        long res = 0;
        int[] pre = new int[N];
        for (int i = M - 1; i >= 0; i--) {
            int[] dp = new int[N];
            for (int j = 0; j < N; j++) {
                char cur = A[i][j];
                if (cur == '#') {
                    res += getLoad(pre[j], M - i - 1);
                } else if (cur == 'O') {
                    dp[j] += pre[j] + 1;
                } else {
                    dp[j] = pre[j];
                }
            }
            pre = dp;
        }
        for (int j = 0; j < N; j++) {
            if (pre[j] != 0) {
                res += getLoad(pre[j], M);
            }
        }
        return res;
    }

    public long totalLoad() {
        // from the bottom row upwards
        // int[] dp  = new int[N];
        // if (A[row][i] == '.') dp[row][i] = dp[row - 1][i];
        long res = 0;
        int[] pre = new int[N];
        for (int i = M - 1; i >= 0; i--) {
            int[] dp = new int[N];
            for (int j = 0; j < N; j++) {
                char cur = graph.get(i).charAt(j);
                if (cur == '#') {
                    res += getLoad(pre[j], M - i - 1);
                } else if (cur == 'O') {
                    dp[j] += pre[j] + 1;
                } else {
                    dp[j] = pre[j];
                }
            }
            pre = dp;
        }
        for (int j = 0; j < N; j++) {
            if (pre[j] != 0) {
                res += getLoad(pre[j], M);
            }
        }
        return res;
    }

    private long getLoad(int numStone, int curRow) {
        if (curRow == 0) return 0;
        long res = 0;
        while(numStone > 0) {
            res += curRow;
            numStone--;
            curRow--;
        }
        return res;

    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D14");

        long start = System.currentTimeMillis();
        D14ReflectorDish ans = new D14ReflectorDish(input);

        long res = ans.getLoadFromNCycle(1000000000);
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
