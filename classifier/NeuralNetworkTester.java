public class NeuralNetworkTester {

  public static void main(String[] args) {
    int NEURONS_MIN = 2;
    int NEURONS_MAX = 100;
    
    int TRAINING_SAMPLES = 5000;
    int TESTING_SAMPLES = 50;
    int TRIALS = 100;
    double LEARNING_RATE = 0.5;
    
    for (int neurons = NEURONS_MIN; neurons <= NEURONS_MAX; neurons++) {
      
      double sdSum = 0;
      
      for (int trials = 0; trials < TRIALS; trials++) {
        
        NeuralNetwork n = new NeuralNetwork(LEARNING_RATE, new int[]{2, neurons, 1});
      
        // Train network
        for (int i = 0; i < TRAINING_SAMPLES; i++) {
          double x1 = random();
          double x2 = random();
          n.train(new double[]{x1, x2}, new double[]{f(x1, x2)});
        }

        // Measure error
        double varianceSum = 0;
        for (int i = 0; i < TESTING_SAMPLES; i++) {
          double x1 = random();
          double x2 = random();

          double actual = n.classify(new double[]{x1, x2})[0];
          double target = f(x1, x2);
          
          varianceSum += Math.pow((actual - target) / target, 2);
        }
        double variance = varianceSum / (double) TESTING_SAMPLES;
        double sd = Math.sqrt(variance);
        
        sdSum += sd;     
      }
      
      System.out.println(neurons + " Neurons -> " + sdSum / TRIALS);   
    }
  }
  
  private static double random() {
    return Math.random();
  }
  
  private static double f(double x1, double x2) {
    // return Math.atan(x1);
    return Math.sin(2 * Math.PI * x1) * Math.cos(0.5 * Math.PI * x2);
  }
}