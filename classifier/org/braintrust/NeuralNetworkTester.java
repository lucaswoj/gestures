package org.braintrust;

public class NeuralNetworkTester {

  public static void main(String[] args) {

    NeuralNetwork n = new NeuralNetwork(0.5, new int[]{2, 10, 100, 10, 1});
    
    int EPOCHS = 10000;
    int TRAINING_SAMPLES_PER_EPOCH = 10;
    int TESTING_SAMPLES_PER_EPOCH = 100;
    
    for (int epoch = 0; epoch < EPOCHS; epoch++) {
     
      // Provide additional training
      for (int i = 0; i < TRAINING_SAMPLES_PER_EPOCH; i++) {
        double x = random();
        double y = random();
        
        n.train(new double[]{x, y}, new double[]{f(x, y)});
      }

      // Measure error
      double errorSum = 0;
      for (int i = 0; i < TESTING_SAMPLES_PER_EPOCH; i++) {
        double x = random();
        double y = random();
        
        double outputActual = n.classify(new double[]{x, y})[0];
        double outputTarget = f(x, y);
        
        errorSum += Math.abs(outputActual - outputTarget);
      }
      double variance = errorSum / (double) TESTING_SAMPLES_PER_EPOCH;
      System.out.println(epoch + " -> " + Math.sqrt(variance));   
    }
  }
  
  private static double random() {
    return Math.random() * 2 - 1;
  }
  
  private static double f(double x, double y) {
    return Math.sin(2 * Math.PI * x) * Math.cos(0.5 * Math.PI* y);
  }
}