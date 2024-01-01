package AOC2023;

import Utils.FileUtil;

import java.util.*;

import static AOC2023.HailStone.cross;

public class D24NeverTellMeTheOdds {

    List<HailStone> hailStones;

    public D24NeverTellMeTheOdds(List<String> input) {
        hailStones = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split("@");
            hailStones.add(new HailStone(parts[0], parts[1]));
        }

    }

    public void rockThrow() {
        Set<Double> seen = new HashSet<>();
        double[] rockLoc = new double[]{-1, -1, -1};
        for (long vz = -500; vz <= 500; vz++) {
            for (long vy = -500; vy <= 500; vy++) {
                for (long vx = -500; vx <= 500; vx++) {
                    int cnt = 0;
                    boolean skip = false;
                    for (int i = 0; i < hailStones.size(); i++) {
                        for (int j = i + 1; j < hailStones.size();j++) {

                            HailStone a = hailStones.get(i), b = hailStones.get(j);
                            int matches = 0;
                            int parallelCount = 0;
                            double ta = -1, tb = -1;
                            double[] xy = cross(a, b, new long[]{vx, vy}, new int[]{0, 1});
                            double[] xy1 = cross(a, b, new long[]{vx, vy}, HailStone.Plane.XY);
//                            double[] yz = cross(a, b, new long[]{vy, vz}, HailStone.Plane.YZ);
                            double[] yz = cross(a, b, new long[]{vy, vz}, new int[]{1, 2});
                            double[] yz2 = cross(a, b, new long[]{vy, vz}, HailStone.Plane.YZ);
//                            double[] xz = cross(a, b, new long[]{vx, vz}, HailStone.Plane.XZ);
                            double[] xz = cross(a, b, new long[]{vx, vz}, new int[]{0, 2});
                            double[] xz2 = cross(a, b, new long[]{vx, vz}, HailStone.Plane.XZ);

                            if (xy == null && yz == null && xz == null) {
                                skip = true;
                                break;
                            }
                            if (xy == null) parallelCount++;
                            if (yz == null) parallelCount++;
                            if (xz == null) parallelCount++;
                            
                            if (xy != null) {
//                                double t1 = (xy[0] - a.x0) / (a.vx - vx);
                                double t1 = getTime(xy[0], a.x0, a.vx, vx);
                                if (t1 < 0) {
                                    t1 = getTime(xy[1], a.y0, a.vy, vy);;
                                }

                                double t2 = getTime(xy[0], b.x0, b.vx, vx);
                                if (t2 < 0) {
                                    t2 = getTime(xy[1], b.y0, b.vy, vy);;
                                }

                                ta = t1;
                                tb = t2;
                                if (t1 > 0 && t2 > 0 && ta == t1 && tb == t2) matches++;
                            }

                            if (yz != null) {
                                //                                double t1 = (xy[0] - a.x0) / (a.vx - vx);
                                double t1 = getTime(yz[0], a.y0, a.vy, vy);
                                if (t1 < 0) {
                                    t1 = getTime(yz[1], a.z0, a.vz, vz);;
                                }

                                double t2 = getTime(yz[0], b.y0, b.vy, vy);
                                if (t2 < 0) {
                                    t2 = getTime(yz[1], b.z0, b.vz, vz);;
                                }

                                if(ta == -1) ta = t1;
                                if (tb == -1) tb = t2;
                                if (t1 > 0 && t2 > 0 && ta == t1 && tb == t2) matches++;
                            }

                            if (xz != null) {
                                double t1 = getTime(xz[0], a.x0, a.vx, vx);
                                if (t1 < 0) {
                                    t1 = getTime(xz[1], a.z0, a.vz, vz);;
                                }

                                double t2 = getTime(xz[0], b.x0, b.vx, vx);
                                if (t2 < 0) {
                                    t2 = getTime(xz[1], b.z0, b.vz, vz);;
                                }

                                if(ta == -1) ta = t1;
                                if (tb == -1) tb = t2;
                                if (t1 > 0 && t2 > 0 && ta == t1 && tb == t2) matches++;
                            }

                            if (matches >= 2) {
                                cnt++;
                            } else {
                                skip = true;
                                break;
                            }

                            if (cnt >= 40 && parallelCount == 0 && xy[0] % 1 == 0) {
                                rockLoc[0] = xy[0]; rockLoc[1] = xy[1]; rockLoc[2] = yz[1];
                                double[] temp = new double[]{xy1[0], xy1[1], yz[1]};
                                double cures = Arrays.stream(rockLoc).sum();

                                if (seen.contains(cures)) {
                                    System.out.println(Arrays.toString(rockLoc) + "=" +
                                            cures);
                                    System.out.println(Arrays.toString(temp) + "=" + Arrays.stream(temp).sum());
                                    System.out.println("=====================");
                                    skip = true;
                                    break;
                                }
                                seen.add(cures);


                            }
                        }
                        if (skip) break;
                    }

                    //870379016024859
                    //481684720581503
                    //9226841901174442E14
//                    if (hits == 200) {
//                        return;
//                    }
                }
            }
        }
    }

    private double getTime(double a, long b, long c, long d) {
        if (c == d) return -1;
        return (a - b) / (c - d);
    }

    public long getIntersections(long min, long max) {
        long collides = 0;
        for (int i = 0; i < hailStones.size(); i++) {
            HailStone cur = hailStones.get(i);
            for (int j = i + 1; j < hailStones.size(); j++) {
                HailStone other = hailStones.get(j);
                double[] intersect = cur.crossPathXY(other);
                if (intersect == null) continue;
                double time1 = cur.timeOfCross(intersect[0]);
                double time2 = other.timeOfCross(intersect[0]);
                if (isWithinArea(intersect[0], intersect[1], min, max)
                && time1 >= 0 && time2 >= 0) {
                    collides++;
                }
            }
        }
        return collides;
    }

    private boolean isWithinArea(double x, double y, long min, long max) {
        return x >= min && x <= max && y >= min && y <= max;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D24");

        long start = System.currentTimeMillis();
        var ans = new D24NeverTellMeTheOdds(input);

//        long res = ans.getIntersections(200000000000000L, 400000000000000L);
        ans.rockThrow();
        System.out.println(System.currentTimeMillis() - start + "ms");
//        System.out.println(res);
    }
}

class HailStone {

    long x0, y0, z0;
    long vx, vy, vz;

    long[] p, v;

    double gradientXY;
    double constantXY;

    double gradientYZ;
    double constantYZ;
    double gradientXZ;
    double constantXZ;

    public HailStone(String cor, String vel) {
        String[] coords = cor.split(", ");
        this.x0 = Long.parseLong(coords[0].trim());
        this.y0 = Long.parseLong(coords[1].trim());
        this.z0 = Long.parseLong(coords[2].trim());
        String[] vels = vel.split(", ");
        this.vx = Long.parseLong(vels[0].trim());
        this.vy = Long.parseLong(vels[1].trim());
        this.vz = Long.parseLong(vels[2].trim());
        this.gradientXY = (double) vy / (double) vx;
        this.constantXY = y0 - gradientXY * x0;
        this.gradientYZ = (double) vz / (double) vy;
        this.constantYZ = z0 - gradientYZ * y0;
        this.gradientXZ = (double) vz / (double) vx;
        this.constantXZ = z0 - gradientXZ * x0;
        p = new long[]{x0, y0, z0};
        v = new long[]{vx, vy, vz};

    }

    public HailStone(long x0, long y0, long z0, long vx, long vy, long vz) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;

        this.gradientXY = (double) vy / (double) vx;
        this.constantXY = y0 - gradientXY * x0;
        this.gradientYZ = (double) vz / (double) vy;
        this.constantYZ = z0 - gradientYZ * y0;
        this.gradientXZ = (double) vz / (double) vx;
        this.constantXZ = z0 - gradientXZ * x0;

        p = new long[]{x0, y0, z0};
        v = new long[]{vx, vy, vz};
    }

    double[] crossPathXY(HailStone b) {
        if (this.gradientXY == b.gradientXY) return null;
        double x = (b.constantXY - this.constantXY) / (this.gradientXY - b.gradientXY);
        double y = this.gradientXY * x + this.constantXY;
        return new double[]{x, y};
    }

    public static double[] cross(HailStone a, HailStone b, long[] shift, int[] coords) {
        long x1 = a.p[coords[0]], y1 = a.p[coords[1]], x2 = a.p[coords[0]] + a.v[coords[0]] - shift[0], y2 = a.p[coords[1]] + a.v[coords[1]] - shift[1];
        long x3 = b.p[coords[0]], y3 = b.p[coords[1]], x4 = b.p[coords[0]] + b.v[coords[0]] - shift[0], y4 = b.p[coords[1]] + b.v[coords[1]] - shift[1];

        long ua, ub, denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0) return null;
        ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        return new double[]{x1 + ua * (x2 - x1),
                y1 + ua * (y2 - y1)};

    }
    public static double[] cross(HailStone a, HailStone b, long[] rockVel, Plane p) {
        if (p == Plane.XY) {
            long aVx = a.vx - rockVel[0], aVy = a.vy - rockVel[1];
            double aGradientXY = (double) aVy / (double) aVx;
            double aConstantXY = a.y0 - aGradientXY * a.x0;

            long bVx = b.vx - rockVel[0], bVy = b.vy - rockVel[1];
            double bGradientXY = (double) bVy / (double) bVx;
            double bConstantXY = b.y0 - bGradientXY * b.x0;

            return getDoubles(aGradientXY, bGradientXY, bConstantXY, aConstantXY);
        } else if (p == Plane.YZ) {
            long aVy = a.vy - rockVel[0], aVz = a.vz - rockVel[1];
            double aGradientYZ = (double) aVz / (double) aVy;
            double aConstantYZ = a.z0 - aGradientYZ * a.y0;

            long bVy = b.vy - rockVel[0], bVz = b.vz - rockVel[1];
            double bGradientYZ = (double) bVz / (double) bVy;
            double bConstantYZ = b.z0 - bGradientYZ * b.y0;

            return getDoubles(aGradientYZ, bGradientYZ, bConstantYZ, aConstantYZ);
        } else {
            long aVx = a.vx - rockVel[0], aVz = a.vz - rockVel[1];
            double aGradientXZ = (double) aVz / (double) aVx;
            double aConstantXZ = a.z0 - aGradientXZ * a.x0;

            long bVx = b.vx - rockVel[0], bVz = b.vz - rockVel[1];
            double bGradientXZ = (double) bVz / (double) bVx;
            double bConstantXZ = b.z0 - bGradientXZ * b.x0;

            return getDoubles(aGradientXZ, bGradientXZ, bConstantXZ, aConstantXZ);
        }
    }

    private static double[] getDoubles(double aG, double bG, double bC, double aC) {
        if (aG == bG) return null;

        double x = (bC - aC) / (aG - bG);
        double y = aG * x + aC;
        return new double[]{x, y};
    }

    @Override
    public String toString() {
        return "HailStone{" +
                "x0=" + x0 +
                ", y0=" + y0 +
                ", z0=" + z0 +
                ", vx=" + vx +
                ", vy=" + vy +
                ", vz=" + vz +
                '}';
    }

    double timeOfCross(double x) {
        return (x - x0) / (double) vx;
    }
    

    enum Plane {
        XY, YZ, XZ
    }
}
