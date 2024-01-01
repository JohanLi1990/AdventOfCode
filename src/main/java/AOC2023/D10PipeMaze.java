package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D10PipeMaze {

    char[][] maze;
    int si, sj;
    int M, N;
    boolean[][] visited;
    //u, r, d, l
    private static final int[] dirs = new int[]{-1, 0, 1, 0, -1};
    private static final List<Set<Character>> patterns = new ArrayList<>();

    static {
        //        patterns.add("F7|");
        patterns.add(Set.of('F', '7', '|'));
        //        patterns.add("J7-");
        patterns.add(Set.of('J', '7', '-'));
        //        patterns.add("JL|");
        patterns.add(Set.of('J', 'L', '|'));
        //        patterns.add("LF-");
        patterns.add(Set.of('L', 'F', '-'));

    }

    public D10PipeMaze(List<String> input) {
        M = input.size();
        N = input.get(0).length();
        maze = new char[M][];
        for (int i = 0; i < M; i++) {
            maze[i] = input.get(i).toCharArray();
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (maze[i][j] == 'S') {
                    si = i;
                    sj = j;
                    return;
                }
            }
        }

    }

    public int getFurthestPoint() {
        // cannot use bfs, because u don't know how the loop looks like
        visited = new boolean[M][N];
        visited[si][sj] = true;
        return bfs(si, sj);
    }

    public int getArea() {
        visited = new boolean[M][N];
        visited[si][sj] = true;
        bfs(si, sj);
        dealWithS(maze);

        int res = 0;
        for (int i = 0; i < M; i++) {
            flood(i, 0);
            flood(i, N - 1);
        }

        for (int i = 0; i < N; i++) {
            flood(0, i);
            flood(M - 1, i);
        }

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (maze[i][j] != '0' && !visited[i][j] ) {
                    if (isWithin(i, j)) {
                        res += count(i, j);
                    }
                }
            }
        }

        return res;

    }

    private int count(int x, int y) {

        if (x < 0 || y < 0 || x >= M || y >= N || visited[x][y]
        || maze[x][y] == '1') return 0;
        maze[x][y] = '1';
        return 1 + count(x - 1, y)
                + count(x + 1, y)
                + count(x, y + 1)
                + count(x, y - 1);
    }

    private boolean isWithin(int x, int y) {

        // move to the right
        int j = y + 1;

//        char pre = maze[x][y];
        int cross = 0;
        while(j < N) {
            if (!visited[x][j]) {
                j++;
                continue;
            }

            if (maze[x][j] == '|') {
                cross++;
            } else if (maze[x][j] == 'L') {
                j++;
                while(j < N && maze[x][j] == '-'){
                    j++;
                }
                if (j < N && maze[x][j] == '7') {
                    cross++;
                }
            } else if (maze[x][j] == 'F') {
                j++;
                while(j < N && maze[x][j] == '-'){
                    j++;
                }
                if (j < N && maze[x][j] == 'J') {
                    cross++;
                }
            }
            j++;
        }
        return cross % 2 == 1;
    }

    private void dealWithS(char[][] A) {
        int res = 0;
        for (int i = 0; i < dirs.length - 1; i++) {
            int nx = si + dirs[i];
            int ny = sj + dirs[i + 1];
            if (nx < 0 || ny < 0 || nx >= M || ny >= N || !visited[nx][ny]) continue;
            Set<Character> pattern = patterns.get(i);
            if (pattern.contains(maze[nx][ny])) {
                res = res * 10 + (i + 1);
            }
        }
        //u, r, d, l
        if (res == 12) {
            A[si][sj] = 'L';
        } else if (res == 23) {
            A[si][sj] = 'F';
        } else if (res == 34) {
            A[si][sj] = '7';
        } else if (res == 13) {
            A[si][sj] = '|';
        } else if (res == 14) {
            A[si][sj] = 'J';
        } else if (res == 24) {
            A[si][sj] = '-';
        }
    }


    private void flood(int x, int y) {
        if (x < 0 || y < 0 || x >= M || y >= N || maze[x][y] == '0'
        || visited[x][y]) return;
        maze[x][y] = '0';
        flood(x + 1, y);
        flood(x - 1, y);
        flood(x, y + 1);
        flood(x, y - 1);
    }

    private int bfs(int x, int y) {
        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[]{x, y});

        int step = 0;
        while (!q.isEmpty()) {
            int size = q.size();

            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                char curPipe = maze[cur[0]][cur[1]];
                for (int j = 0; j < dirs.length - 1; j++) {
                    int nx = cur[0] + dirs[j];
                    int ny = cur[1] + dirs[j + 1];
                    var curPattern = patterns.get(j);

                    if (nx < 0 || ny < 0 || nx >= M || ny >= N || visited[nx][ny]
                            || !curPattern.contains(maze[nx][ny]) || !isConnecting(curPipe, j)) {
                        continue;
                    }

                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny});
                }
            }
            step++;
        }
        return step - 1;
    }

    private boolean isConnecting(char c, int j) {
        //u, r, d, l
        if (c == '|') {
            return j == 0 || j == 2;
        } else if (c == '-') {
            return j == 1 || j == 3;
        } else if (c == 'L') { // l, d
            return j == 0 || j == 1;
        } else if (c == 'J') { // d, r
            return j == 0 || j == 3;
        } else if (c == 'F') {
            // r, d
            return j == 1 || j == 2;
        } else if (c == '7') {
            //l, d
            return j == 2 || j == 3;
        } else return c == 'S';
    }


    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D10");

        long start = System.currentTimeMillis();
        D10PipeMaze ans = new D10PipeMaze(input);
//        long res = ans.totalSteps("AAA", "ZZZ");
        long res = ans.getArea();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
