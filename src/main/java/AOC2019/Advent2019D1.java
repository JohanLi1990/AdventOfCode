package AOC2019;

import Utils.FileUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Advent2019D1 {

    public int calculateFuel(int[] a) {
        return Arrays.stream(a)
                .map(k -> k / 3 - 2)
                .sum();
    }

    public int calculateFuel(List<Integer> list) {
        return list.stream()
                .map(k -> k / 3 - 2)
                .reduce(0, Integer::sum);
    }

    Map<Integer, Integer> map = new HashMap<>();
    public int calculateFuelII(List<Integer> list) {
       int res = 0;
        for (int a : list) {
            res += helper(a);
       }
        return res;
    }

    public int helper(int a) {
        if (map.containsKey(a)) return map.get(a);

        if (a / 3 - 2 <= 0) {
            return 0;
        }

        int res = a / 3 - 2 + helper(a / 3 - 2);
        map.putIfAbsent(a, res);
        return res;


    }

    public static void main(String[] args) {
        Advent2019D1 ans = new Advent2019D1();
        int[] eg1 = new int[] {12, 12, 31, 24};
        assert(ans.calculateFuel(eg1) == 18);
        List<Integer> input = FileUtil.readIntegerLineByLine("2019D1Input");
        System.out.println(ans.calculateFuel(input));

        List<Integer> l1 = List.of(100756);
        assert(ans.calculateFuelII(l1) == 50346);

        System.out.println(ans.calculateFuelII(input));


    }
}
