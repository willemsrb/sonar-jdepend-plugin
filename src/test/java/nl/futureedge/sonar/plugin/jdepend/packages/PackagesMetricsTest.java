package nl.futureedge.sonar.plugin.jdepend.packages;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.measures.Metric;

public class PackagesMetricsTest {

	@Test
	public void test() {
		final PackagesMetrics subject = new PackagesMetrics();
		final List<Metric> metrics = subject.getMetrics();

		Assert.assertEquals(6, metrics.size());
	}
}
