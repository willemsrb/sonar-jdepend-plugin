package nl.futureedge.sonar.plugin.jdepend.test.coupling.packagea;

import nl.futureedge.sonar.plugin.jdepend.test.coupling.packageb.ClassB;
import nl.futureedge.sonar.plugin.jdepend.test.coupling.packagec.ClassC;
import nl.futureedge.sonar.plugin.jdepend.test.coupling.packaged.ClassD;

public class ClassA {

	ClassB toClassB() {
		return new ClassB();
	}

	ClassC toClassC() {
		return new ClassC();
	}

	ClassD toClassD() {
		return new ClassD();
	}
}
