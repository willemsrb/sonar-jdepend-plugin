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
 * Afferent couplings rule.
 *
 * @see JavaPackage#afferentCoupling()
 */
public class AfferentCouplingsRule extends AbstractRule implements Rule {

	/** Rule: Afferent coupling. */
	public static final RuleKey RULE_KEY = RuleKey.of(Rules.REPOSITORY, "afferent-couplings");

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	/**
	 * Define the rule.
	 *
	 * @param repository
	 */
	public static void define(final NewRepository repository) {
		final NewRule afferentCouplingsRule = repository.createRule(RULE_KEY.rule()).setName("Afferent Couplings")
				.setHtmlDescription(
						"The number of other packages that depend upon classes within the package is an indicator of the package's responsibility.");
		afferentCouplingsRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum number of other packages allowed to depend upon classes within the package")
				.setType(RuleParamType.INTEGER);
	}

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public AfferentCouplingsRule(final SensorContext context) {
		super(context, RULE_KEY);
		maximum = getParamAsInteger(PARAM_MAXIMUM, true);
	}

	@Override
	public void execute(final JavaPackage javaPackage, final InputFile packageInfoFile) {
		if (!isActive()) {
			return;
		}

		final int afferentCoupling = javaPackage.afferentCoupling();
		if (afferentCoupling > maximum) {
			final NewIssue issue = getContext().newIssue().forRule(getKey());
			issue.at(issue.newLocation().on(packageInfoFile).at(packageInfoFile.selectLine(1))
					.message("Too much afferent coupling (allowed: " + maximum + ", actual: "
							+ javaPackage.getClassCount() + ")"));
			issue.save();
		}
	}
}
