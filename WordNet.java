import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;
import java.util.ArrayList;


public class WordNet {
  private Digraph DG;
  private String[] synsetsSet;
  private int[] ids;
  private ArrayList<String> nouns;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) {
      throw new IllegalArgumentException("Null argument");
    }
    In synsetsIn = new In(synsets);
    String[] allLines = synsetsIn.readAllLines();
    ids = new int[allLines.length];
    synsetsSet = new String[allLines.length];
    nouns = new ArrayList<>();

    // Filling arrays for id and synset tracking
    for (int i = 0; i < allLines.length; i++) {
      String[] lineStrings = allLines[i].split(",");
      ids[i] = Integer.parseInt(lineStrings[0]);
      synsetsSet[i] = lineStrings[1];
    }

    // Filling arraylist with all nouns
    for (String s : synsetsSet) {
      if (s.contains(" ")) {
        String[] synonyms = s.split(" ");
        for (String syn : synonyms) {
          nouns.add(syn);
        }
      } else {
        nouns.add(s);
      }
    }

    DG = new Digraph(ids.length);
    In hypernymsIn = new In(hypernyms);
    while (hypernymsIn.hasNextLine()) {
      String line = hypernymsIn.readLine();
      String[] hypernymIndexStrings = line.split(",");
      for (int i = 1; i < hypernymIndexStrings.length; i++) {
        DG.addEdge(Integer.parseInt(hypernymIndexStrings[0]), Integer.parseInt(hypernymIndexStrings[i])); // <- Ugly :(
      }
    }

    Topological top = new Topological(DG);
    if (!top.hasOrder()) {
      throw new IllegalArgumentException("Input does not give DAG");
    }
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return nouns;
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException("Null argument");
    }

/*    for (String s : nouns()) {
      if (s.equals(word)) {
        return true;
      }
    }
    return false;*/ //Linear time, need log time so binary search needed

    int left = 0;
    int right = nouns.size() - 1;
    while (left <= right) {
      int mid = left + (right - 1) / 2;
      if (nouns.get(mid) == word) {
        return true;
      } else if (nouns.get(mid).compareTo(word) < 0) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }
    throw new IllegalArgumentException("Not in WordNet");
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException("Null argument");
    }
    return 1;
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    return "s";
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length   = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}