package nl.futureedge.sonar.plugin.jdepend.packages;

import static java.util.Arrays.asList;

import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

/**
 * Packages metrics.
 */
public class PackagesMetrics implements Metrics {

	public static final String DOMAIN = "Packages";

	/**
	 * Metric for missing package-info.java files.
	 */
	public static final Metric<Integer> MISSING_PACKAGE_INFO_METRIC = new Metric.Builder("missing_package_info",
			"Missing package-info.java files", Metric.ValueType.INT).setDomain(DOMAIN)
					.setDirection(Metric.DIRECTION_WORST).setQualitative(true).setBestValue(0.0).create();

	/**
	 * Metric for number of packages.
	 */
	public static final Metric<Integer> PACKAGE_METRIC = new Metric.Builder("package", "Number of packages",
			Metric.ValueType.INT).setDomain(DOMAIN).create();

	/**
	 * Number of missing package-info.java files.
	 */
	public static final Metric<Integer> MISSING_PACKAGE_INFO_COUNT = new Metric.Builder("missing_package_info_count",
			"Missing package-info.java files", Metric.ValueType.INT).setDomain(DOMAIN).setDeleteHistoricalData(true)
					.setHidden(true).create();

	/**
	 * Number of packages.
	 */
	public static final Metric<Integer> PACKAGE_COUNT = new Metric.Builder("package_count", "Number of packages",
			Metric.ValueType.INT).setDomain(DOMAIN).setDeleteHistoricalData(true).setHidden(true).create();

	/**
	 * Number of package-info.java files.
	 */
	public static final Metric<Integer> PACKAGE_INFO_COUNT = new Metric.Builder("package_info_count",
			"Number of package-info.java files", Metric.ValueType.INT).setDomain(DOMAIN).setDeleteHistoricalData(true)
					.setHidden(true).create();

	/**
	 * Number of java files.
	 */
	public static final Metric<Integer> JAVA_COUNT = new Metric.Builder("java_count", "Number of java files",
			Metric.ValueType.INT).setDomain(DOMAIN).setDeleteHistoricalData(true).setHidden(true).create();

	@Override
	@SuppressWarnings("rawtypes")
	public List<Metric> getMetrics() {
		return asList(MISSING_PACKAGE_INFO_METRIC, PACKAGE_METRIC, MISSING_PACKAGE_INFO_COUNT, PACKAGE_COUNT,
				PACKAGE_INFO_COUNT, JAVA_COUNT);
	}
}