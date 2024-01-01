package AOC2023;

import Utils.FileUtil;

import java.util.Arrays;
import java.util.List;

public class D16FloorLava {

    char[][] tiles;
    int M, N;
    int[][] dirs = new int[][]{{0, 1}, {-1, 0}, {0, -1}, {1, 0}}; //R U L D
    boolean[][][] visited;

    public D16FloorLava(List<String> input) {
        M = input.size();
        N = input.get(0).length();

        tiles = new char[M][];
        int index = 0;
        for (String line : input) {
            tiles[index++] = line.toCharArray();
        }
    }

    public int getNumberOfEnergizedTiles() {
        visited = new boolean[M][N][4];

        dfs(0, 0, 0);

        int res = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[i][j][0] || visited[i][j][1]
                        || visited[i][j][2] || visited[i][j][3]) {
                    res++;
                }
            }
        }
        return res;
    }

    public int getMaxEnergized() {

        int res = 0;
        for (int i = 0; i < M; i++) {
            // from two sides
            visited = new boolean[M][N][4];
            res = Math.max(res, dfsII(0, 0, 0, new boolean[M][N]));
            visited = new boolean[M][N][4];
            res = Math.max(res, dfsII(i, N - 1, 2, new boolean[M][N]));
        }

        for (int j = 0; j < N; j++) {
            visited = new boolean[M][N][4];
            res = Math.max(res, dfsII(0, j, 3, new boolean[M][N]));
            visited = new boolean[M][N][4];
            res = Math.max(res, dfsII(M - 1, j, 1, new boolean[M][N]));
        }
        return res;
    }

    private int dfsII(int i, int j, int curD, boolean[][] path) {
        if (i < 0 || j < 0 || i >= M || j >= N || visited[i][j][curD]) {
            return 0;
        }
        visited[i][j][curD] = true;
        int curval = path[i][j] ? 0 : 1;

        path[i][j] = true;
        char cur = tiles[i][j];
        if (cur == '\\') {
            int newD;
            int[] nextD;
            if (curD == 0 || curD == 2) {
                newD = (curD - 1 + 4) % 4;
            } else {
                newD = (curD + 1 + 4) % 4;
            }
            nextD = dirs[newD];
            return curval + dfsII(i + nextD[0], j + nextD[1], newD, path);
        } else if (cur == '/') {
            int newD;
            int[] nextD;
            if (curD == 0 || curD == 2) {
                newD = (curD + 1 + 4) % 4;
            } else {
                newD = (curD - 1 + 4) % 4;
            }
            nextD = dirs[newD];
            return curval + dfsII(i + nextD[0], j + nextD[1], newD, path);
        } else if (cur == '|' && (curD == 0 || curD == 2)) {
            return curval + dfsII(i + 1, j, 3, path) + dfsII(i - 1, j, 1, path);
        } else if (cur == '-' && (curD == 1 || curD == 3)) {
            return curval + dfsII(i, j - 1, 2, path) + dfsII(i, j + 1, 0, path);
        } else {
            return curval + dfsII(i + dirs[curD][0], j + dirs[curD][1], curD, path);
        }
    }

    private void dfs(int i, int j, int curD) {
        if (i < 0 || j < 0 || i >= M || j >= N || visited[i][j][curD]) {
            return;
        }
        visited[i][j][curD] = true;
        char cur = tiles[i][j];
        if (cur == '\\') {
            int newD;
            int[] nextD;
            if (curD == 0 || curD == 2) {
                newD = (curD - 1 + 4) % 4;
            } else {
                newD = (curD + 1 + 4) % 4;
            }
            nextD = dirs[newD];
            dfs(i + nextD[0], j + nextD[1], newD);
        } else if (cur == '/') {
            int newD;
            int[] nextD;
            if (curD == 0 || curD == 2) {
                newD = (curD + 1 + 4) % 4;
            } else {
                newD = (curD - 1 + 4) % 4;
            }
            nextD = dirs[newD];
            dfs(i + nextD[0], j + nextD[1], newD);
        } else if (cur == '|' && (curD == 0 || curD == 2)) {
            dfs(i + 1, j, 3);
            dfs(i - 1, j, 1);
        } else if (cur == '-' && (curD == 1 || curD == 3)) {
            dfs(i, j - 1, 2);
            dfs(i, j + 1, 0);
        } else {
            dfs(i + dirs[curD][0], j + dirs[curD][1], curD);
        }
    }


    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D16");

        long start = System.currentTimeMillis();
        var ans = new D16FloorLava(input);

        long res = ans.getMaxEnergized();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
