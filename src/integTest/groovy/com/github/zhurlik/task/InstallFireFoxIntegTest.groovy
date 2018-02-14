package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Integration tests for checking 'installFireFox' task.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFoxIntegTest {
    @Test
    void testFirefox() {
        final Path projectPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource('').path, 'test-firefox')
        final Project project = ProjectBuilder.builder()
                .withName('test-firefox')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.task(type: InstallFireFox, 'installFireFox', {
            browser = Browsers.FIREFOX
            browserVersion '58.0.2'
        })

        executeTask(project.tasks['installFireFox'])
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
