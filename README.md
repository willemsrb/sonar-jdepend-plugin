# JDepend plugin for SonarQube [![Build Status](https://travis-ci.org/willemsrb/sonar-jdepend-plugin.svg?branch=master)](https://travis-ci.org/willemsrb/sonar-jdepend-plugin) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin)](https://sonarqube.com/dashboard/index?id=nl.future-edge.sonarqube.plugins%3Asonar-jdepend-plugin)
*Requires SonarQube 5.6+ (tested against 5.6, 5.6.3 (LTS), 6.0 and 6.1)*

Uses the JDepend libraries (http://clarkware.com/software/JDepend.html) to add the following rules and metrics to SonarQube:

- Number of Classes and Interfaces (Rule and Metric)
- Afferent Couplings (Rule and Metric)
- Efferent Couplings (Rule and Metric)
- Abstractness (Rule only)
- Instability (Rule only)
- Distance from the Main Sequence (Rule only)
- Package Dependency Cycle (Rule and Metric)

This plugin depends on the existence of a package-info.java file in the package to correctly display issues (if no package-info.java file is present, the issues will not be registered).

Missing package-info.java files can be detected and enforced with the following metrics:

- Number of Packages (Metric)
- Number of missing package-info.java files (Metric)
