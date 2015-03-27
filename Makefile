out: classifier/NeuralNetwork.java classifier/NeuralNetworkTester.java
	javac classifier/NeuralNetworkTester.java classifier/NeuralNetwork.java
	(cd classifier && java NeuralNetworkTester)