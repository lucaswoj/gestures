package org.braintrust;

import java.util.Arrays;
import java.util.Comparator;

public class NeuralNetworkOptimizer {
  
  private static final int LAYERS_MIN = 0;
  private static final int LAYERS_MAX = 5;
  
  private static final int LAYER_NEURONS_MIN = 1;
  private static final int LAYER_NEURONS_MAX = 100;
  
  private static final double LEARNING_RATE_MIN = 0.1;
  private static final double LEARNING_RATE_MAX = 1;
  
  private static final int POPULATION_SIZE = 10;
  
  private static final int INPUT_NEURONS = 1;
  private static final int OUTPUT_NEURONS = 1;
  
  private static final double MUTATION_RATE = 0.1;
  
  public static void main(String[] args) {
    System.out.println("TEST");
    
    Population p = Population.random();
    Population p1 = p.next();
    Population p2 = p1.next();
    Population p3 = p2.next();
    Population p4 = p3.next();
  }
  
  public static double[] getTargetOutput(double[] input) {
    return new double[]{ Math.sin(input[0]) };
  }

  public static double[] getRandomInput() {
    return new double[]{ Math.random() };
  }

  public static double calculateFitness(Individual individual) {
    final int TRIALS = 50;
    final int TRAINING_SAMPLES = 5000;
    final int TESTING_SAMPLES = 50;

    double error = 0;

    for (int i = 0; i < TRIALS; i++) {
      NeuralNetwork n = new NeuralNetwork(individual.learningRate, individual.neurons);

      for (int j = 0; j < TRAINING_SAMPLES; j++) {
        double[] input = getRandomInput();
        double[] outputTarget = getTargetOutput(input);
        n.train(input, outputTarget);
      }

      for (int j = 0; j < TESTING_SAMPLES; j++) {
        double[] input = getRandomInput();
        double[] outputTarget = getTargetOutput(input);
        double[] outputActual = n.classify(input);
        error += calculateError(outputTarget, outputActual);
      }
    }
    
    return error / TESTING_SAMPLES / TRIALS;
  }
  
   private static double calculateError(double[] outputsActual, double[] outputsTarget) {
    assert(outputsActual.length == outputsTarget.length);
    int length = outputsActual.length;

    double error = 0;

    for (int i = 0; i < length; i++) {
      error += Math.pow(outputsTarget[i] - outputsActual[i], 2);
    }

    return 0.5 * error;
  }

  private static class Population {

    public static Population random() {
      Individual[] individuals = new Individual[POPULATION_SIZE];
      for (int i = 0; i < POPULATION_SIZE; i++) {
        individuals[i] = Individual.random();
      }
      return new Population(individuals);
    }
    
    private final Individual[] individuals;
    
    Population(Individual[] individuals) {
      this.individuals = individuals;
    }
    
    Population next() {
      Individual[] individualsNew = new Individual[POPULATION_SIZE];
      Individual[] individualsOld = this.individuals;
      
      double[] individualsOldFitness = new double[POPULATION_SIZE];
      double individualsOldFitnessSum = 0;
      for (int i = 0; i < POPULATION_SIZE; i++) {
        individualsOldFitness[i] = calculateFitness(individualsOld[i]);
        individualsOldFitnessSum += individualsOldFitness[i];
      }
      
      for (int i = 0; i < POPULATION_SIZE; i++) {
        
        double individualASeed = Math.random() * individualsOldFitnessSum;
        Individual individualA;
        
        double individualBSeed = Math.random() * individualsOldFitnessSum;
        Individual individualB;
        
        for (int j = 0; j < POPULATION_SIZE; j++) {
          individualASeed -= individualsOldFitness[j];
          if (individualASeed <= 0) {
            individualA = individualsOld[j];
            break;
          }
        }
        
        for (int j = 0; j < POPULATION_SIZE; j++) {
          individualBSeed -= individualsOldFitness[j];
          if (individualBSeed <= 0) {
            individualB = individualsOld[j];
            break;
          }
        }
        
        Individual individual = Individual.combine(individualA, individualB);
        
        if (Math.random() < MUTATION_RATE) {
          individual = individual.mutate();
        }
        
        individualsNew[i] = individual;
      }

      return new Population(individualsNew);
    }
  }

  private static class Individual {

    public static Individual combine(Individual a, Individual b) {
      int type = (int) (Math.random() * 2);

      // Crossover neurons
      if (type == 1) {
        int aLayer = (int) ((a.neurons.length) * Math.random());
        int bLayer = (int) ((b.neurons.length) * Math.random());

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

    public static Individual random() {
      int layers = (int) (Math.random() * (LAYERS_MAX - LAYERS_MIN) + LAYERS_MIN);    
      int[] neurons = new int[layers + 2];
      
      neurons[0] = INPUT_NEURONS;
      neurons[neurons.length - 1] = OUTPUT_NEURONS;
      
      for (int i = 1; i < layers + 1; i++) {
        neurons[i] = (int) (Math.random() * (LAYER_NEURONS_MAX - LAYER_NEURONS_MIN) + LAYER_NEURONS_MIN);
      }

      int learningRate = (int) (Math.random() * (LEARNING_RATE_MAX - LEARNING_RATE_MIN) + LEARNING_RATE_MIN);

      return new Individual(neurons, learningRate);
    }

    public Individual mutate() {
      int type = (int) (Math.random() * 4);

      // Add Layer
      if (type == 1) {
        int[] neurons = new int[this.neurons.length + 1];
        int layer = (int) ((neurons.length + 1 - 2) * Math.random() + 1);
        int j = 0;
        for (int i = 0; i < this.neurons.length + 1; i++) {
          if (i != layer) {
            neurons[i] = this.neurons[j++];
          } else {
            neurons[i] = (int) (Math.random() * (LAYER_NEURONS_MAX - LAYER_NEURONS_MIN) + LAYER_NEURONS_MIN);
          }
        }
        return new Individual(neurons, this.learningRate);

      // Remove Layer
      } else if (type == 2) {
        int[] neurons = new int[this.neurons.length - 1];
        int layer = (int) ((neurons.length - 2) * Math.random()) + 1;
        int j = 0;
        for (int i = 0; i < this.neurons.length; i++) {
          if (i != layer) {
            neurons[j++] = this.neurons[i];
          }
        }
        return new Individual(neurons, this.learningRate);

      // Change layer size
      } else if (type == 3) {
        if (neurons.length == 2) return this;

        int[] neurons = this.neurons.clone();
        int layer = (int) ((neurons.length - 2) * Math.random()) + 1;
        neurons[layer] = (int) (neurons[layer] + neurons[layer] * (Math.random() - 0.5));
        return new Individual(neurons, this.learningRate);

      // Change learning rate
      } else {
        double learningRate = this.learningRate + this.learningRate * (Math.random() - 0.5);
        return new Individual(this.neurons, learningRate);
      }
    }

    final private int[] neurons;
    final private double learningRate;

    public Individual(int[] neurons, double learningRate) {
      this.neurons = neurons;
      this.learningRate = learningRate;
      
      assert(this.neurons[0] == INPUT_NEURONS);
      assert(this.neurons[this.neurons.length - 1] == OUTPUT_NEURONS);
    }

  }
}