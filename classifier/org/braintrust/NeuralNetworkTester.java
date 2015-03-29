package org.braintrust;

import java.util.Arrays;

public class NeuralNetworkTester {

  public static void main(String[] args) {

    NeuralNetwork n = new NeuralNetwork(0.5, new int[]{1350, 53, 58, 24, 17, 6});
    
    int TRAINING_SAMPLES = 50000;
    for (int i = 0; i < TRAINING_SAMPLES; i++) {
      Gesture gesture = GestureStore.instance.getRandomTrainingGesture();
      n.train(gesture.input, gesture.targetOutput);
    }
    
    int classifyTotal = 0;
    int classifyError = 0;
    
    for (Gesture gesture : GestureStore.instance.getAllTestingGestures()) {
      double[] outputActual = n.classify(gesture.input);
      
      GestureType typeActual = GestureType.fromOutput(outputActual);
      GestureType typeTarget = gesture.type;
      
      classifyTotal++;
      if (typeActual == typeTarget) {
        System.out.println("CORRECTLY CLASSIFIED " + typeActual);
      } else {
        System.out.println("MISTOOK " + typeTarget + " FOR A " + typeActual);
        classifyError++;
      }
    }
    
    System.out.println("\n\nERROR RATE " + classifyError / (double) classifyTotal);
  }
}