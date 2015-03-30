package org.braintrust;

public class Gesture { 
  
  public final double[] input; 
  public final double[] targetOutput;
  public final GestureType type;
  
  public Gesture(double[] input, GestureType type) { 
    this.input = input; 
    this.type = type;
    
    this.targetOutput = new double[GestureType.values().length]; 
    this.targetOutput[type.ordinal()] = 1;
  } 

} 
