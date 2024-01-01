package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D23ALongWalk {


    char[][] graph;
    int M, N;
    int si, sj;
    int ei, ej;

    boolean[][] visiting;
    long[][] dp;
//    long[][][] dpII;

    Map<String, Long> dpII;
    int[] dirs = new int[]{0, 1, 0, -1, 0};
    Vertex[][] vertices;
    boolean[][] visited;
    long partIIres;

    public D23ALongWalk(List<String> input) {
        M = input.size();
        N = input.get(0).length();
        graph = new char[M][];
        for (int i = 0; i < M; i++) {
            graph[i] = input.get(i).toCharArray();
        }

        for (int i = 0; i < N; i++) {
            if (graph[0][i] == '.') {
                si = 0;
                sj = i;
            }

            if (graph[M - 1][i] == '.') {
                ei = M - 1;
                ej = i;
            }
        }

        vertices = new Vertex[M][N];
        vertices[si][sj] = new D23Vertex(si, sj);
        vertices[ei][ej] = new D23Vertex(ei, ej);
        visited = new boolean[M][N];

        for (int i =0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (isJunction(i, j)) {
                    vertices[i][j] = new D23Vertex(i, j);
                }
            }
        }

    }

    public long longestPath() {
        visiting = new boolean[M][N];
        dp = new long[M][N];

        return dfs(si, sj, ei, ej);
    }

    public long longestPathII() {
        visiting = new boolean[M][N];
//        dpII = new long[M][N][4];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (vertices[i][j] != null) {
                    bfs(i, j);
                }
            }
        }

        Vertex start = vertices[si][sj];
        dfsII(start, 0);
        return partIIres;
    }

    private void bfs(int x, int y) {
        visited = new boolean[M][N];
        Queue<int[]> q = new ArrayDeque<>();
        visited[x][y] = true;
        q.offer(new int[]{x, y});
        int step = 1;
        while(!q.isEmpty()) {
            int size = q.size();
            while(size-- > 0) {
                int[] cur = q.poll();
                for (int i = 0; i < dirs.length - 1; i++) {
                    int nx = cur[0] + dirs[i];
                    int ny = cur[1] + dirs[i +1];
                    if (nx >= 0 && ny >= 0 && nx < M && ny < N && graph[nx][ny] != '#' && !visited[nx][ny]) {
                        visited[nx][ny] = true;
                        if (vertices[nx][ny] != null) {
                            vertices[x][y].children.add(new D23Edge(vertices[nx][ny], step));
                        } else {
                            q.offer(new int[]{nx, ny});
                        }
                    }
                }
            }
            step++;
        }
    }


    private boolean isJunction(int x, int y) {
        int num = 0;

        for (int i = 0; i < dirs.length - 1; i++) {
            int nx = x + dirs[i];
            int ny = y + dirs[i + 1];
            if (nx <0 || ny <  0 || nx >= M || ny >= N || graph[nx][ny] == '#') continue;
            num++;
            if(num >= 3) return true;
        }
        return  false;
    }
    private void dfsII(Vertex cur, long step) {
        if (cur == vertices[ei][ej]) {
            partIIres = Math.max(partIIres, step);
            return;
        }

        visiting[cur.x][cur.y] = true;
        for (IEdge edge : cur.children) {
            int[] curCord = edge.getCordinates();
            if (!visiting[curCord[0]][curCord[1]]) {
                dfsII(edge.getVertex(), step + edge.getWeight());
            }
        }
        visiting[cur.x][cur.y] = false;


    }


    private long dfs(int curx, int cury, int destx, int desty) {
        if (curx == destx && cury == desty) {
            return 0;
        }

        if (dp[curx][cury] > 0) {
            return dp[curx][cury];
        }

        visiting[curx][cury] = true;
        char curtile = graph[curx][cury];
        long curRes = Integer.MIN_VALUE;
        if (curtile != '.') {
            int d = getDir(curtile);
            int nx = curx + dirs[d];
            int ny = cury + dirs[d + 1];
            if (canVisit(nx, ny)) {
                curRes = Math.max(curRes, dfs(nx, ny, destx, desty));
            }
        } else {
            for (int i = 0; i < dirs.length - 1; i++) {
                int nx = curx + dirs[i];
                int ny = cury + dirs[i + 1];
                if (!canVisit(nx, ny)) {
                    continue;
                }
                curRes = Math.max(curRes, dfs(nx, ny, destx, desty));
            }
        }

        if (curRes != Integer.MIN_VALUE) {
            curRes += 1;
        }

        visiting[curx][cury] = false;
        return dp[curx][cury] = curRes;
    }

    private boolean canVisit(int x, int y) {
        return x >= 0 && y >= 0 && x < M && y < N && !visiting[x][y] && !visited[x][y]
                && graph[x][y] != '#';
    }

    private int getDir(char c) {
        if (c == '>') {
            return 0;
        } else if (c == 'v') {
            return 1;
        } else if (c == '<') {
            return 2;
        } else {
            return 3;
        }
    }


    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D23");

        long start = System.currentTimeMillis();
        D23ALongWalk ans = new D23ALongWalk(input);

        long res = ans.longestPathII();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class D23Edge implements IEdge {

    Vertex u;
    int val;

    public D23Edge(Vertex u, int val) {
        this.u = u;
        this.val = val;
    }

    @Override
    public long getWeight() {
        return val;
    }

    @Override
    public int[] getCordinates() {
        return new int[]{u.x, u.y};
    }

    @Override
    public Vertex getVertex() {
        return this.u;
    }
}

class D23Vertex extends Vertex{

    public D23Vertex(int x, int y) {
        super(x, y);
    }
}