package nl.futureedge.sonar.plugin.jdepend;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * jDepend rules.
 */
public final class JdependRulesDefinition implements RulesDefinition {

	/** Repository name. */
	public static final String REPOSITORY = "jdepend";

	/** Language: java. */
	public static final String LANGAUGE_JAVA = "java";

	/** Param: maximum. */
	public static final String PARAM_MAXIMUM = "maximum";

	/** Rule: Number of Classes and Interfaces. */
	public static final RuleKey NUMBER_OF_CLASSES_AND_INTERFACES_RULE = RuleKey.of(REPOSITORY,
			"number-of-classes-and-interfaces");

	/** Rule: Number of Classes and Interfaces. */
	public static final RuleKey AFFERENT_COUPLINGS_RULE = RuleKey.of(REPOSITORY, "afferent-couplings");

	/** Rule: Number of Classes and Interfaces. */
	public static final RuleKey EFFERENT_COUPLINGS_RULE = RuleKey.of(REPOSITORY, "efferent-couplings");

	/** Rule: Number of Classes and Interfaces. */
	public static final RuleKey ABSTRACTNESS_RULE = RuleKey.of(REPOSITORY, "abstractness");

	/** Rule: Number of Classes and Interfaces. */
	public static final RuleKey INSTABILITY_RULE = RuleKey.of(REPOSITORY, "instability");

	/** Rule: Number of Classes and Interfaces. */
	public static final RuleKey DISTANCE_FROM_MAIN_SEQUENCE_RULE = RuleKey.of(REPOSITORY,
			"distance-from-main-sequence");

	/** Rule: Package Dependency Cycles. */
	public static final RuleKey PACKAGE_DEPENDENCY_CYCLES_RULE = RuleKey.of(REPOSITORY, "package-cycle");

	@Override
	public void define(final Context context) {
		final NewRepository repository = context.createRepository(REPOSITORY, LANGAUGE_JAVA).setName("jDepend");

		final NewRule numberOfClassesAndInterfacesRule = repository
				.createRule(NUMBER_OF_CLASSES_AND_INTERFACES_RULE.rule()).setName("Number of Classes and Interfaces")
				.setHtmlDescription(
						"The number of concrete and abstract classes (and interfaces) in the package is an indicator of the extensibility of the package.");
		numberOfClassesAndInterfacesRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum number of classes and interfaces allowed in the package")
				.setType(RuleParamType.INTEGER);

		final NewRule afferentCouplingsRule = repository.createRule(AFFERENT_COUPLINGS_RULE.rule())
				.setName("Afferent Couplings").setHtmlDescription(
						"The number of other packages that depend upon classes within the package is an indicator of the package's responsibility.");
		afferentCouplingsRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum number of other packages allowed to depend upon classes within the package")
				.setType(RuleParamType.INTEGER);

		final NewRule efferentCouplingsRule = repository.createRule(EFFERENT_COUPLINGS_RULE.rule())
				.setName("Efferent Couplings").setHtmlDescription(
						"The number of other packages that the classes in the package depend upon is an indicator of the package's independence.");
		efferentCouplingsRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription(
						"Maximum number of other packages that the classes in the package are allowed to depend upon")
				.setType(RuleParamType.INTEGER);

		final NewRule abstractnessRule = repository.createRule(ABSTRACTNESS_RULE.rule()).setName("Abstractness")
				.setHtmlDescription(
						"The ratio of the number of abstract classes (and interfaces) in the analyzed package to the total number of classes in the analyzed package.<br/>"
								+ "The range for this metric is 0% to 100%, with A=0% indicating a completely concrete package and A=100% indicating a completely abstract package.");
		abstractnessRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum abstractness of a package allowed").setType(RuleParamType.INTEGER);

		final NewRule instabilityRule = repository.createRule(INSTABILITY_RULE.rule()).setName("Instability")
				.setHtmlDescription(
						"The ratio of efferent coupling (Ce) to total coupling (Ce + Ca) such that I = Ce / (Ce + Ca). This metric is an indicator of the package's resilience to change.<br/>"
								+ "The range for this metric is 0 to 100%, with I=0% indicating a completely stable package and I=100% indicating a completely instable package.");
		instabilityRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum instability of a package allowed").setType(RuleParamType.INTEGER);

		final NewRule distanceFromMainSequenceRule = repository.createRule(DISTANCE_FROM_MAIN_SEQUENCE_RULE.rule())
				.setName("Distance from the Main Sequence").setHtmlDescription(
						"The perpendicular distance of a package from the idealized line A + I = 1. This metric is an indicator of the package's balance between abstractness and stability.<br/>"
								+ "A package squarely on the main sequence is optimally balanced with respect to its abstractness and stability. Ideal packages are either completely abstract and stable (x=0, y=1) or completely concrete and instable (x=1, y=0).<br/>"
								+ "The range for this metric is 0 to 100%, with D=0% indicating a package that is coincident with the main sequence and D=100% indicating a package that is as far from the main sequence as possible.");
		distanceFromMainSequenceRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum distance of a package allowed").setType(RuleParamType.INTEGER);

		final NewRule packageDependencyCyclesRule = repository.createRule(PACKAGE_DEPENDENCY_CYCLES_RULE.rule())
				.setName("Package Dependency Cycles").setHtmlDescription(
						"Package dependency cycles are reported along with the hierarchical paths of packages participating in package dependency cycles.");
		packageDependencyCyclesRule.createParam(PARAM_MAXIMUM).setName(PARAM_MAXIMUM)
				.setDescription("Maximum number of package dependency cycles allowed").setType(RuleParamType.INTEGER)
				.setDefaultValue("0");

		repository.done();
	}
}
