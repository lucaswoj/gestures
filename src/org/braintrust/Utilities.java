package org.braintrust;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

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
  
  public static <T> T roulette(ArrayList<Double> probabilities, double probabilitiesSum, ArrayList<T> values) {
    assert(values.size() == probabilities.size());
    
    double seed = Math.random() * probabilitiesSum;
    
    for (int i = 0; seed > 0 && i < values.size(); i++) {
      assert(probabilities.get(i) > 0);
      seed -= probabilities.get(i);
      
      if (seed <= 0) {
        return values.get(i);
      }
    }
    
    assert(false);
    return null;
  }
  
  public static <T extends Comparable> int maxIndex(Collection<T> collection) {
    int maxIndex = -1;
    T maxValue = null;
    
    int index = 0;
    for (T value : collection) {
      if (maxIndex == -1 || (maxValue != null && maxValue.compareTo(value) < 0)) {
        maxIndex = index;
        maxValue = value;
      }
      index++;
    }
    
    return maxIndex;
  }
  
  public static int maxIndex(double[] collection) {
    int maxIndex = -1;
    double maxValue = Double.NEGATIVE_INFINITY;
    
    int index = 0;
    for (double value : collection) {
      if (maxIndex == -1 || maxValue < value) {
        maxIndex = index;
        maxValue = value;
      }
      index++;
    }
    
    return maxIndex;
  }
  
  public static double sum(Collection<Double> collection) {
    double sum = 0;
    for (double value : collection) {
      sum += value;
    }
    return sum;
  }
  
  public static <T> T random(List<T> list) {
    return list.get((int) (list.size() * Math.random()));
  }
  
  public static <T> T random(T[] list) {
    return list[(int) (list.length * Math.random())];
  }
}
