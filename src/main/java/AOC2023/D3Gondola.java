package AOC2023;

import Utils.FileUtil;

import java.util.List;

public class D3Gondola {

    int[] dirs = new int[]{-1, 0, 1, 0, -1, 1, 1, -1,-1};
    boolean[][] visited;
    int M, N;
    public int getSum(List<String> input) {
        // for each part symbol, check its 8 direction
        M = input.size();
        N = input.get(0).length();

        visited = new boolean[M][N];
        int res = 0;
        for (int i = 0; i < M; i++) {
            String line = input.get(i);
            for (int j = 0; j < N; j++) {
                if (!visited[i][j] && isSymbol(line.charAt(j))) {
                    res += checkNeighbors(input, i, j, visited, dirs);
                }
            }
        }
        return res;
    }

    public long sumGearRatio(List<String> input) {
        // for each * check if it is gear, then  if it is gear then calculate the numbers;
        // for each part symbol, check its 8 direction
        M = input.size();
        N = input.get(0).length();

//        visited = new boolean[M][N];
        long res = 0;
        for (int i = 0; i < M; i++) {
            String line = input.get(i);
            for (int j = 0; j < N; j++) {
                if (line.charAt(j) == '*') {
                    res += checkGear(input, i, j);
                }
            }
        }
        return res;
    }

    private int checkGear(List<String> input, int r, int c) {
        boolean[][] localVisited = new boolean[M][N];
        localVisited[r][c] = true;
        int res = 1;
        int num = 0;
        for (int i = 0; i < dirs.length - 1; i++) {
            int nr = r + dirs[i];
            int nc = c + dirs[i + 1];
            if (nr < 0 || nc < 0 || nr >= M || nc >= N || localVisited[nr][nc]) {
                continue;
            }

            char cur = input.get(nr).charAt(nc);
            if (isNumber(cur)) {
                if (num < 2) {
                    res *= getEntireNumber(input.get(nr), nr,nc, localVisited);
                    num++;
                } else {
                    return 0;
                }
            }
        }

        if (num == 2) return res;
        return 0;
    }

    private static int checkNeighbors(List<String> input, int r, int c, boolean[][] visited, int[] dirs) {
        int M = input.size();
        int N = input.get(0).length();
        visited[r][c] = true;
        int res = 0;
        for (int i = 0; i < dirs.length - 1; i++) {
            int nr = r + dirs[i];
            int nc = c + dirs[i + 1];
            if (nr < 0 || nc < 0 || nr >= M || nc >= N || visited[nr][nc]) {
                continue;
            }

            char cur = input.get(nr).charAt(nc);
            if (isNumber(cur)) {
                res += getEntireNumber(input.get(nr), nr,nc, visited);
            }
        }

        return res;
    }

    private static int getEntireNumber(String line, int row,  int col, boolean[][] visited) {
        // left
        int left = col;
        while(left >= 0 && isNumber(line.charAt(left))) {
            visited[row][left] = true;
            left--;
        }

        left++;
        int right=col;
        while(right < line.length() && isNumber(line.charAt(right))) {
            visited[row][right] = true;
            right++;
        }
        // right doesn't need to --;
        return Integer.parseInt(line.substring(left, right));
    }

    private boolean isSymbol(char c) {
        if (c - '0' >= 0 && c - '0' < 10) {
            return false;
        }

        if (c == '.') return false;
        return true;
    }

    private static boolean isNumber(char c) {
        return c - '0' >= 0 && c - '0' < 10;
    }


    public static void main(String[] args) {

        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D3");

        long start = System.currentTimeMillis();
        D3Gondola ans = new D3Gondola();
//        int res = ans.getSum(input);
        long res = ans.sumGearRatio(input);
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
