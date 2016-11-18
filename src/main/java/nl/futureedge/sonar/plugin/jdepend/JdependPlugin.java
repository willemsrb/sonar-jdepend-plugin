package nl.futureedge.sonar.plugin.jdepend;

import org.sonar.api.Plugin;

/**
 * jDepend plugin.
 */
public final class JdependPlugin implements Plugin {
	
	@Override
	public void define(final Context context) {
		context.addExtensions(JdependSensor.class, JdependRulesDefinition.class);
	}
}
