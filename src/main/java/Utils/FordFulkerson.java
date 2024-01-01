package Utils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * If use BFS then it is called Edmond Karp, otherwise FF
 */
public class FordFulkerson {
    final int V;

    // Using BFS as a searching algorithm
    public FordFulkerson(int v) {
        this.V = v;
    }
    boolean bfs(int[][] Graph, int s, int t, int[] p) {
        boolean[] visited = new boolean[V];
        Queue<Integer> q = new LinkedList<>();
        q.offer(s);
        visited[s] = true;
        p[s] = -1;
        while(!q.isEmpty()) {
            int u = q.poll();
            for (int v = 0; v < V; v++) {
                if (!visited[v] && Graph[u][v] > 0) {
                    q.offer(v);
                    p[v] = u;
                    visited[v] = true;
                }
            }
        }

        return visited[t];
    }

    // Applying FordFulkerson Algorithm

    int fordFulkerson(int[][] graph, int s, int t) {
        int u, v;
        int[][] rGraph = new int[V][V];
        for (int i = 0; i < rGraph.length; i++) {
            System.arraycopy(graph[i], 0, rGraph[i], 0, rGraph[0].length);
        }

        int[] parent = new int[V];
        int max_flow = 0;

        // Updating the residual graph
        while(bfs(rGraph, s, t, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
            }

            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }
            max_flow += pathFlow;
        }

        return max_flow;
    }

    public static void main(String[] args) {
        int[][] graph = new int[][] {
                { 0, 16, 13, 0, 0, 0 }, { 0, 0, 10, 12, 0, 0 },
                { 0, 4, 0, 0, 14, 0 },  { 0, 0, 9, 0, 0, 20 },
                { 0, 0, 0, 7, 0, 4 },   { 0, 0, 0, 0, 0, 0 }
        };
        var m = new FordFulkerson(6);

        System.out.println("The maximum possible flow is "
                + m.fordFulkerson(graph, 0, 5));
    }
}
