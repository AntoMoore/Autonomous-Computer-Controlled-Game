module gmit.software {
	requires javafx.base;
	requires javafx.graphics;
	requires transitive javafx.controls;
	requires jFuzzyLogic;
	requires encog.core;
	
	exports ie.gmit.sw.ai;
	exports ie.gmit.sw.ai.activator;
	exports ie.gmit.sw.ai.nn;
}