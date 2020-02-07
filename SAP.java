import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
  private Digraph G;
  private Iterable<Integer> reversePost;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    if (G == null) {
      throw new IllegalArgumentException("Null argument");
    }
    this.G = G;
    DepthFirstOrder dfs = new DepthFirstOrder(G);
    reversePost = dfs.reversePost();
  }

  private int ancestorOrLength(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW, String type) {
    int shortestAncestor = -1;
    int shortestPath = -1;
    for (int i : reversePost) {
      if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
        if (shortestAncestor == -1) {
          shortestAncestor = i;
        }
        int distance = bfsV.distTo(i) + bfsW.distTo(i);
        if (shortestPath == -1) {
          shortestPath = distance;
        } else if (shortestPath > distance) {
          shortestPath = distance;
          shortestAncestor = i;
        }
      }
    }

    if (type.equals("Length")) {
      return shortestPath;
    } else {
      return shortestAncestor;
    }
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    if (v > G.V() || w > G.V()) {
      throw new IllegalArgumentException("Out of bound vertices");
    }

    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

    return ancestorOrLength(bfsV, bfsW, "Length");
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    if (v > G.V() || w > G.V()) {
      throw new IllegalArgumentException("Out of bound vertices");
    }

    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

    return ancestorOrLength(bfsV, bfsW, "Ancestor");
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException("Null argument");
    }

    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

    return ancestorOrLength(bfsV, bfsW, "Length");
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException("Null argument");
    }

    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

    return ancestorOrLength(bfsV, bfsW, "Ancestor");
  }

  // do unit testing of this class
  public static void main(String[] args) {

  }
}