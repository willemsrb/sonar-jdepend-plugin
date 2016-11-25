package nl.futureedge.sonar.plugin.jdepend.rules;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.server.rule.RulesDefinition;

import nl.futureedge.sonar.plugin.jdepend.JdependSensor;

public class RulesTest {

	private SensorContextTester sensorContext;

	@Before
	public void setup() throws IOException {
		// Setup base dir
		final URL location = RulesTest.class.getProtectionDomain().getCodeSource().getLocation();
		final File testClassesDir = new File(location.getFile());
		final File targetDir = testClassesDir.getParentFile();
		final File baseDir = targetDir.getParentFile();

		// Sensor context
		sensorContext = SensorContextTester.create(baseDir);

		// Setup classes
		sensorContext.settings().setProperty("sonar.java.binaries", testClassesDir.getCanonicalPath());
	}

	private void addPackageInfo(final String packageName) {
		final DefaultFileSystem fileSystem = sensorContext.fileSystem();
		fileSystem.add(new DefaultInputFile("test", "src/" + packageName.replace('.', '/') + "/package-info.java")
				.setLanguage("java").initMetadata("package " + packageName + ";"));
	}

	@Test
	public void testDefinition() {
		final RulesDefinition.Context context = new RulesDefinition.Context();
		new Rules().define(context);
		final RulesDefinition.Repository repository = context.repository(Rules.REPOSITORY);

		Assert.assertEquals("jDepend", repository.name());
		Assert.assertEquals("java", repository.language());

		Assert.assertEquals(7, repository.rules().size());

		Assert.assertNotNull(repository.rule(NumberOfClassesAndInterfacesRule.RULE_KEY.rule()));
		Assert.assertNotNull(repository.rule(AfferentCouplingsRule.RULE_KEY.rule()));
		Assert.assertNotNull(repository.rule(EfferentCouplingsRule.RULE_KEY.rule()));
		Assert.assertNotNull(repository.rule(AbstractnessRule.RULE_KEY.rule()));
		Assert.assertNotNull(repository.rule(InstabilityRule.RULE_KEY.rule()));
		Assert.assertNotNull(repository.rule(DistanceFromMainSequenceRule.RULE_KEY.rule()));
		Assert.assertNotNull(repository.rule(PackageDependencyCyclesRule.RULE_KEY.rule()));
	}

	@Test
	public void testNumberOfClassesAndInterfaces() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.numberofclassesandinterfaces.packagea");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.numberofclassesandinterfaces.packageb");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(NumberOfClassesAndInterfacesRule.RULE_KEY)
				.setParam(NumberOfClassesAndInterfacesRule.PARAM_MAXIMUM, "2").activate();
		sensorContext.setActiveRules(activeRules.build());

		// Execute
		final JdependSensor subject = new JdependSensor();
		subject.execute(sensorContext);

		// Check
		Assert.assertEquals(1, sensorContext.allIssues().size());
		final InputFile issueLocation = (InputFile) sensorContext.allIssues().iterator().next().primaryLocation()
				.inputComponent();
		Assert.assertTrue(
				issueLocation.relativePath().endsWith("/numberofclassesandinterfaces/packagea/package-info.java"));
	}

	@Test
	public void testAfferentCouplings() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagea");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packageb");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagec");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packaged");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagee");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(AfferentCouplingsRule.RULE_KEY).setParam(AfferentCouplingsRule.PARAM_MAXIMUM, "2")
				.activate();
		sensorContext.setActiveRules(activeRules.build());

		// Execute
		final JdependSensor subject = new JdependSensor();
		subject.execute(sensorContext);

		// Check
		Assert.assertEquals(1, sensorContext.allIssues().size());
		final InputFile issueLocation = (InputFile) sensorContext.allIssues().iterator().next().primaryLocation()
				.inputComponent();
		Assert.assertTrue(issueLocation.relativePath().endsWith("/coupling/packagee/package-info.java"));
	}

	@Test
	public void testEfferentCouplings() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagea");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packageb");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagec");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packaged");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagee");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(EfferentCouplingsRule.RULE_KEY).setParam(EfferentCouplingsRule.PARAM_MAXIMUM, "2")
				.activate();
		sensorContext.setActiveRules(activeRules.build());

		// Execute
		final JdependSensor subject = new JdependSensor();
		subject.execute(sensorContext);

		// Check
		Assert.assertEquals(1, sensorContext.allIssues().size());
		final InputFile issueLocation = (InputFile) sensorContext.allIssues().iterator().next().primaryLocation()
				.inputComponent();
		Assert.assertTrue(issueLocation.relativePath().endsWith("/coupling/packagea/package-info.java"));
	}

	@Test
	public void testAbstractness() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.abstractness.packagea");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.abstractness.packageb");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(AbstractnessRule.RULE_KEY).setParam(AbstractnessRule.PARAM_MAXIMUM, "50").activate();
		sensorContext.setActiveRules(activeRules.build());

		// Execute
		final JdependSensor subject = new JdependSensor();
		subject.execute(sensorContext);

		// Check
		Assert.assertEquals(1, sensorContext.allIssues().size());
		final InputFile issueLocation = (InputFile) sensorContext.allIssues().iterator().next().primaryLocation()
				.inputComponent();
		Assert.assertTrue(issueLocation.relativePath().endsWith("/abstractness/packageb/package-info.java"));
	}

	@Test
	public void testInstability() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagea");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packageb");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagec");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packaged");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagee");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(InstabilityRule.RULE_KEY).setParam(InstabilityRule.PARAM_MAXIMUM, "80").activate();
		sensorContext.setActiveRules(activeRules.build());

		// Execute
		final JdependSensor subject = new JdependSensor();
		subject.execute(sensorContext);

		// Check
		Assert.assertEquals(1, sensorContext.allIssues().size());
		final InputFile issueLocation = (InputFile) sensorContext.allIssues().iterator().next().primaryLocation()
				.inputComponent();
		Assert.assertTrue(issueLocation.relativePath().endsWith("/coupling/packagea/package-info.java"));
	}

	@Test
	public void testDistanceFromMainSequence() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagea");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packageb");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagec");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packaged");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.coupling.packagee");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(DistanceFromMainSequenceRule.RULE_KEY)
				.setParam(DistanceFromMainSequenceRule.PARAM_MAXIMUM, "50").activate();
		sensorContext.setActiveRules(activeRules.build());

		// Execute
		final JdependSensor subject = new JdependSensor();
		subject.execute(sensorContext);

		// Check
		Assert.assertEquals(1, sensorContext.allIssues().size());
		final InputFile issueLocation = (InputFile) sensorContext.allIssues().iterator().next().primaryLocation()
				.inputComponent();
		Assert.assertTrue(issueLocation.relativePath().endsWith("/coupling/packagee/package-info.java"));
	}

	@Test
	public void testPackageDependencyCycle() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packagea");
		// not adding packageb because package dependency cycles are reported at
		// both sides
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packagec");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(PackageDependencyCyclesRule.RULE_KEY)
				.setParam(PackageDependencyCyclesRule.PARAM_MAXIMUM, "0").activate();
		sensorContext.setActiveRules(activeRules.build());

		// Execute
		final JdependSensor subject = new JdependSensor();
		subject.execute(sensorContext);

		// Check
		Assert.assertEquals(1, sensorContext.allIssues().size());
		final InputFile issueLocation = (InputFile) sensorContext.allIssues().iterator().next().primaryLocation()
				.inputComponent();
		Assert.assertTrue(issueLocation.relativePath().endsWith("/packagedependencycycle/packagea/package-info.java"));
	}
}
