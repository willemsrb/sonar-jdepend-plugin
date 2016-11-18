package nl.futureedge.sonar.plugin.jdepend;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * jDepend rules.
 */
public final class JdependRulesDefinition implements RulesDefinition {

	/** Repository name. */
	public static final String REPOSITORY = "jdepend";
	
	/** Language. */
	public static final String JAVA_LANGUAGE = "java";
  
	/** Rule: package cycles. */
	public static final RuleKey PACKAGE_CYCLE_RULE = RuleKey.of(REPOSITORY, "package-cycle");

	@Override
	public void define(final Context context) {
		final NewRepository repository = context.createRepository(REPOSITORY, JAVA_LANGUAGE).setName("jDepend Analyzer");

		repository.createRule(PACKAGE_CYCLE_RULE.rule())
			.setName("Package cycles are not allowed")
			.setHtmlDescription("Check for pacakge cycles.");

    	repository.done();
	}
}
