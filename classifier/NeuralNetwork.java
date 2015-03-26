import java.util.Collections;

public class NeuralNetwork {

  // Create a new NeuralNetwork with some number of layers, all
  // weights randomly initialized.
  public NeuralNetwork(int[] neuronCounts) {
    int layerCount = neuronCounts.length;

    // Refactor to use a flat float array of floats with a custom indexing
    // scheme
    this.weights = new ArrayList<float[][]>;

    // Iterating over layers
    for (int i = 0; i < layerCount - 1; i++) {
      int layerNeuronCount = neuronCounts[i]
      int previousLayerNeuronCount = neuronCounts[i + 1]

      this.weights[i] = new float[layerNeuronCount][previousLayerNeuronCount];
      // Iterating over nodes in this layer
      for (int j = 0; j < layerNeuronCount; j++) {
        for (int k = 0; k < previousLayerNeuronCount; k++) {
          this.weights[i][j][k] = Math.random();
        }
      }
    }
  }

  public NeuralNetwork(ArrayList<float[][]> weights) {
    this.weights = weights;
  }

  // Returns the error after updating weights
  public float train(float[] inputs, float[] outputsTarget) {
    int layersCount = this.weights.length;

    ArrayList<float[][]> layerOutputs = new ArrayList<float[][]>();
    classify(inputs, layerOutputs);

    // Calculate sensitivities for the output nodes
    int outputNeuronCount = this.weights[layersCount - 1].length;
    float[] nextLayerSensitivities = new float[outputNeuronCount];
    for (int i = 0; i < outputNeuronCount; i++) {
      nextLayerSensitivities[i] = errorFunctionPartialDerivitive(
        layerOutputs[layerOutputs.length - 1][i],
        outputsTarget[i]
      );
    }

    for (int i = layerCount - 1; i >= 0; i--) {
      int layerNeuronCount = this.weights[i].length;
      int nextLayerNeuronCount = nextLayerSensitivities.length;

      float[] previousLayerOutputs = layerOutputs[i - 1];
      float[] layerOutputs = layerOutputs[i];
      float[] nextLayerSensitivities = new float[layerNeuronCount];

      for (int j = 0; j < neuronCount; j++) {

        // Calculate the sensitivity of this node
        float sensitivity = 0;
        for (int k = 0; k < nextLayerNeuronCount; k++) {
          sensitivity += nextLayerSensitivities[k] * this.weights[i][j][k];
        }
        sensitivity *= activationFunctionDerivitive(previousLayerOutputs[j]);
        nextLayerSensitivities[j] = sensitivity;

        // TODO which sensitivity should we use here?
        for (int k = 0; k < nextLayerNeuronCount; k++) {
          this.weights[i][j][k] += sensitivity * activationFunction(previousLayerOutputs[j]);
        }

      }

      nextLayerSensitivities = nextLayerSensitivities;
    }
  }

  public float[] classify(float[] inputs, ArrayList<float[]> layerOutputs) {
    assert(layerOutputs.isEmpty());

    int layersCount = this.weights.length;

    for (int i = 0; i < layersCount; i++) {


      int neuronCount = this.weights[i].length;

      int lengthInputs = inputs.length;
      int lengthOutputs = this.weights[i].length;

      float[] outputs = new float[lengthOutputs];
      layerOutputs[i] = new float[lengthOutputs];

      // Iterating over each neuron in layer
      for (int j = 0; j < neuronCount; j++) {
        assert(lengthInputs == this.weights[i][j].length);

        // Iterating over each input value
        float output = 0;
        for (int k = 0; k < this.weights[i][j].length; k++) {
          output += inputs[k] * this.weights[i][j][k]
        }
        layerOutputs[i][j] = output;
        outputs[j] = activationFunction(output);

      }

      inputs = outputs;
    }

    return layerOutputs[layerOutputs.length - 1];
  }

  // (layers) * (nodes in layer) * (nodes in previous layer)
  private ArrayList<float[][]> weights;

  private float activationFunction(float input) {
    return 1 / (1 + Math.exp(-input));
  }

  private float activationFunctionDerivitive(float input) {
    return Math.exp(input) / Math.pow(1 + Math.exp(-input), 2);
  }

  private float errorFunctionPartialDerivitive(float actual, float target) {
    return 2 * (actual - target);
  }

  // Uses sum of squares, but other formulas are possible
  private float errorFunction(float[] outputsActual, float[] outputsTarget) {
    assert(outputsActual.length == outputsTarget.length);
    int length = outputsActual.length;

    float error = 0;
    for (int i = 0; i < length; i++) {
      error += Math.pow(outputsActual[i] - outputsTarget[i], 2);
    }

    return error;
  }

}
