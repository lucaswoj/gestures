import java.util.Collections;
import java.util.ArrayList;

public class NeuralNetwork {

  // Create a new NeuralNetwork with some number of layers, all
  // weights randomly initialized.
  public NeuralNetwork(int[] neuronCounts) {
    int layerCount = neuronCounts.length;

    this.weights = new ArrayList<double[][]>();

    // Iterating over layers, skipping the input layer
    for (int i = 0; i < layerCount - 1; i++) {
      int previousLayerNeuronCount = neuronCounts[i];
      int layerNeuronCount = neuronCounts[i + 1];

      // This effectively sets index i
      double[][] layerWeights = new double[layerNeuronCount][previousLayerNeuronCount];
      
      // Iterating over nodes in this layer
      for (int j = 0; j < layerNeuronCount; j++) {
        for (int k = 0; k < previousLayerNeuronCount; k++) {
          layerWeights[j][k] = Math.random();
        }
      }
      
      this.weights.add(layerWeights);
    }
  }

  public NeuralNetwork(ArrayList<double[][]> weights) {
    this.weights = weights;
  }

  // Returns the error after updating weights
  public void train(double[] inputs, double[] outputsTarget) {
    ArrayList<double[]> layersOutputs = new ArrayList<double[]>();
    classify(inputs, layersOutputs);
    
    // Calculate sensitivities for the output nodes
    int outputNeuronCount = this.weights.get(this.weights.size() - 1).length;
    double[] nextLayerSensitivities = new double[outputNeuronCount];
    assert(layersOutputs.get(layersOutputs.size() - 1).length == outputNeuronCount);
    for (int i = 0; i < outputNeuronCount; i++) {
      nextLayerSensitivities[i] = calculateErrorPartialDerivitive(
        calculateActivation(layersOutputs.get(layersOutputs.size() - 1)[i]),
        outputsTarget[i]
      );
      assert(!Double.isNaN(nextLayerSensitivities[i]));
    }

    for (int i = this.weights.size() - 1; i >= 0; i--) {
      double[][] weights = this.weights.get(i);
      
      int previousLayerNeuronCount = weights[0].length;
      int nextLayerNeuronCount = weights.length;
      assert(nextLayerSensitivities.length == nextLayerNeuronCount);

      double[] previousLayerOutputs = layersOutputs.get(i);
      double[] previousLayerSensitivities = new double[previousLayerNeuronCount];

      // Iterate over neurons in the previous layer
      for (int j = 0; j < previousLayerNeuronCount; j++) {

        // Calculate the sensitivity of this node
        double sensitivity = 0;
        for (int k = 0; k < nextLayerNeuronCount; k++) {
          sensitivity += nextLayerSensitivities[k] * weights[k][j];
        }
        sensitivity *= calculateActivationDerivitive(previousLayerOutputs[j]);
        assert(!Double.isNaN(sensitivity));
        previousLayerSensitivities[j] = sensitivity;

        for (int k = 0; k < nextLayerNeuronCount; k++) {
          weights[k][j] += sensitivity * calculateActivation(previousLayerOutputs[j]);
          assert(!Double.isNaN(weights[k][j]) && !Double.isInfinite(weights[k][j]));
        }
      }

      nextLayerSensitivities = previousLayerSensitivities;
    }
  }

  public double[] classify(double[] inputs) {
    return classify(inputs, null);
  }

  public double[] classify(double[] inputs, ArrayList<double[]> layersOutputsUnactivated) {
    assert(layersOutputsUnactivated == null || layersOutputsUnactivated.isEmpty());
    assert(inputs.length == this.weights.get(0)[0].length);
    
    if (layersOutputsUnactivated != null) layersOutputsUnactivated.add(inputs);
    
    int layerCount = this.weights.size();

    for (int i = 0; i < layerCount; i++) {
      int neuronCount = this.weights.get(i).length;
      int lengthInputs = inputs.length;
      int lengthOutputs = this.weights.get(i).length;

      double[] outputsUnactivated = new double[lengthOutputs];
      double[] outputs = new double[lengthOutputs];

      // Iterating over each neuron in layer
      for (int j = 0; j < neuronCount; j++) {
        assert(lengthInputs == this.weights.get(i)[j].length);

        // Iterating over each input value
        double output = 0;
        for (int k = 0; k < inputs.length; k++) {
          output += inputs[k] * this.weights.get(i)[j][k];
        }
        assert(!Double.isNaN(output));
        outputsUnactivated[j] = output;
        outputs[j] = calculateActivation(output);
      }

      if (layersOutputsUnactivated != null) layersOutputsUnactivated.add(outputsUnactivated);
      inputs = outputs;
    }

    return inputs;
  }

  // (layers) * (nodes in layer) * (nodes in previous layer)
  private ArrayList<double[][]> weights;

  private double calculateActivation(double input) {
    return 1 / (1 + Math.exp(-input));
  }

  private double calculateActivationDerivitive(double input) {
    return Math.exp(input) / Math.pow(1 + Math.exp(input), 2);
  }

  private double calculateErrorPartialDerivitive(double actual, double target) {
    return 2 * (target - actual);
  }

  // Uses sum of squares, but other formulas are possible
  private double calculateError(double[] outputsActual, double[] outputsTarget) {
    assert(outputsActual.length == outputsTarget.length);
    int length = outputsActual.length;

    double error = 0;
    for (int i = 0; i < length; i++) {
      error += Math.pow(outputsTarget[i] - outputsActual[i], 2);
    }

    return error;
  }

}
