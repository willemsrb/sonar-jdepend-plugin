package nl.futureedge.sonar.plugin.jdepend.rules;

import org.sonar.api.batch.fs.InputFile;

import jdepend.framework.JavaPackage;

/**
 * jDepend rule interface.
 */
@FunctionalInterface
public interface Rule {

	/**
	 * Execute the rule.
	 * 
	 * @param javaPackage java package to check
	 * @param packageInfoFile package info file to add issues to
	 */
	public void execute(JavaPackage javaPackage, InputFile packageInfoFile);
	
}
