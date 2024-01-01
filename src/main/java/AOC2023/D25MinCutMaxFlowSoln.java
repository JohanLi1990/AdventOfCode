package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://github.com/p88h/aoc2023/blob/main/day25.py
 *
 * This is a Min Cut Max Flow problem,
 * Use Ford Fulkerson and Edmond Karp Algorithm
 */
public class D25MinCutMaxFlowSoln {

    Map<String, List<String>> graph;
    public D25MinCutMaxFlowSoln(List<String> input) {
        graph = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split(": ");
            String u = parts[0].trim();
            String[] vs = parts[1].split(" ");
            for (String v : vs) {
                String dest = v.trim();
                graph.computeIfAbsent(u, k -> new ArrayList<>()).add(dest);
                graph.computeIfAbsent(dest, k -> new ArrayList<>()).add(u);
            }
            // no connections yet, just simply insert node
        }
    }

    public List<String> bfs(String src, String tgt) {

        Map<String, String> prev = new HashMap<>();
        prev.put(src, "NA");
        List<String> stak = new ArrayList<>();
        stak.add(src);
        int sp = 0;
        while( sp < stak.size()) {
            String cur = stak.get(sp);
            sp++;
            if (cur.equals(tgt)) {
                break;
            }
            for (String dst : graph.get(cur)) {
                if (!prev.containsKey(dst)) {
                    prev.put(dst, cur);
                    stak.add(dst);
                }
            }
        }
        if (tgt == null) {
            tgt = stak.get(stak.size() - 1);
        } else if (!prev.containsKey(tgt)) {
            return stak;
        }

        List<String> path = new ArrayList<>();
        while(!"NA".equals(tgt)) {
            path.add(tgt);
            tgt = prev.get(tgt);
        }
        return path;
    }

    private void printList(List<String> curList) {
        for(String cur : curList) {
            System.out.print(cur + "=");
        }
        System.out.print("END");
        System.out.println("\n");
    }

    public int edmond_karp(int cnt, String src) {
        List<String[]> removed = new ArrayList<>();
        String pre = null;
        int res = 0;
        for (String tgt : graph.keySet()) {
            if (tgt.equals(src)) continue;
            for (int i = 0; i < cnt; i++) {
                List<String> path = bfs(src, tgt);
//                printList(path);
                tgt = pre = path.get(0);
                for (int j = 1; j < path.size(); j++) {
                    String cur = path.get(j);
                    graph.get(cur).remove(pre);
                    removed.add(new String[]{cur, pre});
                    pre = cur;
                }
            }
            // compute the reachable nodes in residual graph
            List<String> reachable = bfs(src, tgt);
//            printList(reachable);
            if (!reachable.contains(tgt)) {
                res = reachable.size();
                break;
            }

            for (String[] edge : removed) {
                graph.get(edge[0]).add(edge[1]);
            }
            removed.clear();
        }


        for (String[] edge : removed) {
            graph.get(edge[0]).add(edge[1]);
        }
        return res;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D25");

        long start = System.currentTimeMillis();
        var ans = new D25MinCutMaxFlowSoln(input);
        int len = ans.graph.size();
        String src = ans.graph.keySet().iterator().next();
        int res = ans.edmond_karp(3, src);
        res *= len - res;
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
