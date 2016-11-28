package nl.futureedge.sonar.plugin.jdepend;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeVersion;

public class JdependPluginTest {

	@Test
	public void test() {
		final JdependPlugin javaPlugin = new JdependPlugin();
		final Plugin.Context context = new Plugin.Context(SonarQubeVersion.V5_6);

		Assert.assertEquals(0, context.getExtensions().size());
		javaPlugin.define(context);
		Assert.assertEquals(7, context.getExtensions().size());
	}
}
