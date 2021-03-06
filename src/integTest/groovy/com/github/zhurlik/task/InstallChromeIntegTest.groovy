package com.github.zhurlik.task

import com.github.zhurlik.Basic
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Integration tests for checking 'installChrome' task.
 *
 * @author zhurlik@gmail.com
 */
class InstallChromeIntegTest extends Basic {
    @Test
    void testChromeOnLinux() {
        if (Os.isFamily(Os.FAMILY_UNIX)) {
            final Path projectPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource('').path, 'test-chrome')
            final Project project = ProjectBuilder.builder()
                    .withName('test-chrome')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.tasks['installChrome'].browserVersion = '64.0.3282.16700'
            project.tasks['installChrome'].driverVersion = '2.35'

            executeTask(project.tasks['installChrome'])
        }
    }

    @Test
    void testChromeOnWindows() {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            final Path projectPath = Paths.get( new File(Thread.currentThread().getContextClassLoader().getResource('')
                    .toURI()).path, 'test-chrome')
            final Project project = ProjectBuilder.builder()
                    .withName('test-chrome')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.tasks['installChrome'].browserVersion = '64.0.3282.16700'
            project.tasks['installChrome'].driverVersion = '2.35'

            executeTask(project.tasks['installChrome'])
        }
    }
}
