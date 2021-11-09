package ie.gmit.sw.ai.fuzzy;

import ie.gmit.sw.ai.Runner;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzyAggression {

	public double calculateAggression(double distanceFromPlayer, int nearbyAllies) {
		//create function block
		FunctionBlock fb = Runner.fis.getFunctionBlock("getAggressionLevel");
		
		// set variables
		Runner.fis.setVariable("distance", distanceFromPlayer); // Apply a value to a variable
		Runner.fis.setVariable("allies", nearbyAllies);
		Runner.fis.evaluate(); // Execute the fuzzy inference engine
		
		// calculate aggression
		Variable aggression = fb.getVariable("aggression");
		return aggression.getValue();
	}

}
