package org.braintrust;

import java.util.Arrays;

public class NeuralNetworkTopologyOptimizer {
  
  private static final int LAYERS_MIN = 1;
  private static final int LAYERS_MAX = 8;
  
  private static final int LAYER_NEURONS_MIN = 1;
  private static final int LAYER_NEURONS_MAX = 200;
  
  private static final double LEARNING_RATE_MIN = 0.3;
  private static final double LEARNING_RATE_MAX = 1;
  
  static final int GENERATIONS = 100;
  private static final int GENERATION_SIZE = 7 * 2;
  
  private static final int INPUT_NEURONS = GestureStore.INPUT_NEURONS;
  private static final int OUTPUT_NEURONS = GestureStore.OUTPUT_NEURONS;
  
  private static final double MUTATION_RATE = 0.05;
  
  static final int FITNESS_TRIALS = 3;
  static final int FITNESS_TRAINING_SAMPLES = 2000;
  
  public static final IndividualFactory factory = new IndividualFactory();
    
  public static void main(String[] args) {
    Individual individual = GeneticAlgorithm.optimize(factory, GENERATIONS, GENERATION_SIZE, MUTATION_RATE);
  }
  
  private static class Individual {

    final private int[] neurons;
    final private double learningRate;

    public Individual(int[] neurons, double learningRate) {
      this.neurons = neurons;
      this.learningRate = learningRate;
      
      assert(this.neurons[0] == INPUT_NEURONS);
      assert(this.neurons[this.neurons.length - 1] == OUTPUT_NEURONS);
    }
    
    @Override
    public String toString() {
      return "(Learning Rate = " + this.learningRate + ", Neurons = " + Arrays.toString(this.neurons) + ")";
    }
  }
  
  private static class IndividualFactory implements GeneticAlgorithm.Factory<Individual> {
    
    private IndividualFactory() {}

    @Override
    public Individual random() {
      int layers = (int) (Math.random() * (LAYERS_MAX - LAYERS_MIN) + LAYERS_MIN);    
      int[] neurons = new int[layers + 2];
      
      neurons[0] = INPUT_NEURONS;
      neurons[neurons.length - 1] = OUTPUT_NEURONS;
      
      for (int i = 1; i < layers + 1; i++) {
        neurons[i] = (int) (Math.random() * (LAYER_NEURONS_MAX - LAYER_NEURONS_MIN) + LAYER_NEURONS_MIN);
      }

      double learningRate = (Math.random() * (LEARNING_RATE_MAX - LEARNING_RATE_MIN) + LEARNING_RATE_MIN);

      return new Individual(neurons, learningRate);
    }

    @Override
    public Individual mutate(Individual individual) {
      int type = (int) (Math.random() * 4);

      // Add Layer
      if (type == 1) {
        int[] neurons = new int[individual.neurons.length + 1];
        int layer = (int) ((neurons.length - 2) * Math.random() + 1);
        int j = 0;
        for (int i = 0; i < individual.neurons.length + 1; i++) {
          if (i != layer) {
            neurons[i] = individual.neurons[j++];
          } else {
            neurons[i] = (int) (Math.random() * (LAYER_NEURONS_MAX - LAYER_NEURONS_MIN) + LAYER_NEURONS_MIN);
          }
        }
        return new Individual(neurons, individual.learningRate);

      // Remove Layer
      } else if (type == 2) {
        if (individual.neurons.length == 2) return individual;
        
        int[] neurons = new int[individual.neurons.length - 1];
        int layer = (int) ((neurons.length - 2) * Math.random()) + 1;
        int j = 0;
        for (int i = 0; i < individual.neurons.length; i++) {
          if (i != layer) {
            neurons[j++] = individual.neurons[i];
          }
        }
        return new Individual(neurons, individual.learningRate);

      // Change layer size
      } else if (type == 3) {
        if (individual.neurons.length == 2) return individual;

        int[] neurons = individual.neurons.clone();
        int layer = (int) ((neurons.length - 2) * Math.random()) + 1;
        neurons[layer] = (int) (neurons[layer] + neurons[layer] * (Math.random() - 0.5));
        return new Individual(neurons, individual.learningRate);

      // Change learning rate
      } else {
        double learningRate = Math.min(Math.max(individual.learningRate + individual.learningRate * (Math.random() - 0.5), LEARNING_RATE_MIN), LEARNING_RATE_MAX);
        return new Individual(individual.neurons, learningRate);
      }
    }

    @Override
    public double fitness(Individual individual) {
      double errorSum = 0;

      for (int i = 0; i < FITNESS_TRIALS; i++) {
        NeuralNetwork n = new NeuralNetwork(individual.learningRate, individual.neurons);

        for (int j = 0; j < FITNESS_TRAINING_SAMPLES; j++) {
          Gesture gesture = GestureStore.instance.getRandomTrainingGesture();
          double[] input = gesture.input;
          double[] outputTarget = gesture.targetOutput;

          n.train(input, outputTarget);
        }

        for (Gesture gesture : GestureStore.instance.getAllTestingGestures()) {
          double[] input = gesture.input;
          double[] outputTarget = gesture.targetOutput;
          double[] outputActual = n.classify(input);

          errorSum += Utilities.calculateError(outputTarget, outputActual);
        }
      }
    
      return 1 / errorSum;
    }

    @Override
    public Individual crossover(Individual a, Individual b) {
      int type = (int) (Math.random() * 2);

      if (type == 1) {
        int aLayer = (int) ((a.neurons.length - 2) * Math.random()) + 1;
        int bLayer = (int) ((b.neurons.length - 2) * Math.random()) + 1;

        int[] neurons = new int[aLayer + b.neurons.length - bLayer];
        for (int i = 0; i < neurons.length; i++) {
          if (i < aLayer) {
            neurons[i] = a.neurons[i];
          } else {
            neurons[i] = b.neurons[bLayer + (i - aLayer)];
          }
        }
        return new Individual(neurons, a.learningRate);

      // Combine learning rates
      } else {
        double learningRate = (a.learningRate + b.learningRate) / 2;
        return new  Individual(a.neurons, learningRate);
      }
    }
    
  }
}

