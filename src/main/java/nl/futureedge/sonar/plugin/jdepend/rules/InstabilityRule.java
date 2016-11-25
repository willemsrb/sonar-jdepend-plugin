package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition.NewRepository;
import org.sonar.api.server.rule.RulesDefinition.NewRule;

import jdepend.framework.JavaPackage;

/**
 * Instability rule.
 *
 * @see JavaPackage#instability()
 */
public class InstabilityRule extends AbstractRule implements Rule {

	/** Rule: Instability. */
	public static final RuleKey RULE_KEY = RuleKey.of(Rules.REPOSITORY, "instability");

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public InstabilityRule(final SensorContext context) {
		super(context, RULE_KEY);
		maximum = getParamAsInteger(PARAM_MAXIMUM, true);
	}

	/**
	 * Define the rule.
	 *
	 * @param repository
	 */
	public static void define(final NewRepository repository) {
		final NewRule instabilityRule = repository.createRule(RULE_KEY.rule()).setName("Instability")
				.setHtmlDescription(
						"The ratio of efferent coupling (Ce) to total coupling (Ce + Ca) such that I = Ce / (Ce + Ca). This metric is an indicator of the package's resilience to change.<br/>"
								+ "The range for this metric is 0 to 100%, with I=0% indicating a completely stable package and I=100% indicating a completely instable package.");
		instabilityRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum instability of a package allowed").setType(RuleParamType.INTEGER);
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
