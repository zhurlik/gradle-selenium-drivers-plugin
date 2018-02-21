package com.github.zhurlik.task

import com.github.zhurlik.Basic
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Integration tests for checking 'installPhantomJs' task.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsIntegTest extends Basic {

    @Test
    void testPantomJsOnLinux() {
        if (Os.isFamily(Os.FAMILY_UNIX)) {
            final Path projectPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource('').path, 'test-phantomjs')
            final Project project = ProjectBuilder.builder()
                    .withName('test-phantomjs')
                    .withProjectDir(projectPath.toFile())
                    .build()

            project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

            project.task(type: InstallPhantomJs, 'installPhantomJs', {
                browserVersion = '2.1.1'
            })

            executeTask(project.tasks['installPhantomJs'])
        }
    }

    @Test
    void testPhantomJsOnWindows() {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            final Path projectPath = Paths.get( new File(Thread.currentThread().getContextClassLoader().getResource('')
                    .toURI()).path, 'test-phantomjs')
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
}
