package nl.futureedge.sonar.plugin.jdepend.packages;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.measure.Measure;
import org.sonar.api.measures.Metric;

public class PackagesSensorTest {

	@Test
	public void testDescriptor() {
		final PackagesSensor subject = new PackagesSensor();
		final DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
		subject.describe(descriptor);

		Assert.assertEquals("Packages sensor", descriptor.name());
		Assert.assertEquals(1, descriptor.languages().size());
		Assert.assertEquals("java", descriptor.languages().iterator().next());
	}

	@Test
	public void testExecute() {
		// Sensor context
		final SensorContextTester sensorContext = SensorContextTester.create(new File("."));
		addFile(sensorContext, "/packagea/package-info.java", "java", Type.MAIN);
		addFile(sensorContext, "/packagea/ClassA.java", "java", Type.MAIN);
		addFile(sensorContext, "/packageb/package-info.java", "java", Type.TEST);
		addFile(sensorContext, "/packageb/ClassB.java", "java", Type.TEST);
		addFile(sensorContext, "/packagec/ClassC.pas", "pascal", Type.MAIN);

		// Execute
		final PackagesSensor subject = new PackagesSensor();
		subject.execute(sensorContext);

		// Check counts
		checkMetrics(sensorContext, "moduleKey:/packagea/package-info.java", 1, 1);
		checkMetrics(sensorContext, "moduleKey:/packagea/ClassA.java", null, 1);
		checkMetrics(sensorContext, "moduleKey:/packageb/package-info.java", null, null);
		checkMetrics(sensorContext, "moduleKey:/packageb/ClassB.java", null, null);
		checkMetrics(sensorContext, "moduleKey:/packagec/ClassC.pas", null, null);
	}

	private void checkMetrics(final SensorContextTester sensorContext, final String component,
			final Integer packageInfoCount, final Integer javaCount) {
		checkMetric(sensorContext, component, PackagesMetrics.PACKAGE_INFO_COUNT, packageInfoCount);
		checkMetric(sensorContext, component, PackagesMetrics.JAVA_COUNT, javaCount);
	}

	private void checkMetric(final SensorContextTester sensorContext, final String component,
			final Metric<Integer> metric, final Integer value) {
		final Measure<Integer> measure = sensorContext.measure(component, metric);
		if (value == null) {
			Assert.assertNull(measure);
		} else {
			Assert.assertNotNull(measure);
			Assert.assertEquals(value, measure.value());
		}
	}

	private void addFile(final SensorContextTester sensorContext, final String name, final String language,
			final Type type) {
		final DefaultInputFile file = new DefaultInputFile("moduleKey", name);
		file.setLanguage(language);
		file.setType(type);

		sensorContext.fileSystem().add(file);
	}
}
