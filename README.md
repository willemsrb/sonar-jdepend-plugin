# JDepend plugin for SonarQube [![Build Status](https://travis-ci.org/willemsrb/sonar-jdepend-plugin.svg?branch=master)](https://travis-ci.org/willemsrb/sonar-jdepend-plugin) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin)](https://sonarqube.com/dashboard/index?id=nl.future-edge.sonarqube.plugins%3Asonar-jdepend-plugin)
*Requires SonarQube 5.6+ (tested against 5.6, 5.6.5 (LTS), 6.0, 6.1, 6.2 and 6.3)*

Uses a fork (https://github.com/nidi3/jdepend) of the JDepend libraries (http://clarkware.com/software/JDepend.html version 2.9.1) to add the following rules and metrics to SonarQube:

- Number of Classes and Interfaces (Rule and Metric)
- Afferent Couplings (Rule and Metric)
- Efferent Couplings (Rule and Metric)
- Abstractness (Rule only)
- Instability (Rule only)
- Distance from the Main Sequence (Rule only)
- Package Dependency Cycle (Rule and Metric)

This plugin depends on the existence of a package-info.java file in the package to correctly display issues (if no package-info.java file is present, the issues cannot be registered, as SonarQube only allows issues on existing files).
(See [Workarounds](WORKAROUNDS.md) to generate package-info.java files using a Maven plugin)

Missing package-info.java files can be detected and enforced with the following metrics:

- Number of Packages (Metric)
- Number of missing package-info.java files (Metric)

#### Configuration

The plugin executes the Jdepend library (packaged within the plugin) during the execution of the SonarQube scanner to scan the binaries, so no extra configuration is needed within the project. The sensor will not be executed if no Jdepend rules have been activated.

#### Installation

Install the plugin via the Update Center in the SonarQube administration pages. Or to install the plugin manually; copy the .jar file from the release to the `extensions/plugins` directory of your SonarQube installation.
