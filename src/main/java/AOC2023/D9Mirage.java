package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D9Mirage {

    List<List<Long>> listOfHistory = new ArrayList<>();
    public D9Mirage(List<String> input) {
        for (String line : input) {
            List<Long> curlist = new ArrayList<>();
            String[] inputs = line.split(" ");
            for (String val : inputs) {
                if (val.isBlank() || val.isEmpty()) continue;
                curlist.add(Long.parseLong(val.trim()));
            }
            listOfHistory.add(new ArrayList<>(curlist));
        }
    }

    public long getPredicationSum() {
        long res = 0;
        for (List<Long> hist : listOfHistory) {
            res += dfs(hist);
        }
        return res;
    }

    public long getPredictionSumII() {
        long res = 0;
        for (List<Long> hist : listOfHistory) {
            res += dfsII(hist);
        }
        return res;
    }

    private long dfs(List<Long> hist) {
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

        return dfs(next) + hist.get(hist.size() - 1);
    }

    private long dfsII(List<Long> hist) {
        Set<Long> diffs = new HashSet<>();
        List<Long> next = new ArrayList<>();
        long curDiff = 0;
        for (int i = 0; i < hist.size() - 1; i++) {
            curDiff = hist.get(i + 1) - hist.get(i);
            diffs.add(curDiff);
            next.add(curDiff);
        }

        if (diffs.size() == 1) {
            return hist.get(0) - curDiff;
        }

        return hist.get(0) - dfsII(next);
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D9");

        long start = System.currentTimeMillis();
        D9Mirage ans = new D9Mirage(input);
//        long res = ans.totalSteps("AAA", "ZZZ");
        long res = ans.getPredictionSumII();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
