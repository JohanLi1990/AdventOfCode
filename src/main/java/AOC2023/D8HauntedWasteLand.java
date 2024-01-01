package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D8HauntedWasteLand {

    Map<String, List<String>> graph = new HashMap<>();
    String instruction;
    public D8HauntedWasteLand(List<String> input) {
        for (String line : input) {
            if (line.isEmpty() || line.isBlank()) continue;
            if (instruction == null) {
                instruction = line;
            } else {
                populateGraph(line);
            }
        }
    }

    public long totalSteps(String start, String end) {
        int step = 0;
        String cur = start;

        while(!cur.endsWith(end)) {
            char instr = instruction.charAt(step++ % instruction.length());
            if (instr == 'R') {
                cur = graph.get(cur).get(1);
            } else {
                cur = graph.get(cur).get(0);
            }
        }
        return step;
    }

    public long totalStepsII() {
        Set<Long> curSet = new HashSet<>();

        for (Map.Entry<String, List<String>> ent : graph.entrySet()) {
            if (ent.getKey().endsWith("A")){
                curSet.add(totalSteps(ent.getKey(), "Z"));
            }
        }
        return getLCM(curSet);
    }


    private long gcd(long a, long b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    private long getLCM(Set<Long> set) {
        List<Long> list = new ArrayList<>(set);
        for (int i = 0; i < list.size() - 1; i++) {
            long cur = list.get(i);
            long next = list.get(i + 1);
            long curLCM = (cur * next) / gcd(cur, next);
            list.set(i + 1, curLCM);
        }
        return list.get(list.size() - 1);
    }

    private boolean isEnd(Set<String> set) {
        for (String a : set) {
            if (!a.endsWith("Z")) return false;
        }
        return true;
    }

    private void populateGraph(String line) {
        String[] ve = line.split("=");
        ve[0] = ve[0].trim();
        ve[1] = ve[1].trim();
        String[] dests = ve[1].split(", ");
        dests[0] = dests[0].substring(1);
        dests[1] = dests[1].substring(0, dests[1].length() - 1);
        graph.computeIfAbsent(ve[0], k -> new ArrayList<>()).addAll(List.of(dests));
        graph.computeIfAbsent(dests[0], k -> new ArrayList<>());
        graph.computeIfAbsent(dests[1], k -> new ArrayList<>());
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D8");

        long start = System.currentTimeMillis();
        D8HauntedWasteLand ans = new D8HauntedWasteLand(input);
//        long res = ans.totalSteps("AAA", "ZZZ");
        long res = ans.totalStepsII();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
