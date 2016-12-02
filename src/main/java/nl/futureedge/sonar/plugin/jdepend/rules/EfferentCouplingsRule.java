package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition.NewRepository;
import org.sonar.api.server.rule.RulesDefinition.NewRule;

import jdepend.framework.JavaPackage;
import nl.futureedge.sonar.plugin.jdepend.metrics.JdependMetrics;

/**
 * Efferent coupling rule.
 *
 * @see JavaPackage#efferentCoupling()
 */
public class EfferentCouplingsRule extends AbstractRule implements Rule {

	/** Rule: Efferent coupling. */
	public static final RuleKey RULE_KEY = RuleKey.of(Rules.REPOSITORY, "efferent-couplings");

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public EfferentCouplingsRule(final SensorContext context) {
		super(context, RULE_KEY);
		maximum = getParamAsInteger(PARAM_MAXIMUM, true);
	}

	/**
	 * Define the rule.
	 *
	 * @param repository
	 */
	public static void define(final NewRepository repository) {
		final NewRule efferentCouplingsRule = repository.createRule(RULE_KEY.rule()).setName("Efferent Couplings")
				.setHtmlDescription(
						"The number of other packages that the classes in the package depend upon is an indicator of the package's independence.");
		efferentCouplingsRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription(
						"Maximum number of other packages that the classes in the package are allowed to depend upon")
				.setType(RuleParamType.INTEGER).setDefaultValue("25");
	}

	@Override
	public void execute(final JavaPackage javaPackage, final InputFile packageInfoFile) {
		final int efferentCoupling = javaPackage.efferentCoupling();
		getContext().<Integer> newMeasure().forMetric(JdependMetrics.EFFERENT_COUPLINGS).on(packageInfoFile)
				.withValue(efferentCoupling).save();

		if (!isActive()) {
			return;
		}

		if (efferentCoupling > maximum) {
			final NewIssue issue = getContext().newIssue().forRule(getKey());
			issue.at(issue.newLocation().on(packageInfoFile).at(packageInfoFile.selectLine(1))
					.message("Too much efferent coupling (allowed: " + maximum + ", actual: "
							+ javaPackage.getClassCount() + ")"));
			issue.save();
		}
	}
}
