out: classifier/NeuralNetwork.java classifier/NeuralNetworkTester.java
	javac classifier/NeuralNetworkTester.java classifier/NeuralNetwork.java
	(cd classifier && java NeuralNetworkTester)

gesture: 
	(cd classifier/org/braintrust && javac -cp ../../../jars/json.jar InputProcessor.java Tuple.java GestureStore.java)
	(cd classifier/org/braintrust && java -cp ../../../jars/json.jar:. InputProcessor)
