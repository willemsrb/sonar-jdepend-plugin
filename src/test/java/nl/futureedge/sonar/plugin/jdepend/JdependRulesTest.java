package nl.futureedge.sonar.plugin.jdepend;

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

public class JdependRulesTest {

	private SensorContextTester sensorContext;

	@Before
	public void setup() throws IOException {
		// Setup base dir
		final URL location = JdependRulesTest.class.getProtectionDomain().getCodeSource().getLocation();
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
	public void testNumberOfClassesAndInterfaces() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.numberofclassesandinterfaces.packagea");
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.numberofclassesandinterfaces.packageb");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(JdependRulesDefinition.NUMBER_OF_CLASSES_AND_INTERFACES_RULE).setParam("maximum", "2")
				.activate();
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
		activeRules.create(JdependRulesDefinition.AFFERENT_COUPLINGS_RULE).setParam("maximum", "2").activate();
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
		activeRules.create(JdependRulesDefinition.EFFERENT_COUPLINGS_RULE).setParam("maximum", "2").activate();
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
		activeRules.create(JdependRulesDefinition.ABSTRACTNESS_RULE).setParam("maximum", "50").activate();
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
		activeRules.create(JdependRulesDefinition.INSTABILITY_RULE).setParam("maximum", "80").activate();
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
		activeRules.create(JdependRulesDefinition.DISTANCE_FROM_MAIN_SEQUENCE_RULE).setParam("maximum", "50")
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
	public void testPackageDependencyCycle() {
		// Add package info
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packagea");
		// not adding packageb because package dependency cycles are reported at
		// both sides
		addPackageInfo("nl.futureedge.sonar.plugin.jdepend.test.packagedependencycycle.packagec");

		// Add rule with config
		final ActiveRulesBuilder activeRules = new ActiveRulesBuilder();
		activeRules.create(JdependRulesDefinition.PACKAGE_DEPENDENCY_CYCLES_RULE).setParam("maximum", "0").activate();
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
