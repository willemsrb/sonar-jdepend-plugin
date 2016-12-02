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
 * Distance from the main sequence rule.
 *
 * @see JavaPackage#distance()
 */
public class DistanceFromMainSequenceRule extends AbstractRule implements Rule {

	/** Rule: Distance from the main sequence. */
	public static final RuleKey RULE_KEY = RuleKey.of(Rules.REPOSITORY, "distance-from-main-sequence");

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public DistanceFromMainSequenceRule(final SensorContext context) {
		super(context, RULE_KEY);
		maximum = getParamAsInteger(PARAM_MAXIMUM, true);
	}

	/**
	 * Define the rule.
	 *
	 * @param repository
	 */
	public static void define(final NewRepository repository) {
		final NewRule distanceFromMainSequenceRule = repository.createRule(RULE_KEY.rule())
				.setName("Distance from the Main Sequence").setHtmlDescription(
						"The perpendicular distance of a package from the idealized line A + I = 1. This metric is an indicator of the package's balance between abstractness and stability.<br/>"
								+ "A package squarely on the main sequence is optimally balanced with respect to its abstractness and stability. Ideal packages are either completely abstract and stable (x=0, y=1) or completely concrete and instable (x=1, y=0).<br/>"
								+ "The range for this metric is 0 to 100%, with D=0% indicating a package that is coincident with the main sequence and D=100% indicating a package that is as far from the main sequence as possible.");
		distanceFromMainSequenceRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum distance of a package allowed").setType(RuleParamType.INTEGER)
				.setDefaultValue("75");
	}

	@Override
	public void execute(final JavaPackage javaPackage, final InputFile packageInfoFile) {
		if (!isActive()) {
			return;
		}

		final int distance = Math.round(javaPackage.distance() * 100);
		if (distance > maximum) {
			final NewIssue issue = getContext().newIssue().forRule(getKey());
			issue.at(issue.newLocation().on(packageInfoFile).at(packageInfoFile.selectLine(1)).message(
					"Too much distance from the main sequence (allowed: " + maximum + ", actual: " + distance + ")"));
			issue.save();
		}
	}
}
