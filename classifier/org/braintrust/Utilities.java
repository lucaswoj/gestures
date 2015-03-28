package org.braintrust;

public class Utilities {

  public static double calculateErrorPartialDerivitive(double actual, double target) {
    return (target - actual);
  }

  // Uses sum of squares, but other formulas are possible
  public static double calculateError(double[] outputsActual, double[] outputsTarget) {
    assert(outputsActual.length == outputsTarget.length);
    int length = outputsActual.length;

    double error = 0;

    for (int i = 0; i < length; i++) {
      error += Math.pow(outputsTarget[i] - outputsActual[i], 2);
    }

    return 0.5 * error;
  }
  
  public static <T> T roulette(double[] probabilities, double probabilitiesSum, T[] values) {
    assert(values.length == probabilities.length);
    
    double seed = Math.random() * probabilitiesSum;
    
    int i = 0;
    for (; seed > 0 && i < values.length; i++) {
      assert(probabilities[i] > 0);
      seed -= probabilities[i];
      
      if (seed <= 0) {
        return values[i];
      }
    }
    
    assert(false);
    return null;
  }
}
