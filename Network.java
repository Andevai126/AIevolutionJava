import java.util.ArrayList;
import java.lang.Math;

// Note that the number of inputs of a node (nInputs) is equal to the number of weights a node has.
// The number of inputs of a column is the same as the number of inputs of a node in said column.

public class Network{
    private ArrayList<Column> columns = new ArrayList<>();

    public Network(ArrayList<Integer> Dimensions){
        for (int i = 1; i < Dimensions.size(); i++) {
            columns.add(new Column(Dimensions.get(i-1), Dimensions.get(i)));
        }
    }

    public ArrayList<Double> calculateOutputs(ArrayList<Double> inputs){
        ArrayList<Double> outputs = columns.get(0).calculateOutputs(inputs);
        for (int i = 1; i < columns.size(); i++) {
            outputs = columns.get(i).calculateOutputs(outputs);
        }
        return outputs;
    }

    public ArrayList<ArrayList<ArrayList<Double>>> retrieveWeights(){
        ArrayList<ArrayList<ArrayList<Double>>> weights = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            weights.add(columns.get(i).retrieveWeights());
        }
        return weights;
    }

    public ArrayList<ArrayList<Double>> retrieveBiases(){
        ArrayList<ArrayList<Double>> biases = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            biases.add(columns.get(i).retrieveBiases());
        }
        return biases;
    }

    public void overwriteWeights(ArrayList<ArrayList<ArrayList<Double>>> newWeights){
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).overwriteWeights(newWeights.get(i));
        }
    }

    public void overwriteBiases(ArrayList<ArrayList<Double>> newBiases){
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).overwriteBiases(newBiases.get(i));
        }
    }

    private int randomInt(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public void adjustWeightsRandom(int nChanges, double changeAmount, boolean changeIsRandom){
        int nChangesLeft = nChanges;
        while (nChangesLeft > 0) {
            int randomColumn = randomInt(0, columns.size()-1);
            int randomNode = randomInt(0, columns.get(randomColumn).getnNodes()-1);
            int randomWeight = randomInt(0, columns.get(randomColumn).getnInputs()-1);
            double change = (randomInt(0,1) == 1) ? changeAmount*-1 : changeAmount;
            change = (changeIsRandom) ? change*Math.random() : change;
            columns.get(randomColumn).adjustWeights(randomNode, randomWeight, change);
            nChangesLeft--;
        }
    }

    public void adjustBiasesRandom(int nChanges, double changeAmount, boolean changeIsRandom){
        int nChangesLeft = nChanges;
        while (nChangesLeft > 0) {
            int randomColumn = randomInt(0, columns.size()-1);
            int randomNode = randomInt(0, columns.get(randomColumn).getnNodes()-1);
            double change = (randomInt(0,1) == 1) ? changeAmount*-1 : changeAmount;
            change = (changeIsRandom) ? change*Math.random() : change;
            columns.get(randomColumn).adjustBias(randomNode, change);
            nChangesLeft--;
        }
    }
}

class Column{
    private int nInputs;
    private int nNodes;
    private ArrayList<Node> nodes = new ArrayList<>();

    public Column(int nInputs, int nNodes){
        this.nInputs = nInputs;
        this.nNodes = nNodes;
        for (int i = 0; i < nNodes; i++) {
            nodes.add(new Node(nInputs));
        }
    }

    public ArrayList<Double> calculateOutputs(ArrayList<Double> inputs){
        ArrayList<Double> outputs = new ArrayList<>();
        for (int i = 0; i < nNodes; i++) {
            outputs.add(nodes.get(i).calculateOutput(inputs));
        }
        return outputs;
    }

    public ArrayList<ArrayList<Double>> retrieveWeights(){
        ArrayList<ArrayList<Double>> weights = new ArrayList<>();
        for (int i = 0; i < nNodes; i++) {
            weights.add(nodes.get(i).retrieveWeights());
        }
        return weights;
    }

    public ArrayList<Double> retrieveBiases(){
        ArrayList<Double> biases = new ArrayList<>();
        for (int i = 0; i < nNodes; i++) {
            biases.add(nodes.get(i).retrieveBias());
        }
        return biases;
    }

    public void overwriteWeights(ArrayList<ArrayList<Double>> newWeights){
        for (int i = 0; i < nNodes; i++) {
            nodes.get(i).overwriteWeights(newWeights.get(i));
        }
    }

    public void overwriteBiases(ArrayList<Double> newBiases){
        for (int i = 0; i < nNodes; i++) {
            nodes.get(i).overwriteBias(newBiases.get(i));
        }
    }

    public void adjustWeights(int nodeIndex, int weightIndex, double change){
        nodes.get(nodeIndex).adjustWeight(weightIndex, change);
    }

    public void adjustBias(int nodeIndex, double change){
        nodes.get(nodeIndex).adjustBias(change);
    }

    public int getnNodes() {
        return nNodes;
    }

    public int getnInputs() {
        return nInputs;
    }

}

class Node{
    private int nInputs;
    private ArrayList<Double> weights = new ArrayList<>();
    private double bias;
    private double z;

    private int randomInt(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public Node(int nInputs){
        this.nInputs = nInputs;
        for (int i = 0; i < nInputs; i++) {
            // weights.add(Math.random()*30-15);
            weights.add((randomInt(0,1) == 1) ? Math.random()*-1 : Math.random());
        }
        // bias = 5;
        bias = 0;
        // bias = (randomInt(0,1) == 1) ? Math.random()*-1 : Math.random();
    }

    private double sigmoidFunction(double z){
        return 1/(1+Math.pow(Math.E, -1*z));
    }

    public double calculateOutput(ArrayList<Double> inputs){
        z = 0;
        for (int i = 0; i < nInputs; i++) {
            z += inputs.get(i)*weights.get(i);
        }
        z -= bias;
        return sigmoidFunction(z);
    }

    public ArrayList<Double> retrieveWeights(){
        return weights;
    }

    public double retrieveBias(){
        return bias;
    }

    public void overwriteWeights(ArrayList<Double> newWeights){
        this.weights = newWeights;
    }

    public void overwriteBias(double newBias){
        this.bias = newBias;
    }

    public void adjustWeight(int weightIndex, double change){
        weights.set(weightIndex, weights.get(weightIndex)+change);
    }

    public void adjustBias(double change){
        bias += change;
    }
}