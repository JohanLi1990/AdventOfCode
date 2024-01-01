package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D11CosmicExpansion {

    char[][] A;
    int M, N;

    List<int[]> galaxies;
    public D11CosmicExpansion(List<String> input) {
        M = input.size();
        N = input.get(0).length();
        A = new char[M][];
        for (int i = 0; i < M; i++) {
            A[i] = input.get(i).toCharArray();
        }

        galaxies = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (A[i][j] == '#') {
                    galaxies.add(new int[]{i, j});
                }
            }
        }
    }

    public int distI() {
        int[] cols = new int[N];
        int[] row = new int[M];

        for (int i = 0; i < M; i++) {
            boolean allEmpty = true;
            for (int j = 0; j < N; j++) {
                if (A[i][j] !=  '.') {
                    allEmpty = false;
                    break;
                }
            }

            if (allEmpty) {
                row[i] = 1;
            }
        }

        for (int j = 0; j < N; j++) {
            boolean allEmpty = true;
            for (int i = 0; i < M; i++) {
                if (A[i][j] !=  '.') {
                    allEmpty = false;
                    break;
                }
            }

            if (allEmpty) {
                cols[j] = 1;
            }
        }

        return calculateDist(row, cols);
    }

    private long distII(long multiple) {
        int[] cols = new int[N];
        int[] row = new int[M];

        for (int i = 0; i < M; i++) {
            boolean allEmpty = true;
            for (int j = 0; j < N; j++) {
                if (A[i][j] !=  '.') {
                    allEmpty = false;
                    break;
                }
            }

            if (allEmpty) {
                row[i] = 1;
            }
        }

        for (int j = 0; j < N; j++) {
            boolean allEmpty = true;
            for (int i = 0; i < M; i++) {
                if (A[i][j] !=  '.') {
                    allEmpty = false;
                    break;
                }
            }

            if (allEmpty) {
                cols[j] = 1;
            }
        }

        return calculateDistII(row, cols, multiple);
    }

    private long calculateDistII(int[] ro, int[] col, long mul) {
        long res = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            int[] cur = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                int[] next = galaxies.get(j);

                long dist = Math.abs(next[0] - cur[0]) + Math.abs(next[1] - cur[1]);
//                System.out.println(Arrays.toString(cur) + "->" + Arrays.toString(next)
//                        + ": " + dist);
                dist += (long) getExtra(ro, cur[0], next[0]) * (mul - 1);
                dist += (long) getExtra(col, cur[1], next[1]) * (mul -1);
//                System.out.println(Arrays.toString(cur) + "->" + Arrays.toString(next)
//                + ": " + dist);
                res += dist;
            }
        }
        return res;

    }

    private int calculateDist(int[] ro, int[] col) {
        int res = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            int[] cur = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                int[] next = galaxies.get(j);

                int dist = Math.abs(next[0] - cur[0]) + Math.abs(next[1] - cur[1]);
//                System.out.println(Arrays.toString(cur) + "->" + Arrays.toString(next)
//                        + ": " + dist);
                dist += getExtra(ro, cur[0], next[0]);
                dist += getExtra(col, cur[1], next[1]);
//                System.out.println(Arrays.toString(cur) + "->" + Arrays.toString(next)
//                + ": " + dist);
                res += dist;
            }
        }
        return res;

    }

    private int getExtra(int[] arr, int s, int e) {
        if (s > e) {
            return getExtra(arr, e, s);
        }

        int res = 0;
        for (int i = s; i <= e; i++) {
            if (arr[i] == 1) {
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D11");

        long start = System.currentTimeMillis();
        D11CosmicExpansion ans = new D11CosmicExpansion(input);

        long res = ans.distII((int)1e6);
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
