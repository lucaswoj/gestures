public class NeuralNetworkTester {

  public static void main(String[] args) {
    NeuralNetwork n = new NeuralNetwork(new int[]{1, 50, 50, 1});

    for (int j = 0; j < 10; j++) {
      for (int i = 0; i < 10; i++) {
        n.train(new double[]{i}, new double[]{i});
      }
    }
    
    for (int i = 0; i < 10; i++) {
      System.out.println(n.classify(new double[]{i})[0]);
    }
  }
}