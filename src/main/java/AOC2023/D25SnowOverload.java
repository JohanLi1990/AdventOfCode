package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D25SnowOverload {

    List<String[]> edges;
    DJS uf;
    public D25SnowOverload(List<String> input) {
        uf = new DJS();
        edges = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(": ");
            String u = parts[0].trim();
            String[] vs = parts[1].split(" ");
            uf.insertNode(u);
            for (String v : vs) {
                String dest = v.trim();
                uf.insertNode(dest);
                edges.add(new String[]{u, dest});
            }
            // no connections yet, just simply insert node
        }
    }

    public int getTwoGroupsByRemovingThreeWires(){
        boolean found = false;
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                for (int k = j + 1; k < edges.size(); k++) {
                    // ignore edges at i, j, k
                    int cur = initDJS(uf, i, j, k);
                    if (cur == 2) {
                        found = true;
                        break;
                    }
                    uf.clear();
                }
                if (found) break;
            }
            if (found) break;;
        }

        if (uf.size != 2) {
            System.out.println("Cannot find the trio, current size: " + uf.size);
            return -1;
        }

        String a = null, b = null;
        for (var ent : uf.parent.keySet()) {
            if (uf.find(ent).equals(ent)) {
                if (a == null){
                    a = ent;
                } else {
                    b = ent;
                }
                if (b != null) {
                    break;
                }
            }
        }

        int asize = uf.findSize(a), bsize = uf.findSize(b);
        return asize * bsize;

    }

    private int initDJS(DJS uf, int i, int j, int k) {
        for (int x = 0; x < edges.size(); x++) {
            if (x == i || x == j || x == k) continue;
            String[] edge = edges.get(x);
            uf.union(edge[0], edge[1]);
        }

        return uf.size;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D25");

        long start = System.currentTimeMillis();
        var ans = new D25SnowOverload(input);

        long res = ans.getTwoGroupsByRemovingThreeWires();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class DJS {
    Map<String, String> parent;
    Map<String, Integer> rank;

    int size = -1;
    public DJS() {
        parent = new HashMap<>();
        rank = new HashMap<>();
    }

    public void insertNode(String name) {
        parent.putIfAbsent(name, name);
        rank.putIfAbsent(name, 1);
        size = parent.size();
    }

    public String find(String cur) {
        String curName = parent.get(cur);
        if (!cur.equals(curName)){
            curName = find(curName);
        }
        parent.put(cur, curName);
        return curName;
    }

    public int findSize(String name) {
        int res = 0;
        for (String k : parent.keySet()) {
            if (find(k).equals(name)) {
                res++;
            }
        }
        return res;
    }

    public boolean union(String a, String b) {
        String pa = find(a), pb = find(b);

        if (pa.equals(pb)) return false;

        int ra = rank.get(a), rb = rank.get(b);
        if (ra >= rb) {
            parent.put(pb, pa);
            rank.put(pa, rb + ra);
        } else {
            parent.put(pa, pb);
            rank.put(pb, ra + rb);
        }
        size--; // number of groups reduces.
        return true;
    }

    public void clear() {
        for (String k : parent.keySet()) {
            parent.put(k, k);
            rank.put(k, 1);
        }
        size = parent.size();
    }


}
