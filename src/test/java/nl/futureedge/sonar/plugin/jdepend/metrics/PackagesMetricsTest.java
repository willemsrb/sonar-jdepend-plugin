package nl.futureedge.sonar.plugin.jdepend.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.measures.Metric;

import nl.futureedge.sonar.plugin.jdepend.metrics.PackagesMetrics;

public class PackagesMetricsTest {

	@Test
	public void test() {
		final PackagesMetrics subject = new PackagesMetrics();
		final List<Metric> metrics = subject.getMetrics();

		Assert.assertEquals(6, metrics.size());
	}
}
