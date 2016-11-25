package nl.futureedge.sonar.plugin.jdepend.packages;

import org.sonar.api.ce.measure.Component.Type;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

/**
 * Packages computer.
 */
public class PackagesComputer implements MeasureComputer {

	@Override
	public MeasureComputerDefinition define(final MeasureComputerDefinitionContext definitionContext) {
		return definitionContext.newDefinitionBuilder()
				.setInputMetrics(PackagesMetrics.PACKAGE_INFO_COUNT.key(), PackagesMetrics.JAVA_COUNT.key())
				.setOutputMetrics(PackagesMetrics.MISSING_PACKAGE_INFO_METRIC.key(),
						PackagesMetrics.PACKAGE_METRIC.key(), PackagesMetrics.PACKAGE_COUNT.key(),
						PackagesMetrics.MISSING_PACKAGE_INFO_COUNT.key())
				.build();
	}

	@Override
	public void compute(final MeasureComputerContext context) {
		final int countPackageInfo = countFromChildren(context, PackagesMetrics.PACKAGE_INFO_COUNT.key());
		final int countJava = countFromChildren(context, PackagesMetrics.JAVA_COUNT.key());
		int countPackage = countFromChildren(context, PackagesMetrics.PACKAGE_COUNT.key());
		int countMissing = countFromChildren(context, PackagesMetrics.MISSING_PACKAGE_INFO_COUNT.key());

		if (countJava > 0) {
			countPackage++;
			if (countPackageInfo == 0) {
				countMissing++;
			}
		}

		context.addMeasure(PackagesMetrics.PACKAGE_COUNT.key(), countPackage);
		context.addMeasure(PackagesMetrics.MISSING_PACKAGE_INFO_COUNT.key(), countMissing);

		final Type type = context.getComponent().getType();
		if (Type.PROJECT == type || Type.MODULE == type || Type.VIEW == type || Type.SUBVIEW == type) {
			context.addMeasure(PackagesMetrics.PACKAGE_METRIC.key(), countPackage);
			context.addMeasure(PackagesMetrics.MISSING_PACKAGE_INFO_METRIC.key(), countMissing);
		}
	}

	private int countFromChildren(final MeasureComputerContext context, final String key) {
		int result = 0;
		for (final Measure measure : context.getChildrenMeasures(key)) {
			result += measure.getIntValue();
		}

		return result;
	}
}
