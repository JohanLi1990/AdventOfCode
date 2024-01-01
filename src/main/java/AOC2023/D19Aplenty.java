package AOC2023;

import Utils.FileUtil;

import java.util.*;
import java.util.function.Predicate;

public class D19Aplenty {

    Map<String,Workflow> map = new HashMap<>();
    List<int[]> allParts = new ArrayList<>();
    List<int[]> accepted = new ArrayList<>();
    List<int[]> rejected = new ArrayList<>();

    long resultPartII;
    public D19Aplenty(List<String> input) {
        boolean isParts = false;
        for (String line : input) {
            if (line.isBlank() || line.isEmpty()) {
                isParts = true;
                continue;
            }

            if (!isParts) {
                addToMap(line);
            } else {
                addToList(line);
            }
        }

    }

    private void addToList(String line) {
        int index1 = line.indexOf("{");
        int index2 = line.indexOf("}");
        String parts = line.substring(index1 + 1, index2);
        int[] curPart = new int[4];
        int pos = 0;
        for (String part : parts.split(",")) {
            curPart[pos++] = Integer.parseInt(part.substring(2));
        }
        allParts.add(curPart);
    }

    private void addToMap(String line) {
        int index1 = line.indexOf("{");
        int second = line.indexOf("}");
        String workFlowName = line.substring(0, index1);
        String rules = line.substring(index1 + 1, second);


        if (!map.containsKey(workFlowName)) {
            map.put(workFlowName, new Workflow(workFlowName));
        }

        for (String rule: rules.split(",")) {
            Rule curRule = new Rule(rule);
            if (curRule.partName.equals(curRule.destNation)) {
                map.get(workFlowName).dest = curRule.partName;
            } else {
                map.get(workFlowName).add2Rules(curRule.partName, curRule);
            }
        }
    }

    public long getSumRatings() {
        long res = 0;
        for (int[] part : allParts) {
            sortParts(part, "in");
        }

        for (int[] acc : accepted) {
            if (acc.length != 4) {
                System.out.println("Wrong format");
            }
            res += acc[0];
            res += acc[1];
            res += acc[2];
            res += acc[3];
        }
        return res;

    }

    public long getCombinations() {
        dfs("in", new int[][]{new int[]{1, 4000}, new int[]{1, 4000},
        new int[]{1, 4000}, new int[]{1, 4000}});
        return resultPartII;
    }

    private void dfs(String workflow, int[][] pre) {
        if ("A".equals(workflow)) {
            resultPartII += getResult(pre);
            return;
        }
        if ("R".equals(workflow)) return;

        Workflow cur = map.get(workflow);
        int[][] original = getCopy(pre);
        for (Rule r : cur.rules) {
            String pname = r.partName;
            // for each rule we can choose to accept, or not accept and only accept the next

            int pos = getPart(pname);
            int[] curRange = pre[pos];
            int[] ruleRange = r.range;
            int[] outerRange = r.outerRange;
            int x = curRange[0], y = curRange[1];

            int[][] curCopy = getCopy(pre);
            // accept rule range, and try to dfs, if destination is not "R"
            if (!"R".equals(r.destNation)) {
                curRange[0] = Math.max(x, ruleRange[0]);
                curRange[1] = Math.min(y, ruleRange[1]);
                if (curRange[0] <= curRange[1]) {
                    dfs(r.destNation, pre);
                }
            }
            pre = curCopy;
            curRange = pre[pos];
            // Not accepting the rule, move on to next Rule
            curRange[0] = Math.max(x, outerRange[0]);
            curRange[1] = Math.min(y, outerRange[1]);
            if (curRange[0] > curRange[1]) {
                // cannot proceed
                pre = original;
                return;
            }
        }

        dfs(cur.dest, pre);
        pre = original;
    }

    private int[][] getCopy(int[][] pre) {
        int[][] res = new int[pre.length][];
        for (int i = 0; i < pre.length; i++) {
            res[i] = Arrays.copyOf(pre[i], pre[i].length);
        }
        return res;

    }
//43408786844500
//496534091000000
//167409079868000
//167409079868000

    private long getResult(int[][] p) {
        long res = 1;
        for (int[] each : p) {
            res *= (each[1] - each[0] + 1);
        }
        return res;
    }

    private void sortParts(int[] part, String workflow) {

        String curWorkflow = workflow;
        while(!"A".equals(curWorkflow) && !"R".equals(curWorkflow)) {
            Workflow wf = map.get(curWorkflow);
            var rules = wf.rules;
            boolean found = false;
            for (Rule each : rules) {
                int pos = getPart(each.partName);
                if (each.pred.test(part[pos])) {
                    found = true;
                    curWorkflow = each.destNation;
                    break;
                }
            }
            if (!found) {
                curWorkflow = wf.dest;
            }
        }

        if ("A".equals(curWorkflow)) {
            accepted.add(part);
        } else {
            rejected.add(part);
        }

    }

    private int getPart(String name){
        if ("x".equals(name)) {
            return 0;
        } else if ("m".equals(name)) {
            return 1;
        } else if ("a".equals(name)) {
            return 2;
        } else {
            return 3;
        }
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D19");

        long start = System.currentTimeMillis();
        var ans = new D19Aplenty(input);

        long res = ans.getCombinations();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class Workflow {
    String name;
    List<Rule> rules;
    String dest;
    public Workflow(String name) {
        this.name = name;
        rules = new ArrayList<>();
    }

    public void add2Rules(String rname, Rule rule) {
        this.rules.add(rule);
    }
}

class Rule {
    String partName;
    Predicate<Integer> pred;
    String destNation;

    int[] range, outerRange;
    public Rule(String theRule) {
        init(theRule);
    }

    private void init(String theRule) {
        int index = theRule.indexOf(":");
        if (index == -1) {
            this.partName = theRule;
            this.destNation = theRule;
            return;
        }
        String curRule = theRule.substring(0, index);

        this.partName = curRule.substring(0, 1);

        char operand = curRule.charAt(1);
        int val = Integer.parseInt(theRule.substring(2, index));
        this.destNation = theRule.substring(index + 1);
        if (operand == '>') {
            pred = k -> k > val;
            range = new int[]{val + 1, 4000};
            outerRange = new int[]{1, val};
        } else if (operand == '<') {
            pred = k -> k < val;
            range = new int[]{0, val - 1};
            outerRange = new int[]{val, 4000};
        } else {
            pred = k -> k == val;
        }
    }

}
