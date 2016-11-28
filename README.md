# JDepend plugin for SonarQube [![Build Status](https://travis-ci.org/willemsrb/sonar-jdepend-plugin.svg?branch=master)](https://travis-ci.org/willemsrb/sonar-jdepend-plugin) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin)](https://sonarqube.com/dashboard/index?id=nl.future-edge.sonarqube.plugins%3Asonar-jdepend-plugin)
*Requires SonarQube 5.6+ (tested against 5.6, 5.6.3 (LTS), 6.0 and 6.1)*

Uses the JDepend libraries (http://clarkware.com/software/JDepend.html) to add the following rules to SonarQube:

- Number of Classes and Interfaces
- Afferent Couplings
- Efferent Couplings
- Abstractness
- Instability
- Distance from the Main Sequence
- Package Dependency Cycle

This plugin depends on the existence of a package-info.java file in the package to correctly display issues.  Missing
package-info.java files can be detected and enforced with the 'missing-package-info' metric.
