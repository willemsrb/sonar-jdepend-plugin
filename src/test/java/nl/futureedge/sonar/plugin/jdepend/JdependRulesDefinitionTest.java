package nl.futureedge.sonar.plugin.jdepend;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;

public class JdependRulesDefinitionTest {

	@Test
	public void test() {
		final RulesDefinition.Context context = new RulesDefinition.Context();
		new JdependRulesDefinition().define(context);
		final RulesDefinition.Repository repository = context.repository(JdependRulesDefinition.REPOSITORY);

		Assert.assertEquals("jDepend", repository.name());
		Assert.assertEquals("java", repository.language());

		Assert.assertEquals(7, repository.rules().size());

		Assert.assertNotNull(repository.rule(JdependRulesDefinition.NUMBER_OF_CLASSES_AND_INTERFACES_RULE.rule()));
		Assert.assertNotNull(repository.rule(JdependRulesDefinition.AFFERENT_COUPLINGS_RULE.rule()));
		Assert.assertNotNull(repository.rule(JdependRulesDefinition.EFFERENT_COUPLINGS_RULE.rule()));
		Assert.assertNotNull(repository.rule(JdependRulesDefinition.ABSTRACTNESS_RULE.rule()));
		Assert.assertNotNull(repository.rule(JdependRulesDefinition.INSTABILITY_RULE.rule()));
		Assert.assertNotNull(repository.rule(JdependRulesDefinition.DISTANCE_FROM_MAIN_SEQUENCE_RULE.rule()));
		Assert.assertNotNull(repository.rule(JdependRulesDefinition.PACKAGE_DEPENDENCY_CYCLES_RULE.rule()));
	}
}
