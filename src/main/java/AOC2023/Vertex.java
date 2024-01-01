package AOC2023;

import java.util.ArrayList;
import java.util.List;

public abstract class Vertex {

    int x, y;
    List<IEdge> children;

    public Vertex(int x, int y) {
        this.x = x; this.y = y;
        children = new ArrayList<>();
    }
}

