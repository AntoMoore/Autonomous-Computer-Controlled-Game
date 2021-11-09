package ie.gmit.sw.ai.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;

public class SetupFCL {
	// reference to file
	public static final String FCL_FILE = "./resources/fuzzy/aggressionLevel.fcl";
	
	// load 
	public static FIS loadFCL() {
		FIS fis = FIS.load(FCL_FILE, true);
		return fis;
		
	}
}
