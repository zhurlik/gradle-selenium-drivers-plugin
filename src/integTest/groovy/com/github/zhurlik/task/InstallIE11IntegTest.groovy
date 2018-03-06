package com.github.zhurlik.task

import com.github.zhurlik.Basic
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Integration tests for checking 'installIE11' task.
 *
 * @author zhurlik@gmail.com
 */
class InstallIE11IntegTest extends Basic {
    @Test(expected = GradleException)
    void testIE11OnLinux() {
        if (Os.isFamily(Os.FAMILY_UNIX)) {
            final Path projectPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource('').path, 'test-ie11')
            final Project project = ProjectBuilder.builder()
                    .withName('test-ie11')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.tasks['installIE11'].browserVersion = '0.2'
            project.tasks['installIE11'].driverVersion = '3.8.0'

            executeTask(project.tasks['installIE11'])
        }
    }

    @Test
    void testIE11OnWindows() {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            final Path projectPath = Paths.get( new File(Thread.currentThread().getContextClassLoader().getResource('')
                    .toURI()).path, 'test-ie11')
            final Project project = ProjectBuilder.builder()
                    .withName('test-ie11')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.tasks['installIE11'].browserVersion = '0.2'
            project.tasks['installIE11'].driverVersion = '3.8.0'

            executeTask(project.tasks['installIE11'])
        }
    }
}
