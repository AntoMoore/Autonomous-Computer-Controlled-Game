package ie.gmit.sw.ai;

import ie.gmit.sw.ai.fuzzy.SetupFCL;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.SetupNN;
import javafx.application.Application;
import net.sourceforge.jFuzzyLogic.FIS;
import java.io.FileNotFoundException;

public class Runner {
	// static variables
	@SuppressWarnings("exports")
	public static FIS fis;
	public static NeuralNetwork nn;
	
	public static void main(String[] args) throws FileNotFoundException {
		// setup FCL
		fis = SetupFCL.loadFCL();
		
		// setup Neural network
		nn = SetupNN.loadNN();
		
		// launch game
		Application.launch(GameWindow.class, args);
	}	
}