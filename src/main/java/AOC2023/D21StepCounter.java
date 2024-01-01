package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D21StepCounter {


    char[][] graph;
    int M, N;
    int si, sj;
    int[] dirs = new int[]{0, 1, 0, -1, 0};
    boolean[][][] visited;
    Map<Long, Long> visitedII = new HashMap<>();

    public D21StepCounter(List<String> input) {
        M = input.size();
        N = input.get(0).length();
        graph = new char[M][];
        for (int i = 0; i < M; i++) {
            graph[i] = input.get(i).toCharArray();
        }

        findSiSj();
    }

    private void findSiSj() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (graph[i][j] == 'S') {
                    si = i;
                    sj = j;
                }
            }
        }
    }

    public D21StepCounter(char[][] graph) {
        M = graph.length;
        N = graph[0].length;
        this.graph = graph;
        findSiSj();

    }

    public int numPlots(int step, int sx, int sy) {
        boolean[][] reached = new boolean[M][N];
        // he can go backwards!!!
        visited = new boolean[M][N][step + 1];
        if (sx < 0) sx = si;
        if (sy < 0) sy = sj;
        dfs(sx, sy, step, reached);


        int res = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (reached[i][j]) {
                    graph[i][j] = '0';
                    res++;
                }
            }
            System.out.println(Arrays.toString(graph[i]));
        }
        return res;

    }

    public int numPlotsII(int step, int sx, int sy) {
        boolean[][] reached = new boolean[M][N];
        // he can go backwards!!!
        if (sx < 0 || sy < 0) {
            sx = si;
            sy = sj;
        }
        visited = new boolean[M][N][step + 1];
        dfsII(sx, sy, step, reached);

//        char[][] temp = arrayCopy(graph);
//        for (var row : temp) {
//            System.out.println(Arrays.toString(row));
//        }
        System.out.println("\n");
        int res = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (reached[i][j]) {
//                    temp[i][j] = '0';
                    res++;
                }
            }
//            System.out.println(Arrays.toString(temp[i]));
        }
        return res;

    }

    private char[][] arrayCopy(char[][] graph) {
        char[][] res = new char[M][];
        for (int i = 0; i < M; i++) {
            res[i] = Arrays.copyOf(graph[i], N);
        }
        return res;
    }

    private char[][] arrayCopy(char[][] graph, int times) {
        char[][] res = new char[M * times][N * times];

        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j += N) {
                System.arraycopy(graph[i % M], 0, res[i], j, N);
            }
        }

        for (int i = si; i < res.length; i += M) {
            for (int j = sj; j < res[0].length; j += N) {
                if (i == si && j == sj) continue;
                if (res[i][j] == 'S') {
                    res[i][j] = '.';
                }
            }
        }
        return res;
    }

    private void dfs(int x, int y, int target, boolean[][] reached) {

        if (target == 0) {
            return;
        }
        visited[x][y][target] = true;
        for (int i = 0; i < dirs.length - 1; i++) {
            int nx = dirs[i] + x;
            int ny = dirs[i + 1] + y;

            if (nx < 0 || ny < 0 || nx >= M ||
                    ny >= N || graph[nx][ny] == '#' || visited[nx][ny][target - 1]) {
                // I have checked all its neigbours, no need to check again
                continue;
            }

            if (target % 2 == 0) {
                reached[x][y] = true;
            } else {
                reached[nx][ny] = true;
            }
            dfs(nx, ny, target - 1, reached);
        }

    }

    private void dfsII(int x, int y, int target, boolean[][] reached) {

        if (target == 0) {
            return;
        }
        int rx = (x + M) % M;
        int ry = (y + N) % N;
        visited[rx][ry][target] = true;
        for (int i = 0; i < dirs.length - 1; i++) {
            int nx = (dirs[i] + rx + M) % M;
            int ny = (dirs[i + 1] + ry + N) % N;

            if (graph[nx][ny] == '#' || visited[nx][ny][target - 1]) {
                // I have checked all its neigbours, no need to check again
                continue;
            }

            if (target % 2 == 0) {
//                System.out.println(rx + ":" + ry);
                reached[rx][ry] = true;
            } else {
//                System.out.println(nx + ":" + ny);
                reached[nx][ny] = true;
            }
            dfsII(nx, ny, target - 1, reached);
        }

    }

    private static void print2DGraph(char[][] g) {
        for (char[] r : g) {
            System.out.println(Arrays.toString(r));
        }
        System.out.println("\n");
    }

    public static long[][] findThree() {
        List<String> input1 = FileUtil.readStringLineByLine("2023Resource/2023D21");
        long[][] res = new long[4][];
        // f(65);
        var ans = new D21StepCounter(input1);
        int N = ans.N;
        long res1 = ans.numPlotsII(N / 2, -1, -1);
        res[0] = new long[]{N/ 2, res1};

        char[][] newGraph = ans.arrayCopy(ans.graph, 3);
        var ansII = new D21StepCounter(newGraph);
        long res2 = ansII.numPlotsII(N / 2 + N, -1, -1);
        res[1] = new long[]{N / 2 + N, res2};

        char[][] newGraphII = ans.arrayCopy(ans.graph, 5);
        var ansIII = new D21StepCounter(newGraphII);
        long res3 = ansIII.numPlotsII(N / 2 + 2 * N, -1, -1);
        res[2] = new long[]{N/2 + 2L * N, res3};

        char[][] newGraphIII = ans.arrayCopy(ans.graph, 7);
        var ansIV = new D21StepCounter(newGraphIII);
        long res4 = ansIV.numPlotsII(N / 2 + 3 * N, -1, -1);
        res[3] = new long[]{N/2 + 3L * N, res4};
        return res;
    }

    private static long nextPred(List<Long> hist) {
        Set<Long> diffs = new HashSet<>();
        List<Long> next = new ArrayList<>();
        long curDiff = 0;
        for (int i = 0; i < hist.size() - 1; i++) {
            curDiff = hist.get(i + 1) - hist.get(i);
            diffs.add(curDiff);
            next.add(curDiff);
        }

        if (diffs.size() == 1) {
            return hist.get(hist.size() - 1) + curDiff;
        }

        return nextPred(next) + hist.get(hist.size() - 1);
    }


    public static void main(String[] args) {
//        List<String> input2 = FileUtil.readStringLineByLine("2023Resource/2023D21_Sample");
        long start = System.currentTimeMillis();
        long[][] res = findThree();
        List<Long> hist = new ArrayList<>();
        for (long[] r : res) {
            hist.add(r[1]);
            System.out.println(Arrays.toString(r));
        }

        for (long i = res[3][0] + 131; i <= 26501365; i += 131 ) {
            long next = nextPred(hist);
//            System.out.println(next);
            hist.add(next);
            hist.remove(0);
        }

        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(hist.get(hist.size() - 1));

//        System.out.println(ans.numPlotsII(10, -1, -1));

    }
}
