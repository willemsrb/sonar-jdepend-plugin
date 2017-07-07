package nl.futureedge.sonar.plugin.jdepend.sensor;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;

public class JdependSensorTest {

	@Test
	public void testDescriptor() {
		final JdependSensor subject = new JdependSensor();
		final DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
		subject.describe(descriptor);

		Assert.assertEquals("jDepend sensor", descriptor.name());
		Assert.assertEquals(1, descriptor.languages().size());
		Assert.assertEquals("java", descriptor.languages().iterator().next());
		Assert.assertEquals(0, descriptor.ruleRepositories().size());
	}
}
