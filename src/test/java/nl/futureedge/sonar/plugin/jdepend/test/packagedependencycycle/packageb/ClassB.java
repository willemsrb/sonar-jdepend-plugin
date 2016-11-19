package nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packageb;

import nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packagea.ClassA;

public class ClassB {

	public ClassA toClassA() {
		return new ClassA();
	}
}
