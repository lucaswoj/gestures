out: classifier/NeuralNetwork.java classifier/NeuralNetworkTester.java
	javac classifier/NeuralNetworkTester.java classifier/NeuralNetwork.java
	(cd classifier && java NeuralNetworkTester)

neuroph: classifier/NeurophTester.java
	javac -cp "neuroph-2.9/*:neuroph-2.9/libs/*" classifier/NeurophTester.java
	(cd classifier && java -cp ".:../neuroph-2.9/*:../neuroph-2.9/libs/*" NeurophTester)