package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D20PulsePropagation {

    Map<String, ModulePart> map = new HashMap<>();

    public D20PulsePropagation(List<String> input) {
        for (String line : input) {
            String[] uv = line.split("->");
            String curU = uv[0].trim();
            char first = curU.charAt(0);
            ModulePart cur;
            if (first == '%') {
                String uname = curU.substring(1);
                cur = new FlipFlop(uname);
            } else if (first == '&'){
                String uname = curU.substring(1);
                cur = new Conjuction(uname);
            } else {
                cur =new BroadCaster(curU);
            }

            for (String dest : uv[1].split(",")) {
                String realdest = dest.trim();
                if (realdest.isEmpty()) {
                    continue;
                }
                cur.children.add(realdest);
            }
            map.put(cur.name, cur);
        }

        List<ModulePart> theNormalParts = new ArrayList<>();
        for (var ent : map.entrySet()) {
            for (String child : ent.getValue().children) {
                if (!map.containsKey(child)) {
                    ModulePart curChild = new BroadCaster(child);
                    curChild.parentsModule.add(ent.getValue());
                    theNormalParts.add(curChild);
                } else {
                    map.get(child).parents.put(ent.getKey(), 0);
                    map.get(child).parentsModule.add(ent.getValue());
                }
            }
        }
        for (ModulePart each : theNormalParts) {
            map.put(each.name, each);
        }



    }

    public long getMinPress() {
        // fk rz, br, lf
        ModulePart t = map.get("broadcaster");
        Queue<Transaction> q = new ArrayDeque<>();
        Map<String, Integer> valmap =  new HashMap<>();
        Set<String> set = new HashSet<>(List.of("fk", "rz", "br", "lf"));
        int step = 1;
        do {
            q.offer(new Transaction(null, t, 0));
            while(!q.isEmpty()) {
                Transaction cur = q.poll();
                ModulePart receiver = cur.receiver;

                if (valmap.size() == 4) break;
                if (!receiver.willReact(cur.val)) continue;
                receiver.receiveSignal(cur.sender == null? "" : cur.sender.name, cur.val);

                int signalToSend = receiver.getSignalToSend();

                if (set.contains(receiver.name) && signalToSend == 1) {
                    valmap.put(receiver.name, step);
                }
                for (String child : receiver.children) {
                    q.offer(new Transaction(receiver, map.get(child), signalToSend));
                }
            }
            if (valmap.size() == 4) {
                return getLcm(valmap);
            }
            step++;

        } while(step < (int)1e10);
        return -1;

    }

    private  long getLcm(Map<String, Integer> curMap) {
        long res = 1;
        for (var ent : curMap.entrySet()) {
            res = (res * ent.getValue()) / (getGcd(res, ent.getValue()));
        }
        return res;
    }

    public long cycleHit(String target) {
        return cycleHit(map.get(target));
    }
    private long cycleHit(ModulePart target) {
        ModulePart t = map.get("broadcaster");
        Queue<Transaction> q = new ArrayDeque<>();
        int step = 1;
        do {
            q.offer(new Transaction(null, t, 0));
            while(!q.isEmpty()) {
                Transaction cur = q.poll();
                ModulePart receiver = cur.receiver;
                if (!receiver.willReact(cur.val)) continue;
                receiver.receiveSignal(cur.sender == null? "" : cur.sender.name, cur.val);

                int signalToSend = receiver.getSignalToSend();

                if (receiver == target && signalToSend == 1) {
                    return step;
                }
                for (String child : receiver.children) {
                    q.offer(new Transaction(receiver, map.get(child), signalToSend));
                }
            }

            step++;

        } while(step < (int)1e10);
        return -1;
    }

    private long getGcd(long a, long b) {
        if (b == 0) return a;
        return getGcd(b, a % b);
    }

    public int getPulse(int rounds) {
        int[] res = new int[2];
        long start = getSnapshot();
        int i = 0;
        for (i = 0; i < rounds; i++) {
            int[] curRes = getPulseForOneRound();
            res[0] += curRes[0];
            res[1] += curRes[1];
            long curSnapshot = getSnapshot();
            if (curSnapshot == start) {
                System.out.println("found cycle at: " + i);
                break;
            }
        }

        if (i == rounds) {
            return res[0] * res[1];
        }


        int multiple = rounds / (i + 1);
        res[0] = multiple * res[0];
        res[1] = multiple * res[1];
        for (int j = 0; j < rounds % (i + 1); j++) {
            int[] curRes = getPulseForOneRound();
            res[0] += curRes[0];
            res[1] += curRes[1];
        }

        return res[0] * res[1];
    }

    public long getSnapshot() {
        long res = 0;
        for (var ent : map.entrySet()) {
            res = 31 * res + ent.getValue().getState();
        }
        return res;
    }

    public int[] getPulseForOneRound() {
        // return [lowPulse, highPulse] back;
        ModulePart start = map.get("broadcaster");

        Queue<Transaction> q = new ArrayDeque<>();
        q.offer(new Transaction(null, start, 0));
        int lo = 0, hi =0;

        while(!q.isEmpty()) {

            Transaction cur = q.poll();
            if (cur.val == 1) {
                hi++;
            } else {
                lo++;
            }
            ModulePart receiver = cur.receiver;
            if (!receiver.willReact(cur.val)) continue;
            receiver.receiveSignal(cur.sender == null? "" : cur.sender.name, cur.val);

            int signalToSend = receiver.getSignalToSend();

            for (String child : receiver.children) {
                q.offer(new Transaction(receiver, map.get(child), signalToSend));
            }
        }
        return new int[]{lo, hi};
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D20");

        long start = System.currentTimeMillis();
        var ans = new D20PulsePropagation(input);

//        long res = ans.getPulse(1000);
        long res = ans.getMinPress();
//        long res = ans.cycleHit("rz");
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
        //rx -> &lb -> [&fk, &rz, &br, &lf]
        // -> [&fz, &th, &vd, &pn
    }
}

class Transaction {
    ModulePart sender, receiver;
    int val;

    public Transaction(ModulePart s, ModulePart r, int val) {
        this.sender = s;
        this.receiver = r;
        this.val = val;
    }
}


abstract class ModulePart {

    Map<String, Integer> parents;
    List<String> children;
    List<ModulePart> parentsModule;
    String name;

    int status;

    public ModulePart(String name) {
        this.name = name;
        parents = new HashMap<>();
        children = new ArrayList<>();
        status = 0;
        parentsModule = new ArrayList<>();
    }


    public abstract void receiveSignal(String name, int val);
    public abstract int getSignalToSend();

    public abstract boolean willReact(int val);
    public abstract int getState();

    @Override
    public String toString() {
        return "[" + name +"=" + getState() + "]";
    }
}

class FlipFlop extends ModulePart {

    public FlipFlop(String name) {
        super(name);
    }

    @Override
    public void receiveSignal(String name, int val) {
        if (val == 0) {
            status = 1 - status;
        }
    }

    @Override
    public int getSignalToSend() {
        return status;
    }

    @Override
    public boolean willReact(int val) {
        return val == 0;
    }

    @Override
    public int getState() {
        return status;
    }
}

class Conjuction extends ModulePart {

    int curSum = 0;
    public Conjuction(String name) {
        super(name);
    }

    @Override
    public void receiveSignal(String name, int val) {
        curSum -= parents.getOrDefault(name, 0);
        curSum += val;
        parents.put(name, val);
    }

    @Override
    public int getSignalToSend() {
        if (curSum == parentsModule.size()) return 0;
        return 1;
    }

    @Override
    public boolean willReact(int val) {
        return true;
    }

    @Override
    public int getState() {
        return curSum;
    }

}

class BroadCaster extends ModulePart {

    public BroadCaster(String name) {
        super(name);
    }

    @Override
    public void receiveSignal(String name, int val) {
        status = val;
    }

    @Override
    public int getSignalToSend() {
        return  status;
    }

    @Override
    public boolean willReact(int val) {
        return true;
    }

    @Override
    public int getState() {
        return 0;
    }
}
