package AOC2023;

import Utils.FileUtil;
import org.jgrapht.Graph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;

public class D25StoerWagner {

    private Graph<String, DefaultWeightedEdge> graph;

    public D25StoerWagner(List<String> input) {
        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for (String line : input) {
            String[] parts = line.split(": ");
            String u = parts[0].trim();
            graph.addVertex(u);
            String[] vs = parts[1].split(" ");
            for (String v : vs) {
                String dest = v.trim();
                graph.addVertex(dest);
                graph.addEdge(u, dest);
            }
        }

    }

    public int minCutSize() {
        var ans = new StoerWagnerMinimumCut<>(graph);
        return ans.minCut().size();
    }

    public int graphSize() {
        return graph.vertexSet().size();
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D25");

        long start = System.currentTimeMillis();
        var ans = new D25StoerWagner(input);

        var res = ans.minCutSize();
        res *= ans.graphSize() - res;
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
