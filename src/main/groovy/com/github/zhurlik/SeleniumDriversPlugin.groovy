package com.github.zhurlik

import com.github.zhurlik.task.InstallChromeOnLinux
import com.github.zhurlik.task.InstallChromeOnMac
import com.github.zhurlik.task.InstallChromeOnWindows
import com.github.zhurlik.task.InstallFireFoxOnLinux
import com.github.zhurlik.task.InstallFireFoxOnMac
import com.github.zhurlik.task.InstallFireFoxOnWindows
import com.github.zhurlik.task.InstallIE11OnWindows
import com.github.zhurlik.task.InstallOperaOnLinux
import com.github.zhurlik.task.InstallOperaOnMac
import com.github.zhurlik.task.InstallOperaOnWindows
import com.github.zhurlik.task.InstallPhantomJsOnLinux
import com.github.zhurlik.task.InstallPhantomJsOnMac
import com.github.zhurlik.task.InstallPhantomJsOnWindows
import com.github.zhurlik.task.InstallSafariOnMac
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
    private final static OperatingSystem OS =  OperatingSystem.current()

    @Override
    void apply(final Project project) {
        project.logger.quiet('Gradle Plugin: Selenium Drivers')

        tasksOnLinux(project)
        tasksOnMac(project)
        tasksOnWindows(project)
    }

    /**
     * Creates installation tasks for browsers and web drivers that are supported under Linux.
     *
     * @param project current gradle project
     */
    private void tasksOnLinux(final Project project) {
        if (OS.isLinux()) {
            // PhantomJs
            project.tasks.create('installPhantomJs', InstallPhantomJsOnLinux)

            // Opera
            project.tasks.create('installOpera', InstallOperaOnLinux)

            // Chrome
            project.tasks.create('installChrome', InstallChromeOnLinux)

            // FireFox
            project.tasks.create('installFireFox', InstallFireFoxOnLinux)
        }
    }

    /**
     * Creates installation tasks for browsers and web drivers that are supported under Mac OS X.
     *
     * @param project current gradle project
     */
    private void tasksOnMac(final Project project) {
        if (OS.isMacOsX()) {
            // PhantomJs
            project.tasks.create('installPhantomJs', InstallPhantomJsOnMac)

            // Safari
            project.tasks.create('installSafari', InstallSafariOnMac)

            // Opera
            project.tasks.create('installOpera', InstallOperaOnMac)

            // Chrome
            project.tasks.create('installChrome', InstallChromeOnMac)

            // FireFox
            project.tasks.create('installFireFox', InstallFireFoxOnMac)
        }
    }

    /**
     * Creates installation tasks for browsers and web drivers that are supported under Mac OS X.
     *
     * @param project current gradle project
     */
    private void tasksOnWindows(final Project project) {
        if (OS.isWindows()) {
            checkChoco(project)
            // PhantomJs
            project.tasks.create('installPhantomJs', InstallPhantomJsOnWindows)

            // Opera
            project.tasks.create('installOpera', InstallOperaOnWindows)

            // IE11
            project.tasks.create('installIE11', InstallIE11OnWindows)

            // Chrome
            project.tasks.create('installChrome', InstallChromeOnWindows)

            // FireFox
            project.tasks.create('installFireFox', InstallFireFoxOnWindows)
        }
    }

    /**
     * For Windows we are going to use the package manager Chocolatey.
     * Please follow steps on this https://chocolatey.org/docs/installation.
     *
     * @param project current gradle project
     */
    private void checkChoco(final Project proj) {
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
