package nl.futureedge.sonar.plugin.jdepend.packages;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sonar.api.ce.measure.Component.Type;
import org.sonar.api.ce.measure.MeasureComputer.MeasureComputerDefinition;
import org.sonar.api.ce.measure.test.TestComponent;
import org.sonar.api.ce.measure.test.TestMeasureComputerContext;
import org.sonar.api.ce.measure.test.TestMeasureComputerDefinitionContext;
import org.sonar.api.ce.measure.test.TestSettings;

@RunWith(Parameterized.class)
public class PackagesComputerTest {

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] { { Type.DIRECTORY, false }, { Type.PROJECT, true }, { Type.MODULE, true },
				{ Type.VIEW, true }, { Type.SUBVIEW, true }, });
	}

	private final Type testComponentType;
	private final boolean shouldHaveShownMetrics;

	private final PackagesComputer subject = new PackagesComputer();

	private TestMeasureComputerDefinitionContext definitionContext;
	private MeasureComputerDefinition definition;

	private TestSettings settings;
	private TestComponent component;

	private TestMeasureComputerContext context;

	public PackagesComputerTest(final Type type, final boolean shouldHaveShownMetrics) {
		testComponentType = type;
		this.shouldHaveShownMetrics = shouldHaveShownMetrics;
	}

	@Before
	public void setup() {
		definitionContext = new TestMeasureComputerDefinitionContext();
		definition = subject.define(definitionContext);

		settings = new TestSettings();
		component = new TestComponent("test", testComponentType, null);

		context = new TestMeasureComputerContext(component, settings, definition);
	}

	@Test
	public void testDefinition() {
		final TestMeasureComputerDefinitionContext definitionContext = new TestMeasureComputerDefinitionContext();
		final MeasureComputerDefinition definition = subject.define(definitionContext);

		Assert.assertNotNull(definition);
		Assert.assertEquals(4, definition.getOutputMetrics().size());
		Assert.assertTrue(definition.getOutputMetrics().contains("missing_package_info"));
		Assert.assertTrue(definition.getOutputMetrics().contains("package"));
		Assert.assertTrue(definition.getOutputMetrics().contains("missing_package_info_count"));
		Assert.assertTrue(definition.getOutputMetrics().contains("package_count"));

		Assert.assertEquals(2, definition.getInputMetrics().size());
		Assert.assertTrue(definition.getInputMetrics().contains("package_info_count"));
		Assert.assertTrue(definition.getInputMetrics().contains("java_count"));
	}

	@Test
	public void test() {
		subject.compute(context);

		Assert.assertEquals(0, context.getMeasure("package_count").getIntValue());
		Assert.assertEquals(0, context.getMeasure("missing_package_info_count").getIntValue());
		if (shouldHaveShownMetrics) {
			Assert.assertEquals(0, context.getMeasure("package").getIntValue());
			Assert.assertEquals(0, context.getMeasure("missing_package_info").getIntValue());
		} else {
			Assert.assertNull(context.getMeasure("package"));
			Assert.assertNull(context.getMeasure("missing_package_info"));
		}
	}

	@Test
	public void testGoodPackage() {
		context.addChildrenMeasures("package_info_count", 1);
		context.addChildrenMeasures("java_count", 1, 1, 1, 1, 1);

		subject.compute(context);

		Assert.assertEquals(1, context.getMeasure("package_count").getIntValue());
		Assert.assertEquals(0, context.getMeasure("missing_package_info_count").getIntValue());
		if (shouldHaveShownMetrics) {
			Assert.assertEquals(1, context.getMeasure("package").getIntValue());
			Assert.assertEquals(0, context.getMeasure("missing_package_info").getIntValue());
		} else {
			Assert.assertNull(context.getMeasure("package"));
			Assert.assertNull(context.getMeasure("missing_package_info"));
		}
	}

	@Test
	public void testSubPackages() {
		context.addChildrenMeasures("package_info_count", 1);
		context.addChildrenMeasures("java_count", 1, 1, 1, 1, 1);
		context.addChildrenMeasures("missing_package_info_count", 1, 4, 3, 2);
		context.addChildrenMeasures("package_count", 5, 7, 5, 3);

		subject.compute(context);

		Assert.assertEquals(21, context.getMeasure("package_count").getIntValue());
		Assert.assertEquals(10, context.getMeasure("missing_package_info_count").getIntValue());
		if (shouldHaveShownMetrics) {
			Assert.assertEquals(21, context.getMeasure("package").getIntValue());
			Assert.assertEquals(10, context.getMeasure("missing_package_info").getIntValue());
		} else {
			Assert.assertNull(context.getMeasure("package"));
			Assert.assertNull(context.getMeasure("missing_package_info"));
		}
	}

	@Test
	public void testBadPackage() {
		// context.addChildrenMeasures("package_info_count", 1);
		context.addChildrenMeasures("java_count", 1, 1, 1, 1, 1);

		subject.compute(context);

		Assert.assertEquals(1, context.getMeasure("package_count").getIntValue());
		Assert.assertEquals(1, context.getMeasure("missing_package_info_count").getIntValue());
		if (shouldHaveShownMetrics) {
			Assert.assertEquals(1, context.getMeasure("package").getIntValue());
			Assert.assertEquals(1, context.getMeasure("missing_package_info").getIntValue());
		} else {
			Assert.assertNull(context.getMeasure("package"));
			Assert.assertNull(context.getMeasure("missing_package_info"));
		}
	}

}
