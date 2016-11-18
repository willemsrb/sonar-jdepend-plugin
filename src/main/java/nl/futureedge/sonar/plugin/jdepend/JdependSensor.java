package nl.futureedge.sonar.plugin.jdepend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;

/**
 * jDepend sensor.
 */
public final class JdependSensor implements Sensor {

	private static final Logger LOGGER = Loggers.get(JdependSensor.class);

	@Override
	public void describe(final SensorDescriptor descriptor) {
	    descriptor.name("jDepend sensor");
	    descriptor.onlyOnLanguage(JdependRulesDefinition.JAVA_LANGUAGE);
	    descriptor.createIssuesForRuleRepositories(JdependRulesDefinition.REPOSITORY);
	}

	@Override
	public void execute(final SensorContext context) {
		// jDepend
		final JDepend jdepend = new JDepend();
		    
		// Configure
		final String javaClasses = context.settings().getString("sonar.java.binaries");
		try {
	        jdepend.addDirectory(javaClasses);
		} catch(IOException e) {
		    throw new IllegalStateException("Could not analyse classes in " + javaClasses, e);
		}

		// Analyze
		@SuppressWarnings("unchecked")
		final Collection<JavaPackage> javaPackages = jdepend.analyze();
		
		// Report issues
		for(final JavaPackage javaPackage : javaPackages) {
			if(javaPackage.containsCycle()) {
				final InputFile packageInfoFile = findPackageInfo(context, javaPackage);
				if(packageInfoFile == null) {
					LOGGER.info("Package " + javaPackage.getName() + " contains a package cycle, but does not contain a package-info.java to add the issue to.");
				} else {
					final NewIssue issue = context.newIssue().forRule(JdependRulesDefinition.PACKAGE_CYCLE_RULE);
				    issue.at(issue.newLocation()
				    		.on(packageInfoFile)
					        .at(packageInfoFile.selectLine(1))
					        .message(createMessage(javaPackage)));
				    issue.save();
				}
			}
		}
	}

	private String createMessage(final JavaPackage javaPackage) {
		final StringBuilder message = new StringBuilder("This package contains a package cycle with: ");
		final List<JavaPackage> cycles = new ArrayList<>();
		javaPackage.collectCycle(cycles);
		
		for(final JavaPackage cycle : cycles) {
			message.append("<br/>").append(cycle.getName());
		}
		
		return message.toString();
	}

	private InputFile findPackageInfo(final SensorContext context, final JavaPackage javaPackage) {
		return context.fileSystem().inputFile(new PackageInfoPredicate(javaPackage.getName()) );
	}

	/**
	 * File predicate to find a package-info.java file for a given package.
	 */
	private static final class PackageInfoPredicate implements FilePredicate {
		
		private final String packageInfoName ;

		/**
		 * Constructor.
		 * 
		 * @param packageName package name to find the package-info.java file for
		 */
		public PackageInfoPredicate(final String packageName) {
			this.packageInfoName="/"+packageName.replace('.', '/')+"/package-info.java";
		}

		@Override
		public boolean apply(final InputFile inputFile) {
			return inputFile.relativePath().endsWith(packageInfoName);
		}
	}
}
