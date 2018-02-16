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
 * Integration tests for checking 'installPhantomJs' task.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsIntegTest {
    @Test(expected = GradleException)
    void testChromeOnLinux() {
        if (Os.isFamily(Os.FAMILY_UNIX)) {
            final Path projectPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource('').path, 'test-phantomjs')
            final Project project = ProjectBuilder.builder()
                    .withName('test-phantomjs')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.task(type: InstallPhantomJs, 'installPhantomJs', {
                browserVersion '2.1.1'
            })

            executeTask(project.tasks['installPhantomJs'])
        }
    }

    @Test
    void testChromeOnWindows() {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            final Path projectPath = Paths.get( new File(Thread.currentThread().getContextClassLoader().getResource('')
                    .toURI()).path, 'test-phantomjs')
            final Project project = ProjectBuilder.builder()
                    .withName('test-phantomjs')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.task(type: InstallOpera, 'installPhantomJs', {
                browserVersion '2.1.1'
            })

            executeTask(project.tasks['installPhantomJs'])
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
