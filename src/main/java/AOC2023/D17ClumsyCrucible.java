package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D17ClumsyCrucible {

    int[][] A;
    int M, N;

    int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    public D17ClumsyCrucible(List<String> input) {
        M = input.size();
        N = input.get(0).length();

        A = new int[M][N];
        for (int i = 0; i < M; i++) {
            String cur = input.get(i);
            for (int j = 0; j < cur.length(); j++) {
                A[i][j] = cur.charAt(j) - '0';
            }
        }
    }

    public long minHeatLoss(int minStreak, int maxStreak) {

        // bfs with priority queue,
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a.score));
        // dist from source, int i, int j, int dir, int block
        pq.offer(new State(new int[]{0, 0}, 0, 0, 0));
        pq.offer(new State(new int[]{0, 0}, 0, 1, 0));
        Map<Long, Long> cost = new HashMap<>();
        cost.put(getState(0, 0, 0, 1), 0L);
        cost.put(getState(0, 0, 1, 1), 0L);

        while(!pq.isEmpty()) {

            State cur = pq.poll();
            if (cur.loc[0] == M - 1 && cur.loc[1] == N - 1 && cur.streak >= minStreak) {
                return cur.score;
            }

            int x = cur.loc[0], y = cur.loc[1];

            for (int i = 0; i < 4; i++) {
                int nextStreak;
                if (i == ((cur.dir) + 2) % 4) continue; // cannot reverse
                if (cur.dir == i) {
                    if (cur.streak + 1 > maxStreak) continue;
                    nextStreak = cur.streak + 1;

                } else {
                    if (cur.streak < minStreak) continue;
                    nextStreak = 1;
                }

                int nx = x + dirs[i][0];
                int ny = y + dirs[i][1];
                if (nx < 0 || ny < 0 || nx >= M || ny >= N) continue;

                long nextScore = A[nx][ny] + cur.score;
                long nextHash = getState(nx, ny, i, nextStreak);
                if (nextScore < cost.getOrDefault(nextHash, (long)Integer.MAX_VALUE)) {
                    cost.put(nextHash, nextScore);
                    pq.offer(new State(new int[]{nx, ny}, nextStreak, i, nextScore));
                }
            }
        }
        return -1;
    }


    private long getState(int i, int j, int curD, int curBlock) {
        long res = i;
        res = res * 443 + j;
        res = res * 443+ curD;
        res = res * 443 + curBlock;
        return res;
    }


    public static void main(String[] args) {

        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D17");

        long start = System.currentTimeMillis();
        var ans = new D17ClumsyCrucible(input);

        long res = ans.minHeatLoss(4, 10);
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class State {
    int[] loc;
    int streak;
    int dir;
    long score;

    public State(int[] l, int s, int d, long score) {
        this.loc = l;
        this.streak = s;
        this.dir = d;
        this.score = score;
    }

}
