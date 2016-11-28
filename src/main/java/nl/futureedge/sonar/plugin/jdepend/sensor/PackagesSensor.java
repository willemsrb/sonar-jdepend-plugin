package nl.futureedge.sonar.plugin.jdepend.sensor;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.Metric;

import nl.futureedge.sonar.plugin.jdepend.metrics.PackagesMetrics;
import nl.futureedge.sonar.plugin.jdepend.rules.Rules;

/**
 * Pacakges sensor.
 */
public final class PackagesSensor implements Sensor {

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name("Packages sensor");
		descriptor.onlyOnLanguage(Rules.LANGUAGE_JAVA);
	}

	@Override
	public void execute(final SensorContext context) {
		final FileSystem fileSystem = context.fileSystem();
		final FilePredicates predicates = fileSystem.predicates();
		for (final InputFile inputFile : context.fileSystem().inputFiles(
				predicates.and(predicates.hasLanguage(Rules.LANGUAGE_JAVA), predicates.hasType(Type.MAIN)))) {
			if (inputFile.relativePath().endsWith("/package-info.java")) {
				addMetric(context, inputFile, PackagesMetrics.PACKAGE_INFO_COUNT);
			}
			addMetric(context, inputFile, PackagesMetrics.JAVA_COUNT);
		}
	}

	private void addMetric(final SensorContext context, final InputFile inputFile, final Metric<Integer> metric) {
		context.<Integer> newMeasure().forMetric(metric).on(inputFile).withValue(1).save();
	}

}
