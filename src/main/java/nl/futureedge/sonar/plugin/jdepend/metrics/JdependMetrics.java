package nl.futureedge.sonar.plugin.jdepend.metrics;

import static java.util.Arrays.asList;

import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class JdependMetrics implements Metrics {

	public static final String DOMAIN = "Jdepend";

	/** Metric for afferent couplings. */
	public static final Metric<Integer> AFFERENT_COUPLINGS = new Metric.Builder("afferent-couplings",
			"Afferent couplings", Metric.ValueType.INT).setDomain(DOMAIN).create();

	/** Metric for efferent couplings. */
	public static final Metric<Integer> EFFERENT_COUPLINGS = new Metric.Builder("efferent-couplings",
			"Efferent couplings", Metric.ValueType.INT).setDomain(DOMAIN).create();

	/** Metric for number of classes and interfaces. */
	public static final Metric<Integer> NUMBER_OF_CLASSES_AND_INTERFACES = new Metric.Builder(
			"number-of-classes-and-interfaces", "Number of classes and interfaces", Metric.ValueType.INT)
					.setDomain(DOMAIN).create();

	/** Metric for package dependency cycles. */
	public static final Metric<Integer> PACKAGE_DEPENDENCY_CYCLES = new Metric.Builder("package-dependency-cycles",
			"Package dependency cycles", Metric.ValueType.INT).setDomain(DOMAIN).create();

	@Override
	@SuppressWarnings("rawtypes")
	public List<Metric> getMetrics() {
		return asList(AFFERENT_COUPLINGS, EFFERENT_COUPLINGS, NUMBER_OF_CLASSES_AND_INTERFACES,
				PACKAGE_DEPENDENCY_CYCLES);
	}
}
