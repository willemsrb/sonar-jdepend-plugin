package nl.futureedge.sonar.plugin.jdepend;

import org.sonar.api.Plugin;

import nl.futureedge.sonar.plugin.jdepend.packages.PackagesComputer;
import nl.futureedge.sonar.plugin.jdepend.packages.PackagesMetrics;
import nl.futureedge.sonar.plugin.jdepend.packages.PackagesSensor;
import nl.futureedge.sonar.plugin.jdepend.rules.Rules;

/**
 * jDepend plugin.
 */
public final class JdependPlugin implements Plugin {

	@Override
	public void define(final Context context) {
		// JDepend sensor and rules
		context.addExtensions(JdependSensor.class, Rules.class);

		// Supporting extensions for package metrics
		context.addExtensions(PackagesMetrics.class, PackagesSensor.class, PackagesComputer.class);
	}
}
