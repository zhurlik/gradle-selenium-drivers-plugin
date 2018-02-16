package com.github.zhurlik.task

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Integration tests for checking 'installChrome' task.
 *
 * @author zhurlik@gmail.com
 */
class InstallChromeIntegTest {
    @Test(expected = GradleException)
    void testChromeOnLinux() {
        if (Os.isFamily(Os.FAMILY_UNIX)) {
            final Path projectPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource('').path, 'test-chrome')
            final Project project = ProjectBuilder.builder()
                    .withName('test-chrome')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.task(type: InstallChrome, 'installChrome', {
                browserVersion '64.0.3282.16700'
            })

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

            project.task(type: InstallChrome, 'installChrome', {
                browserVersion '64.0.3282.16700'
            })

            executeTask(project.tasks['installChrome'])
        }
    }

    /**
     * To be able to execute a gradle task.
     *
     * @param task
     */
    private void executeTask(final Task task) {
        task.taskDependencies.getDependencies(task).each {
            subTask -> executeTask(subTask)
        }

        task.execute()
    }
}
