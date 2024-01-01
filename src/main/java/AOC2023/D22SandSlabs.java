package AOC2023;

import Utils.FileUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class D22SandSlabs {

    List<Slab> slabs;
    AtomicLong index = new AtomicLong(0);
    public D22SandSlabs(List<String> input) {
        slabs = new ArrayList<>();
        for (String line : input) {
            String[] cords = line.split("~");
            String[] cord1 = cords[0].split(",");
            String[] cord2 = cords[1].split(",");
            Slab cur = new Slab(
                    Integer.parseInt(cord1[0]), Integer.parseInt(cord1[1]),
                    Integer.parseInt(cord1[2]), Integer.parseInt(cord2[0]),
                    Integer.parseInt(cord2[1]), Integer.parseInt(cord2[2]),
                    index.getAndIncrement()
            );
            slabs.add(cur);
        }
    }

    public long chainReaction() {
        sortListOfSlabs();
        long res = 0;

        for(Slab s : slabs) {
            res += dfs(s) - 1;
        }
        return res;
    }

    private long dfs(Slab s) {
        Queue<Slab> q = new ArrayDeque<>();
        q.offer(s);
        Set<Long> visited = new HashSet<>();
        visited.add(s.index);
        while(!q.isEmpty()) {

            Slab cur = q.poll();

            for (Slab c: cur.children) {
                if (allParentsDead(c, visited)) {
                    visited.add(c.index);
                    q.offer(c);
                }
            }
        }
        return visited.size();
    }

    private boolean allParentsDead(Slab cur, Set<Long> visited) {
        for (Slab p : cur.parent) {
            if (!visited.contains(p.index)) {
                return false;
            }
        }
        return true;
    }

    public long getNumOfSlabsTofall() {
        sortListOfSlabs(); // reorder
        long res = 0;
        for (Slab s : slabs) {
            boolean canRemove = true;
            for (Slab schild : s.children) {
                if (schild.parent.size() == 1) {
                    canRemove = false;
                    break;
                }
            }
            if (canRemove) res++;
        }
        return res;
    }


    private void sortListOfSlabs() {
        slabs.sort(Comparator.comparingInt(a -> a.z1));
        for (Slab s : slabs) {
            List<Slab> intersects = getIntersectingBlocks(s);
            while(intersects.isEmpty() && s.z1 > 0) {
                s.fall();
                intersects = getIntersectingBlocks(s);
            }

            if (!intersects.isEmpty()) {
                s.up();
                for (Slab p : intersects) {
                    p.children.add(s);
                    s.parent.add(p);
                }
            }
        }
    }

    private List<Slab> getIntersectingBlocks(Slab cur) {
        return slabs.stream().filter(k -> k != cur && k.isIntersecting(cur)).toList();
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D22");

        long start = System.currentTimeMillis();
        var ans = new D22SandSlabs(input);

//        long res = ans.getNumOfSlabsTofall();
        long res = ans.chainReaction();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }


}

class Slab {

    long index;
    List<Slab> children; // those slabs I support
    List<Slab> parent; // num of paremts
    int x1, y1, z1;
    int x2, y2, z2;
    
    public Slab(int x1, int y1, int z1, int x2, int y2, int z2, long index) {
        children = new ArrayList<>();
        parent = new ArrayList<>();
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.index = index;
    }

    void fall() {
        z1--;
        z2--;
    }

    void up() {
        z1++;
        z2++;
    }

    @Override
    public String toString() {
        return "Slab[" + "(" + x1 + "," + y1 + "," + z1 + ") -> (" +
                x2 + "," + y2 + "," + z2 + ")";
    }

    boolean isIntersecting(Slab other) {
        return x2 >= other.x1 && x1 <= other.x2
                && y2 >= other.y1 && y1 <= other.y2
                && z2 >= other.z1 && z1 <= other.z2;
    }

}

