import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedHashMap;


public class WordNet {
  private final Digraph DG;
  private String[] synsetsSet;
  private final String synsets;
  private final String hypernyms;
  private int[] ids;
  private LinkedHashMap<String, Integer> nouns;
  private SAP sap;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) {
      throw new IllegalArgumentException("Null argument");
    }
    this.synsets = synsets;
    this.hypernyms = hypernyms;
    In synsetsIn = new In(this.synsets);
    String[] allLines = synsetsIn.readAllLines();
    ids = new int[allLines.length];
    synsetsSet = new String[allLines.length];
    nouns = new LinkedHashMap<>();

    // Filling arrays & linkedhashmap for id and synset tracking
    for (int i = 0; i < allLines.length; i++) {
      String[] lineStrings = allLines[i].split(",");
      ids[i] = Integer.parseInt(lineStrings[0]);
      synsetsSet[i] = lineStrings[1];
      if (synsetsSet[i].contains(" ")) {
        String[] synonyms = synsetsSet[i].split(" ");
        for (String syn : synonyms) {
          nouns.put(syn, i);
        }
      } else {
        nouns.put(synsetsSet[i], i);
      }
    }

    DG = new Digraph(ids.length);
    In hypernymsIn = new In(this.hypernyms);
    int rootCount = DG.V();
    while (hypernymsIn.hasNextLine()) {
      String line = hypernymsIn.readLine();
      String[] hypernymIndexStrings = line.split(",");
      int id = Integer.parseInt(hypernymIndexStrings[0]);
      for (int i = 1; i < hypernymIndexStrings.length; i++) {
        if (DG.outdegree(id) == 0) {
          rootCount--;
        }
        DG.addEdge(Integer.parseInt(hypernymIndexStrings[0]), Integer.parseInt(hypernymIndexStrings[i]));
      }
    }
    if (rootCount != 1) {
      throw new IllegalArgumentException("Invalid structure");
    }

    DirectedCycle cycleCheck = new DirectedCycle(DG);
    if (cycleCheck.hasCycle()) {
      throw new IllegalArgumentException("Input does not give DAG");
    }

    sap = new SAP(DG);
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return nouns.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException("Null argument");
    }
    return nouns.containsKey(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null || !nouns.containsKey(nounA) || !nouns.containsKey(nounB)) {
      throw new IllegalArgumentException("Null argument or not in wordnet");
    }
    return sap.length(nouns.get(nounA), nouns.get(nounB));
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null || !nouns.containsKey(nounA) || !nouns.containsKey(nounB)) {
      throw new IllegalArgumentException("Null argument or not in wordnet");
    }
    return synsetsSet[sap.ancestor(nouns.get(nounA), nouns.get(nounB))];
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