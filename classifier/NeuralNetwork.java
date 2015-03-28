import java.util.Collections;
import java.util.ArrayList;

public class NeuralNetwork {

  final double learningRate;

  // Create a new NeuralNetwork with some number of layers, all
  // weights randomly initialized.
  public NeuralNetwork(double learningRate, int[] neuronCounts) {
    this.learningRate = learningRate;
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
          layerWeights[j][k] = 0.5;
        }
      }

      this.weights.add(layerWeights);
    }
  }

  public NeuralNetwork(double learningRate, ArrayList<double[][]> weights) {
    this.learningRate = learningRate;
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
        layersOutputs.get(layersOutputs.size() - 1)[i],
        outputsTarget[i]
      ) * calculateActivationDerivitive(layersOutputs.get(layersOutputs.size() - 1)[i]);
      assert(!Double.isNaN(nextLayerSensitivities[i]));
    }

    for (int l = this.weights.size() - 1; l >= 0; l--) {
      int previousLayerNeuronCount = this.weights.get(l)[0].length;
      int nextLayerNeuronCount = this.weights.get(l).length;
      assert(nextLayerSensitivities.length == nextLayerNeuronCount);

      double[] previousLayerOutputs = layersOutputs.get(l);
      double[] previousLayerSensitivities = new double[previousLayerNeuronCount];

      // Iterate over neurons in the previous layer
      for (int j = 0; j < previousLayerNeuronCount; j++) {

        // Calculate the sensitivity of this node
        double sensitivity = 0;
        for (int i = 0; i < nextLayerNeuronCount; i++) {
          sensitivity += nextLayerSensitivities[i] * this.weights.get(l)[i][j];
        }
        sensitivity *= calculateActivationDerivitive(previousLayerOutputs[j]);
        assert(!Double.isNaN(sensitivity));
        previousLayerSensitivities[j] = sensitivity;

        for (int i = 0; i < nextLayerNeuronCount; i++) {
          double weightDelta = learningRate * sensitivity * calculateActivation(previousLayerOutputs[j]);
          this.weights.get(l)[i][j] += weightDelta;
          assert(!Double.isNaN(this.weights.get(l)[i][j]) && !Double.isInfinite(this.weights.get(l)[i][j]));
        }
      }

      nextLayerSensitivities = previousLayerSensitivities;
    }
  }

  public double[] classify(double[] inputs) {
    ArrayList<double[]> layersOutputsUnactivated = new ArrayList<double[]>();
    return classify(inputs, layersOutputsUnactivated);
  }

  public double[] classify(double[] inputs, ArrayList<double[]> layersOutputsUnactivated) {
    assert(layersOutputsUnactivated.isEmpty());
    assert(inputs.length == this.weights.get(0)[0].length);

    layersOutputsUnactivated.add(inputs);

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

      layersOutputsUnactivated.add(outputsUnactivated);
      inputs = outputs;
    }

    return layersOutputsUnactivated.get(layersOutputsUnactivated.size() - 1);
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
    return (target - actual);
  }

  // Uses sum of squares, but other formulas are possible
  private double calculateError(double[] outputsActual, double[] outputsTarget) {
    assert(outputsActual.length == outputsTarget.length);
    int length = outputsActual.length;

    double error = 0;

    for (int i = 0; i < length; i++) {
      error += Math.pow(outputsTarget[i] - outputsActual[i], 2);
    }

    return 0.5 * error;
  }

}
