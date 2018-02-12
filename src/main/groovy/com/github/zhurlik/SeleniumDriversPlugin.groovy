package com.github.zhurlik

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *  A Gradle plugin that allows an ability to install supported Selenium Drivers on localhost.
 *
 * @author zhurlik@gmail.com
 */
class SeleniumDriversPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        project.logger.quiet('Gradle Plugin: Selenium Drivers')
    }
}
