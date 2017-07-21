package packagedependencycycle.packagea;

import packagedependencycycle.packageb.ClassB;

public class ClassA {

    public ClassB toClassB() {
        return new ClassB();
    }

    public String toString() {
        return "ClassA";
    }
}
