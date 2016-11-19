package nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packagea;

import nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packageb.ClassB;

public class ClassA {

	public ClassB toClassB() {
		return new ClassB();
	}
}
