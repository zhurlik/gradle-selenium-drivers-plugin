package com.github.zhurlik.task

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertNotNull

/**
 * Unit tests for {@link InstallSafariOnMac} methods that will be invoked via {@link org.gradle.internal.reflect.JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallSafariOnMacTest  extends BaseTest {

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks.create('testInstallSafari', InstallSafariOnMac)
        assertNotNull(task)
    }

    @Test
    void testApply() {
        apply()
    }
}
