package nl.futureedge.sonar.plugin.jdepend.sensor;

import java.io.IOException;
import java.util.Collection;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import nl.futureedge.sonar.plugin.jdepend.rules.AbstractnessRule;
import nl.futureedge.sonar.plugin.jdepend.rules.AfferentCouplingsRule;
import nl.futureedge.sonar.plugin.jdepend.rules.DistanceFromMainSequenceRule;
import nl.futureedge.sonar.plugin.jdepend.rules.EfferentCouplingsRule;
import nl.futureedge.sonar.plugin.jdepend.rules.InstabilityRule;
import nl.futureedge.sonar.plugin.jdepend.rules.NumberOfClassesAndInterfacesRule;
import nl.futureedge.sonar.plugin.jdepend.rules.PackageDependencyCyclesRule;
import nl.futureedge.sonar.plugin.jdepend.rules.Rule;
import nl.futureedge.sonar.plugin.jdepend.rules.Rules;

/**
 * jDepend sensor.
 */
public final class JdependSensor implements Sensor {

	private static final Logger LOGGER = Loggers.get(JdependSensor.class);

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name("jDepend sensor");
		descriptor.onlyOnLanguage(Rules.LANGUAGE_JAVA);
		descriptor.createIssuesForRuleRepositories(Rules.REPOSITORY);
	}

	@Override
	public void execute(final SensorContext context) {
		// jDepend
		final JDepend jdepend = new JDepend();

		// Configure
		final String javaClasses = context.settings().getString("sonar.java.binaries");
		try {
			if (javaClasses == null || "".equals(javaClasses)) {
				LOGGER.info("No classes to analyse, skipping analysis");
				return;
			}
			LOGGER.info("Analysing classes in {}", javaClasses);
			jdepend.addDirectory(javaClasses);
		} catch (final IOException e) {
			throw new IllegalStateException("Could not analyse classes in " + javaClasses, e);
		}

		// Analyze
		@SuppressWarnings("unchecked")
		final Collection<JavaPackage> javaPackages = jdepend.analyze();

		// Setup rules
		final Rule numberOfClassesAndInterfacesRule = new NumberOfClassesAndInterfacesRule(context);
		final Rule afferentCouplingsRule = new AfferentCouplingsRule(context);
		final Rule efferentCouplingsRule = new EfferentCouplingsRule(context);
		final Rule abstractnessRule = new AbstractnessRule(context);
		final Rule instabilityRule = new InstabilityRule(context);
		final Rule distanceFromMainSequenceRule = new DistanceFromMainSequenceRule(context);
		final Rule packageDependencyCyclesRule = new PackageDependencyCyclesRule(context);

		// Report issues
		for (final JavaPackage javaPackage : javaPackages) {
			final InputFile packageInfoFile = findPackageInfo(context, javaPackage);
			if (packageInfoFile != null) {
				// Execute rules
				numberOfClassesAndInterfacesRule.execute(javaPackage, packageInfoFile);
				afferentCouplingsRule.execute(javaPackage, packageInfoFile);
				efferentCouplingsRule.execute(javaPackage, packageInfoFile);
				abstractnessRule.execute(javaPackage, packageInfoFile);
				instabilityRule.execute(javaPackage, packageInfoFile);
				distanceFromMainSequenceRule.execute(javaPackage, packageInfoFile);
				packageDependencyCyclesRule.execute(javaPackage, packageInfoFile);
			}
		}
	}

	private InputFile findPackageInfo(final SensorContext context, final JavaPackage javaPackage) {
		return context.fileSystem().inputFile(new PackageInfoPredicate(javaPackage.getName()));
	}

	/**
	 * File predicate to find a package-info.java file for a given package.
	 */
	private static final class PackageInfoPredicate implements FilePredicate {

		private final String packageInfoName;

		/**
		 * Constructor.
		 *
		 * @param packageName
		 *            package name to find the package-info.java file for
		 */
		public PackageInfoPredicate(final String packageName) {
			packageInfoName = "/" + packageName.replace('.', '/') + "/package-info.java";
		}

		@Override
		public boolean apply(final InputFile inputFile) {
			return inputFile.relativePath().endsWith(packageInfoName);
		}
	}
}
