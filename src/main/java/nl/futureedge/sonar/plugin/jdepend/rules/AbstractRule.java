package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;

/**
 * Base rule implementation.
 */
public abstract class AbstractRule implements Rule {

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
	protected final String getParamAsString(final String paramKey) {
		return rule == null ? null : rule.param(paramKey);
	}

	/**
	 * Return parameter value as an Integer.
	 *
	 * @param paramKey
	 *            parameter key
	 * @return parameter value (can be null)
	 */
	protected final Integer getParamAsInteger(final String paramKey) {
		final String value = getParamAsString(paramKey);
		if (value == null || "".equals(value)) {
			return null;
		} else {
			return Integer.valueOf(value);
		}
	}
}
