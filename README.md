# sonar-jdepend-plugin [![Build Status](https://travis-ci.org/willemsrb/sonar-jdepend-plugin.svg?branch=master)](https://travis-ci.org/willemsrb/sonar-jdepend-plugin) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=nl.future-edge.sonarqube.plugins:sonar-jdepend-plugin)](https://sonarqube.com/dashboard/index?id=nl.future-edge.sonarqube.plugins%3Asonar-jdepend-plugin)
Sonar plugin for jDepend

Uses the JDepend libraries (http://clarkware.com/software/JDepend.html) to add the following rules to SonarQube:

- Number of Classes and Interfaces
- Afferent Couplings
- Efferent Couplings
- Abstractness
- Instability
- Distance from the Main Sequence
- Package Dependency Cycle
