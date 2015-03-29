package org.braintrust;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneticOptimizer {
  
  private static final ExecutorService executor = Executors.newFixedThreadPool(8);

  static interface Factory<T> {
    T random();
    T mutate(T t);
    double fitness(T t);
    T crossover(T t1, T t2);
  }
  
  static <T> T optimize(Factory<T> factory, int generations, int generationSize, double mutationRate) {
    
    ArrayList<T> individuals = getRandomGeneration(factory, generationSize);
    T fittestIndividual = null;
    
    for (int generation = 0; generation < generations; generation++) {
      
      ArrayList<Double> fitnesses = calculateGenerationFitnesses(factory, individuals);
      int fittestIndex = Utilities.maxIndex(fitnesses);
      fittestIndividual = individuals.get(fittestIndex);
      double fitnessesSum = Utilities.sum(fitnesses);
      
      // Build the next generation
      ArrayList<T> children = new ArrayList<T>(generationSize);
      children.add(fittestIndividual);
      
      while (children.size() < generationSize) {
        T mother = Utilities.roulette(fitnesses, fitnessesSum, individuals);
        T father = Utilities.roulette(fitnesses, fitnessesSum, individuals);
        
        T child = factory.crossover(father, mother);

        if (Math.random() < mutationRate) {
          child = factory.mutate(child);
        }
        
        children.add(child);
      }
      
      individuals = children;
    }
    
    return fittestIndividual;
  }
  
  private static <T> ArrayList<T> getRandomGeneration(Factory<T> factory, int size) {
    ArrayList<T> individuals = new ArrayList<T>(size);
    for (int i = 0; i < size; i++) {
      individuals.add(factory.random());
    }
    return individuals;
  }
  
  private static <T> ArrayList<Double> calculateGenerationFitnesses(Factory<T> factory, ArrayList<T> individuals) {
    ArrayList<Future<Double>> fitnessFutures = new ArrayList<>(individuals.size());
    
    for (final T individual : individuals) {
      fitnessFutures.add(executor.submit(() -> {
        return factory.fitness(individual); 
      }));
    }
    
    ArrayList<Double> fitnesses = new ArrayList<>(individuals.size());
    for (Future<Double> fitnessFuture : fitnessFutures) {
      try {
        fitnesses.add(fitnessFuture.get());
      } catch (ExecutionException | InterruptedException ex) {
        Logger.getLogger(GeneticOptimizer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    assert(fitnesses.size() == individuals.size());
    return fitnesses; 
  }
  
}
