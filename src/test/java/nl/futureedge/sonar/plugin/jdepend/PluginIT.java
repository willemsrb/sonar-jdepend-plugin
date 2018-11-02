package nl.futureedge.sonar.plugin.jdepend;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.OrchestratorBuilder;
import com.sonar.orchestrator.build.MavenBuild;
import com.sonar.orchestrator.locator.FileLocation;
import com.sonar.orchestrator.locator.MavenLocation;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueClient;
import org.sonar.wsclient.issue.IssueQuery;
import org.sonar.wsclient.project.NewProject;

/**
 * Tests compatibility with sonar-java-plugin and SonarQube. <br>
 */
@RunWith(Parameterized.class)
public class PluginIT {

    /**
     * Returns a list of versions (sonarQube and Java plugin).
     * @return versions
     */
    @Parameterized.Parameters
    public static Collection<Object[]> sonarQubeVersions() {
        return Arrays.asList(new Object[][] {//
            {"LATEST_RELEASE[7.4]", "5.8.0.15699"}, //
            {"LATEST_RELEASE[6.7]", "5.8.0.15699"}, //
        });
    }

    private final String sonarQubeVersion;
    private final String javaVersion;
    private Orchestrator orchestrator;

    public PluginIT(final String sonarQubeVersion, final String javaVersion) {
        this.sonarQubeVersion = sonarQubeVersion;
        this.javaVersion = javaVersion;
    }

    @Before
    public void setupSonarQube() {
        final OrchestratorBuilder builder = Orchestrator.builderEnv()
            .setSonarVersion(sonarQubeVersion)
            .restoreProfileAtStartup(FileLocation.of(new File("target/it", "profile.xml")))
            .addPlugin(FileLocation.byWildcardMavenFilename(new File("target"), "sonar-jdepend-plugin-*.jar"))
            .addPlugin(MavenLocation.of("org.sonarsource.java", "sonar-java-plugin", javaVersion))
            .setServerProperty("sonar.web.javaOpts", "-Xmx1g") //
            .setServerProperty("sonar.es.javaOpts", "-Xmx1g");
        orchestrator = builder.build();
        orchestrator.start();

        // Provision project
        orchestrator.getServer().adminWsClient().projectClient()
                .create(NewProject.create().key("nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin-it")
                        .name("Test project for Integration Test"));
        orchestrator.getServer().associateProjectToQualityProfile(
                "nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin-it", "java", "test");
    }

    @After
    public void teardownSonarQube() {
        if (orchestrator != null) {
            orchestrator.stop();
        }
    }

    public void runSonar() {
        final File pom = new File(new File(".", "target/it"), "pom.xml");

        final MavenBuild install = MavenBuild.create(pom).setGoals("clean verify");
        Assert.assertTrue("'clean verify' failed", orchestrator.executeBuild(install).isSuccess());

        final HashMap<String, String> sonarProperties = new HashMap<>();
        sonarProperties.put("sonar.login", "");
        sonarProperties.put("sonar.password", "");
        sonarProperties.put("sonar.skip", "false");
        sonarProperties.put("sonar.scanner.skip", "false");

        final MavenBuild sonar = MavenBuild.create(pom).setGoals("sonar:sonar").setProperties(sonarProperties);
        Assert.assertTrue("'sonar:sonar' failed", orchestrator.executeBuild(sonar).isSuccess());
    }

    @Test
    public void test() {
        runSonar();

        final IssueClient issueClient = orchestrator.getServer().wsClient().issueClient();

        final List<Issue> issues = issueClient
                .find(IssueQuery.create().componentRoots("nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin-it"))
                .list();
        Assert.assertEquals(1, issues.size());
        final Issue issue = issues.iterator().next();
        Assert.assertEquals(
                "nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin-it:src/main/java/packagedependencycycle/packagea/package-info.java",
                issue.componentKey());
    }

}
