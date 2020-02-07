import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("Null argument");
        }
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("Null argument");
        }
        String outcast = null;
        int greatestDistance = 0;
        for (String s : nouns) {
            int distance = 0;
            for (int i = 0; i < nouns.length; i++) {
                if (!nouns[i].equals(s)) {
                    distance += wordnet.distance(s, nouns[i]);
                }
            }
            if (distance > greatestDistance) {
                greatestDistance = distance;
                outcast = s;
            }
        }
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}