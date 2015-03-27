public class NeuralNetworkTester {

  public static void main(String[] args) {
    NeuralNetwork n = new NeuralNetwork(new int[]{1, 100, 100, 1});

    for (int j = 0; j < 10; j++) {
      for (int i = 0; i < 100; i++) {
        n.train(new double[]{i}, new double[]{Math.abs(1 - (i - 50) / 100)});
      }
    }
    
    for (int i = 0; i < 100; i++) {
      System.out.println(n.classify(new double[]{i})[0]);
    }

  }

}