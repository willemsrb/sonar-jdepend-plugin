package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Base rule implementation.
 */
public abstract class AbstractRule implements Rule {

	private static final Logger LOGGER = Loggers.get(AbstractnessRule.class);

	private final SensorContext context;
	private final RuleKey ruleKey;
	private final ActiveRule rule;
	private boolean enabled;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context (to check if rule is activated)
	 * @param ruleKey
	 *            rule key
	 */
	protected AbstractRule(final SensorContext context, final RuleKey ruleKey) {
		this.context = context;
		this.ruleKey = ruleKey;
		rule = context.activeRules().find(ruleKey);
		enabled = true;
	}

	/**
	 * @return sensor context
	 */
	protected SensorContext getContext() {
		return context;
	}

	/**
	 * @return rule key
	 */
	protected final RuleKey getKey() {
		return ruleKey;
	}

	/**
	 * Set the rule as disabled (affects {@link #isActive()}).
	 */
	protected final void disable() {
		enabled = false;
	}

	/**
	 * Should the rule be checked?
	 *
	 * @return true, if the rule should be checked
	 */
	protected final boolean isActive() {
		return enabled && rule != null;
	}

	/**
	 * Return parameter value as a String.
	 *
	 * @param paramKey
	 *            parameter key
	 * @return parameter value (can be null)
	 */
	protected final String getParamAsString(final String paramKey, final boolean required) {
		if (rule == null) {
			return null;
		} else {
			final String result = rule.param(paramKey);
			if (required && isEmpty(result)) {
				LOGGER.info("Rule {} activated, no value for parameter {} set. Disabling rule...", getKey(), paramKey);
				disable();
			}

			return result;
		}
	}

	/**
	 * Return parameter value as an Integer.
	 *
	 * @param paramKey
	 *            parameter key
	 * @return parameter value (can be null)
	 */
	protected final Integer getParamAsInteger(final String paramKey, final boolean required) {
		final String value = getParamAsString(paramKey, required);
		if (isEmpty(value)) {
			return null;
		} else {
			return Integer.valueOf(value);
		}
	}

	private boolean isEmpty(final String result) {
		return result == null || "".equals(result);
	}

	/**
	 * Register an issue.
	 *
	 * @param inputFile
	 *            input file
	 * @param message
	 *            issue message
	 */
	protected void registerIssue(final InputFile inputFile, final String message) {
		final NewIssue issue = getContext().newIssue().forRule(getKey());
		issue.at(issue.newLocation().on(inputFile).at(inputFile.selectLine(1)).message(message));
		issue.save();
	}
}
