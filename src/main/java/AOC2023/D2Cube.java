package AOC2023;

import Utils.FileUtil;

import java.util.List;

public class D2Cube {

    public int checkConstrain(List<String> input, int[] constraint) {
        int res = 0;
        int index = 1;
        for (String line : input) {
            String[] headers = line.split(":");
            String cur = headers[1];
            String[] allSets = cur.split(";");
            if (isValid(allSets, constraint)) {
                res += index;
            }
            index++;
        }
        return res;

    }

    public int getPower(List<String> input) {
        int res = 0;
        for (String line : input) {
            String[] headers = line.split(":");
            String cur = headers[1];
            String[] allSets = cur.split(";");
            res += minPower(allSets);
        }
        return res;
    }

    private int minPower(String[] as) {
        int red = 0, green = 0 ,blue = 0;
        for (String set : as) {
            String[] reveals = set.split(",");
            for (String reveal : reveals) {
                reveal = reveal.trim();
//                System.out.println(reveal);
                if (reveal.contains("red")) {
                    int lastIndex = reveal.indexOf("red");
                    int curNum = Integer.parseInt(reveal.substring(0, lastIndex - 1));
                    red = Math.max(red, curNum);
                } else if (reveal.contains("green")) {
                    int lastIndex = reveal.indexOf("green");
                    int curNum = Integer.parseInt(reveal.substring(0, lastIndex - 1));
                    green = Math.max(green, curNum);
                } else {
                    int lastIndex = reveal.indexOf("blue");
                    int curNum = Integer.parseInt(reveal.substring(0, lastIndex - 1));
                    blue = Math.max(blue, curNum);
                }
            }
        }
        return red * blue * green;

    }

    private boolean isValid(String[] A, int[] cons) {
        for (String set : A) {
            String[] reveals = set.split(",");
            for (String reveal : reveals) {
                reveal = reveal.trim();
//                System.out.println(reveal);
                if (reveal.contains("red")) {
                    int lastIndex = reveal.indexOf("red");
                    int curNum = Integer.parseInt(reveal.substring(0, lastIndex - 1));
                    if (curNum > cons[0]) return false;
                } else if (reveal.contains("green")) {
                    int lastIndex = reveal.indexOf("green");
                    int curNum = Integer.parseInt(reveal.substring(0, lastIndex - 1));
                    if (curNum > cons[1]) return false;
                } else {
                    int lastIndex = reveal.indexOf("blue");
                    int curNum = Integer.parseInt(reveal.substring(0, lastIndex - 1));
                    if (curNum > cons[2]) return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D2");
        long start = System.currentTimeMillis();
        D2Cube ans = new D2Cube();
        int[] cons = new int[]{12, 13, 14};
//        System.out.println(ans.getSum(input));
//        int res = ans.checkConstrain(input, cons);
        int res = ans.getPower(input);
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
