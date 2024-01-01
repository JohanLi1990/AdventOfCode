package AOC2023;

import Utils.FileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D1Trebuchet {

    String[] patterns = new String[]{"one", "two"
    , "three", "four", "five", "six", "seven", "eight", "nine", "1", "2", "3",
    "4", "5", "6", "7", "8", "9", "0"};

    Map<String, Integer> map = new HashMap<>();
    Map<String, Integer> rev = new HashMap<>();

    {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("five", 5);
        map.put("six", 6);
        map.put("seven", 7);
        map.put("eight", 8);
        map.put("nine", 9);
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        map.put("5", 5);
        map.put("6", 6);
        map.put("7", 7);
        map.put("8", 8);
        map.put("9", 9);
        map.put("0", 0);
        for (Map.Entry<String, Integer> ent : map.entrySet()) {
            rev.put(new StringBuilder(ent.getKey()).reverse().toString(), ent.getValue());
        }
    }
    public int getSum(List<String> input) {
        int res = 0;
        for (String line : input) {
            int[] num = getFrontAndBack(line);
            res += 10 * num[0] + num[1];
        }
        return res;
    }

    public int getSumII(List<String> input) {
        int res = 0;

        for (String line : input) {
            res += getFrontAndBackII(line);
        }
        return res;
    }

    private int getFrontAndBackII(String line) {
        int index = Integer.MAX_VALUE;
        int res = 0;
        for (Map.Entry<String, Integer> ent : map.entrySet()) {
            int idx = line.indexOf(ent.getKey());
            if (idx != -1 && idx < index) {
                index = idx;
                res = ent.getValue();
            }
        }

        index = Integer.MAX_VALUE;
        int sec = 0;
        String reverse = new StringBuilder(line).reverse().toString();
        for (Map.Entry<String, Integer> ent : rev.entrySet()) {
            int idx = reverse.indexOf(ent.getKey());
            if (idx != -1 && idx < index) {
                index = idx;
                sec = ent.getValue();
            }
        }
//        System.out.println(line + ":" + "front: " + res + " back: " + sec);
        return res * 10 + sec;
    }

    private int[] getFrontAndBack(String line) {
        int[] res = new int[2];
        char[] arr = line.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] - '0' >= 0 && arr[i] - '0' < 10) {
                res[0] = arr[i] - '0';
                break;
            }
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] - '0' >= 0 && arr[i] - '0' < 10) {
                res[1] = arr[i] - '0';
                break;
            }
        }
        return res;




    }

    public static void main(String[] args) {

        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D1");
        long start = System.currentTimeMillis();
        D1Trebuchet ans = new D1Trebuchet();
//        System.out.println(ans.getSum(input));
        int res = ans.getSumII(input);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(res);
    }
}
