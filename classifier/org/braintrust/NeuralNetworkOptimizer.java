package org.braintrust;

import java.util.Arrays;
import java.util.Comparator;

public class NeuralNetworkOptimizer {
  
  private static final int LAYERS_MIN = 0;
  private static final int LAYERS_MAX = 3;
  
  private static final int LAYER_NEURONS_MIN = 1;
  private static final int LAYER_NEURONS_MAX = 30;
  
  private static final double LEARNING_RATE_MIN = 0.1;
  private static final double LEARNING_RATE_MAX = 1;
  
  static final int POPULATIONS = 100;
  private static final int POPULATION_SIZE = 25;
  
  private static final int INPUT_NEURONS = 2;
  private static final int OUTPUT_NEURONS = 1;
  
  private static final double MUTATION_RATE = 0.05;
  
  static final int FITNESS_TRIALS = 25;
  static final int FITNESS_TRAINING_SAMPLES = 5000;
  static final int FITNESS_TESTING_SAMPLES = 100;
  
  
  public static void main(String[] args) {    
    Population p = Population.random();
    for (int i = 0; i < 10; i++) {
      Tuple <Individual, Double> fittest = p.getFittest();
      System.out.println("The fittest individual in generation " + i + " is " + fittest.x + " with a fitness of " + fittest.y);
      p = p.next();
    }
  }
  
  public static Tuple<double[], double[]> getRandom() {
    double[] input = new double[]{ Math.random() * 2 - 1, Math.random() * 2 - 1 };
    double[] output = new double[]{ Math.sin(input[0]) * Math.cos(input[1]) };
    return new Tuple(input, output);
  }

  public static double calculateFitness(Individual individual) {
    double errorSum = 0;

    for (int i = 0; i < FITNESS_TRIALS; i++) {
      NeuralNetwork n = new NeuralNetwork(individual.learningRate, individual.neurons);

      for (int j = 0; j < FITNESS_TRAINING_SAMPLES; j++) {
        Tuple<double[], double[]> r = getRandom();
        double[] input = r.x;
        double[] outputTarget = r.y;
        
        n.train(input, outputTarget);
      }

      for (int j = 0; j < FITNESS_TESTING_SAMPLES; j++) {
        Tuple<double[], double[]> r = getRandom();
        double[] input = r.x;
        double[] outputTarget = r.y;
        double[] outputActual = n.classify(input);
        
        errorSum += Utilities.calculateError(outputTarget, outputActual);
      }
    }
    
    return 1 / errorSum;
  }
   
  private static class Individual {

    public static Individual combine(Individual a, Individual b) {
      int type = (int) (Math.random() * 2);

      // Crossover neurons
      if (type == 1) {
        int aLayer = (int) ((a.neurons.length - 1) * Math.random()) + 1;
        int bLayer = (int) ((b.neurons.length - 1) * Math.random());

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

      double learningRate = (Math.random() * (LEARNING_RATE_MAX - LEARNING_RATE_MIN) + LEARNING_RATE_MIN);

      return new Individual(neurons, learningRate);
    }

    public Individual mutate() {
      int type = (int) (Math.random() * 4);

      // Add Layer
      if (type == 1) {
        int[] neurons = new int[this.neurons.length + 1];
        int layer = (int) ((neurons.length - 2) * Math.random() + 1);
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
        if (neurons.length == 2) return this;
        
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
        double learningRate = Math.min(Math.max(this.learningRate + this.learningRate * (Math.random() - 0.5), LEARNING_RATE_MIN), LEARNING_RATE_MAX);
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
    
    @Override
    public String toString() {
      return "(Learning Rate = " + this.learningRate + ", Neurons = " + Arrays.toString(this.neurons) + ")";
    }
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
    
    private final double[] fitnesses;
    private final double fitnessesSum;
    private final int fitnessesMaxIndex;
    
    Population(Individual[] individuals) {
      this.individuals = individuals;
      this.fitnesses = new double[individuals.length];
      
      double fitnessesSum = 0;
      double fitnessesMax = 0;
      int fitnessesMaxIndex = -1;
      
      for (int i = 0; i < POPULATION_SIZE; i++) {
        double fitness = calculateFitness(this.individuals[i]);
        fitnesses[i] = fitness;
        fitnessesSum += fitness;
        if (fitness > fitnessesMax) {
          fitnessesMax = fitness;
          fitnessesMaxIndex = i;
        }
      }
      
      assert(fitnessesMaxIndex != -1);
      
      this.fitnessesSum = fitnessesSum;
      this.fitnessesMaxIndex = fitnessesMaxIndex; 
    }
        
    public Tuple<Individual, Double> getFittest() {
      return new Tuple(
        individuals[fitnessesMaxIndex],
        fitnesses[fitnessesMaxIndex]
      );
    }
    
    Population next() {
      Individual[] individualsNew = new Individual[POPULATION_SIZE];
      individualsNew[0] = getFittest().x;
            
      for (int i = 1; i < POPULATION_SIZE; i++) {
        Individual individualA = Utilities.roulette(fitnesses, fitnessesSum, individuals);
        Individual individualB = Utilities.roulette(fitnesses, fitnessesSum, individuals);
        
        Individual individual = Individual.combine(individualA, individualB);
        
        if (Math.random() < MUTATION_RATE) {
          individual = individual.mutate();
        }
        
        individualsNew[i] = individual;
      }

      return new Population(individualsNew);
    }
  }
}