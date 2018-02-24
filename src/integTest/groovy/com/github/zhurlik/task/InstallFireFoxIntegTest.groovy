package com.github.zhurlik.task

import com.github.zhurlik.Basic
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Integration tests for checking 'installFireFox' task.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFoxIntegTest extends Basic {
    @Test
    void testFirefox() {
        final Path projectPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource('').path, 'test-firefox')
        final Project project = ProjectBuilder.builder()
                .withName('test-firefox')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.task(type: InstallFireFox, 'installFireFox', {
            browserVersion '58.0.2'
            driverVersion '0.19.1'
        })

        executeTask(project.tasks['installFireFox'])
    }
}
