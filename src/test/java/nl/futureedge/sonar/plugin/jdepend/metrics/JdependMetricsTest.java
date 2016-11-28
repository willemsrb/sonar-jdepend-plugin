package nl.futureedge.sonar.plugin.jdepend.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.measures.Metric;

public class JdependMetricsTest {

	@Test
	public void test() {
		final JdependMetrics subject = new JdependMetrics();
		final List<Metric> metrics = subject.getMetrics();

		Assert.assertEquals(4, metrics.size());
	}
}
