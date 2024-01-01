package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class D5Fertilizer {

    List<Long> seeds;
    List<List<Range>> entireMap = new ArrayList<>();

    Predicate<String> startWithNumber = k -> {
        char c = k.charAt(0);
        return (c - '0') >= 0 && (c - '0') < 10;
    };


    public D5Fertilizer(List<String> input) {
        // initialize the real input
        List<Range> cur = new ArrayList<>();
        for (String line : input) {
            if (line.isBlank() || line.isEmpty()) continue;
            if (line.startsWith("seeds:")) {
                seeds = getAllSeed(line);
            } else if (startWithNumber.test(line)) {
                cur.add(convertToRange(line));
            } else if (!cur.isEmpty()) {
                cur.sort(Comparator.comparingLong(a -> a.source));
                entireMap.add(new ArrayList<>(cur));
                cur = new ArrayList<>();
            }
        }
        if (!cur.isEmpty()) {
            entireMap.add(new ArrayList<>(cur));
        }
    }

    private Range convertToRange(String line) {
        line = line.trim();
        String[] numbers = line.split(" ");
        return new Range(Long.parseLong(numbers[1]), Long.parseLong(numbers[0])
                , Long.parseLong(numbers[2]));
    }


    private List<Long> getAllSeed(String line) {

        String[] splits = line.substring(7).split(" ");
        var res = new ArrayList<Long>();
        for (String seed : splits) {
            if (seed.isEmpty() || seed.isBlank()) continue;
            res.add(Long.parseLong(seed));
        }
        return res;
    }

    public long getLowestLoc() {
        long res = Long.MAX_VALUE;

        for (long seed : seeds) {
            res = Math.min(traverseMap(seed), res);
        }
        return res;
    }

    public long getLowestLocII() {
        long res = Long.MAX_VALUE;
        for (int i = 0; i < seeds.size(); i += 2) {
            long[] interval = new long[]{seeds.get(i), seeds.get(i) + seeds.get(i + 1) - 1};
            res = Math.min(res,  dfs(0, interval));
        }
        return res;
    }

    private long dfs(int level, long[] interval) {
        if (level == entireMap.size()) return interval[0];
        List<Range> curMap = entireMap.get(level);
//        System.out.println("====" + Arrays.toString(interval) + "=====");
        long res = Long.MAX_VALUE;
        long[] temp = new long[]{interval[0], interval[1]};
        for (Range cur : curMap) {
            // use recursion
            if (temp[0] > temp[1] || cur.source > temp[1]) break;
            if (cur.source + cur.coverage - 1 < temp[0]) continue;
            // overlap
            if (temp[0] < cur.source) {
                // a part is not mapped
                long curRes = dfs(level + 1, new long[]{temp[0], cur.source - 1});
                temp[0] = cur.source;
                res = Math.min(res, curRes);
            }

            long[] next = new long[]{temp[0], Math.min(temp[1], cur.source + cur.coverage - 1)};
            next[0] += cur.destination - cur.source;
            next[1] += cur.destination - cur.source;
            res = Math.min(res, dfs(level + 1, next));

            temp[0] = Math.min(temp[1], cur.source+ cur.coverage - 1) + 1;
        }

        if (temp[0] <= temp[1]) {
            res = Math.min(res, dfs(level + 1, temp));
        }
        return res;
    }

    private long traverseMap(long num) {
        long res = num;
//        System.out.println("==================");
        for (List<Range> map : entireMap) {
            // Range are ordered based on source
            Range closest = bSearch(map, res);
            if (res >= closest.source && closest.source + closest.coverage >= res) {
                res = closest.destination + (res - closest.source);
            }
        }
        return res;
    }


    private Range bSearch(List<Range> list, long num) {
        int s = 0, e = list.size() - 1;

        while (s < e - 1) {
            int mid = s + (e - s) / 2;
            if (list.get(mid).source > num) {
                e = mid - 1;
            } else {
                s = mid;
            }
        }
        if (e < 0) return list.get(s);

        return list.get(e).source <= num ? list.get(e) : list.get(s);
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D5");
        long start = System.currentTimeMillis();
        var ans = new D5Fertilizer(input);
//        int res = ans.getSum(input);
        long res = ans.getLowestLocII();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class Range {
    long source, destination, coverage;

    public Range(long s, long e, long cov) {
        source = s;
        destination = e;
        coverage = cov;
    }

    @Override
    public String toString() {
        return "Range[source: " + source + " dest: " + destination + " cover: " + coverage + "]";
    }
}
