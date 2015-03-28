package org.braintrust;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

public class NeurophTester {

  public static void main(String[] args) {
    DataSet trainingSet = new DataSet(2, 1);

    for (int i = 0; i < 50; i++) {
      double x = random();
      double y = random();
      trainingSet.addRow(new DataSetRow(new double[]{x, y}, new double[]{f(x,y)}));
    }

    // create multi layer perceptron
    MultiLayerPerceptron mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 2, 1);
    
    mlp.randomizeWeights();

    mlp.getLearningRule().setMaxIterations(1000);
    System.out.println("max iterations: " + mlp.getLearningRule().getMaxIterations());

    System.out.println("Learning...");
    mlp.learn(trainingSet);

    // test perceptron
    System.out.println("Testing trained neural network");
    testNeuralNetwork(mlp, trainingSet);
  }

  public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

    for(DataSetRow dataRow : testSet.getRows()) {
      nnet.setInput(dataRow.getInput());
      nnet.calculate();
      double[ ] networkOutput = nnet.getOutput();
      // System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
      System.out.print(" Output: " + Arrays.toString(networkOutput) );
      System.out.print(" Expected: " + f(dataRow.getInput()[0], dataRow.getInput()[1]));
      System.out.println(" Error: " + calculateError(f(dataRow.getInput()[0], dataRow.getInput()[1]), networkOutput[0]));
    }
}
  
  private static double random() {
    return Math.random() * 2 - 1;
  }
  
  private static double f(double x1, double x2) {
    // return Math.atan(x1);
    return Math.sin(2 * Math.PI * x1) * Math.cos(0.5 * Math.PI * x2);
  }

  private static double calculateError(double target, double output) {
    return 0.5 * Math.pow(target - output, 2);
  }
}