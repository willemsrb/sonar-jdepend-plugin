package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.server.rule.RulesDefinition;

/**
 * Rules.
 */
public final class Rules implements RulesDefinition {

	/** Repository name. */
	public static final String REPOSITORY = "jdepend";

	/** Repository name. */
	public static final String LANGUAGE_JAVA = "java";

	@Override
	public void define(final Context context) {
		final NewRepository repository = context.createRepository(REPOSITORY, LANGUAGE_JAVA).setName("jDepend");

		AbstractnessRule.define(repository);
		AfferentCouplingsRule.define(repository);
		DistanceFromMainSequenceRule.define(repository);
		EfferentCouplingsRule.define(repository);
		InstabilityRule.define(repository);
		NumberOfClassesAndInterfacesRule.define(repository);
		PackageDependencyCyclesRule.define(repository);

		repository.done();
	}
}
