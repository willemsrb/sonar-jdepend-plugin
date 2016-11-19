package nl.futureedge.sonar.plugin.jdepend.rules;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import jdepend.framework.JavaPackage;
import nl.futureedge.sonar.plugin.jdepend.JdependRulesDefinition;

/**
 * Package dependency cycles rule.
 *
 * @see JavaPackage#containsCycle()
 * @see JavaPackage#collectCycle(List)
 */
public class PackageDependencyCyclesRule extends AbstractRule implements Rule {

	private static final Logger LOGGER = Loggers.get(PackageDependencyCyclesRule.class);

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public PackageDependencyCyclesRule(final SensorContext context) {
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

		if (javaPackage.containsCycle()) {
			final NewIssue issue = getContext().newIssue().forRule(getKey());
			issue.at(issue.newLocation().on(packageInfoFile).at(packageInfoFile.selectLine(1))
					.message(createMessage(javaPackage)));
			issue.save();
		}
	}

	private String createMessage(final JavaPackage javaPackage) {
		final StringBuilder message = new StringBuilder("This package contains a package cycle with: ");
		final List<JavaPackage> cycles = new ArrayList<>();
		javaPackage.collectCycle(cycles);

		for (final JavaPackage cycle : cycles) {
			message.append("<br/>").append(cycle.getName());
		}

		return message.toString();
	}
}
