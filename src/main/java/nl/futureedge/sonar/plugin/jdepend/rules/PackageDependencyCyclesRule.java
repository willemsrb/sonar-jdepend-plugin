package nl.futureedge.sonar.plugin.jdepend.rules;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition.NewRepository;
import org.sonar.api.server.rule.RulesDefinition.NewRule;

import jdepend.framework.JavaPackage;

/**
 * Package dependency cycles rule.
 *
 * @see JavaPackage#containsCycle()
 * @see JavaPackage#collectCycle(List)
 */
public class PackageDependencyCyclesRule extends AbstractRule implements Rule {

	/** Rule: Package Dependency Cycles. */
	public static final RuleKey RULE_KEY = RuleKey.of(Rules.REPOSITORY, "package-cycle");

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	private final Integer maximum;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            sensor context
	 */
	public PackageDependencyCyclesRule(final SensorContext context) {
		super(context, RULE_KEY);
		maximum = getParamAsInteger(PARAM_MAXIMUM, true);
	}

	/**
	 * Define the rule.
	 *
	 * @param repository
	 */
	public static void define(final NewRepository repository) {
		final NewRule packageDependencyCyclesRule = repository.createRule(RULE_KEY.rule())
				.setName("Package Dependency Cycles").setHtmlDescription(
						"Package dependency cycles are reported along with the hierarchical paths of packages participating in package dependency cycles.");
		packageDependencyCyclesRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum number of package dependency cycles allowed").setType(RuleParamType.INTEGER)
				.setDefaultValue("0");
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
