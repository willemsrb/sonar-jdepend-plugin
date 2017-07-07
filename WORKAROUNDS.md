# Workarounds

## Generate package-info.java using Maven plugin
Thank you [@akafred](https://github.com/akafred) for this workaround:

```
<plugin>
    <groupId>de.shadowhunt.maven.plugins</groupId>
    <artifactId>package-info-maven-plugin</artifactId>
    <version>2.0.0</version>
</plugin>
```

```
<plugin>
    <!-- We generate package-info.java-files to get information from Jdepend in Sonar -->
    <groupId>de.shadowhunt.maven.plugins</groupId>
    <artifactId>package-info-maven-plugin</artifactId>
    <configuration>
        <packages>
            <package>
            </package>
        </packages>
        <!-- Sonar doesn't like the files to be in generated-sources :-(
             so we have to put them in src-folder - however we don't check them in -->
        <outputDirectory>${project.basedir}/src/main/java</outputDirectory>
    </configuration>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>package-info</goal>
            </goals>
        </execution>
    </executions>
</plugin>

```
