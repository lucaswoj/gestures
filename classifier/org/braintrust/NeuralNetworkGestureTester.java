package org.braintrust;

public class NeuralNetworkGestureTester {

    private static GestureStore gestureStore = new GestureStore();
    
	public static void main(String[] args) {
            Tuple<double[], Integer> data = gestureStore.getRandom();
            double[] input = data.x;
            
            NeuralNetwork n = new NeuralNetwork(0.5, new int[]{ input.length, 5, 1 });

            n.train(data.x, new double[]{1});

            double result = n.classify(data.x)[0];
            System.out.println("result: " + result);
	}
}