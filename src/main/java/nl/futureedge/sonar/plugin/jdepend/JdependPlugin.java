package nl.futureedge.sonar.plugin.jdepend;

import org.sonar.api.Plugin;

import nl.futureedge.sonar.plugin.jdepend.computer.JdependComputer;
import nl.futureedge.sonar.plugin.jdepend.computer.PackagesComputer;
import nl.futureedge.sonar.plugin.jdepend.metrics.JdependMetrics;
import nl.futureedge.sonar.plugin.jdepend.metrics.PackagesMetrics;
import nl.futureedge.sonar.plugin.jdepend.rules.Rules;
import nl.futureedge.sonar.plugin.jdepend.sensor.JdependSensor;
import nl.futureedge.sonar.plugin.jdepend.sensor.PackagesSensor;

/**
 * jDepend plugin.
 */
public final class JdependPlugin implements Plugin {

	@Override
	public void define(final Context context) {
		// Jdepend sensor and rules
		context.addExtensions(JdependSensor.class, Rules.class);
		// Jdepend metrics
		context.addExtensions(JdependMetrics.class, JdependComputer.class);

		// Supporting extensions for package metrics
		context.addExtensions(PackagesMetrics.class, PackagesSensor.class, PackagesComputer.class);
	}
}
