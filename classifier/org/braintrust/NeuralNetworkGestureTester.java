package org.braintrust;

public class NeuralNetworkGestureTester {

    private static GestureStore gestureStore = new GestureStore();
    
	public static void main(String[] args) {
            
            
            NeuralNetwork n = new NeuralNetwork(0.5, new int[]{ 1350, 10, 5, 1 });

            for (int i = 0; i < 10; i++) {
                Tuple<double[], Integer> data = gestureStore.getRandomTrainingData(GestureStore.Gesture.FLICK_FORWARD);
                double[] input = data.x;
                n.train(data.x, new double[]{1});
            }
            
            for (int i = 0; i < 10; i++) {
                Tuple<double[], Integer> data = gestureStore.getRandomTestData(GestureStore.Gesture.FLICK_FORWARD);
                double result = n.classify(data.x)[0];
                System.out.println("result: " + result);
            }
            
            
	}
}