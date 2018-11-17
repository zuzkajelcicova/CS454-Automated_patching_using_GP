package GP;

public class Bug {

    private String codeLine;
    private double probability;

    public Bug(String line, double prob) {
        this.codeLine = line;
        this.probability = prob;
    }

    public String getCodeLine() {
        return codeLine;
    }

    public double getProbability() {
        return probability;
    }
}

