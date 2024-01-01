package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class D13PointOfIncurrence {

    List<List<String>> patterns = new ArrayList<>();

    public D13PointOfIncurrence(List<String> input) {
        // for each row and column identify palindrome
        List<String> cur = new ArrayList<>();
        for (String line : input) {
            if (!line.isEmpty() && !line.isBlank()) {
                cur.add(line);
            } else {
                patterns.add(new ArrayList<>(cur));
                cur = new ArrayList<>();
            }
        }

        patterns.add(new ArrayList<>(cur));
    }

    public long summarizePatternII() {
        long res = 0;
        for (List<String> pat : patterns) {
            int cur = calculateRowWithOneSmudge(pat);
            if (cur != 0) {
                res += cur;
            } else {
                res += calculateColWithOneSmudge(pat);
            }
        }
        return res;
    }

    private int calculateColWithOneSmudge(List<String> pat) {

        int N = pat.get(0).length();
        for (int j = 0; j < N - 1; j++) {
            int diff = 0;
            for (String line : pat) {
                int x = j, y = j + 1;
                while(x >= 0 && y < N) {
                    if (line.charAt(x--) != line.charAt(y++)) {
                        diff++;
                    }
                }
                if (diff > 1) break;
            }
            if (diff == 1) {
                return j + 1;
            }
        }
        return 0;
    }


    private int calculateRowWithOneSmudge(List<String> pat) {
        for (int i = 0; i < pat.size() - 1; i++) {

            int x = i, y = i + 1;
            int diff = 0;
            while(x >= 0 && y < pat.size()) {
                String before = pat.get(x--);
                String after = pat.get(y++);
                diff += getDist(before, after);
                if (diff > 1){
                    break;
                }

            }
            if (diff == 1) {
                return 100 * (i + 1);
            }
        }
        System.out.println("No mirror found");
        return 0;
    }

    private int getDist(String a, String b) {
        int dist = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                dist++;
            }
        }
        return dist;
    }

    public long summarizePattern() {
        long res = 0;
        for (List<String> pattern : patterns) {
            res += calculateRow(pattern);
            res += calculateCol(pattern);
        }
        return res;
    }

    private int calculateCol(List<String> pat) {
        int N = pat.get(0).length();
        for (int i = 0; i < N - 1; i++) {
            boolean isMatch = true;
            for (String ro : pat) {
                int x = i, y = i + 1;
                while(x >= 0 && y < N) {
                    if (ro.charAt(x--) != ro.charAt(y++)) {
                        isMatch = false;
                        break;
                    }
                }
                if (!isMatch) break;
            }

            if (isMatch) {
                return (i + 1);
            }
        }
        return 0;
    }

    private static int calculateRow(List<String> pat) {

        for (int i = 0; i < pat.size() - 1; i++) {

            int x = i, y = i + 1;
            boolean isMatch = true;
            while(x >= 0 && y < pat.size()) {
                if (!pat.get(x--).equals( pat.get(y++))) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                return 100 * (i + 1);
            }

        }
        System.out.println("No mirror found");
        return 0;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D13");

        long start = System.currentTimeMillis();
        D13PointOfIncurrence ans = new D13PointOfIncurrence(input);

        long res = ans.summarizePatternII();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

