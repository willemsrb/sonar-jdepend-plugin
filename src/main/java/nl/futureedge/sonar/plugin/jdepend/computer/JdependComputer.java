package nl.futureedge.sonar.plugin.jdepend.computer;

import org.sonar.api.ce.measure.Component.Type;
import org.sonar.api.ce.measure.MeasureComputer;

import nl.futureedge.sonar.plugin.jdepend.metrics.JdependMetrics;

public class JdependComputer implements MeasureComputer {

	@Override
	public MeasureComputerDefinition define(final MeasureComputerDefinitionContext definitionContext) {
		return definitionContext.newDefinitionBuilder()
				.setInputMetrics(JdependMetrics.AFFERENT_COUPLINGS.key(), JdependMetrics.EFFERENT_COUPLINGS.key(),
						JdependMetrics.NUMBER_OF_CLASSES_AND_INTERFACES.key(),
						JdependMetrics.PACKAGE_DEPENDENCY_CYCLES.key())
				.setOutputMetrics(JdependMetrics.AFFERENT_COUPLINGS.key(), JdependMetrics.EFFERENT_COUPLINGS.key(),
						JdependMetrics.NUMBER_OF_CLASSES_AND_INTERFACES.key(),
						JdependMetrics.PACKAGE_DEPENDENCY_CYCLES.key())
				.build();
	}

	@Override
	public void compute(final MeasureComputerContext context) {
		if (context.getComponent().getType() == Type.FILE) {
			return;
		}

		context.addMeasure(JdependMetrics.AFFERENT_COUPLINGS.key(),
				ComputerUtil.countFromChildren(context, JdependMetrics.AFFERENT_COUPLINGS.key()));
		context.addMeasure(JdependMetrics.EFFERENT_COUPLINGS.key(),
				ComputerUtil.countFromChildren(context, JdependMetrics.EFFERENT_COUPLINGS.key()));
		context.addMeasure(JdependMetrics.NUMBER_OF_CLASSES_AND_INTERFACES.key(),
				ComputerUtil.countFromChildren(context, JdependMetrics.NUMBER_OF_CLASSES_AND_INTERFACES.key()));
		context.addMeasure(JdependMetrics.PACKAGE_DEPENDENCY_CYCLES.key(),
				ComputerUtil.countFromChildren(context, JdependMetrics.PACKAGE_DEPENDENCY_CYCLES.key()));
	}

}