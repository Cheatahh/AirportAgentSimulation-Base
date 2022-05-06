module dhbw.sose2022.softwareengineering.AASBase {
	
	requires java.base;
	requires java.logging;
	requires java.desktop;
	
	requires org.apache.commons.lang3;
	
	requires simple.xml;
	
	exports dhbw.sose2022.softwareengineering.api;
	exports dhbw.sose2022.softwareengineering.api.geometry;
	exports dhbw.sose2022.softwareengineering.api.plugin;
	exports dhbw.sose2022.softwareengineering.api.simulation;
	exports dhbw.sose2022.softwareengineering.api.simulation.entity;
	exports dhbw.sose2022.softwareengineering.api.simulation.message;
	
}
