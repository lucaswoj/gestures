public class NeuralNetworkTester {

  public static void main(String[] args) {
    NeuralNetwork n = new NeuralNetwork(new int[]{1, 10, 5, 10, 1});
    
    for (int i = 0; i < 100000; i++) {
      double value = Math.random();
      n.train(new double[]{value}, new double[]{Math.sin(value)});
    }
    
    for (int i = 0; i < 10; i++) {
      double value = i / (double) 10;
      System.out.println(value + " -> " + n.classify(new double[]{value})[0] + " vs " + Math.sin(value));
    }
  }
}