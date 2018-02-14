package com.github.zhurlik

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

/**
 *  A Gradle plugin that allows an ability to install supported Selenium Drivers on localhost.
 *
 * @author zhurlik@gmail.com
 */
class SeleniumDriversPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        project.logger.quiet('Gradle Plugin: Selenium Drivers')
        checkChoco(project)
    }

    /**
     * For Windows we are going to use the package manager Chocolatey.
     * Please follow steps on this https://chocolatey.org/docs/installation.
     */
    private void checkChoco(final Project proj) {
        if (OperatingSystem.current().isWindows()) {
            try {
                new ByteArrayOutputStream().withCloseable { out ->
                    proj.exec {
                        commandLine 'cmd', '/c', 'choco -v'
                        standardOutput = out
                    }
                    final String chocoVersion = out.toString()
                    proj.logger.quiet("Choco: ${chocoVersion}")
                }
            } catch (Exception ex) {
                throw new GradleException('Choco is not installed, please visit: https://chocolatey.org/install', ex)
            }
        }
    }
}
