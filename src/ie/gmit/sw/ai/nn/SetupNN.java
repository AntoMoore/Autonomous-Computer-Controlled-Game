package ie.gmit.sw.ai.nn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import ie.gmit.sw.ai.activator.Activator;

public class SetupNN {
	// reference to files
	public static final String TRAINING_FILE = "./resources/neural/training.txt";
	public static final String EXPECTED_FILE = "./resources/neural/expected.txt";

	// load
	public static NeuralNetwork loadNN() throws FileNotFoundException {
		
		double[][] trainingData = readFromFile(TRAINING_FILE, 180, 3);
		double[][] expectedResults = readFromFile(EXPECTED_FILE, 180, 4);
		
		// create network with an input layer of 3 nodes, hidden layer of 4 nodes and an output layer of 4 nodes
		// use sigmoid activation since the data is non-linear
		NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 4, 4);
		
		// Instantiate back prop training algorithm
		BackpropagationTrainer trainer = new BackpropagationTrainer(nn);
		trainer.train(trainingData, expectedResults, 0.001, 1000000);
		
		return nn;

	}

	public static double[][] readFromFile(String filePath, int rows, int cols) throws FileNotFoundException {
		// read file
		Scanner sc = new Scanner(new BufferedReader(new FileReader(filePath)));
		double[][] data = new double[rows][cols];
		
		// loop through file
		while (sc.hasNextLine()) {
			for (int i = 0; i < data.length; i++) {
				// store each line as a string and remove commas
				String[] line = sc.nextLine().trim().split(", ");
				
				// store individual values from string into array
				for (int j = 0; j < line.length; j++) {
					data[i][j] = Double.parseDouble(line[j]);
				}
			}
		}
		return data;
	}

}
