package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class D6WaitForIt {

    List<Integer> time = new ArrayList<>();
    List<Integer> records = new ArrayList<>();

    long timeII, recordII;
    public D6WaitForIt(List<String> input) {
        String timeString = input.get(0).split(":")[1];
        StringBuilder timeIIString = new StringBuilder();
        StringBuilder recordIIString = new StringBuilder();
        for (String timeInput : timeString.split(" ")) {
            if (timeInput.isEmpty() || timeInput.isBlank()) continue;
            time.add(Integer.parseInt(timeInput));
            timeIIString.append(timeInput);
        }

        String distanceInput = input.get(1).split(":")[1];
        for (String dist : distanceInput.split(" ")) {
            if (dist.isBlank() || dist.isEmpty()) continue;
            records.add(Integer.parseInt(dist));
            recordIIString.append(dist);
        }

        timeII = Long.parseLong(timeIIString.toString());
        recordII = Long.parseLong(recordIIString.toString());
    }

    public long totalWaysII() {
        return countWaysII(timeII, recordII);
    }

    private long countWaysII(long time, long record) {
        long lo = 0, hi = (time + 1) / 2;
        while(lo < hi) {
            long mi = lo + (hi - lo) / 2;

            long curDist = (time - mi) * mi;
            if (curDist <= record) {
                lo = mi + 1;
            } else {
                hi = mi;
            }
        }
        // lower bound, mirror image with the t / 2
        return time - 2 * lo + 1;
    }

    public int totalWaysToBeatRecord() {
        // s = x * (t - x)
        // xmax = t / 2; (dy/dx = 0)
        int res = 1;
        for (int i = 0; i < time.size(); i++) {
            int curTime = time.get(i);
            int curRecord = records.get(i);
            res *= countWays(curTime, curRecord);
        }
        return res;
    }

    private int countWays(int time, int dist) {
        // binary search

        int lo = 0, hi = (time + 1) / 2;
        while(lo < hi) {
            int mi = lo + (hi - lo) / 2;

            int curDist = (time - mi) * mi;
            if (curDist <= dist) {
                lo = mi + 1;
            } else {
                hi = mi;
            }
        }
        // lower bound, mirror image with the t / 2
        return time - 2 * lo + 1;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D6");

        long start = System.currentTimeMillis();
        D6WaitForIt ans = new D6WaitForIt(input);
//        int res = ans.getSum(input);
//        long res = ans.totalWaysToBeatRecord();
        long res = ans.totalWaysII();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
