package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import jdepend.framework.JavaPackage;
import nl.futureedge.sonar.plugin.jdepend.JdependRulesDefinition;

/**
 * Instability rule.
 *
 * @see JavaPackage#instability()
 */
public class InstabilityRule extends AbstractRule implements Rule {

	private static final Logger LOGGER = Loggers.get(InstabilityRule.class);

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public InstabilityRule(final SensorContext context) {
		super(context, JdependRulesDefinition.NUMBER_OF_CLASSES_AND_INTERFACES_RULE);

		maximum = getParamAsInteger(JdependRulesDefinition.PARAM_MAXIMUM);
		if (maximum == null) {
			LOGGER.info("Rule activated, no value for parameter {} set. Disabling rule...",
					JdependRulesDefinition.PARAM_MAXIMUM);
			disable();
		}
	}

	@Override
	public void execute(final JavaPackage javaPackage, final InputFile packageInfoFile) {
		if (!isActive()) {
			return;
		}

		final int instability = Math.round(javaPackage.instability() * 100);

		if (instability > maximum) {
			final NewIssue issue = getContext().newIssue().forRule(getKey());
			issue.at(issue.newLocation().on(packageInfoFile).at(packageInfoFile.selectLine(1))
					.message("Too much instability (allowed: " + maximum + ", actual: " + instability + ")"));
			issue.save();
		}
	}
}
