package nl.futureedge.sonar.plugin.jdepend.test.coupling.packagec;

import nl.futureedge.sonar.plugin.jdepend.test.coupling.packagee.ClassE;

public class ClassC {
	ClassE toClassE() {
		return new ClassE();
	}

}
