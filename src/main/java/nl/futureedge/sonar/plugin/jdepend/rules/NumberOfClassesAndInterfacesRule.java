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
 * Number of classes and interfaces rule.
 *
 * @see JavaPackage#getClassCount()
 */
public class NumberOfClassesAndInterfacesRule extends AbstractRule implements Rule {

	/** Rule: Number of Classes and Interfaces. */
	public static final RuleKey RULE_KEY = RuleKey.of(Rules.REPOSITORY, "number-of-classes-and-interfaces");

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public NumberOfClassesAndInterfacesRule(final SensorContext context) {
		super(context, RULE_KEY);
		maximum = getParamAsInteger(PARAM_MAXIMUM, true);
	}

	/**
	 * Define the rule.
	 *
	 * @param repository
	 */
	public static void define(final NewRepository repository) {
		final NewRule numberOfClassesAndInterfacesRule = repository.createRule(RULE_KEY.rule())
				.setName("Number of Classes and Interfaces").setHtmlDescription(
						"The number of concrete and abstract classes (and interfaces) in the package is an indicator of the extensibility of the package.");
		numberOfClassesAndInterfacesRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum number of classes and interfaces allowed in the package")
				.setType(RuleParamType.INTEGER).setDefaultValue("50");
	}

	@Override
	public void execute(final JavaPackage javaPackage, final InputFile packageInfoFile) {
		final int classcount = javaPackage.getClassCount();
		getContext().<Integer> newMeasure().forMetric(JdependMetrics.NUMBER_OF_CLASSES_AND_INTERFACES)
				.on(packageInfoFile).withValue(classcount).save();

		if (!isActive()) {
			return;
		}

		if (classcount > maximum) {
			final NewIssue issue = getContext().newIssue().forRule(getKey());
			issue.at(issue.newLocation().on(packageInfoFile).at(packageInfoFile.selectLine(1))
					.message("Too many classes and interfaces (allowed: " + maximum + ", actual: "
							+ javaPackage.getClassCount() + ")"));
			issue.save();
		}
	}
}
