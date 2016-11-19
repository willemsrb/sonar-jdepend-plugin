package nl.futureedge.sonar.plugin.jdepend;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

public class JdependPluginTest {

	@Test
	public void test() {
		final JdependPlugin javaPlugin = new JdependPlugin();
		final SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(5, 6), SonarQubeSide.SERVER);
		final Plugin.Context context = new Plugin.Context(runtime);

		Assert.assertEquals(0, context.getExtensions().size());
		javaPlugin.define(context);
		Assert.assertEquals(2, context.getExtensions().size());
	}

}
