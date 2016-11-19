package nl.futureedge.sonar.plugin.jdepend.test.coupling.packageb;

import nl.futureedge.sonar.plugin.jdepend.test.coupling.packagee.ClassE;

public class ClassB {
	ClassE toClassE() {
		return new ClassE();
	}
}
