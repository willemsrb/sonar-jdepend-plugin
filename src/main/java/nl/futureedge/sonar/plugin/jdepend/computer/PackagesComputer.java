package nl.futureedge.sonar.plugin.jdepend.computer;

import org.sonar.api.ce.measure.Component.Type;
import org.sonar.api.ce.measure.MeasureComputer;

import nl.futureedge.sonar.plugin.jdepend.metrics.PackagesMetrics;

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
		final int countPackageInfo = ComputerUtil.countFromChildren(context, PackagesMetrics.PACKAGE_INFO_COUNT.key());
		final int countJava = ComputerUtil.countFromChildren(context, PackagesMetrics.JAVA_COUNT.key());
		int countPackage = ComputerUtil.countFromChildren(context, PackagesMetrics.PACKAGE_COUNT.key());
		int countMissing = ComputerUtil.countFromChildren(context, PackagesMetrics.MISSING_PACKAGE_INFO_COUNT.key());

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

}
