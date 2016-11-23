package packagedependencycycle.packageb;

import packagedependencycycle.packagea.ClassA;

public class ClassB {

	public ClassA toClassA() {
		return new ClassA();
	}
}
