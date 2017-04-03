Version 1.1:
 - Updated to fork guru.nidi:jdepend:2.9.5
   Changes from original (see https://github.com/nidi3/jdepend):
     - Fix bug in DependencyConstraint
     - Support Java 8 class files
     - Add hamcrest matchers
     - Use generics
     - Mavenize
     - New APIs for DependencyConstraint and PackageFilter
     - Parse generic signatures in class files
 - Added IT for SonarQube 6.2 and 6.3

Version 1.0:
 - Added metric for 'Number of Classes and Interfaces'
 - Added metric for 'Afferent Couplings'
 - Added metric for 'Efferent Couplings'
 - Added metric for 'Package Dependency Cycle'

Version 0.3:
 - Added metric to support missing packages-info.java files
 - Fixed 'Package Dependency Cycle' to correctly support maximum parameter
 - Clean up message from 'Package Dependency Cycle'

Version 0.2:
 - Added support for SonarQube 5.6+
 
Version 0.1.1:
 - Fixed NPE during analysis of project without (binary) source files

Version 0.1:
 - First initial release
 - Using JDepend version 2.9.1
 - Added rule: Number of Classes and Interfaces
 - Added rule: Afferent Couplings
 - Added rule: Efferent Couplings
 - Added rule: Abstractness
 - Added rule: Instability
 - Added rule: Distance from the Main Sequence
 - Added rule: Package Dependency Cycle
 
