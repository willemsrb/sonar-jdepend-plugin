package nl.futureedge.sonar.plugin.jdepend.computer;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer.MeasureComputerContext;

/**
 * Computer utilities.
 */
public class ComputerUtil {

	private ComputerUtil() {
		// Not instantiable
	}

	public static int countFromChildren(final MeasureComputerContext context, final String key) {
		int result = 0;
		for (final Measure measure : context.getChildrenMeasures(key)) {
			result += measure.getIntValue();
		}

		return result;
	}
}
