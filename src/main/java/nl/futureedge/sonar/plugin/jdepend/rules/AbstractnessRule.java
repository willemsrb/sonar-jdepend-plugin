package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition.NewRepository;
import org.sonar.api.server.rule.RulesDefinition.NewRule;

import jdepend.framework.JavaPackage;

/**
 * Abstractness rule.
 *
 * @see JavaPackage#abstractness()
 */
public class AbstractnessRule extends AbstractRule implements Rule {

	/** Rule: Abstractness. */
	public static final RuleKey RULE_KEY = RuleKey.of(Rules.REPOSITORY, "abstractness");

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public AbstractnessRule(final SensorContext context) {
		super(context, RULE_KEY);
		maximum = getParamAsInteger(PARAM_MAXIMUM, true);
	}

	/**
	 * Define the rule.
	 *
	 * @param repository
	 */
	public static void define(final NewRepository repository) {
		final NewRule abstractnessRule = repository.createRule(RULE_KEY.rule()).setName("Abstractness")
				.setHtmlDescription(
						"The ratio of the number of abstract classes (and interfaces) in the analyzed package to the total number of classes in the analyzed package.<br/>"
								+ "The range for this metric is 0% to 100%, with A=0% indicating a completely concrete package and A=100% indicating a completely abstract package.");
		abstractnessRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum abstractness of a package allowed").setType(RuleParamType.INTEGER)
				.setDefaultValue("75");
	}

	@Override
	public void execute(final JavaPackage javaPackage, final InputFile packageInfoFile) {
		if (!isActive()) {
			return;
		}

		final int abstractness = Math.round(javaPackage.abstractness() * 100);
		if (abstractness > maximum) {
			registerIssue(packageInfoFile,
					"Too much abstractness (allowed: " + maximum + ", actual: " + abstractness + ")");
		}
	}

}
