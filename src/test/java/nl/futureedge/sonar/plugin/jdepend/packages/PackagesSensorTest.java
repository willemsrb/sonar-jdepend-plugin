package nl.futureedge.sonar.plugin.jdepend.packages;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;

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
}
