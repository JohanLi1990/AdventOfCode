package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class D18LavaDuctLagoon {

    int[] dirs = new int[]{0, 1, 0, -1, 0}; //RULD
    List<Edge> digs = new ArrayList<>();
    List<Edge> digsII = new ArrayList<>();


    public D18LavaDuctLagoon(List<String> input) {
//        partI(input);
        partII(input);
    }

    private void partII(List<String> input) {
        long x = 0, y = 0;

        for (String line : input) {
            int n = line.length();
            String real = line.substring(n - 7, n - 1); //R 6 (#70c710)
            long realD = Long.parseLong(real.substring(0, real.length() - 1), 16);
            String realDir = real.substring(real.length() - 1);

            int dir = getNextDir(realDir);
            long nx = x +  dirs[dir] * (realD), ny = y + dirs[dir + 1] * (realD);
            digs.add(new Edge("", x, y, nx, ny, realD));
            x = nx; y = ny;

        }
    }

    private void partI(List<String> input) {
        int x = 0, y = 0;

        for (String line : input) {
            int n = line.length();
            String secondPart = line.substring(n - 7, n - 1); //R 6 (#70c710)
            String firstPart = line.substring(0, n - 10);

            String[] firstParts = firstPart.split(" ");
            int dir = getNextDir(firstParts[0].trim());
            int steps = Integer.parseInt(firstParts[1].trim());
            int nx = x +  dirs[dir] * (steps), ny = y + dirs[dir + 1] * (steps);
            digs.add(new Edge(secondPart, x, y, nx, ny, steps));
            x = nx; y = ny;

        }
    }

    public long getArea() {
        long res = 0;
        double edgeArea = 0.0;
        for (Edge uv : digs) {
            res += uv.getCrossProduct();
            edgeArea += uv.len;
        }

        res = Math.abs(res) / 2;
        return res + (long)edgeArea / 2 + 1;

    }

    private int getNextDir(String cur) {
        if ("R".equals(cur) || "0".equals(cur)) {
            return 0;
        } else if ("D".equals(cur) || "1".equals(cur)) {
            return 1;
        } else if ("L".equals(cur) || "2".equals(cur)) {
            return 2;
        } else {
            return 3;
        }
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D18");

        long start = System.currentTimeMillis();
        var ans = new D18LavaDuctLagoon(input);

        long res = ans.getArea();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class Edge {
    String color;
    long[] s, e;
    long len;

    public Edge(String col, long sx, long sy, long ex, long ey, long len) {
        this.color = col;
        s = new long[]{sx, sy};
        e = new long[]{ex, ey};
        this.len = len;
    }

    public long getCrossProduct() {
        return s[0]*e[1] - s[1]*e[0];
    }
}
