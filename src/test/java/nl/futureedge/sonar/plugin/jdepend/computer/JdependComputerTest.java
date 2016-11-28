package nl.futureedge.sonar.plugin.jdepend.computer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.ce.measure.Component.Type;
import org.sonar.api.ce.measure.MeasureComputer.MeasureComputerDefinition;
import org.sonar.api.ce.measure.test.TestComponent;
import org.sonar.api.ce.measure.test.TestComponent.FileAttributesImpl;
import org.sonar.api.ce.measure.test.TestMeasureComputerContext;
import org.sonar.api.ce.measure.test.TestMeasureComputerDefinitionContext;
import org.sonar.api.ce.measure.test.TestSettings;

public class JdependComputerTest {

	private final JdependComputer subject = new JdependComputer();

	private TestMeasureComputerDefinitionContext definitionContext;
	private MeasureComputerDefinition definition;

	private TestSettings settings;
	private TestComponent component;

	private TestMeasureComputerContext context;

	@Before
	public void setup() {
		definitionContext = new TestMeasureComputerDefinitionContext();
		definition = subject.define(definitionContext);

		settings = new TestSettings();
		component = new TestComponent("test", Type.DIRECTORY, null);

		context = new TestMeasureComputerContext(component, settings, definition);
	}

	@Test
	public void testDefinition() {
		final TestMeasureComputerDefinitionContext definitionContext = new TestMeasureComputerDefinitionContext();
		final MeasureComputerDefinition definition = subject.define(definitionContext);

		Assert.assertNotNull(definition);
		Assert.assertEquals(4, definition.getOutputMetrics().size());
		Assert.assertTrue(definition.getOutputMetrics().contains("afferent-couplings"));
		Assert.assertTrue(definition.getOutputMetrics().contains("efferent-couplings"));
		Assert.assertTrue(definition.getOutputMetrics().contains("number-of-classes-and-interfaces"));
		Assert.assertTrue(definition.getOutputMetrics().contains("package-dependency-cycles"));

		Assert.assertEquals(4, definition.getInputMetrics().size());
		Assert.assertTrue(definition.getInputMetrics().contains("afferent-couplings"));
		Assert.assertTrue(definition.getInputMetrics().contains("efferent-couplings"));
		Assert.assertTrue(definition.getInputMetrics().contains("number-of-classes-and-interfaces"));
		Assert.assertTrue(definition.getInputMetrics().contains("package-dependency-cycles"));
	}

	@Test
	public void testEmpty() {
		subject.compute(context);

		Assert.assertEquals(0, context.getMeasure("afferent-couplings").getIntValue());
		Assert.assertEquals(0, context.getMeasure("efferent-couplings").getIntValue());
		Assert.assertEquals(0, context.getMeasure("number-of-classes-and-interfaces").getIntValue());
		Assert.assertEquals(0, context.getMeasure("package-dependency-cycles").getIntValue());
	}

	@Test
	public void test() {
		context.addChildrenMeasures("afferent-couplings", 1);
		context.addChildrenMeasures("efferent-couplings", 1, 4, 5);
		context.addChildrenMeasures("number-of-classes-and-interfaces", 3, 4);
		context.addChildrenMeasures("package-dependency-cycles", 2, 2, 2, 2, 2, 2, 2);

		subject.compute(context);

		Assert.assertEquals(1, context.getMeasure("afferent-couplings").getIntValue());
		Assert.assertEquals(10, context.getMeasure("efferent-couplings").getIntValue());
		Assert.assertEquals(7, context.getMeasure("number-of-classes-and-interfaces").getIntValue());
		Assert.assertEquals(14, context.getMeasure("package-dependency-cycles").getIntValue());
	}

	@Test
	public void testFile() {
		component = new TestComponent("test", Type.FILE, new FileAttributesImpl("java", false));
		context = new TestMeasureComputerContext(component, settings, definition);

		Assert.assertNull(context.getMeasure("afferent-couplings"));
		Assert.assertNull(context.getMeasure("efferent-couplings"));
		Assert.assertNull(context.getMeasure("number-of-classes-and-interfaces"));
		Assert.assertNull(context.getMeasure("package-dependency-cycles"));
	}
}
