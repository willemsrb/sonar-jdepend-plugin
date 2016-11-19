package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import jdepend.framework.JavaPackage;
import nl.futureedge.sonar.plugin.jdepend.JdependRulesDefinition;

/**
 * Efferent coupling rule.
 *
 * @see JavaPackage#efferentCoupling()
 */
public class EfferentCouplingsRule extends AbstractRule implements Rule {

	private static final Logger LOGGER = Loggers.get(EfferentCouplingsRule.class);

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public EfferentCouplingsRule(final SensorContext context) {
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

		final int efferentCoupling = javaPackage.efferentCoupling();

		if (efferentCoupling > maximum) {
			final NewIssue issue = getContext().newIssue().forRule(getKey());
			issue.at(issue.newLocation().on(packageInfoFile).at(packageInfoFile.selectLine(1))
					.message("Too much efferent coupling (allowed: " + maximum + ", actual: "
							+ javaPackage.getClassCount() + ")"));
			issue.save();
		}
	}
}
