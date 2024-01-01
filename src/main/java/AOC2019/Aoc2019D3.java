package AOC2019;

import Utils.FileUtil;

import java.util.*;

public class Aoc2019D3 {

    // we need a way to mark all coordinates

    public int minDist(List<List<String>> paths) {
        List<Line> pointsA = travel(paths.get(0));
        List<Line> pointsB = travel(paths.get(1));

        int minD = Integer.MAX_VALUE;
        for (Line lineA : pointsA) {
            for (Line lineB : pointsB) {
                if (lineA.isSameOrientation(lineB)) continue;
                int[] res = lineB.intersects(lineA);
                if (res == null || (res[0] == 0 && res[1] == 0)) {
//                    System.out.println("Null:");
//                    System.out.println(lineA);
//                    System.out.println(lineB);
                    continue;
                }
                minD = Math.min(Math.abs(res[0]) + Math.abs(res[1]), minD);
            }
        }
        return minD;
    }

    public int minDistII(List<List<String>> paths) {
        List<Line> pointsA = travel(paths.get(0));
        List<Line> pointsB = travel(paths.get(1));

        int minD = Integer.MAX_VALUE;
        for (Line lineA : pointsA) {
            for (Line lineB : pointsB) {
                if (lineA.isSameOrientation(lineB)) continue;
                int[] res = lineB.intersects(lineA);
                if (res == null || (res[0] == 0 && res[1] == 0)) {
//                    System.out.println("Null:");
//                    System.out.println(lineA);
//                    System.out.println(lineB);
                    continue;
                }

                int distA = lineA.prevD + shift(res, lineA.start);
                int distB = lineB.prevD + shift(res, lineB.start);
                minD = Math.min(distA + distB, minD);
            }
        }
        return minD;
    }

    private static int shift(int[] pointA, int[] pointB) {
        return Math.abs(pointA[0] - pointB[0]) + Math.abs(pointA[1] - pointB[1]);
    }

    private static int getDist(String A) {
        String[] arr = A.split(",");
        return Math.abs(Integer.parseInt(arr[0])) + Math.abs(Integer.parseInt(arr[1]));
    }

    private List<Line> travel(List<String> steps) {
        List<Line> set = new ArrayList<>();
        int x = 0, y = 0;
        int prevD = 0;
        for (String step : steps) {
            char dir = step.charAt(0);
            int dist = Integer.parseInt(step.substring(1));
            int nX = x, nY = y;
            if (dir == 'R') {
                nX += dist;
            } else if (dir == 'L') {
                nX -= dist;
            } else if (dir == 'U') {
                nY += dist;
            } else if (dir == 'D') {
                nY -= dist;
            }
            Line curLine = new Line(new int[]{x, y}, new int[]{nX, nY}, prevD);
            prevD += dist;
            x = nX;
            y = nY;
            set.add(curLine);
        }
        return set;
    }





    public static void main(String[] args) {

        List<List<String>> input = FileUtil.readCsvLineByLine("2019D3");
//        for (List<String> line : input) {
//            System.out.println(line.toString());
//        }
        Aoc2019D3 ans = new Aoc2019D3();
        System.out.println(ans.minDist(input));
        System.out.println(ans.minDistII(input));
    }
}

class Line {

    int[] start, end;
    int prevD;
    boolean isHor;
    public Line(int[] s, int[] e, int preD) {
        start = s;
        end = e;
        isHor = e[1] == s[1];
        prevD = preD;
    }

    @Override
    public String toString() {
        return Arrays.toString(start) + "-" + Arrays.toString(end);
    }


    public int[] intersects(Line B) {
        if (this.isHor) {
            return getIntersects(this, B);
        }
        return getIntersects(B, this);

    }

    private static int[] getIntersects(Line A, Line B) {
        if (B.start[0] < Math.min(A.start[0], A.end[0]) || B.start[0] > Math.max(A.start[0],A.end[0])) return null;
        if (A.start[1] < Math.min(B.end[1], B.start[1]) || A.start[1] > Math.max(B.start[1], B.end[1])) return null;
        return new int[]{B.start[0], A.start[1]};
    }

    public boolean isSameOrientation(Line B) {
        return this.isHor == B.isHor;
    }

}
